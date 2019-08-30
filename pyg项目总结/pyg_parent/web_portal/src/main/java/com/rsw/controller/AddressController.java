package com.rsw.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.rsw.pojo.address.Address;
import com.rsw.pojo.entity.BuyerCart;
import com.rsw.pojo.entity.Result;
import com.rsw.service.AddressService;
import com.rsw.service.CartService;
import com.rsw.util.Constants;
import com.rsw.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping("/address")
@RestController
public class AddressController {

    @Reference
    AddressService addressService;

    @RequestMapping("/findListByLoginUser")
    public List<Address> findListByLoginUser(){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return addressService.findListByLoginUser(userId);
    }




}



