CREATE DATABASE IF NOT EXISTS poetize DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;




DROP TABLE IF EXISTS `poetize`.`user`;

CREATE TABLE `poetize`.`user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(32) DEFAULT NULL COMMENT '用户名',
  `password` varchar(128) DEFAULT NULL COMMENT '密码',
  `phone_number` varchar(16) DEFAULT NULL COMMENT '手机号',
  `email` varchar(32) DEFAULT NULL COMMENT '用户邮箱',
  `user_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用[0:否，1:是]',
  `gender` tinyint(2) DEFAULT NULL COMMENT '性别[1:男，2:女，0:保密]',
  `open_id` varchar(128) DEFAULT NULL COMMENT 'openId',
  `avatar` varchar(256) DEFAULT NULL COMMENT '头像',
  `admire` varchar(32) DEFAULT NULL COMMENT '赞赏',
  `subscribe` text DEFAULT NULL COMMENT '订阅',
  `introduction` varchar(4096) DEFAULT NULL COMMENT '简介',
  `user_type` tinyint(2) NOT NULL DEFAULT 2 COMMENT '用户类型[0:admin，1:管理员，2:普通用户]',

  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最终修改时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '最终修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否启用[0:未删除，1:已删除]',

  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

DROP TABLE IF EXISTS `poetize`.`article`;

CREATE TABLE `poetize`.`article` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int NOT NULL COMMENT '用户ID',
  `sort_id` int NOT NULL COMMENT '分类ID',
  `label_id` int NOT NULL COMMENT '标签ID',
  `article_cover` varchar(256) DEFAULT NULL COMMENT '封面',
  `article_title` varchar(32) NOT NULL COMMENT '博文标题',
  `article_content` text NOT NULL COMMENT '博文内容',
  `video_url` varchar(1024) DEFAULT NULL COMMENT '视频链接',
  `view_count` int NOT NULL DEFAULT 0 COMMENT '浏览量',
  `like_count` int NOT NULL DEFAULT 0 COMMENT '点赞数',
  `view_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否可见[0:否，1:是]',
  `password` varchar(128) DEFAULT NULL COMMENT '密码',
  `tips` varchar(128) DEFAULT NULL COMMENT '提示',
  `recommend_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否推荐[0:否，1:是]',
  `comment_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用评论[0:否，1:是]',

  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime  DEFAULT CURRENT_TIMESTAMP COMMENT '最终修改时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '最终修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否启用[0:未删除，1:已删除]',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

DROP TABLE IF EXISTS `poetize`.`comment`;

CREATE TABLE `poetize`.`comment` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `source` int NOT NULL COMMENT '评论来源标识',
  `type` varchar(32) NOT NULL COMMENT '评论来源类型',
  `parent_comment_id` int NOT NULL DEFAULT 0 COMMENT '父评论ID',
  `user_id` int NOT NULL COMMENT '发表用户ID',
  `floor_comment_id` int DEFAULT NULL COMMENT '楼层评论ID',
  `parent_user_id` int DEFAULT NULL COMMENT '父发表用户名ID',
  `like_count` int NOT NULL DEFAULT 0 COMMENT '点赞数',
  `comment_content` varchar(1024) NOT NULL COMMENT '评论内容',
  `comment_info` varchar(256) DEFAULT NULL COMMENT '评论额外信息',

  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

  PRIMARY KEY (`id`),
  KEY `source` (`source`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章评论表';

DROP TABLE IF EXISTS `poetize`.`sort`;

CREATE TABLE `poetize`.`sort` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sort_name` varchar(32) NOT NULL COMMENT '分类名称',
  `sort_description` varchar(256) NOT NULL COMMENT '分类描述',
  `sort_type` tinyint(2) NOT NULL DEFAULT 1 COMMENT '分类类型[0:导航栏分类，1:普通分类]',
  `priority` int DEFAULT NULL COMMENT '分类优先级：数字小的在前面',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类';

DROP TABLE IF EXISTS `poetize`.`label`;

CREATE TABLE `poetize`.`label` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sort_id` int NOT NULL COMMENT '分类ID',
  `label_name` varchar(32) NOT NULL COMMENT '标签名称',
  `label_description` varchar(256) NOT NULL COMMENT '标签描述',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签';

DROP TABLE IF EXISTS `poetize`.`tree_hole`;

CREATE TABLE `poetize`.`tree_hole` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `avatar` varchar(256) DEFAULT NULL COMMENT '头像',
  `message` varchar(64) NOT NULL COMMENT '留言',

  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='树洞';

DROP TABLE IF EXISTS `poetize`.`wei_yan`;

CREATE TABLE `poetize`.`wei_yan` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int NOT NULL COMMENT '用户ID',
  `like_count` int NOT NULL DEFAULT 0 COMMENT '点赞数',
  `content` varchar(1024) NOT NULL COMMENT '内容',
  `type` varchar(32) NOT NULL COMMENT '类型',
  `source` int DEFAULT NULL COMMENT '来源标识',
  `is_public` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否公开[0:仅自己可见，1:所有人可见]',

  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微言表';

DROP TABLE IF EXISTS `poetize`.`web_info`;

CREATE TABLE `poetize`.`web_info` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `web_name` varchar(16) NOT NULL COMMENT '网站名称',
  `web_title` varchar(512) NOT NULL COMMENT '网站信息',
  `notices` varchar(512) DEFAULT NULL COMMENT '公告',
  `footer` varchar(256) NOT NULL COMMENT '页脚',
  `background_image` varchar(256) DEFAULT NULL COMMENT '背景',
  `avatar` varchar(256) NOT NULL COMMENT '头像',
  `random_avatar` text DEFAULT NULL COMMENT '随机头像',
  `random_name` varchar(4096) DEFAULT NULL COMMENT '随机名称',
  `random_cover` text DEFAULT NULL COMMENT '随机封面',
  `waifu_json` text DEFAULT NULL COMMENT '看板娘消息',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用[0:否，1:是]',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网站信息表';

DROP TABLE IF EXISTS `poetize`.`resource_path`;

CREATE TABLE `poetize`.`resource_path` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(64) NOT NULL COMMENT '标题',
  `classify` varchar(32) DEFAULT NULL COMMENT '分类',
  `cover` varchar(256) DEFAULT NULL COMMENT '封面',
  `url` varchar(256) DEFAULT NULL COMMENT '链接',
  `introduction` varchar(1024) DEFAULT NULL COMMENT '简介',
  `type` varchar(32) NOT NULL COMMENT '资源类型',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用[0:否，1:是]',
  `remark` text DEFAULT NULL COMMENT '备注',

  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源聚合';

DROP TABLE IF EXISTS `poetize`.`resource`;

CREATE TABLE `poetize`.`resource` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int NOT NULL COMMENT '用户ID',
  `type` varchar(32) NOT NULL COMMENT '资源类型',
  `path` varchar(256) NOT NULL COMMENT '资源路径',
  `size` int DEFAULT NULL COMMENT '资源内容的大小，单位：字节',
  `original_name` varchar(512) DEFAULT NULL COMMENT '文件名称',
  `mime_type` varchar(256) DEFAULT NULL COMMENT '资源的 MIME 类型',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用[0:否，1:是]',
  `store_type` varchar(16) DEFAULT NULL COMMENT '存储平台',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_path` (`path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源信息';

DROP TABLE IF EXISTS `poetize`.`history_info`;

CREATE TABLE `poetize`.`history_info` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int DEFAULT NULL COMMENT '用户ID',
  `ip` varchar(128) NOT NULL COMMENT 'ip',
  `nation` varchar(64) DEFAULT NULL COMMENT '国家',
  `province` varchar(64) DEFAULT NULL COMMENT '省份',
  `city` varchar(64) DEFAULT NULL COMMENT '城市',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='历史信息';

DROP TABLE IF EXISTS `poetize`.`sys_config`;

CREATE TABLE `poetize`.`sys_config` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `config_name` varchar(128) NOT NULL COMMENT '名称',
  `config_key` varchar(64) NOT NULL COMMENT '键名',
  `config_value` varchar(256) DEFAULT NULL COMMENT '键值',
  `config_type` char(1) NOT NULL COMMENT '1 私用 2 公开',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='参数配置表';

DROP TABLE IF EXISTS `poetize`.`family`;

CREATE TABLE `poetize`.`family` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int NOT NULL COMMENT '用户ID',
  `bg_cover` varchar(256) NOT NULL COMMENT '背景封面',
  `man_cover` varchar(256) NOT NULL COMMENT '男生头像',
  `woman_cover` varchar(256) NOT NULL COMMENT '女生头像',
  `man_name` varchar(32) NOT NULL COMMENT '男生昵称',
  `woman_name` varchar(32) NOT NULL COMMENT '女生昵称',
  `timing` varchar(32) NOT NULL COMMENT '计时',
  `countdown_title` varchar(32) DEFAULT NULL COMMENT '倒计时标题',
  `countdown_time` varchar(32) DEFAULT NULL COMMENT '倒计时时间',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用[0:否，1:是]',
  `family_info` varchar(1024) DEFAULT NULL COMMENT '额外信息',
  `like_count` int NOT NULL DEFAULT 0 COMMENT '点赞数',

  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最终修改时间',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家庭信息';


DROP TABLE IF EXISTS `poetize`.`im_chat_user_friend`;

CREATE TABLE `poetize`.`im_chat_user_friend` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int NOT NULL COMMENT '用户ID',
  `friend_id` int NOT NULL COMMENT '好友ID',
  `friend_status` tinyint(2) NOT NULL COMMENT '朋友状态[0:未审核，1:审核通过]',
  `remark` varchar(32) DEFAULT NULL COMMENT '备注',

  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='好友';

DROP TABLE IF EXISTS `poetize`.`im_chat_group`;

CREATE TABLE `poetize`.`im_chat_group` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `group_name` varchar(32) NOT NULL COMMENT '群名称',
  `master_user_id` int NOT NULL COMMENT '群主用户ID',
  `avatar` varchar(256) DEFAULT NULL COMMENT '群头像',
  `introduction` varchar(128) DEFAULT NULL COMMENT '简介',
  `notice` varchar(1024) DEFAULT NULL COMMENT '公告',
  `in_type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '进入方式[0:无需验证，1:需要群主或管理员同意]',
  `group_type` tinyint(2) NOT NULL DEFAULT 1 COMMENT '类型[1:聊天群，2:话题]',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天群';

DROP TABLE IF EXISTS `poetize`.`im_chat_group_user`;

CREATE TABLE `poetize`.`im_chat_group_user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `group_id` int NOT NULL COMMENT '群ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `verify_user_id` int DEFAULT NULL COMMENT '审核用户ID',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `admin_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否管理员[0:否，1:是]',
  `user_status` tinyint(2) NOT NULL COMMENT '用户状态[0:未审核，1:审核通过，2:禁言]',

  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天群成员';

DROP TABLE IF EXISTS `poetize`.`im_chat_user_message`;

CREATE TABLE `poetize`.`im_chat_user_message` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `from_id` int NOT NULL COMMENT '发送ID',
  `to_id` int NOT NULL COMMENT '接收ID',
  `content` varchar(1024) NOT NULL COMMENT '内容',
  `message_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已读[0:未读，1:已读]',

  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

  PRIMARY KEY (`id`),
  KEY `union_index` (`to_id`,`message_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='单聊记录';

DROP TABLE IF EXISTS `poetize`.`im_chat_user_group_message`;

CREATE TABLE `poetize`.`im_chat_user_group_message` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `group_id` int NOT NULL COMMENT '群ID',
  `from_id` int NOT NULL COMMENT '发送ID',
  `to_id` int DEFAULT NULL COMMENT '接收ID',
  `content` varchar(1024) NOT NULL COMMENT '内容',

  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='群聊记录';




INSERT INTO `poetize`.`user`(`id`, `username`, `password`, `phone_number`, `email`, `user_status`, `gender`, `open_id`, `admire`, `subscribe`, `avatar`, `introduction`, `user_type`, `update_by`, `deleted`) VALUES (1, 'Sara', '47bce5c74f589f4867dbd57e9ca9f808', '', '', 1, 1, '', '', '', '', '', 0, 'Sara', 0);

INSERT INTO `poetize`.`web_info`(`id`, `web_name`, `web_title`, `notices`, `footer`, `background_image`, `avatar`, `random_avatar`, `random_name`, `random_cover`, `waifu_json`, `status`) VALUES (1, 'Sara', 'POETIZE', '[]', '云想衣裳花想容， 春风拂槛露华浓。', '', '', '[]', '[]', '[]', '{}', 1);

INSERT INTO `poetize`.`family` (`id`, `user_id`, `bg_cover`, `man_cover`, `woman_cover`, `man_name`, `woman_name`, `timing`, `countdown_title`, `countdown_time`, `status`, `family_info`, `like_count`, `create_time`, `update_time`) VALUES (1, 1, '背景封面', '男生头像', '女生头像', 'Sara', 'Abby', '2000-01-01 00:00:00', '春节倒计时', '2025-01-29 00:00:00', 1, '', 0, '2000-01-01 00:00:00', '2000-01-01 00:00:00');

INSERT INTO `poetize`.`im_chat_group` (`id`, `group_name`, `master_user_id`, `introduction`, `notice`, `in_type`) VALUES(-1, '公共聊天室', 1, '公共聊天室', '欢迎光临！', 0);

insert into `poetize`.`im_chat_group_user` (`id`, `group_id`, `user_id`, `admin_flag`, `user_status`) values(1, -1, 1, 1, 1);




INSERT INTO `poetize`.`sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`) VALUES (1, 'QQ邮箱号', 'spring.mail.username', '', '1');
INSERT INTO `poetize`.`sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`) VALUES (2, 'QQ邮箱授权码', 'spring.mail.password', '', '1');
INSERT INTO `poetize`.`sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`) VALUES (3, '邮箱验证码模板', 'user.code.format', '【POETIZE】%s为本次验证的验证码，请在5分钟内完成验证。为保证账号安全，请勿泄漏此验证码。', '1');
INSERT INTO `poetize`.`sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`) VALUES (4, '邮箱订阅模板', 'user.subscribe.format', '【POETIZE】您订阅的专栏【%s】新增一篇文章：%s。', '1');
INSERT INTO `poetize`.`sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`) VALUES (5, '默认存储平台', 'store.type', 'local', '2');
INSERT INTO `poetize`.`sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`) VALUES (6, '本地存储启用状态', 'local.enable', 'true', '2');
INSERT INTO `poetize`.`sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`) VALUES (7, '七牛云存储启用状态', 'qiniu.enable', 'false', '2');
INSERT INTO `poetize`.`sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`) VALUES (8, '本地存储上传文件根目录', 'local.uploadUrl', '/home/file/', '1');
INSERT INTO `poetize`.`sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`) VALUES (9, '本地存储下载前缀', 'local.downloadUrl', '仿照：【https://poetize.cn/static/】，将域名换成自己的服务器ip或域名', '2');
INSERT INTO `poetize`.`sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`) VALUES (10, '七牛云-accessKey', 'qiniu.accessKey', '', '1');
INSERT INTO `poetize`.`sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`) VALUES (11, '七牛云-secretKey', 'qiniu.secretKey', '', '1');
INSERT INTO `poetize`.`sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`) VALUES (12, '七牛云-bucket', 'qiniu.bucket', '', '1');
INSERT INTO `poetize`.`sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`) VALUES (13, '七牛云-域名', 'qiniu.downloadUrl', '仿照：【https://file.poetize.cn/】，将域名换成自己的七牛云ip或域名', '2');
INSERT INTO `poetize`.`sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`) VALUES (15, 'IM-聊天室启用状态', 'im.enable', 'true', '1');
INSERT INTO `poetize`.`sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`) VALUES (16, '七牛云上传地址', 'qiniuUrl', 'https://upload.qiniup.com', '2');
INSERT INTO `poetize`.`sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`) VALUES (17, '备案号', 'beian', '', '2');
INSERT INTO `poetize`.`sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`) VALUES (18, '前端静态资源路径前缀', 'webStaticResourcePrefix', '仿照：【https://poetize.cn/static/】', '2');
