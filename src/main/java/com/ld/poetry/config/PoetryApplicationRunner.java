package com.ld.poetry.config;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.ld.poetry.dao.HistoryInfoMapper;
import com.ld.poetry.dao.WebInfoMapper;
import com.ld.poetry.entity.*;
import com.ld.poetry.im.websocket.TioWebsocketStarter;
import com.ld.poetry.service.FamilyService;
import com.ld.poetry.service.UserService;
import com.ld.poetry.utils.CommonConst;
import com.ld.poetry.utils.CommonQuery;
import com.ld.poetry.utils.PoetryCache;
import com.ld.poetry.utils.PoetryEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@Component
public class PoetryApplicationRunner implements ApplicationRunner {

    @Value("${store.type}")
    private String defaultType;

    @Autowired
    private WebInfoMapper webInfoMapper;

    @Autowired
    private CommonQuery commonQuery;

    @Autowired
    private UserService userService;

    @Autowired
    private FamilyService familyService;

    @Autowired
    private HistoryInfoMapper historyInfoMapper;

    @Autowired
    private TioWebsocketStarter tioWebsocketStarter;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LambdaQueryChainWrapper<WebInfo> wrapper = new LambdaQueryChainWrapper<>(webInfoMapper);
        List<WebInfo> list = wrapper.list();
        if (!CollectionUtils.isEmpty(list)) {
            list.get(0).setDefaultStoreType(defaultType);
            PoetryCache.put(CommonConst.WEB_INFO, list.get(0));
        }

        List<Sort> sortInfo = commonQuery.getSortInfo();
        if (!CollectionUtils.isEmpty(sortInfo)) {
            PoetryCache.put(CommonConst.SORT_INFO, sortInfo);
        }

        User admin = userService.lambdaQuery().eq(User::getUserType, PoetryEnum.USER_TYPE_ADMIN.getCode()).one();
        PoetryCache.put(CommonConst.ADMIN, admin);

        Family family = familyService.lambdaQuery().eq(Family::getUserId, admin.getId()).one();
        PoetryCache.put(CommonConst.ADMIN_FAMILY, family);

        List<HistoryInfo> infoList = new LambdaQueryChainWrapper<>(historyInfoMapper)
                .select(HistoryInfo::getIp, HistoryInfo::getUserId)
                .ge(HistoryInfo::getCreateTime, LocalDateTime.now().with(LocalTime.MIN))
                .list();

        PoetryCache.put(CommonConst.IP_HISTORY, new CopyOnWriteArraySet<>(infoList.stream().map(info -> info.getIp() + (info.getUserId() != null ? "_" + info.getUserId().toString() : "")).collect(Collectors.toList())));

        Map<String, Object> history = new HashMap<>();
        history.put(CommonConst.IP_HISTORY_PROVINCE, historyInfoMapper.getHistoryByProvince());
        history.put(CommonConst.IP_HISTORY_IP, historyInfoMapper.getHistoryByIp());
        history.put(CommonConst.IP_HISTORY_HOUR, historyInfoMapper.getHistoryBy24Hour());
        history.put(CommonConst.IP_HISTORY_COUNT, historyInfoMapper.getHistoryCount());
        PoetryCache.put(CommonConst.IP_HISTORY_STATISTICS, history);

        tioWebsocketStarter.start();
    }
}
