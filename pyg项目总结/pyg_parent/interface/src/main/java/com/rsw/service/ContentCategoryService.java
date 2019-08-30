package com.rsw.service;

import com.rsw.pojo.ad.ContentCategory;
import com.rsw.pojo.entity.PageResult;

import java.util.List;

public interface ContentCategoryService {

    List<ContentCategory> findAll();

    PageResult findPage(ContentCategory contentCategory, Integer page, Integer rows);

    void save(ContentCategory contentCategory);

    void update(ContentCategory contentCategory);

    ContentCategory findOne(Long id);

    void delete(Long[] ids);
}
