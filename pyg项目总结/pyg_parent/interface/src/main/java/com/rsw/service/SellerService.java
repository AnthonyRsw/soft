package com.rsw.service;

import com.rsw.pojo.entity.PageResult;
import com.rsw.pojo.seller.Seller;

public interface SellerService {
    void add(Seller seller);

    Seller findOne(String id);

    PageResult findPage(Seller seller, Integer page, Integer rows);

    void updateStatus(String sellerId, String status);
}
