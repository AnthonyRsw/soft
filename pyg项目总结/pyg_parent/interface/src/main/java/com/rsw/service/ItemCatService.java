package com.rsw.service;

import com.rsw.pojo.item.ItemCat;

import java.util.List;

public interface ItemCatService {
    List<ItemCat> findByParentId(Long parentId);

    void add(ItemCat itemCat);

    void update(ItemCat itemCat);

    ItemCat findOne(Long id);

    void delete(Long[] ids);

    List<ItemCat> findAll();

}
