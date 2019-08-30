package com.rsw.service;

import com.rsw.pojo.entity.PageResult;
import com.rsw.pojo.template.TypeTemplate;

import java.util.List;
import java.util.Map;

public interface TypeTemplateService {
    PageResult findPage(TypeTemplate typeTemplate, Integer page, Integer rows);

    void add(TypeTemplate typeTemplate);

    TypeTemplate findOne(Long id);

    void update(TypeTemplate typeTemplate);

    void delete(Long[] ids);

    List<Map> findBySpecList(Long id);
}
