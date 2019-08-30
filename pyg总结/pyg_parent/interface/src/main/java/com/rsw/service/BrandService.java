package com.rsw.service;

import com.rsw.pojo.entity.PageResult;
import com.rsw.pojo.good.Brand;

import java.util.List;
import java.util.Map;

public interface BrandService {

    public List<Brand> findAll();

    PageResult findPage(Brand brand, Integer page, Integer rows);

    void save(Brand brand);

    void update(Brand brand);

    Brand findOne(Long id);

    void delete(Long[] ids);

    List<Map> selectOptionList();

}
