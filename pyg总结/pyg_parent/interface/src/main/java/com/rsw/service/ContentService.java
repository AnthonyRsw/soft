package com.rsw.service;

import com.rsw.pojo.ad.Content;
import com.rsw.pojo.entity.PageResult;

import java.util.List;

public interface ContentService {

    List<Content> findAll();

    PageResult findPage(Content content, Integer page, Integer rows);

    void save(Content content);

    void update(Content content);

    Content findOne(Long id);

    void delete(Long[] ids);

    List<Content> findByCategoryId(Long categoryId);

}
