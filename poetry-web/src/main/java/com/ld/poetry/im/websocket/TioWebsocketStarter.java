package com.ld.poetry.im.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.tio.server.ServerTioConfig;
import org.tio.websocket.server.WsServerStarter;

import javax.annotation.PostConstruct;

@Component
@Slf4j
@ConditionalOnProperty(name = "im.enable", havingValue = "true")
public class TioWebsocketStarter {

    private WsServerStarter wsServerStarter;

    private ServerTioConfig serverTioConfig;

    private ImWsMsgHandler imWsMsgHandler;

    private ImServerAioListener imServerAioListener;

    private ImIpStatListener imIpStatListener;

    public TioWebsocketStarter(ImWsMsgHandler imWsMsgHandler, ImServerAioListener imServerAioListener, ImIpStatListener imIpStatListener) {
        this.imWsMsgHandler = imWsMsgHandler;
        this.imServerAioListener = imServerAioListener;
        this.imIpStatListener = imIpStatListener;
    }

    @PostConstruct
    public void init() throws Exception {
        wsServerStarter = new WsServerStarter(ImConfigConst.SERVER_PORT, imWsMsgHandler);
        serverTioConfig = wsServerStarter.getServerTioConfig();
        serverTioConfig.setName(ImConfigConst.PROTOCOL_NAME);
        serverTioConfig.setServerAioListener(imServerAioListener);
        serverTioConfig.setIpStatListener(imIpStatListener);
        serverTioConfig.ipStats.addDurations(ImConfigConst.IP_STAT_DURATIONS);
        serverTioConfig.setHeartbeatTimeout(ImConfigConst.HEARTBEAT_TIMEOUT);
    }

    public void start() throws Exception {
        wsServerStarter.start();
    }

    public WsServerStarter getWsServerStarter() {
        return wsServerStarter;
    }

    public ServerTioConfig getServerTioConfig() {
        return serverTioConfig;
    }

    public ImWsMsgHandler getImWsMsgHandler() {
        return imWsMsgHandler;
    }

    public ImServerAioListener getImServerAioListener() {
        return imServerAioListener;
    }

    public ImIpStatListener getImIpStatListener() {
        return imIpStatListener;
    }
}
