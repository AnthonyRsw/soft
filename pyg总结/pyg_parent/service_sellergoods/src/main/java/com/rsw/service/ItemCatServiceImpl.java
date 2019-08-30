package com.rsw.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.rsw.dao.item.ItemCatDao;
import com.rsw.pojo.item.ItemCat;
import com.rsw.pojo.item.ItemCatQuery;
import com.rsw.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    ItemCatDao itemCatDao;

    @Autowired
    RedisTemplate redisTemplate;


    @Override
    public List<ItemCat> findByParentId(Long parentId) {
        //1.先缓存分类,格式是hash(categoryList),key是分类名称,value是模板id
        List<ItemCat> itemCatsList = itemCatDao.selectByExample(null);
        if (itemCatsList!=null&&itemCatsList.size()>0){
            for (ItemCat itemCat : itemCatsList) {
                redisTemplate.boundHashOps(Constants.CATEGORY_LIST_REDIS).put(itemCat.getName(),itemCat.getTypeId());
            }
        }
        ItemCatQuery catQuery=new ItemCatQuery();
        ItemCatQuery.Criteria criteria = catQuery.createCriteria();
        criteria.andParentIdEqualTo(parentId);

        List<ItemCat> itemCats = itemCatDao.selectByExample(catQuery);
        return itemCats;
    }

    @Override
    public void add(ItemCat itemCat) {
        itemCatDao.insertSelective(itemCat);
    }

    @Override
    public void update(ItemCat itemCat) {
        itemCatDao.updateByPrimaryKeySelective(itemCat);
    }

    @Override
    public ItemCat findOne(Long id) {
        return itemCatDao.selectByPrimaryKey(id);
    }

    @Override
    public void delete(Long[] ids) {
        if (ids!=null){
            for (Long id : ids) {
                itemCatDao.deleteByPrimaryKey(id);
            }
        }
    }

    @Override
    public List<ItemCat> findAll() {
        List<ItemCat> itemCats = itemCatDao.selectByExample(null);
        return itemCats;
    }
}
