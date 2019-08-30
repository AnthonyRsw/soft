package com.rsw.service;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rsw.dao.specification.SpecificationOptionDao;
import com.rsw.dao.template.TypeTemplateDao;
import com.rsw.pojo.entity.PageResult;
import com.rsw.pojo.specification.SpecificationOption;
import com.rsw.pojo.specification.SpecificationOptionQuery;
import com.rsw.pojo.template.TypeTemplate;
import com.rsw.pojo.template.TypeTemplateQuery;
import com.rsw.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@Service
public class TypeTemplateServiceImpl implements TypeTemplateService {

    @Autowired
    TypeTemplateDao typeTemplateDao;

    @Autowired
    SpecificationOptionDao specificationOptionDao;

    @Autowired
    RedisTemplate redisTemplate;


    @Override
    public PageResult findPage(TypeTemplate typeTemplate, Integer page, Integer rows) {

        List<TypeTemplate> typeTemplatesList = typeTemplateDao.selectByExample(null);
        if (typeTemplatesList!=null&&typeTemplatesList.size()>0){
            for (TypeTemplate template : typeTemplatesList) {
                //1.先缓存品牌list根据模板id到Redis,hash(bandList),key(模板id),value(品牌集合)
                String brandIds = template.getBrandIds();
                List<Map> brandIdsList = JSON.parseArray(brandIds, Map.class);
                redisTemplate.boundHashOps(Constants.BRAND_LIST_REDIS).put(template.getId(),brandIdsList);
                //2.缓存规格list根据模板id到Redis,hash(specList),key(模板id),value(规格集合)
                List<Map> specIdsList = findBySpecList(template.getId());
                redisTemplate.boundHashOps(Constants.SPEC_LIST_REDIS).put(template.getId(),specIdsList);
            }
        }

        PageHelper.startPage(page,rows);
        TypeTemplateQuery typeTemplateQuery=new TypeTemplateQuery();
        TypeTemplateQuery.Criteria criteria = typeTemplateQuery.createCriteria();
        if (typeTemplate!=null){
            if (typeTemplate.getName()!=null && !"".equals(typeTemplate.getName())){
                criteria.andNameEqualTo(typeTemplate.getName());
            }
        }

        Page<TypeTemplate> typeTemplates = (Page<TypeTemplate>) typeTemplateDao.selectByExample(typeTemplateQuery);
        return new PageResult(typeTemplates.getTotal(),typeTemplates.getResult());
    }

    @Override
    public void add(TypeTemplate typeTemplate) {
        typeTemplateDao.insertSelective(typeTemplate);

    }

    @Override
    public TypeTemplate findOne(Long id) {
        TypeTemplate typeTemplate = typeTemplateDao.selectByPrimaryKey(id);
        return typeTemplate;
    }

    @Override
    public void update(TypeTemplate typeTemplate) {
        typeTemplateDao.updateByPrimaryKeySelective(typeTemplate);
    }

    @Override
    public void delete(Long[] ids) {
        if (ids!=null){
            for (Long id : ids) {
                typeTemplateDao.deleteByPrimaryKey(id);
            }
        }
    }

    @Override
    public List<Map> findBySpecList(Long id) {
        TypeTemplate typeTemplate = typeTemplateDao.selectByPrimaryKey(id);
        String specIds = typeTemplate.getSpecIds();
        List<Map> maps = JSONArray.parseArray(specIds, Map.class);
        for (Map map : maps) {
            //查询规格选项列表
            SpecificationOptionQuery example=new SpecificationOptionQuery();
            SpecificationOptionQuery.Criteria criteria = example.createCriteria();
            criteria.andSpecIdEqualTo( new Long( (Integer)map.get("id") ) );
            List<SpecificationOption> specificationOptions = specificationOptionDao.selectByExample(example);
            map.put("options", specificationOptions);
        }
        return maps;
    }
}
