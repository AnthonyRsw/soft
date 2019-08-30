package com.rsw.service;

import com.rsw.pojo.entity.GoodsEntity;
import com.rsw.pojo.entity.PageResult;
import com.rsw.pojo.good.Goods;

public interface GoodsService {

    void add(GoodsEntity goodsEntity);

    PageResult findByPage(Goods goods, Integer page, Integer rows);

    GoodsEntity findOne(Long id);

    void update(GoodsEntity goodsEntity);

    void delete(Long id);

    void updateStatus(Long id, String status);

}
