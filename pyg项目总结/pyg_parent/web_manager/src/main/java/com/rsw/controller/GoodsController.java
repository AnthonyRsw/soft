package com.rsw.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rsw.pojo.entity.GoodsEntity;
import com.rsw.pojo.entity.PageResult;
import com.rsw.pojo.entity.Result;
import com.rsw.pojo.good.Goods;
import com.rsw.service.CmsService;
import com.rsw.service.GoodsService;
import com.rsw.service.SolrManagerService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/goods")
@RestController
public class GoodsController {

    @Reference
    GoodsService goodsService;

    @Reference
    SolrManagerService solrManagerService;

    @Reference
    CmsService cmsService;

    @RequestMapping("/add")
    public Result add(@RequestBody GoodsEntity goodsEntity) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            goodsEntity.getGoods().setSellerId(username);

            goodsService.add(goodsEntity);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }

    }

    @RequestMapping("/search")
    public PageResult search(@RequestBody Goods goods, Integer page, Integer rows) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        goods.setSellerId(username);
        PageResult pb = goodsService.findByPage(goods, page, rows);
        return pb;

    }

    @RequestMapping("/findOne")
    public GoodsEntity findOne(Long id) {
        return goodsService.findOne(id);

    }

    @RequestMapping("/updateStatus")
    public Result updateStatus(Long[] ids,String status) {
        try {
            for (Long id : ids) {
                goodsService.updateStatus(id,status);
                //对审核通过的商品向solr中添加数据
                if (status.equals("1")){
                    solrManagerService.saveItemByGoodId(id);
                    Map<String, Object> goodsData = cmsService.findGoodsData(id);
                    cmsService.createStaticPage(id,goodsData);
                }

            }
            return new Result(true,"审核成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"审核失败");
        }

    }


}



