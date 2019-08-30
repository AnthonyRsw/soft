package com.rsw.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rsw.pojo.ad.Content;
import com.rsw.pojo.ad.ContentCategory;
import com.rsw.pojo.entity.PageResult;
import com.rsw.pojo.entity.Result;
import com.rsw.service.ContentService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/content")
@RestController
public class ContentController {

    @Reference
    ContentService contentService;

    @RequestMapping("/findAll")
    public List<Content> findAll() {
        List<Content> list = contentService.findAll();
        return list;
    }

    @RequestMapping("/search")
    public PageResult search(@RequestBody Content  content, Integer page, Integer rows) {
        return contentService.findPage(content, page, rows);

    }

    @RequestMapping("/add")
    public Result add(@RequestBody Content content) {

        try {
            contentService.save(content);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }

    }

    @RequestMapping("/update")
    public Result update(@RequestBody Content content) {

        try {
            contentService.update(content);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }

    }

    @RequestMapping("/findOne")
    public Content findOne(Long id) {

        return contentService.findOne(id);
    }

    @RequestMapping("/delete")
    public Result delete(Long[] ids) {

        try {
            contentService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }

    }


}



