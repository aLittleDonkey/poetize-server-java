package com.ld.poetry.config;

import com.ld.poetry.entity.User;
import com.ld.poetry.handle.PoetryRuntimeException;
import com.ld.poetry.utils.CommonConst;
import com.ld.poetry.utils.PoetryCache;
import com.ld.poetry.utils.PoetryUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;


@Aspect
@Component
@Order(1)
@Slf4j
public class SaveCheckAspect {

    @Around("@annotation(saveCheck)")
    public Object around(ProceedingJoinPoint joinPoint, SaveCheck saveCheck) throws Throwable {
        boolean flag = false;

        String token = PoetryUtil.getToken();
        if (StringUtils.hasText(token)) {
            User user = (User) PoetryCache.get(token);
            if (user != null) {
                if (user.getId().intValue() == PoetryUtil.getAdminUser().getId().intValue()) {
                    return joinPoint.proceed();
                }

                AtomicInteger atomicInteger = (AtomicInteger) PoetryCache.get(CommonConst.SAVE_COUNT_USER_ID + user.getId().toString());
                if (atomicInteger == null) {
                    atomicInteger = new AtomicInteger();
                    PoetryCache.put(CommonConst.SAVE_COUNT_USER_ID + user.getId().toString(), atomicInteger, CommonConst.SAVE_EXPIRE);
                }
                int userIdCount = atomicInteger.getAndIncrement();
                if (userIdCount >= CommonConst.SAVE_MAX_COUNT) {
                    log.info("用户保存超限：" + user.getId().toString() + "，次数：" + userIdCount);
                    flag = true;
                }
            }
        }

        String ip = PoetryUtil.getIpAddr(PoetryUtil.getRequest());
        AtomicInteger atomic = (AtomicInteger) PoetryCache.get(CommonConst.SAVE_COUNT_IP + ip);
        if (atomic == null) {
            atomic = new AtomicInteger();
            PoetryCache.put(CommonConst.SAVE_COUNT_IP + ip, atomic, CommonConst.SAVE_EXPIRE);
        }
        int ipCount = atomic.getAndIncrement();
        if (ipCount > CommonConst.SAVE_MAX_COUNT) {
            log.info("IP保存超限：" + ip + "，次数：" + ipCount);
            flag = true;
        }

        if (flag) {
            throw new PoetryRuntimeException("今日提交次数已用尽，请一天后再来！");
        }

        return joinPoint.proceed();
    }
}
