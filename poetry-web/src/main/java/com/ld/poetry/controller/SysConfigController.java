package com.ld.poetry.controller;


import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.ld.poetry.aop.LoginCheck;
import com.ld.poetry.config.PoetryResult;
import com.ld.poetry.entity.SysConfig;
import com.ld.poetry.enums.PoetryEnum;
import com.ld.poetry.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 参数配置表 前端控制器
 * </p>
 *
 * @author sara
 * @since 2024-03-23
 */
@RestController
@RequestMapping("/sysConfig")
public class SysConfigController {

    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 查询系统参数
     */
    @GetMapping("/listSysConfig")
    public PoetryResult<Map<String, String>> listSysConfig() {
        LambdaQueryChainWrapper<SysConfig> wrapper = new LambdaQueryChainWrapper<>(sysConfigService.getBaseMapper());
        List<SysConfig> sysConfigs = wrapper.eq(SysConfig::getConfigType, Integer.toString(PoetryEnum.SYS_CONFIG_PUBLIC.getCode()))
                .list();
        Map<String, String> collect = sysConfigs.stream().collect(Collectors.toMap(SysConfig::getConfigKey, SysConfig::getConfigValue));
        return PoetryResult.success(collect);
    }

    /**
     * 保存或更新
     */
    @PostMapping("/saveOrUpdateConfig")
    @LoginCheck(0)
    public PoetryResult saveConfig(@RequestBody SysConfig sysConfig) {
        if (!StringUtils.hasText(sysConfig.getConfigName()) ||
                !StringUtils.hasText(sysConfig.getConfigKey()) ||
                !StringUtils.hasText(sysConfig.getConfigType())) {
            return PoetryResult.fail("请完善所有配置信息！");
        }
        String configType = sysConfig.getConfigType();
        if (!Integer.toString(PoetryEnum.SYS_CONFIG_PUBLIC.getCode()).equals(configType) &&
                !Integer.toString(PoetryEnum.SYS_CONFIG_PRIVATE.getCode()).equals(configType)) {
            return PoetryResult.fail("配置类型不正确！");
        }
        sysConfigService.saveOrUpdate(sysConfig);
        return PoetryResult.success();
    }

    /**
     * 删除
     */
    @GetMapping("/deleteConfig")
    @LoginCheck(0)
    public PoetryResult deleteConfig(@RequestParam("id") Integer id) {
        sysConfigService.removeById(id);
        return PoetryResult.success();
    }

    /**
     * 查询
     */
    @GetMapping("/listConfig")
    @LoginCheck(0)
    public PoetryResult<List<SysConfig>> listConfig() {
        return PoetryResult.success(new LambdaQueryChainWrapper<>(sysConfigService.getBaseMapper()).list());
    }
}
