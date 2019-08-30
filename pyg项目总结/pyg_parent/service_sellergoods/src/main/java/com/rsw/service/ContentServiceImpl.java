package com.rsw.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rsw.dao.ad.ContentDao;
import com.rsw.pojo.ad.Content;
import com.rsw.pojo.ad.ContentQuery;
import com.rsw.pojo.entity.PageResult;
import com.rsw.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.TestExecutionListeners;

import java.util.List;

@Service
@TestExecutionListeners
public class ContentServiceImpl implements ContentService {

    @Autowired
    ContentDao contentDao;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public List<Content> findAll() {
        return contentDao.selectByExample(null);
    }

    @Override
    public PageResult findPage(Content content, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        ContentQuery query = new ContentQuery();
        ContentQuery.Criteria criteria = query.createCriteria();
        if (content != null) {
            if (content.getTitle() != null && !"".equals(content.getTitle())) {
                criteria.andTitleLike("%"+content.getTitle()+"%");
            }
        }
        Page<Content> list = (Page<Content>)contentDao.selectByExample(query);
        return new PageResult(list.getTotal(), list.getResult());
    }

    @Override
    public void save(Content content) {
        contentDao.insertSelective(content);
    }

    @Override
    public void update(Content content) {
        //1.根据id查询该条记录
        Content oldContent = contentDao.selectByPrimaryKey(content.getId());
        //2.删除Redis中原来的数据
        redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).delete(oldContent.getCategoryId());
        //3.删除Redis中修改关联到的类别
        redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).delete(content.getCategoryId());
        //4.修改数据库中数据
        contentDao.updateByPrimaryKey(content);

    }

    @Override
    public Content findOne(Long id) {
        return contentDao.selectByPrimaryKey(id);
    }

    @Override
    public void delete(Long[] ids) {
        if (ids != null) {
            for (Long id : ids) {
                //1.先删除redis中数据
                Content content = contentDao.selectByPrimaryKey(id);
                redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).delete(content.getCategoryId());
                //2.再删除数据库数据
                contentDao.deleteByPrimaryKey(id);
            }
        }

    }

    @Override
    public List<Content> findByCategoryId(Long categoryId) {
        //1.先查Redis
        List<Content> rList = (List<Content>) redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).get(categoryId);

        //Redis中没有,查数据库,再将数据放入Redis
        if (rList==null){
            ContentQuery query=new ContentQuery();
            ContentQuery.Criteria criteria = query.createCriteria();
            criteria.andCategoryIdEqualTo(categoryId);
            List<Content> contents = contentDao.selectByExample(query);
            redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).put(categoryId,contents);
            return contents;
        }
        return rList;
    }
}
