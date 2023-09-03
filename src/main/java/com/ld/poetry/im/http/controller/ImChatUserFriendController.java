package com.ld.poetry.im.http.controller;


import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.ld.poetry.config.LoginCheck;
import com.ld.poetry.config.PoetryResult;
import com.ld.poetry.entity.User;
import com.ld.poetry.im.http.entity.ImChatUserFriend;
import com.ld.poetry.im.http.service.ImChatUserFriendService;
import com.ld.poetry.im.http.vo.UserFriendVO;
import com.ld.poetry.im.websocket.ImConfigConst;
import com.ld.poetry.utils.CommonQuery;
import com.ld.poetry.utils.PoetryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 好友 前端控制器
 * </p>
 *
 * @author sara
 * @since 2021-12-02
 */
@RestController
@RequestMapping("/imChatUserFriend")
public class ImChatUserFriendController {

    @Autowired
    private ImChatUserFriendService userFriendService;

    @Autowired
    private CommonQuery commonQuery;

    /**
     * 添加好友申请
     */
    @GetMapping("/addFriend")
    @LoginCheck
    public PoetryResult addFriend(@RequestParam("friendId") Integer friendId, @RequestParam(value = "remark", required = false) String remark) {
        User friend = commonQuery.getUser(friendId);
        if (friend == null) {
            return PoetryResult.fail("用户不存在！");
        }

        Integer userId = PoetryUtil.getUserId();

        Integer count = userFriendService.lambdaQuery()
                .and(wrapper -> wrapper.eq(ImChatUserFriend::getUserId, userId).eq(ImChatUserFriend::getFriendId, friendId))
                .or(wrapper -> wrapper.eq(ImChatUserFriend::getFriendId, userId).eq(ImChatUserFriend::getUserId, friendId))
                .count();
        if (count > 0) {
            return PoetryResult.success();
        }

        ImChatUserFriend imChatFriend = new ImChatUserFriend();
        imChatFriend.setUserId(friendId);
        imChatFriend.setFriendId(userId);
        imChatFriend.setFriendStatus(ImConfigConst.FRIEND_STATUS_NOT_VERIFY);
        imChatFriend.setRemark(remark);
        userFriendService.save(imChatFriend);
        return PoetryResult.success();
    }

    /**
     * 查询好友
     */
    @GetMapping("/getFriend")
    @LoginCheck
    public PoetryResult<List<UserFriendVO>> getFriend(@RequestParam(value = "friendStatus", required = false) Integer friendStatus) {
        Integer userId = PoetryUtil.getUserId();
        LambdaQueryChainWrapper<ImChatUserFriend> wrapper = userFriendService.lambdaQuery().eq(ImChatUserFriend::getUserId, userId);
        if (friendStatus != null) {
            wrapper.eq(ImChatUserFriend::getFriendStatus, friendStatus);
        }

        List<ImChatUserFriend> userFriends = wrapper.orderByDesc(ImChatUserFriend::getCreateTime).list();
        List<UserFriendVO> userFriendVOS = new ArrayList<>(userFriends.size());
        userFriends.forEach(userFriend -> {
            User friend = commonQuery.getUser(userFriend.getFriendId());
            if (friend != null) {
                UserFriendVO userFriendVO = new UserFriendVO();
                userFriendVO.setId(userFriend.getId());
                userFriendVO.setUserId(userFriend.getUserId());
                userFriendVO.setFriendId(userFriend.getFriendId());
                userFriendVO.setCreateTime(userFriend.getCreateTime());
                userFriendVO.setRemark(StringUtils.hasText(userFriend.getRemark()) ? userFriend.getRemark() : friend.getUsername());
                userFriendVO.setFriendStatus(userFriend.getFriendStatus());
                userFriendVO.setUsername(friend.getUsername());
                userFriendVO.setAvatar(friend.getAvatar());
                userFriendVO.setGender(friend.getGender());
                userFriendVO.setIntroduction(friend.getIntroduction());
                userFriendVOS.add(userFriendVO);
            }
        });
        return PoetryResult.success(userFriendVOS);
    }

    /**
     * 修改好友
     * <p>
     * 朋友状态[-1:审核不通过或者删除好友，0:未审核，1:审核通过]
     */
    @GetMapping("/changeFriend")
    @LoginCheck
    public PoetryResult changeFriend(@RequestParam("friendId") Integer friendId,
                                     @RequestParam(value = "friendStatus", required = false) Integer friendStatus,
                                     @RequestParam(value = "remark", required = false) String remark) {
        Integer userId = PoetryUtil.getUserId();
        ImChatUserFriend userFriend = userFriendService.lambdaQuery()
                .eq(ImChatUserFriend::getUserId, userId)
                .eq(ImChatUserFriend::getFriendId, friendId).one();

        if (userFriend == null) {
            return PoetryResult.fail("好友不存在！");
        }

        if (friendStatus != null) {
            if (friendStatus == ImConfigConst.FRIEND_STATUS_PASS) {
                userFriendService.lambdaUpdate()
                        .set(ImChatUserFriend::getFriendStatus, friendStatus)
                        .eq(ImChatUserFriend::getId, userFriend.getId()).update();

                ImChatUserFriend imChatFriend = new ImChatUserFriend();
                imChatFriend.setUserId(friendId);
                imChatFriend.setFriendId(userId);
                imChatFriend.setFriendStatus(ImConfigConst.FRIEND_STATUS_PASS);
                userFriendService.save(imChatFriend);
            }

            if (friendStatus == ImConfigConst.FRIEND_STATUS_BAN) {
                userFriendService.removeById(userFriend.getId());
                userFriendService.lambdaUpdate()
                        .eq(ImChatUserFriend::getUserId, friendId)
                        .eq(ImChatUserFriend::getFriendId, userId).remove();
            }
        }

        if (StringUtils.hasText(remark)) {
            userFriendService.lambdaUpdate()
                    .set(ImChatUserFriend::getRemark, remark)
                    .eq(ImChatUserFriend::getId, userFriend.getId()).update();
        }


        return PoetryResult.success();
    }
}

