package com.ld.poetry.controller;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.ld.poetry.aop.LoginCheck;
import com.ld.poetry.config.PoetryResult;
import com.ld.poetry.constants.CommonConst;
import com.ld.poetry.dao.LabelMapper;
import com.ld.poetry.dao.SortMapper;
import com.ld.poetry.entity.Label;
import com.ld.poetry.entity.Sort;
import com.ld.poetry.utils.CommonQuery;
import com.ld.poetry.utils.cache.PoetryCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 分类标签 前端控制器
 * </p>
 *
 * @author sara
 * @since 2021-09-14
 */
@RestController
@RequestMapping("/webInfo")
public class SortLabelController {

    @Autowired
    private SortMapper sortMapper;

    @Autowired
    private LabelMapper labelMapper;

    @Autowired
    private CommonQuery commonQuery;

    /**
     * 获取分类标签信息
     */
    @GetMapping("/getSortInfo")
    public PoetryResult<List<Sort>> getSortInfo() {
        return PoetryResult.success(commonQuery.getSortInfo());
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

        if (sort.getPriority() == null) {
            return PoetryResult.fail("分类必须配置优先级！");
        }

        sortMapper.insert(sort);
        PoetryCache.remove(CommonConst.SORT_INFO);
        return PoetryResult.success();
    }


    /**
     * 删除
     */
    @GetMapping("/deleteSort")
    @LoginCheck(0)
    public PoetryResult deleteSort(@RequestParam("id") Integer id) {
        sortMapper.deleteById(id);
        PoetryCache.remove(CommonConst.SORT_INFO);
        return PoetryResult.success();
    }


    /**
     * 更新
     */
    @PostMapping("/updateSort")
    @LoginCheck(0)
    public PoetryResult updateSort(@RequestBody Sort sort) {
        sortMapper.updateById(sort);
        PoetryCache.remove(CommonConst.SORT_INFO);
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
        PoetryCache.remove(CommonConst.SORT_INFO);
        return PoetryResult.success();
    }


    /**
     * 删除
     */
    @GetMapping("/deleteLabel")
    @LoginCheck(0)
    public PoetryResult deleteLabel(@RequestParam("id") Integer id) {
        labelMapper.deleteById(id);
        PoetryCache.remove(CommonConst.SORT_INFO);
        return PoetryResult.success();
    }


    /**
     * 更新
     */
    @PostMapping("/updateLabel")
    @LoginCheck(0)
    public PoetryResult updateLabel(@RequestBody Label label) {
        labelMapper.updateById(label);
        PoetryCache.remove(CommonConst.SORT_INFO);
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
