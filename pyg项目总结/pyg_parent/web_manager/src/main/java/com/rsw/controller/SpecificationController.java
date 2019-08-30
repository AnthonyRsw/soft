package com.rsw.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rsw.pojo.entity.PageResult;
import com.rsw.pojo.entity.Result;
import com.rsw.pojo.entity.SpecEntity;
import com.rsw.pojo.good.Brand;
import com.rsw.pojo.specification.Specification;
import com.rsw.service.BrandService;
import com.rsw.service.SpecificationService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequestMapping("/specification")
@RestController
public class SpecificationController {

    @Reference
    SpecificationService specificationService;

    @RequestMapping("/search")
    public PageResult search(@RequestBody Specification specification, Integer page, Integer rows) {
        return specificationService.findPage(specification,page, rows);

    }

    @RequestMapping("/add")
    public Result add(@RequestBody SpecEntity specEntity) {

        try {
            specificationService.add(specEntity);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }

    }

    @RequestMapping("/update")
    public Result update(@RequestBody SpecEntity specEntity) {

        try {
            specificationService.update(specEntity);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }

    }

    @RequestMapping("/findOne")
    public SpecEntity findOne(Long id) {

        return specificationService.findOne(id);
    }

    @RequestMapping("/delete")
    public Result delete(Long[] ids) {

        try {
            specificationService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }

    }

    @RequestMapping("/selectOptionList")
    public List<Map> selectOptionList() {

        return specificationService.selectOptionList();
    }



}
