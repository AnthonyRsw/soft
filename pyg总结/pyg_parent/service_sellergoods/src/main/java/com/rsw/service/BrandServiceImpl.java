package com.rsw.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rsw.dao.good.BrandDao;
import com.rsw.pojo.entity.PageResult;
import com.rsw.pojo.good.Brand;
import com.rsw.pojo.good.BrandQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    BrandDao brandDao;

    @Override
    public List<Brand> findAll() {
        return brandDao.selectByExample(null);
    }

    @Override
    public PageResult findPage(Brand brand, Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        BrandQuery brandQuery=new BrandQuery();
        BrandQuery.Criteria criteria = brandQuery.createCriteria();
        if (brand.getName()!=null && !"".equals(brand.getName())){
            criteria.andNameLike("%"+brand.getName()+"%");
        }
        if (brand.getFirstChar()!=null && !"".equals(brand.getFirstChar())){
            criteria.andFirstCharEqualTo(brand.getFirstChar());
        }
        Page<Brand> list = (Page<Brand>) brandDao.selectByExample(brandQuery);
        return new PageResult(list.getTotal(),list.getResult());
    }

    @Override
    public void save(Brand brand) {
        brandDao.insertSelective(brand);
    }

    @Override
    public void update(Brand brand) {
         brandDao.updateByPrimaryKeySelective(brand);
    }

    @Override
    public Brand findOne(Long id) {
       return brandDao.selectByPrimaryKey(id);
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            brandDao.deleteByPrimaryKey(id);
        }
    }

    @Override
    public List<Map> selectOptionList() {
        return brandDao.selectOptionList();
    }
}
