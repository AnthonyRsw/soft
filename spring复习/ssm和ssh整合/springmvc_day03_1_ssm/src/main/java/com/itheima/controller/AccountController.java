package com.itheima.controller;

import com.itheima.domain.Account;
import com.itheima.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    /**
     * 查询全部
     * @return
     */
    @RequestMapping("/findAll")
    public ModelAndView findAll(){
        //查询数据
        List<Account> accountList = accountService.findAll();
        System.out.println(accountList);
        ModelAndView modelAndView = new ModelAndView();
        //添加数据
        modelAndView.addObject("accountList",accountList);
        //指定页面
        modelAndView.setViewName("show");
        return modelAndView;
    }
    /**
     * 保存操作
     *  执行service中的save方法
     *  最后查询数据库中所有的的内容，在页面展示
     *
     */
    @RequestMapping("/save")
    public String save(Account account){
        //执行保存操作
        accountService.save(account);
        //执行查询所有
        return "redirect:findAll";
    }

    @RequestMapping("/del")
    public String del(Integer id){

        //执行删除操作
        accountService.del(id);
        //执行查询所有
        return "redirect:findAll";
    }

    /**
     * 更新页面数据回显
     * @return
     */
    @RequestMapping("/updateUI")
    public ModelAndView updateUI(Integer id){
        //根据id查询一个账户
        Account account = accountService.findById(id);
        //创建模型视图对象
        ModelAndView modelAndView = new ModelAndView();
        //添加数据
        modelAndView.addObject("account", account);
        //指定页面
        modelAndView.setViewName("update");
        return modelAndView;
    }

    /**
     * 更新账户
     * @param account
     * @return
     */
    @RequestMapping("/update")
    public String update(Account account){
        //更新操作
        accountService.update(account);
        //执行查询所有
        return "redirect:findAll";
    }
}
