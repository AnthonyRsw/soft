package com.rsw.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.rsw.pojo.entity.BuyerCart;
import com.rsw.pojo.entity.Result;
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

@RequestMapping("/cart")
@RestController
public class BuyerCartController {

    @Reference
    CartService cartService;

    @Autowired
    HttpServletRequest request;

    @Autowired
    HttpServletResponse response;

    @CrossOrigin(origins="http://localhost:8085",allowCredentials="true")
    @RequestMapping("/addGoodsToCartList")
    public Result addGoodsToCartList(Long itemId, Integer num){

        try {
            //1.获取用户名
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            //2.获取购物车列表
            List<BuyerCart> cartList=findCartList();

            //3.将当前商品加入购物车
            List<BuyerCart> list = cartService.addItemToCartList(cartList, itemId, num);
            //4.判断当前用户是否登录,未登录用户名:anonymousUser
            if ("anonymousUser".equals(username)){
                //未登录,将购物车列表存入cookie中
                CookieUtil.setCookie(request,response, Constants.CART_LIST_COOKIE, JSON.toJSONString(list),60*60*24,"utf-8");
            }else{
                //已登录,将购物车列表存入redis中
                cartService.setCartListToRedis(username,cartList);

            }

            return new Result(true,"添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,"添加失败!");
        }
    }

    /**
     * 获得购物车
     * @return
     */
    @RequestMapping("/findCartList")
    private List<BuyerCart> findCartList() {
        //1.获取用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //2.从cookie中获得购物车
        String cookieCartListStr = CookieUtil.getCookieValue(request, Constants.CART_LIST_COOKIE, "utf-8");
        //3.判断数据是否为空
        if (cookieCartListStr==null||"".equals(cookieCartListStr)){
            cookieCartListStr="[]";
        }
        //4.将cookie中数据转为对象
        List<BuyerCart> cookieCartsList = JSON.parseArray(cookieCartListStr, BuyerCart.class);
        //4.判断用户是否登录
        if ("anonymousUser".equals(username)){
            return cookieCartsList;
        }else{
            List<BuyerCart> redisCartList = cartService.getCartListFromRedis(username);
            if (cookieCartsList.size()>0){
                redisCartList = cartService.mergeCookieCartListToRedisCartList(cookieCartsList, redisCartList);
                CookieUtil.deleteCookie(request,response,Constants.CART_LIST_COOKIE);
                cartService.setCartListToRedis(username,redisCartList);
            }
            return redisCartList;
        }

    }


}



