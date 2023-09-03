package com.ld.poetry.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ld.poetry.config.LoginCheck;
import com.ld.poetry.config.PoetryResult;
import com.ld.poetry.entity.Resource;
import com.ld.poetry.service.ResourceService;
import com.ld.poetry.utils.CommonConst;
import com.ld.poetry.utils.PoetryEnum;
import com.ld.poetry.utils.PoetryUtil;
import com.ld.poetry.utils.QiniuUtil;
import com.ld.poetry.vo.BaseRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 资源信息 前端控制器
 * </p>
 *
 * @author sara
 * @since 2022-03-06
 */
@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Value("${qiniu.downloadUrl}")
    private String downloadUrl;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private QiniuUtil qiniuUtil;

    /**
     * 保存
     */
    @PostMapping("/saveResource")
    @LoginCheck
    public PoetryResult saveResource(@RequestBody Resource resource) {
        if (!StringUtils.hasText(resource.getType()) || !StringUtils.hasText(resource.getPath())) {
            return PoetryResult.fail("资源类型和资源路径不能为空！");
        }
        Resource re = new Resource();
        re.setPath(resource.getPath());
        re.setType(resource.getType());
        re.setSize(resource.getSize());
        re.setMimeType(resource.getMimeType());
        re.setUserId(PoetryUtil.getUserId());
        resourceService.save(re);
        return PoetryResult.success();
    }

    /**
     * 删除
     */
    @PostMapping("/deleteResource")
    @LoginCheck(0)
    public PoetryResult deleteResource(@RequestParam("path") String path) {
        qiniuUtil.deleteFile(Collections.singletonList(path.replace(downloadUrl, "")));
        resourceService.lambdaUpdate().eq(Resource::getPath, path).remove();
        return PoetryResult.success();
    }

    @GetMapping("/getResourceInfo")
    @LoginCheck(0)
    public PoetryResult getResourceInfo() {
        List<Resource> resources = resourceService.lambdaQuery()
                .select(Resource::getId, Resource::getPath)
                .like(Resource::getPath, downloadUrl)
                .isNull(Resource::getSize)
                .list();
        if (!CollectionUtils.isEmpty(resources)) {
            Map<String, Integer> resourceMap = resources.stream().collect(Collectors.toMap(resource -> resource.getPath().replace(downloadUrl, ""), Resource::getId));
            Map<String, Map<String, String>> fileInfo = qiniuUtil.getFileInfo(new ArrayList<>(resourceMap.keySet()));
            if (!CollectionUtils.isEmpty(fileInfo)) {
                List<Resource> collect = fileInfo.entrySet().stream().map(entry -> {
                    Resource resource = new Resource();
                    resource.setId(resourceMap.get(entry.getKey()));
                    resource.setSize(Integer.valueOf(entry.getValue().get("size")));
                    resource.setMimeType(entry.getValue().get("mimeType"));
                    return resource;
                }).collect(Collectors.toList());
                resourceService.updateBatchById(collect);
            }
        }
        return PoetryResult.success();
    }

    /**
     * 查询表情包
     */
    @GetMapping("/getImageList")
    @LoginCheck
    public PoetryResult<List<String>> getImageList() {
        List<Resource> list = resourceService.lambdaQuery().select(Resource::getPath)
                .eq(Resource::getType, CommonConst.PATH_TYPE_INTERNET_MEME)
                .eq(Resource::getStatus, PoetryEnum.STATUS_ENABLE.getCode())
                .eq(Resource::getUserId, PoetryUtil.getAdminUser().getId())
                .orderByDesc(Resource::getCreateTime)
                .list();
        List<String> paths = list.stream().map(Resource::getPath).collect(Collectors.toList());
        return PoetryResult.success(paths);
    }

    /**
     * 查询资源
     */
    @PostMapping("/listResource")
    @LoginCheck(0)
    public PoetryResult<Page> listResource(@RequestBody BaseRequestVO baseRequestVO) {
        resourceService.lambdaQuery()
                .eq(StringUtils.hasText(baseRequestVO.getResourceType()), Resource::getType, baseRequestVO.getResourceType())
                .orderByDesc(Resource::getCreateTime).page(baseRequestVO);
        return PoetryResult.success(baseRequestVO);
    }

    /**
     * 修改资源状态
     */
    @GetMapping("/changeResourceStatus")
    @LoginCheck(0)
    public PoetryResult changeResourceStatus(@RequestParam("id") Integer id, @RequestParam("flag") Boolean flag) {
        resourceService.lambdaUpdate().eq(Resource::getId, id).set(Resource::getStatus, flag).update();
        return PoetryResult.success();
    }
}

