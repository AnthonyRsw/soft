package com.rsw.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.Page;
import com.rsw.pojo.entity.PageResult;
import com.rsw.pojo.entity.Result;
import com.rsw.pojo.good.Brand;
import com.rsw.service.BrandService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequestMapping("/brand")
@RestController
public class BrandController {

    @Reference
    BrandService brandService;

    @RequestMapping("/findAll")
    public List<Brand> findAll() {
        List<Brand> brandList = brandService.findAll();
        return brandList;
    }

    @RequestMapping("/search")
    public PageResult search(@RequestBody Brand brand, Integer page, Integer rows) {
        return brandService.findPage(brand, page, rows);

    }

    @RequestMapping("/save")
    public Result add(@RequestBody Brand brand) {

        try {
            brandService.save(brand);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }

    }

    @RequestMapping("/update")
    public Result update(@RequestBody Brand brand) {

        try {
            brandService.update(brand);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }

    }

    @RequestMapping("/findById")
    public Brand findById(Long id) {

        return brandService.findOne(id);
    }

    @RequestMapping("/delete")
    public Result delete(Long[] ids) {

        try {
            brandService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }

    }

    @RequestMapping("/selectOptionList")
    public List<Map> selectOptionList() {

        return brandService.selectOptionList();
    }

}



