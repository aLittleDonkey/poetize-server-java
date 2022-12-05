package com.ld.poetry.im.http.controller;


import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ld.poetry.config.LoginCheck;
import com.ld.poetry.config.PoetryResult;
import com.ld.poetry.entity.User;
import com.ld.poetry.im.http.entity.ImChatUserMessage;
import com.ld.poetry.im.http.service.ImChatUserMessageService;
import com.ld.poetry.im.http.vo.UserMessageVO;
import com.ld.poetry.im.websocket.ImConfigConst;
import com.ld.poetry.utils.CommonQuery;
import com.ld.poetry.utils.PoetryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 单聊记录 前端控制器
 * </p>
 *
 * @author sara
 * @since 2021-12-02
 */
@RestController
@RequestMapping("/imChatUserMessage")
public class ImChatUserMessageController {

    @Autowired
    private ImChatUserMessageService imChatUserMessageService;

    @Autowired
    private CommonQuery commonQuery;

    /**
     * 获取系统消息（只获取前十条）
     */
    @GetMapping("/listSystemMessage")
    @LoginCheck
    public PoetryResult<Page> listSystemMessage(@RequestParam(value = "current", defaultValue = "1") Long current,
                                                @RequestParam(value = "size", defaultValue = "10") Long size) {
        Page<ImChatUserMessage> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);

        LambdaQueryChainWrapper<ImChatUserMessage> lambdaQuery = imChatUserMessageService.lambdaQuery();
        lambdaQuery.select(ImChatUserMessage::getId, ImChatUserMessage::getContent, ImChatUserMessage::getCreateTime);

        lambdaQuery.eq(ImChatUserMessage::getFromId, ImConfigConst.DEFAULT_SYSTEM_MESSAGE_ID);
        lambdaQuery.eq(ImChatUserMessage::getToId, ImConfigConst.DEFAULT_SYSTEM_MESSAGE_ID);
        lambdaQuery.orderByDesc(ImChatUserMessage::getCreateTime);
        Page<ImChatUserMessage> result = lambdaQuery.page(page);
        List<ImChatUserMessage> records = result.getRecords();
        Collections.reverse(records);
        if (CollectionUtils.isEmpty(records)) {
            return PoetryResult.success(result);
        } else {
            List<UserMessageVO> collect = records.stream().map(message -> {
                UserMessageVO userMessageVO = new UserMessageVO();
                userMessageVO.setContent(message.getContent());
                userMessageVO.setId(message.getId());
                userMessageVO.setCreateTime(message.getCreateTime());
                return userMessageVO;
            }).collect(Collectors.toList());
            Page<UserMessageVO> resultVO = new Page<>();
            resultVO.setRecords(collect);
            resultVO.setTotal(result.getTotal());
            resultVO.setCurrent(result.getCurrent());
            resultVO.setSize(result.getSize());
            return PoetryResult.success(resultVO);
        }
    }


    /**
     * 管理员添加系统消息
     */
    @GetMapping("/saveSystemMessage")
    @LoginCheck(0)
    public PoetryResult saveSystemMessage(@RequestParam("content") String content) {
        ImChatUserMessage userMessage = new ImChatUserMessage();
        userMessage.setContent(content);
        userMessage.setFromId(ImConfigConst.DEFAULT_SYSTEM_MESSAGE_ID);
        userMessage.setToId(ImConfigConst.DEFAULT_SYSTEM_MESSAGE_ID);
        userMessage.setMessageStatus(ImConfigConst.USER_MESSAGE_STATUS_TRUE);
        imChatUserMessageService.save(userMessage);
        return PoetryResult.success();
    }


    /**
     * 删除系统消息
     */
    @GetMapping("/deleteSystemMessage")
    @LoginCheck(0)
    public PoetryResult deleteSystemMessage(@RequestParam("id") Integer id) {
        imChatUserMessageService.removeById(id);
        return PoetryResult.success();
    }

    /**
     * 获取朋友消息（只获取前四十条）
     */
    @GetMapping("/listFriendMessage")
    @LoginCheck
    public PoetryResult<Page> listFriendMessage(@RequestParam(value = "current", defaultValue = "1") Long current,
                                                @RequestParam(value = "size", defaultValue = "40") Long size,
                                                @RequestParam(value = "friendId") Integer friendId) {
        Page<ImChatUserMessage> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);

        Integer userId = PoetryUtil.getUserId();

        LambdaQueryChainWrapper<ImChatUserMessage> lambdaQuery = imChatUserMessageService.lambdaQuery();
        lambdaQuery.and(wrapper -> wrapper.eq(ImChatUserMessage::getFromId, userId).eq(ImChatUserMessage::getToId, friendId))
                .or(wrapper -> wrapper.eq(ImChatUserMessage::getFromId, friendId).eq(ImChatUserMessage::getToId, userId));
        lambdaQuery.orderByDesc(ImChatUserMessage::getCreateTime);
        Page<ImChatUserMessage> result = lambdaQuery.page(page);
        List<ImChatUserMessage> records = result.getRecords();
        Collections.reverse(records);
        if (CollectionUtils.isEmpty(records)) {
            return PoetryResult.success(result);
        } else {
            List<UserMessageVO> collect = records.stream().map(message -> {
                UserMessageVO userMessageVO = new UserMessageVO();
                userMessageVO.setContent(message.getContent());
                userMessageVO.setFromId(message.getFromId());
                userMessageVO.setToId(message.getToId());
                userMessageVO.setMessageStatus(message.getMessageStatus());
                userMessageVO.setId(message.getId());
                userMessageVO.setCreateTime(message.getCreateTime());
                User from = commonQuery.getUser(message.getFromId());
                if (from != null) {
                    userMessageVO.setAvatar(from.getAvatar());
                }
                return userMessageVO;
            }).collect(Collectors.toList());
            Page<UserMessageVO> resultVO = new Page<>();
            resultVO.setRecords(collect);
            resultVO.setTotal(result.getTotal());
            resultVO.setCurrent(result.getCurrent());
            resultVO.setSize(result.getSize());
            return PoetryResult.success(resultVO);
        }
    }
}

