package com.itheima.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Controller
public class LoginController {

    /**
     *  模拟登录操作
     *   判断用户名和密码是否在数据库中存在，如果存在，登录成功，跳转到查询全部账户页面
     *          如果不存在，登录失败，跳转到登录页面
     *  假设：用户名：zhangsan 密码：123
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/login")
    public String login(String username , String password, HttpServletRequest request){
        if("zhangsan".equals(username) && "123".equals(password)){
            //登录成功
            System.out.println("登录成功");
//            将登录信息存储到session域中
            request.getSession().setAttribute("username",username);
            return "redirect:/account/findAll";
        }else{
            System.out.println("登录失败");
            //进入登录页面
            return "redirect:/login.jsp";
        }
    }
}
