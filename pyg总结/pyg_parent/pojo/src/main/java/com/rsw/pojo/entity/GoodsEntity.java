package com.rsw.pojo.entity;


import com.rsw.pojo.good.Goods;
import com.rsw.pojo.good.GoodsDesc;
import com.rsw.pojo.item.Item;

import java.io.Serializable;
import java.util.List;

/**
 * 自定义封装商品实体类
 * 这里面包含, 商品对象, 商品详情对象, 库存集合对象
 */
public class GoodsEntity implements Serializable {
    //商品对象
    private Goods goods;
    //商品详情对象
    private GoodsDesc goodsDesc;
    //库存集合对象
    private List<Item> itemList;

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public GoodsDesc getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(GoodsDesc goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}
