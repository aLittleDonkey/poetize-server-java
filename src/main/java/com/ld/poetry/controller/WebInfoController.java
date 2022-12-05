package com.ld.poetry.controller;


import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ld.poetry.config.LoginCheck;
import com.ld.poetry.config.PoetryResult;
import com.ld.poetry.dao.LabelMapper;
import com.ld.poetry.dao.ResourcePathMapper;
import com.ld.poetry.dao.SortMapper;
import com.ld.poetry.dao.TreeHoleMapper;
import com.ld.poetry.entity.*;
import com.ld.poetry.service.WebInfoService;
import com.ld.poetry.utils.*;
import com.ld.poetry.vo.BaseRequestVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * <p>
 * 网站信息表 前端控制器
 * </p>
 *
 * @author sara
 * @since 2021-09-14
 * <p>
 * 仅站长可以操作
 */
@RestController
@RequestMapping("/webInfo")
public class WebInfoController {

    @Autowired
    private WebInfoService webInfoService;

    @Autowired
    private ResourcePathMapper resourcePathMapper;

    @Autowired
    private TreeHoleMapper treeHoleMapper;

    @Autowired
    private SortMapper sortMapper;

    @Autowired
    private LabelMapper labelMapper;

    @Autowired
    private CommonQuery commonQuery;


    /**
     * 更新网站信息
     */
    @LoginCheck(0)
    @PostMapping("/updateWebInfo")
    public PoetryResult<WebInfo> updateWebInfo(@RequestBody WebInfo webInfo) {
        webInfoService.updateById(webInfo);

        LambdaQueryChainWrapper<WebInfo> wrapper = new LambdaQueryChainWrapper<>(webInfoService.getBaseMapper());
        List<WebInfo> list = wrapper.list();
        if (!CollectionUtils.isEmpty(list)) {
            PoetryCache.put(CommonConst.WEB_INFO, list.get(0));
        }
        return PoetryResult.success();
    }


    /**
     * 获取网站信息
     */
    @GetMapping("/getWebInfo")
    public PoetryResult<WebInfo> getWebInfo() {
        WebInfo webInfo = (WebInfo) PoetryCache.get(CommonConst.WEB_INFO);
        if (webInfo != null) {
            WebInfo result = new WebInfo();
            BeanUtils.copyProperties(webInfo, result);
            result.setRandomAvatar(null);
            result.setRandomCover(null);
            result.setRandomName(null);
            result.setWaifuJson(null);
            return PoetryResult.success(result);
        }
        return PoetryResult.success();
    }

    /**
     * 获取分类标签信息
     */
    @GetMapping("/getSortInfo")
    public PoetryResult<List<Sort>> getSortInfo() {
        List<Sort> sortInfo = (List<Sort>) PoetryCache.get(CommonConst.SORT_INFO);
        if (sortInfo != null) {
            return PoetryResult.success(sortInfo);
        }
        return PoetryResult.success();
    }

    /**
     * 获取看板娘消息
     */
    @GetMapping("/getWaifuJson")
    public String getWaifuJson() {
        WebInfo webInfo = (WebInfo) PoetryCache.get(CommonConst.WEB_INFO);
        if (webInfo != null && StringUtils.hasText(webInfo.getWaifuJson())) {
            return webInfo.getWaifuJson();
        }
        return "{}";
    }


    /**
     * 保存
     */
    @LoginCheck(0)
    @PostMapping("/saveResourcePath")
    public PoetryResult saveResourcePath(@RequestBody ResourcePath resourcePath) {
        if (!StringUtils.hasText(resourcePath.getTitle()) || !StringUtils.hasText(resourcePath.getType())) {
            return PoetryResult.fail("标题和资源类型不能为空！");
        }
        resourcePathMapper.insert(resourcePath);
        return PoetryResult.success();
    }

    /**
     * 保存友链
     */
    @LoginCheck
    @PostMapping("/saveFriend")
    public PoetryResult saveFriend(@RequestBody ResourcePath resourcePath) {
        PoetryUtil.checkEmail();
        if (!StringUtils.hasText(resourcePath.getTitle()) || !StringUtils.hasText(resourcePath.getCover()) ||
                !StringUtils.hasText(resourcePath.getUrl()) || !StringUtils.hasText(resourcePath.getIntroduction())) {
            return PoetryResult.fail("信息不全！");
        }
        ResourcePath friend = new ResourcePath();
        friend.setTitle(resourcePath.getTitle());
        friend.setIntroduction(resourcePath.getIntroduction());
        friend.setCover(resourcePath.getCover());
        friend.setUrl(resourcePath.getUrl());
        friend.setType(CommonConst.RESOURCE_PATH_TYPE_FRIEND);
        friend.setStatus(Boolean.FALSE);
        resourcePathMapper.insert(friend);
        return PoetryResult.success();
    }


    /**
     * 删除
     */
    @GetMapping("/deleteResourcePath")
    @LoginCheck(0)
    public PoetryResult deleteResourcePath(@RequestParam("id") Integer id) {
        resourcePathMapper.deleteById(id);
        return PoetryResult.success();
    }


    /**
     * 更新
     */
    @PostMapping("/updateResourcePath")
    @LoginCheck(0)
    public PoetryResult updateResourcePath(@RequestBody ResourcePath resourcePath) {
        if (!StringUtils.hasText(resourcePath.getTitle()) || !StringUtils.hasText(resourcePath.getType())) {
            return PoetryResult.fail("标题和资源类型不能为空！");
        }
        if (resourcePath.getId() == null) {
            return PoetryResult.fail("Id不能为空！");
        }
        resourcePathMapper.updateById(resourcePath);
        return PoetryResult.success();
    }


    /**
     * 查询资源
     */
    @PostMapping("/listResourcePath")
    public PoetryResult<Page> listResourcePath(@RequestBody BaseRequestVO baseRequestVO) {
        LambdaQueryChainWrapper<ResourcePath> wrapper = new LambdaQueryChainWrapper<>(resourcePathMapper);
        wrapper.eq(StringUtils.hasText(baseRequestVO.getResourceType()), ResourcePath::getType, baseRequestVO.getResourceType());

        Integer userId = PoetryUtil.getUserId();
        if (!PoetryUtil.getAdminUser().getId().equals(userId)) {
            wrapper.eq(ResourcePath::getStatus, Boolean.TRUE);
        } else {
            wrapper.eq(baseRequestVO.getResourceStatus() != null, ResourcePath::getStatus, baseRequestVO.getResourceStatus());
        }
        wrapper.orderByDesc(ResourcePath::getCreateTime).page(baseRequestVO);

        return PoetryResult.success(baseRequestVO);
    }


    /**
     * 保存
     */
    @PostMapping("/saveTreeHole")
    public PoetryResult<TreeHole> saveTreeHole(@RequestBody TreeHole treeHole) {
        if (!StringUtils.hasText(treeHole.getMessage())) {
            return PoetryResult.fail("留言不能为空！");
        }
        treeHoleMapper.insert(treeHole);
        if (!StringUtils.hasText(treeHole.getAvatar())) {
            treeHole.setAvatar(PoetryUtil.getRandomAvatar(null));
        }
        return PoetryResult.success(treeHole);
    }


    /**
     * 删除
     */
    @GetMapping("/deleteTreeHole")
    @LoginCheck(0)
    public PoetryResult deleteTreeHole(@RequestParam("id") Integer id) {
        treeHoleMapper.deleteById(id);
        return PoetryResult.success();
    }


    /**
     * 查询List
     */
    @GetMapping("/listTreeHole")
    public PoetryResult<List<TreeHole>> listTreeHole() {
        List<TreeHole> treeHoles;
        Integer count = new LambdaQueryChainWrapper<>(treeHoleMapper).count();
        if (count > CommonConst.TREE_HOLE_COUNT) {
            int i = new Random().nextInt(count + 1 - CommonConst.TREE_HOLE_COUNT);
            treeHoles = treeHoleMapper.queryAllByLimit(i, CommonConst.TREE_HOLE_COUNT);
        } else {
            treeHoles = new LambdaQueryChainWrapper<>(treeHoleMapper).list();
        }

        treeHoles.forEach(treeHole -> {
            if (!StringUtils.hasText(treeHole.getAvatar())) {
                treeHole.setAvatar(PoetryUtil.getRandomAvatar(treeHole.getId().toString()));
            }
            treeHole.setDeleted(null);
        });
        return PoetryResult.success(treeHoles);
    }


    /**
     * 保存
     */
    @PostMapping("/saveSort")
    @LoginCheck(0)
    public PoetryResult saveSort(@RequestBody Sort sort) {
        if (!StringUtils.hasText(sort.getSortName()) || !StringUtils.hasText(sort.getSortDescription())) {
            return PoetryResult.fail("分类名称和分类描述不能为空！");
        }

        if (sort.getSortType() != null && sort.getSortType() == PoetryEnum.SORT_TYPE_BAR.getCode() && sort.getPriority() == null) {
            return PoetryResult.fail("导航栏分类必须配置优先级！");
        }

        sortMapper.insert(sort);
        List<Sort> sortInfo = commonQuery.getSortInfo();
        if (!CollectionUtils.isEmpty(sortInfo)) {
            PoetryCache.put(CommonConst.SORT_INFO, sortInfo);
        }
        return PoetryResult.success();
    }


    /**
     * 删除
     */
    @GetMapping("/deleteSort")
    @LoginCheck(0)
    public PoetryResult deleteSort(@RequestParam("id") Integer id) {
        sortMapper.deleteById(id);
        List<Sort> sortInfo = commonQuery.getSortInfo();
        if (!CollectionUtils.isEmpty(sortInfo)) {
            PoetryCache.put(CommonConst.SORT_INFO, sortInfo);
        }
        return PoetryResult.success();
    }


    /**
     * 更新
     */
    @PostMapping("/updateSort")
    @LoginCheck(0)
    public PoetryResult updateSort(@RequestBody Sort sort) {
        sortMapper.updateById(sort);
        List<Sort> sortInfo = commonQuery.getSortInfo();
        if (!CollectionUtils.isEmpty(sortInfo)) {
            PoetryCache.put(CommonConst.SORT_INFO, sortInfo);
        }
        return PoetryResult.success();
    }


    /**
     * 查询List
     */
    @GetMapping("/listSort")
    public PoetryResult<List<Sort>> listSort() {
        return PoetryResult.success(new LambdaQueryChainWrapper<>(sortMapper).list());
    }


    /**
     * 保存
     */
    @PostMapping("/saveLabel")
    @LoginCheck(0)
    public PoetryResult saveLabel(@RequestBody Label label) {
        if (!StringUtils.hasText(label.getLabelName()) || !StringUtils.hasText(label.getLabelDescription()) || label.getSortId() == null) {
            return PoetryResult.fail("标签名称和标签描述和分类Id不能为空！");
        }
        labelMapper.insert(label);
        List<Sort> sortInfo = commonQuery.getSortInfo();
        if (!CollectionUtils.isEmpty(sortInfo)) {
            PoetryCache.put(CommonConst.SORT_INFO, sortInfo);
        }
        return PoetryResult.success();
    }


    /**
     * 删除
     */
    @GetMapping("/deleteLabel")
    @LoginCheck(0)
    public PoetryResult deleteLabel(@RequestParam("id") Integer id) {
        labelMapper.deleteById(id);
        List<Sort> sortInfo = commonQuery.getSortInfo();
        if (!CollectionUtils.isEmpty(sortInfo)) {
            PoetryCache.put(CommonConst.SORT_INFO, sortInfo);
        }
        return PoetryResult.success();
    }


    /**
     * 更新
     */
    @PostMapping("/updateLabel")
    @LoginCheck(0)
    public PoetryResult updateLabel(@RequestBody Label label) {
        labelMapper.updateById(label);
        List<Sort> sortInfo = commonQuery.getSortInfo();
        if (!CollectionUtils.isEmpty(sortInfo)) {
            PoetryCache.put(CommonConst.SORT_INFO, sortInfo);
        }
        return PoetryResult.success();
    }


    /**
     * 查询List
     */
    @GetMapping("/listLabel")
    public PoetryResult<List<Label>> listLabel() {
        return PoetryResult.success(new LambdaQueryChainWrapper<>(labelMapper).list());
    }


    /**
     * 查询List
     */
    @GetMapping("/listSortAndLabel")
    public PoetryResult<Map> listSortAndLabel() {
        Map<String, List> map = new HashMap<>();
        map.put("sorts", new LambdaQueryChainWrapper<>(sortMapper).list());
        map.put("labels", new LambdaQueryChainWrapper<>(labelMapper).list());
        return PoetryResult.success(map);
    }
}

