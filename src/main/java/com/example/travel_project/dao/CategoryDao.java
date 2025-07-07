package com.example.travel_project.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.travel_project.entity.Category;

import java.util.List;

public interface CategoryDao extends BaseMapper<Category> {
    public List<Category> selectByPage(Category entity);
}
