package com.rsw.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rsw.pojo.ad.ContentCategory;
import com.rsw.pojo.entity.PageResult;
import com.rsw.pojo.entity.Result;
import com.rsw.service.ContentCategoryService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/contentCategory")
@RestController
public class ContentCategoryController {

    @Reference
    ContentCategoryService contentCategoryService;

    @RequestMapping("/findAll")
    public List<ContentCategory> findAll() {
        List<ContentCategory> list = contentCategoryService.findAll();
        return list;
    }

    @RequestMapping("/search")
    public PageResult search(@RequestBody ContentCategory contentCategory, Integer page, Integer rows) {
        return contentCategoryService.findPage(contentCategory, page, rows);

    }

    @RequestMapping("/add")
    public Result add(@RequestBody ContentCategory contentCategory) {

        try {
            contentCategoryService.save(contentCategory);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }

    }

    @RequestMapping("/update")
    public Result update(@RequestBody ContentCategory contentCategory) {

        try {
            contentCategoryService.update(contentCategory);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }

    }

    @RequestMapping("/findOne")
    public ContentCategory findOne(Long id) {

        return contentCategoryService.findOne(id);
    }

    @RequestMapping("/delete")
    public Result delete(Long[] ids) {

        try {
            contentCategoryService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }

    }


}



