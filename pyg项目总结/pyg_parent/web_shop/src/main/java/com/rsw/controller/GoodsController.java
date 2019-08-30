package com.rsw.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.Page;
import com.rsw.pojo.entity.GoodsEntity;
import com.rsw.pojo.entity.PageResult;
import com.rsw.pojo.entity.Result;
import com.rsw.pojo.good.Goods;
import com.rsw.service.GoodsService;
import com.rsw.service.SolrManagerService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/goods")
@RestController
public class GoodsController {

    @Reference
    GoodsService goodsService;

    @Reference
    SolrManagerService solrManagerService;

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

    @RequestMapping("/update")
    public Result update(@RequestBody GoodsEntity goodsEntity) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if (!username.equals(goodsEntity.getGoods().getSellerId())){
                return new Result(false, "您没有权限修改");
            }

            goodsService.update(goodsEntity);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }

    }

    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            for (Long id : ids) {
                goodsService.delete(id);
                //从solr中删除该商品根据id
                solrManagerService.deleteItemFromSolr(id);

            }
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
        }

    }


}



