package com.ld.poetry.utils.storage;


import com.ld.poetry.handle.PoetryRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 用来处理文件存储，对接多个平台
 */
@Slf4j
@Component
public class FileStorageService implements ApplicationContextAware {

    @Value("${store.type}")
    private String defaultType;

    private final Map<String, StoreService> storeServiceMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, StoreService> consumeService = applicationContext.getBeansOfType(StoreService.class);
        if (!CollectionUtils.isEmpty(consumeService)) {
            for (StoreService value : consumeService.values()) {
                storeServiceMap.put(value.getStoreName(), value);
            }
        }
    }

    /**
     * 获取对应的存储平台，如果存储平台不存在则抛出异常
     */
    public StoreService getFileStorageByStoreType(String storeType) {
        if (!StringUtils.hasText(storeType) || !storeServiceMap.containsKey(storeType)) {
            throw new PoetryRuntimeException("没有找到对应的存储平台：" + storeType);
        }

        return storeServiceMap.get(storeType);
    }

    /**
     * 获取对应的存储平台，如果没有指定则使用默认值
     */
    public StoreService getFileStorage(String storeType) {
        if (!StringUtils.hasText(storeType)) {
            storeType = defaultType;
        }

        if (!storeServiceMap.containsKey(storeType)) {
            throw new PoetryRuntimeException("没有找到对应的存储平台：" + storeType);
        }

        return storeServiceMap.get(storeType);
    }
}
