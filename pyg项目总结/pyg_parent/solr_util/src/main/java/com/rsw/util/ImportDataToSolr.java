package com.rsw.util;

import com.alibaba.fastjson.JSON;
import com.rsw.dao.item.ItemDao;
import com.rsw.pojo.item.Item;
import com.rsw.pojo.item.ItemQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ImportDataToSolr {

    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private ItemDao itemDao;

    public void importItemDataToSolr() {
        ItemQuery itemQuery = new ItemQuery();
        ItemQuery.Criteria criteria = itemQuery.createCriteria();
        criteria.andStatusEqualTo("1");
        List<Item> items = itemDao.selectByExample(itemQuery);
        if (items != null) {
            for (Item item : items) {
                Map map = JSON.parseObject(item.getSpec(), Map.class);
                item.setSpecMap(map);
            }
            solrTemplate.saveBeans(items);
            solrTemplate.commit();
        }
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        ImportDataToSolr importDataToSolr = (ImportDataToSolr) applicationContext.getBean("importDataToSolr");
        importDataToSolr.importItemDataToSolr();
    }

}
