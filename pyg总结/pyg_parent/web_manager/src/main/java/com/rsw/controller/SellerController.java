package com.rsw.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rsw.pojo.entity.PageResult;
import com.rsw.pojo.entity.Result;
import com.rsw.pojo.seller.Seller;
import com.rsw.service.SellerService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Reference
    private SellerService sellerService;

    @RequestMapping("/search")
    public PageResult search(@RequestBody Seller seller, Integer page, Integer rows) {
        PageResult result = sellerService.findPage(seller, page, rows);
        return result;
    }
    @RequestMapping("/findOne")
    public Seller findOne(String id) {
        Seller seller = sellerService.findOne(id);
        return seller;
    }
    @RequestMapping("/updateStatus")
    public Result updateStatus(String sellerId,String status) {
        try {
            sellerService.updateStatus(sellerId,status);
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"失败");
        }
    }

}
