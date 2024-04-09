package com.ld.poetry.service.impl;

import com.ld.poetry.entity.WebInfo;
import com.ld.poetry.dao.WebInfoMapper;
import com.ld.poetry.service.WebInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 网站信息表 服务实现类
 * </p>
 *
 * @author sara
 * @since 2021-09-14
 */
@Service
public class WebInfoServiceImpl extends ServiceImpl<WebInfoMapper, WebInfo> implements WebInfoService {

}
