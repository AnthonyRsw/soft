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

@RequestMapping("/seller")
@RestController
public class SellerController {

    @Reference
    SellerService sellerService;

    @RequestMapping("/add")
    public Result add(@RequestBody Seller seller) {
        try {
            sellerService.add(seller);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, "添加失败");

        }

    }

}



