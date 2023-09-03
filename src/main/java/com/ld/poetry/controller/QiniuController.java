package com.ld.poetry.controller;

import com.ld.poetry.config.LoginCheck;
import com.ld.poetry.config.PoetryResult;
import com.ld.poetry.config.SaveCheck;
import com.ld.poetry.utils.QiniuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 七牛云
 */
@RestController
@RequestMapping("/qiniu")
public class QiniuController {

    @Autowired
    private QiniuUtil qiniuUtil;

    /**
     * 获取覆盖凭证
     */
    @GetMapping("/getUpToken")
    @LoginCheck
    @SaveCheck
    public PoetryResult<String> getUpToken(@RequestParam(value = "key") String key) {
        return PoetryResult.success(qiniuUtil.getToken(key));
    }
}
