package com.ld.poetry.utils;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.ld.poetry.dao.ArticleMapper;
import com.ld.poetry.dao.CommentMapper;
import com.ld.poetry.dao.LabelMapper;
import com.ld.poetry.dao.SortMapper;
import com.ld.poetry.entity.*;
import com.ld.poetry.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class CommonQuery {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private SortMapper sortMapper;

    @Autowired
    private LabelMapper labelMapper;

    @Autowired
    private ArticleMapper articleMapper;

    public User getUser(Integer userId) {
        User user = (User) PoetryCache.get(CommonConst.USER_CACHE + userId.toString());
        if (user != null) {
            return user;
        }
        User u = userService.getById(userId);
        if (u != null) {
            PoetryCache.put(CommonConst.USER_CACHE + userId.toString(), u, CommonConst.EXPIRE);
            return u;
        }
        return null;
    }

    public Integer getCommentCount(Integer source) {
        Integer count = (Integer) PoetryCache.get(CommonConst.COMMENT_COUNT_CACHE + source.toString());
        if (count != null) {
            return count;
        }
        LambdaQueryChainWrapper<Comment> wrapper = new LambdaQueryChainWrapper<>(commentMapper);
        Integer c = wrapper.eq(Comment::getSource, source).count();
        PoetryCache.put(CommonConst.COMMENT_COUNT_CACHE + source.toString(), c, CommonConst.EXPIRE);
        return c;
    }

    public List<Integer> getUserArticleIds(Integer userId) {
        List<Integer> ids = (List<Integer>) PoetryCache.get(CommonConst.USER_ARTICLE_LIST + userId.toString());
        if (ids != null) {
            return ids;
        }
        LambdaQueryChainWrapper<Article> wrapper = new LambdaQueryChainWrapper<>(articleMapper);
        List<Article> articles = wrapper.eq(Article::getUserId, userId).select(Article::getId).list();
        List<Integer> collect = articles.stream().map(Article::getId).collect(Collectors.toList());
        PoetryCache.put(CommonConst.USER_ARTICLE_LIST + userId.toString(), collect, CommonConst.EXPIRE);
        return collect;
    }

    public List<Sort> getSortInfo() {
        List<Sort> sorts = new LambdaQueryChainWrapper<>(sortMapper).list();
        if (!CollectionUtils.isEmpty(sorts)) {
            sorts.forEach(sort -> {
                LambdaQueryChainWrapper<Article> sortWrapper = new LambdaQueryChainWrapper<>(articleMapper);
                Integer countOfSort = sortWrapper.eq(Article::getSortId, sort.getId()).eq(Article::getViewStatus, PoetryEnum.STATUS_ENABLE.getCode()).count();
                sort.setCountOfSort(countOfSort);

                LambdaQueryChainWrapper<Label> wrapper = new LambdaQueryChainWrapper<>(labelMapper);
                List<Label> labels = wrapper.eq(Label::getSortId, sort.getId()).list();
                if (!CollectionUtils.isEmpty(labels)) {
                    labels.forEach(label -> {
                        LambdaQueryChainWrapper<Article> labelWrapper = new LambdaQueryChainWrapper<>(articleMapper);
                        Integer countOfLabel = labelWrapper.eq(Article::getLabelId, label.getId()).eq(Article::getViewStatus, PoetryEnum.STATUS_ENABLE.getCode()).count();
                        label.setCountOfLabel(countOfLabel);
                    });
                    sort.setLabels(labels);
                }
            });
            return sorts;
        } else {
            return null;
        }
    }
}
