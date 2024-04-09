package com.ld.poetry.im.websocket;

import org.tio.utils.time.Time;

public class ImConfigConst {

    /**
     * 默认群组ID，注册时自动加入
     */
    public static final int DEFAULT_GROUP_ID = -1;

    /**
     * 系统消息ID
     */
    public static final int DEFAULT_SYSTEM_MESSAGE_ID = -1;

    /**
     * 协议名字
     */
    public static final String PROTOCOL_NAME = "protocol_poetize";

    public static final String CHARSET = "UTF-8";

    /**
     * 监听端口
     */
    public static final int SERVER_PORT = 9324;

    /**
     * 心跳超时时间，单位：毫秒
     */
    public static final int HEARTBEAT_TIMEOUT = 1000 * 600;

    /**
     * IP数据监控统计，时间段
     */
    public static final Long DURATION_DEFAULT = Time.MINUTE_1 * 5;
    public static final Long[] IP_STAT_DURATIONS = new Long[]{DURATION_DEFAULT};

    /**
     * 默认群聊
     */
    public static final String GROUP_DEFAULT = "group_default";

    /**
     * 加入群
     * <p>
     * 0：无需验证
     * 1：需要群主或管理员同意
     */
    public static final boolean IN_TYPE_FALSE = false;
    public static final boolean IN_TYPE_TRUE = true;

    /**
     * 用户状态[-1:审核不通过或者踢出群聊，0:未审核，1:审核通过，2:禁言]
     */
    public static final int GROUP_USER_STATUS_BAN = -1;
    public static final int GROUP_USER_STATUS_NOT_VERIFY = 0;
    public static final int GROUP_USER_STATUS_PASS = 1;
    public static final int GROUP_USER_STATUS_SILENCE = 2;

    /**
     * 朋友状态[-1:审核不通过或者删除好友，0:未审核，1:审核通过]
     */
    public static final int FRIEND_STATUS_BAN = -1;
    public static final int FRIEND_STATUS_NOT_VERIFY = 0;
    public static final int FRIEND_STATUS_PASS = 1;

    /**
     * 是否已读
     * <p>
     * 0：未读
     * 1：已读
     */
    public static final boolean USER_MESSAGE_STATUS_FALSE = false;
    public static final boolean USER_MESSAGE_STATUS_TRUE = true;

    /**
     * 是否是群组管理员
     * <p>
     * 0：否
     * 1：是
     */
    public static final boolean ADMIN_FLAG_FALSE = false;
    public static final boolean ADMIN_FLAG_TRUE = true;

    /**
     * 群类型[1:聊天群，2:话题]
     */
    public static final int GROUP_COMMON = 1;
    public static final int GROUP_TOPIC = 2;
}
