package com.rsw.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.rsw.pojo.item.Item;
import com.rsw.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.*;

@Service
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> search(Map searchMap) {
        //1. 根据查询参数, 到solr中分页, 高亮, 过滤, 排序查询
        Map<String, Object> resultMap = highlightSearch(searchMap);

        //2. 根据查询参数, 到solr中获取对应的分类结果集, 由于分类重复, 所以需要分组去重
        List<String> groupCategoryList = findGroupCatagoryList(searchMap);

        //3.判断传入的参数中是否有分类名称
        String category = String.valueOf(searchMap.get("category"));
        if (category != null && !"".equals(category)) {
            //4.如果有分类参数,则根据分类查询对应的品牌集合和规格集合
            Map map = findBrandAndSpecList(category);
            resultMap.putAll(map);

        } else {
            //5.如果没有默认根据第一个分类查询对应的品牌集合和规格集合
            if (groupCategoryList != null && groupCategoryList.size() > 0) {
                Map map = findBrandAndSpecList(groupCategoryList.get(0));
                resultMap.putAll(map);
            }
        }
        return resultMap;
    }

    /**
     * 根据分类名称从Redis中查询品牌和规格集合
     *
     * @param category
     * @return
     */
    private Map findBrandAndSpecList(String category) {
        //1. 根据分类名称到redis中查询对应的模板id
        Long templateId = (Long) redisTemplate.boundHashOps(Constants.CATEGORY_LIST_REDIS).get(category);
        //2. 根据模板id到redis中查询对应的品牌集合
        List<Map> brandList = (List<Map>) redisTemplate.boundHashOps(Constants.BRAND_LIST_REDIS).get(templateId);
        //3. 根据模板id到reids中查询对应的规格集合
        List<Map> specList = (List<Map>) redisTemplate.boundHashOps(Constants.SPEC_LIST_REDIS).get(templateId);
        //4. 将品牌集合和规格集合数据封装到Map中返回
        Map resultMap = new HashMap();
        resultMap.put("brandList", brandList);
        resultMap.put("specList", specList);
        return resultMap;
    }

    /**
     * 根据查询参数, 到solr中获取对应的分类结果集, 由于分类重复, 所以需要分组去重
     *
     * @param searchMap
     * @return
     */
    private List<String> findGroupCatagoryList(Map searchMap) {
        List<String> resultList = new ArrayList<>();
        //获取查询关键字
        String keywords = String.valueOf(searchMap.get("keywords"));
        //创建查询对象
        Query query = new SimpleQuery();
        //创建查询条件对象
        Criteria criteria = new Criteria("item_keywords").is(keywords);
        //将查询条件放入查询对象中
        query.addCriteria(criteria);

        //创建分组对象
        GroupOptions groupOptions = new GroupOptions();
        //设置根据分类域进行分组
        groupOptions.addGroupByField("item_category");
        //将分组对象放入查询对象中
        query.setGroupOptions(groupOptions);

        //分组查询分类集合
        GroupPage<Item> items = solrTemplate.queryForGroupPage(query, Item.class);

        //获取结果集中分类域的集合
        GroupResult<Item> item_category = items.getGroupResult("item_category");

        //获取分类域的实体集合
        Page<GroupEntry<Item>> groupEntries = item_category.getGroupEntries();

        //遍历实体集合得到实体对象
        for (GroupEntry<Item> groupEntry : groupEntries) {
            String groupCategory = groupEntry.getGroupValue();
            resultList.add(groupCategory);
        }
        return resultList;
    }

    /**
     * 根据关键字, 分页, 高亮, 过滤, 排序查询, 并且将查询结果返回
     *
     * @param searchMap 从页面传入进来的查询参数
     * @return
     */
    private Map<String, Object> highlightSearch(Map searchMap) {
        //关键字
        String keywords = String.valueOf(searchMap.get("keywords"));
        //当前页
        Integer pageNo = Integer.parseInt(String.valueOf(searchMap.get("pageNo")));
        //每页数据量
        Integer pageSize = Integer.parseInt(String.valueOf(searchMap.get("pageSize")));
        //获取页面点击的分类过滤条件
        String category = String.valueOf(searchMap.get("category"));
        //获取页面点击的品牌过滤条件
        String brand = String.valueOf(searchMap.get("brand"));
        //获取页面点击的规格过滤条件
        String spec = String.valueOf(searchMap.get("spec"));
        //获取页面点击的价格区间过滤条件
        String price = String.valueOf(searchMap.get("price"));
        //获取页面排序字段
        String sortField = String.valueOf(searchMap.get("sortField"));
        //获取页面排序规则
        String sortType = String.valueOf(searchMap.get("sort"));

        HighlightQuery query = new SimpleHighlightQuery();
        //  Query query=new SimpleQuery();
        //查询条件对象
        Criteria criteria = new Criteria("item_keywords").is(keywords);

        //设置高亮
        HighlightOptions options = new HighlightOptions();
        options.addField("item_title");
        options.setSimplePrefix("<em style=\"color:red\">");
        options.setSimplePostfix("</em>");
        query.setHighlightOptions(options);

        query.addCriteria(criteria);

        if (pageNo == null || pageNo <= 0) {
            pageNo = 1;
        }

        //添加分页
        //分页条件
        Integer start = (pageNo - 1) * pageSize;
        query.setOffset(start);
        query.setRows(pageSize);

        //过滤查询
        //分类
        if (category != null && !"".equals(category)) {
            FilterQuery filterQuery = new SimpleFilterQuery();
            Criteria filterCriteria = new Criteria("item_category").is(category);
            filterQuery.addCriteria(filterCriteria);
            query.addFilterQuery(filterQuery);
        }
        //品牌
        //根据品牌顾虑查询
        if (brand != null && !"".equals(brand)) {
            //创建过滤查询对象
            FilterQuery filterQuery = new SimpleFilterQuery();
            //创建条件对象
            Criteria filterCriteria = new Criteria("item_brand").is(brand);
            //将条件对象放入过滤对象中
            filterQuery.addCriteria(filterCriteria);
            //过滤对象放入查询对象中
            query.addFilterQuery(filterQuery);
        }
        //规格
        if (spec != null && !"".equals(spec)) {
            Map<String, String> specMap = JSON.parseObject(spec, Map.class);
            if (specMap != null && specMap.size() > 0) {
                Set<Map.Entry<String, String>> entries = specMap.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    FilterQuery filterQuery = new SimpleFilterQuery();
                    Criteria fiCriteria = new Criteria("item_spec" + entry.getKey()).is(entry.getValue());
                    filterQuery.addCriteria(fiCriteria);
                    query.addFilterQuery(filterQuery);
                }
            }

        }
        //价格
        if (price != null && !"".equals(price)) {
            String[] split = price.split("-");
            if (split != null && split.length == 2) {
                if (!"0".equals(split[0])) {
                    FilterQuery filterQuery = new SimpleFilterQuery();
                    Criteria filCriteria = new Criteria("item_price").greaterThanEqual(split[0]);
                    filterQuery.addCriteria(filCriteria);
                    query.addFilterQuery(filterQuery);

                }
                if (!"*".equals(split[1])) {
                    FilterQuery filterQuery = new SimpleFilterQuery();
                    Criteria filCriteria = new Criteria("item_price").lessThanEqual(split[1]);
                    filterQuery.addCriteria(filCriteria);
                    query.addFilterQuery(filterQuery);
                }
            }

        }

        //排序
        if (sortField != null && sortType != null && !"".equals(sortField) && !"".equals(sortType)) {
            if ("ASC".equals(sortType)) {
                Sort sort = new Sort(Sort.Direction.ASC, "item_" + sortField);
                query.addSort(sort);
            }
            if ("DESC".equals(sortType)) {
                Sort sort = new Sort(Sort.Direction.DESC, "item_" + sortField);
                query.addSort(sort);
            }
        }


        HighlightPage<Item> items = solrTemplate.queryForHighlightPage(query, Item.class);

        List<HighlightEntry<Item>> highlighted = items.getHighlighted();

        List<Item> itemList = new ArrayList<>();

        for (HighlightEntry<Item> itemHighlightEntry : highlighted) {
            Item entity = itemHighlightEntry.getEntity();
            List<HighlightEntry.Highlight> highlights = itemHighlightEntry.getHighlights();
            if (highlights.size() > 0 && highlights != null) {
                List<String> snipplets = highlights.get(0).getSnipplets();
                if (snipplets != null && snipplets.size() > 0) {
                    String title = snipplets.get(0);
                    entity.setTitle(title);
                }
            }
            itemList.add(entity);
        }


        Map<String, Object> resultMap = new HashMap<>();

        //查询到的结果集
        resultMap.put("rows", itemList);
        //查询到的总页数
        resultMap.put("totalPages", items.getTotalPages());
        //查询到的总条数
        resultMap.put("total", items.getTotalElements());

        return resultMap;
    }
}
