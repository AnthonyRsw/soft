package com.rsw.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rsw.dao.ad.ContentCategoryDao;
import com.rsw.pojo.ad.Content;
import com.rsw.pojo.ad.ContentCategory;
import com.rsw.pojo.ad.ContentCategoryQuery;
import com.rsw.pojo.entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.TestExecutionListeners;

import java.util.List;

@Service
@TestExecutionListeners
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    ContentCategoryDao contentCategoryDao;

    @Override
    public List<ContentCategory> findAll() {
        return contentCategoryDao.selectByExample(null);
    }

    @Override
    public PageResult findPage(ContentCategory contentCategory, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        ContentCategoryQuery query = new ContentCategoryQuery();
        ContentCategoryQuery.Criteria criteria = query.createCriteria();
        if (contentCategory != null) {
            if (contentCategory.getName() != null && !"".equals(contentCategory.getName())) {
                criteria.andNameLike("%"+contentCategory.getName()+"%");
            }
        }
        Page<ContentCategory> categoryList = (Page<ContentCategory>)contentCategoryDao.selectByExample(query);
        return new PageResult(categoryList.getTotal(), categoryList.getResult());
    }

    @Override
    public void save(ContentCategory contentCategory) {
        contentCategoryDao.insertSelective(contentCategory);
    }

    @Override
    public void update(ContentCategory contentCategory) {
        contentCategoryDao.updateByPrimaryKey(contentCategory);

    }

    @Override
    public ContentCategory findOne(Long id) {
        return contentCategoryDao.selectByPrimaryKey(id);
    }

    @Override
    public void delete(Long[] ids) {
        if (ids != null) {
            for (Long id : ids) {
                contentCategoryDao.deleteByPrimaryKey(id);
            }
        }

    }
}
