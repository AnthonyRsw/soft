package com.rsw.service;

import com.rsw.pojo.entity.PageResult;
import com.rsw.pojo.entity.SpecEntity;
import com.rsw.pojo.good.Brand;
import com.rsw.pojo.specification.Specification;

import java.util.List;
import java.util.Map;


public interface SpecificationService {
    PageResult findPage(Specification specification, Integer page, Integer rows);

    SpecEntity findOne(Long id);

    void add(SpecEntity specEntity);

    void update(SpecEntity specEntity);

    void delete(Long[] ids);

    List<Map> selectOptionList();

}
