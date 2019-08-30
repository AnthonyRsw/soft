package com.rsw.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rsw.dao.specification.SpecificationDao;
import com.rsw.dao.specification.SpecificationOptionDao;
import com.rsw.pojo.entity.PageResult;
import com.rsw.pojo.entity.SpecEntity;
import com.rsw.pojo.specification.Specification;
import com.rsw.pojo.specification.SpecificationOption;
import com.rsw.pojo.specification.SpecificationOptionQuery;
import com.rsw.pojo.specification.SpecificationQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    SpecificationDao specificationDao;

    @Autowired
    SpecificationOptionDao specificationOptionDao;

    @Override
    public PageResult findPage(Specification specification, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        SpecificationQuery specificationQuery = new SpecificationQuery();
        SpecificationQuery.Criteria criteria = specificationQuery.createCriteria();
        if (specification != null) {
            if (specification.getSpecName() != null && !"".equals(specification.getSpecName())) {
                criteria.andSpecNameLike("%" + specification.getSpecName() + "%");
            }
        }
        Page<Specification> list = (Page<Specification>) specificationDao.selectByExample(specificationQuery);
        return new PageResult(list.getTotal(), list.getResult());

    }

    @Override
    public SpecEntity findOne(Long id) {

        Specification specification = specificationDao.selectByPrimaryKey(id);

        SpecificationOptionQuery specificationOptionQuery = new SpecificationOptionQuery();
        SpecificationOptionQuery.Criteria criteria = specificationOptionQuery.createCriteria();
        criteria.andSpecIdEqualTo(id);
        List<SpecificationOption> specificationOptions = specificationOptionDao.selectByExample(specificationOptionQuery);
        SpecEntity specEntity = new SpecEntity();
        specEntity.setSpecification(specification);
        specEntity.setSpecificationOptionList(specificationOptions);
        return specEntity;

    }

    @Override
    public void add(SpecEntity specEntity) {
        //1. 添加规格对象
        specificationDao.insertSelective(specEntity.getSpecification());

        //2. 添加规格选项对象
        if (specEntity.getSpecificationOptionList() != null) {
            for (SpecificationOption option : specEntity.getSpecificationOptionList()) {
                //设置规格选项外键
                option.setSpecId(specEntity.getSpecification().getId());
                specificationOptionDao.insertSelective(option);
            }
        }
    }

    @Override
    public void update(SpecEntity specEntity) {
        specificationDao.updateByPrimaryKey(specEntity.getSpecification());

        SpecificationOptionQuery specificationOptionQuery = new SpecificationOptionQuery();
        SpecificationOptionQuery.Criteria criteria = specificationOptionQuery.createCriteria();
        criteria.andSpecIdEqualTo(specEntity.getSpecification().getId());
        specificationOptionDao.deleteByExample(specificationOptionQuery);

        List<SpecificationOption> specificationOptionList = specEntity.getSpecificationOptionList();
        for (SpecificationOption specificationOption : specificationOptionList) {
            specificationOptionDao.insert(specificationOption);
        }

    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            SpecificationOptionQuery specificationOptionQuery = new SpecificationOptionQuery();
            SpecificationOptionQuery.Criteria criteria = specificationOptionQuery.createCriteria();
            criteria.andSpecIdEqualTo(id);
            specificationOptionDao.deleteByExample(specificationOptionQuery);
            specificationDao.deleteByPrimaryKey(id);
        }

    }

    @Override
    public List<Map> selectOptionList() {
        return specificationDao.selectOptionList();
    }
}
