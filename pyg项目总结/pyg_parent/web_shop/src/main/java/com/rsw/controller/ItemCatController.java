package com.rsw.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rsw.pojo.entity.Result;
import com.rsw.pojo.item.ItemCat;
import com.rsw.pojo.seller.Seller;
import com.rsw.pojo.template.TypeTemplate;
import com.rsw.service.ItemCatService;
import com.rsw.service.SellerService;
import com.rsw.service.TypeTemplateService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference
    ItemCatService itemCatService;

    @Reference
    TypeTemplateService typeTemplateService;


    @RequestMapping("/findByParentId")
    public List<ItemCat> findByParentId(Long parentId) {
        List<ItemCat> list = itemCatService.findByParentId(parentId);
        return list;

    }

    @RequestMapping("/findOne")
    public ItemCat findOne(Long id) {
        ItemCat itemCat = itemCatService.findOne(id);
        return itemCat;

    }
    @RequestMapping("/findAll")
    public List<ItemCat> findAll() {
        List<ItemCat> list=itemCatService.findAll();
        return list;

    }


}
