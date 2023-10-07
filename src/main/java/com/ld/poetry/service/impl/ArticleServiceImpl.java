package com.ld.poetry.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ld.poetry.config.PoetryResult;
import com.ld.poetry.dao.ArticleMapper;
import com.ld.poetry.dao.LabelMapper;
import com.ld.poetry.entity.*;
import com.ld.poetry.service.ArticleService;
import com.ld.poetry.service.UserService;
import com.ld.poetry.utils.*;
import com.ld.poetry.vo.ArticleVO;
import com.ld.poetry.vo.BaseRequestVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author sara
 * @since 2021-08-13
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CommonQuery commonQuery;

    @Autowired
    private UserService userService;

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private LabelMapper labelMapper;

    @Value("${user.subscribe.format}")
    private String subscribeFormat;

    @Override
    public PoetryResult saveArticle(ArticleVO articleVO) {
        if (articleVO.getViewStatus() != null && !articleVO.getViewStatus() && !StringUtils.hasText(articleVO.getPassword())) {
            return PoetryResult.fail("请设置文章密码！");
        }
        Article article = new Article();
        if (StringUtils.hasText(articleVO.getArticleCover())) {
            article.setArticleCover(articleVO.getArticleCover());
        }
        if (articleVO.getViewStatus() != null && !articleVO.getViewStatus() && StringUtils.hasText(articleVO.getPassword())) {
            article.setPassword(articleVO.getPassword());
            article.setTips(articleVO.getTips());
        }
        article.setViewStatus(articleVO.getViewStatus());
        article.setCommentStatus(articleVO.getCommentStatus());
        article.setRecommendStatus(articleVO.getRecommendStatus());
        article.setArticleTitle(articleVO.getArticleTitle());
        article.setArticleContent(articleVO.getArticleContent());
        article.setSortId(articleVO.getSortId());
        article.setLabelId(articleVO.getLabelId());
        article.setUserId(PoetryUtil.getUserId());
        save(article);

        List<Sort> sortInfo = commonQuery.getSortInfo();
        if (!CollectionUtils.isEmpty(sortInfo)) {
            PoetryCache.put(CommonConst.SORT_INFO, sortInfo);
        }

        try {
            if (articleVO.getViewStatus()) {
                List<User> users = userService.lambdaQuery().select(User::getEmail, User::getSubscribe).eq(User::getUserStatus, PoetryEnum.STATUS_ENABLE.getCode()).list();
                List<String> emails = users.stream().filter(u -> {
                    List<Integer> sub = JSON.parseArray(u.getSubscribe(), Integer.class);
                    return !CollectionUtils.isEmpty(sub) && sub.contains(articleVO.getLabelId());
                }).map(User::getEmail).collect(Collectors.toList());

                if (!CollectionUtils.isEmpty(emails)) {
                    LambdaQueryChainWrapper<Label> wrapper = new LambdaQueryChainWrapper<>(labelMapper);
                    Label label = wrapper.select(Label::getLabelName).eq(Label::getId, articleVO.getLabelId()).one();
                    String text = getSubscribeMail(label.getLabelName(), articleVO.getArticleTitle());
                    WebInfo webInfo = (WebInfo) PoetryCache.get(CommonConst.WEB_INFO);
                    mailUtil.sendMailMessage(emails, "您有一封来自" + (webInfo == null ? "Poetize" : webInfo.getWebName()) + "的回执！", text);
                }
            }
        } catch (Exception e) {
            log.error("订阅邮件发送失败：", e);
        }
        return PoetryResult.success();
    }

    private String getSubscribeMail(String labelName, String articleTitle) {
        WebInfo webInfo = (WebInfo) PoetryCache.get(CommonConst.WEB_INFO);
        String webName = (webInfo == null ? "Poetize" : webInfo.getWebName());
        return String.format(mailUtil.getMailText(),
                webName,
                String.format(MailUtil.notificationMail, PoetryUtil.getAdminUser().getUsername()),
                PoetryUtil.getAdminUser().getUsername(),
                String.format(subscribeFormat, labelName, articleTitle),
                "",
                webName);
    }

    @Override
    public PoetryResult deleteArticle(Integer id) {
        Integer userId = PoetryUtil.getUserId();
        lambdaUpdate().eq(Article::getId, id)
                .eq(Article::getUserId, userId)
                .remove();
        List<Sort> sortInfo = commonQuery.getSortInfo();
        if (!CollectionUtils.isEmpty(sortInfo)) {
            PoetryCache.put(CommonConst.SORT_INFO, sortInfo);
        }
        return PoetryResult.success();
    }

    @Override
    public PoetryResult updateArticle(ArticleVO articleVO) {
        if (articleVO.getViewStatus() != null && !articleVO.getViewStatus() && !StringUtils.hasText(articleVO.getPassword())) {
            return PoetryResult.fail("请设置文章密码！");
        }

        Integer userId = PoetryUtil.getUserId();
        LambdaUpdateChainWrapper<Article> updateChainWrapper = lambdaUpdate()
                .eq(Article::getId, articleVO.getId())
                .eq(Article::getUserId, userId)
                .set(Article::getLabelId, articleVO.getLabelId())
                .set(Article::getSortId, articleVO.getSortId())
                .set(Article::getArticleTitle, articleVO.getArticleTitle())
                .set(Article::getUpdateBy, PoetryUtil.getUsername())
                .set(Article::getArticleContent, articleVO.getArticleContent());

        if (StringUtils.hasText(articleVO.getArticleCover())) {
            updateChainWrapper.set(Article::getArticleCover, articleVO.getArticleCover());
        }
        if (articleVO.getCommentStatus() != null) {
            updateChainWrapper.set(Article::getCommentStatus, articleVO.getCommentStatus());
        }

        if (articleVO.getRecommendStatus() != null) {
            updateChainWrapper.set(Article::getRecommendStatus, articleVO.getRecommendStatus());
        }

        if (articleVO.getViewStatus() != null && !articleVO.getViewStatus() && StringUtils.hasText(articleVO.getPassword())) {
            updateChainWrapper.set(Article::getPassword, articleVO.getPassword());
            updateChainWrapper.set(StringUtils.hasText(articleVO.getTips()), Article::getTips, articleVO.getTips());
        }
        if (articleVO.getViewStatus() != null) {
            updateChainWrapper.set(Article::getViewStatus, articleVO.getViewStatus());
        }
        updateChainWrapper.update();
        List<Sort> sortInfo = commonQuery.getSortInfo();
        if (!CollectionUtils.isEmpty(sortInfo)) {
            PoetryCache.put(CommonConst.SORT_INFO, sortInfo);
        }
        return PoetryResult.success();
    }

    @Override
    public PoetryResult<Page> listArticle(BaseRequestVO baseRequestVO) {
        List<Integer> ids = null;
        List<List<Integer>> idList = null;
        if (StringUtils.hasText(baseRequestVO.getArticleSearch())) {
            idList = commonQuery.getArticleIds(baseRequestVO.getArticleSearch());
            ids = idList.stream().flatMap(Collection::stream).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(ids)) {
                baseRequestVO.setRecords(new ArrayList<>());
                return PoetryResult.success(baseRequestVO);
            }
        }

        LambdaQueryChainWrapper<Article> lambdaQuery = lambdaQuery();
        lambdaQuery.in(!CollectionUtils.isEmpty(ids), Article::getId, ids);
        lambdaQuery.like(StringUtils.hasText(baseRequestVO.getSearchKey()), Article::getArticleTitle, baseRequestVO.getSearchKey());
        lambdaQuery.eq(baseRequestVO.getRecommendStatus() != null && baseRequestVO.getRecommendStatus(), Article::getRecommendStatus, PoetryEnum.STATUS_ENABLE.getCode());


        if (baseRequestVO.getLabelId() != null) {
            lambdaQuery.eq(Article::getLabelId, baseRequestVO.getLabelId());
        } else if (baseRequestVO.getSortId() != null) {
            lambdaQuery.eq(Article::getSortId, baseRequestVO.getSortId());
        }

        lambdaQuery.orderByDesc(Article::getCreateTime);

        lambdaQuery.page(baseRequestVO);

        List<Article> records = baseRequestVO.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            List<ArticleVO> articles = new ArrayList<>();
            List<ArticleVO> titles = new ArrayList<>();
            List<ArticleVO> contents = new ArrayList<>();

            for (Article article : records) {
                article.setPassword(null);
                if (article.getArticleContent().length() > CommonConst.SUMMARY) {
                    article.setArticleContent(article.getArticleContent().substring(0, CommonConst.SUMMARY).replace("`", "").replace("#", "").replace(">", ""));
                }
                ArticleVO articleVO = buildArticleVO(article, false);
                if (CollectionUtils.isEmpty(ids)) {
                    articles.add(articleVO);
                } else if (idList.get(0).contains(articleVO.getId())) {
                    titles.add(articleVO);
                } else if (idList.get(1).contains(articleVO.getId())) {
                    contents.add(articleVO);
                }
            }

            List<ArticleVO> collect = new ArrayList<>();
            collect.addAll(articles);
            collect.addAll(titles);
            collect.addAll(contents);
            baseRequestVO.setRecords(collect);
        }
        return PoetryResult.success(baseRequestVO);
    }

    @Override
    public PoetryResult<ArticleVO> getArticleById(Integer id, String password) {
        LambdaQueryChainWrapper<Article> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(Article::getId, id);

        Article article = lambdaQuery.one();
        if (article == null) {
            return PoetryResult.success();
        }
        if (!article.getViewStatus() && (!StringUtils.hasText(password) || !password.equals(article.getPassword()))) {
            return PoetryResult.fail("密码错误" + (StringUtils.hasText(article.getTips()) ? article.getTips() : "请联系作者获取密码"));
        }
        article.setPassword(null);
        articleMapper.updateViewCount(id);
        ArticleVO articleVO = buildArticleVO(article, false);
        return PoetryResult.success(articleVO);
    }

    @Override
    public PoetryResult<Page> listAdminArticle(BaseRequestVO baseRequestVO, Boolean isBoss) {
        LambdaQueryChainWrapper<Article> lambdaQuery = lambdaQuery();
        lambdaQuery.select(Article.class, a -> !a.getColumn().equals("article_content"));
        if (!isBoss) {
            lambdaQuery.eq(Article::getUserId, PoetryUtil.getUserId());
        } else {
            if (baseRequestVO.getUserId() != null) {
                lambdaQuery.eq(Article::getUserId, baseRequestVO.getUserId());
            }
        }
        if (StringUtils.hasText(baseRequestVO.getSearchKey())) {
            lambdaQuery.like(Article::getArticleTitle, baseRequestVO.getSearchKey());
        }
        if (baseRequestVO.getRecommendStatus() != null && baseRequestVO.getRecommendStatus()) {
            lambdaQuery.eq(Article::getRecommendStatus, PoetryEnum.STATUS_ENABLE.getCode());
        }

        if (baseRequestVO.getLabelId() != null) {
            lambdaQuery.eq(Article::getLabelId, baseRequestVO.getLabelId());
        }

        if (baseRequestVO.getSortId() != null) {
            lambdaQuery.eq(Article::getSortId, baseRequestVO.getSortId());
        }

        lambdaQuery.orderByDesc(Article::getCreateTime).page(baseRequestVO);

        List<Article> records = baseRequestVO.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            List<ArticleVO> collect = records.stream().map(article -> {
                article.setPassword(null);
                ArticleVO articleVO = buildArticleVO(article, true);
                return articleVO;
            }).collect(Collectors.toList());
            baseRequestVO.setRecords(collect);
        }
        return PoetryResult.success(baseRequestVO);
    }

    @Override
    public PoetryResult<ArticleVO> getArticleByIdForUser(Integer id) {
        LambdaQueryChainWrapper<Article> lambdaQuery = lambdaQuery();
        lambdaQuery.eq(Article::getId, id).eq(Article::getUserId, PoetryUtil.getUserId());
        Article article = lambdaQuery.one();
        if (article == null) {
            return PoetryResult.fail("文章不存在！");
        }
        ArticleVO articleVO = new ArticleVO();
        BeanUtils.copyProperties(article, articleVO);
        return PoetryResult.success(articleVO);
    }

    private ArticleVO buildArticleVO(Article article, Boolean isAdmin) {
        ArticleVO articleVO = new ArticleVO();
        BeanUtils.copyProperties(article, articleVO);
        if (!isAdmin) {
            if (!StringUtils.hasText(articleVO.getArticleCover())) {
                articleVO.setArticleCover(PoetryUtil.getRandomCover(articleVO.getId().toString()));
            }
        }

        User user = commonQuery.getUser(articleVO.getUserId());
        if (user != null && StringUtils.hasText(user.getUsername())) {
            articleVO.setUsername(user.getUsername());
        } else if (!isAdmin) {
            articleVO.setUsername(PoetryUtil.getRandomName(articleVO.getUserId().toString()));
        }
        if (articleVO.getCommentStatus()) {
            articleVO.setCommentCount(commonQuery.getCommentCount(articleVO.getId(), CommentTypeEnum.COMMENT_TYPE_ARTICLE.getCode()));
        } else {
            articleVO.setCommentCount(0);
        }

        List<Sort> sortInfo = (List<Sort>) PoetryCache.get(CommonConst.SORT_INFO);
        if (sortInfo != null) {
            for (Sort s : sortInfo) {
                if (s.getId().intValue() == articleVO.getSortId().intValue()) {
                    Sort sort = new Sort();
                    BeanUtils.copyProperties(s, sort);
                    sort.setLabels(null);
                    articleVO.setSort(sort);
                    if (!CollectionUtils.isEmpty(s.getLabels())) {
                        for (int j = 0; j < s.getLabels().size(); j++) {
                            Label l = s.getLabels().get(j);
                            if (l.getId().intValue() == articleVO.getLabelId().intValue()) {
                                Label label = new Label();
                                BeanUtils.copyProperties(l, label);
                                articleVO.setLabel(label);
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
        return articleVO;
    }
}
