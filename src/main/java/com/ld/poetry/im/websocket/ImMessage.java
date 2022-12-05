package com.ld.poetry.im.websocket;

import lombok.Data;

@Data
public class ImMessage {

    private Integer messageType;

    private String content;

    private Integer fromId;

    private Integer toId;

    private Integer groupId;

    private String avatar;

    private String username;
}
