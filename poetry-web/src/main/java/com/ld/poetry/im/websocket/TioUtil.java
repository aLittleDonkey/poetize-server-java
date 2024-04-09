package com.ld.poetry.im.websocket;

import cn.hutool.extra.spring.SpringUtil;

public class TioUtil {

    private static TioWebsocketStarter tioWebsocketStarter;

    public static void buildTio() {
        TioWebsocketStarter websocketStarter = null;
        try {
            websocketStarter = SpringUtil.getBean(TioWebsocketStarter.class);
        } catch (Exception e) {
        }
        TioUtil.tioWebsocketStarter = websocketStarter;
    }

    public static TioWebsocketStarter getTio() {
        return TioUtil.tioWebsocketStarter;
    }
}
