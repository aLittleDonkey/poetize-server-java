package com.ld.poetry.dao;

import com.ld.poetry.entity.TreeHole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 树洞 Mapper 接口
 * </p>
 *
 * @author sara
 * @since 2021-09-14
 */
public interface TreeHoleMapper extends BaseMapper<TreeHole> {

    List<TreeHole> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

}
