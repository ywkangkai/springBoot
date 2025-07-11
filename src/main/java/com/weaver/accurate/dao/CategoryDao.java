package com.weaver.accurate.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weaver.accurate.entity.Category;

import java.util.List;

public interface CategoryDao extends BaseMapper<Category> {
    public List<Category> selectByPage(Category entity);
}
