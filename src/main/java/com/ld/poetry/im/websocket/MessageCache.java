package com.ld.poetry.im.websocket;

import com.ld.poetry.im.http.entity.ImChatUserGroupMessage;
import com.ld.poetry.im.http.entity.ImChatUserMessage;
import com.ld.poetry.im.http.service.ImChatUserGroupMessageService;
import com.ld.poetry.im.http.service.ImChatUserMessageService;
import com.ld.poetry.utils.MailSendUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


@Component
@Slf4j
public class MessageCache {

    @Autowired
    private ImChatUserMessageService imChatUserMessageService;

    @Autowired
    private ImChatUserGroupMessageService imChatUserGroupMessageService;

    @Autowired
    private MailSendUtil mailSendUtil;

    private final List<ImChatUserMessage> userMessage = new ArrayList<>();

    private final List<ImChatUserGroupMessage> groupMessage = new ArrayList<>();

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public void putUserMessage(ImChatUserMessage message) {
        readWriteLock.readLock().lock();
        try {
            userMessage.add(message);
        } finally {
            readWriteLock.readLock().unlock();
        }

        try {
            mailSendUtil.sendImMail(message);
        } catch (Exception e) {
            log.error("发送IM邮件失败：", e);
        }
    }

    public void putGroupMessage(ImChatUserGroupMessage message) {
        readWriteLock.readLock().lock();
        try {
            groupMessage.add(message);
        } finally {
            readWriteLock.readLock().unlock();
        }

    }

    @Scheduled(fixedDelay = 5000)
    public void saveUserMessage() {
        readWriteLock.writeLock().lock();
        try {
            if (!CollectionUtils.isEmpty(userMessage)) {
                imChatUserMessageService.saveBatch(userMessage);
                userMessage.clear();
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Scheduled(fixedDelay = 10000)
    public void saveGroupMessage() {
        readWriteLock.writeLock().lock();
        try {
            if (!CollectionUtils.isEmpty(groupMessage)) {
                imChatUserGroupMessageService.saveBatch(groupMessage);
                groupMessage.clear();
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
}
