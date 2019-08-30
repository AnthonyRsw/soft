package com.rsw.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rsw.pojo.entity.Result;
import com.rsw.pojo.item.Item;
import com.rsw.pojo.item.ItemCat;
import com.rsw.service.ItemCatService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference
    private ItemCatService catService;

    @RequestMapping("/findByParentId")
    public List<ItemCat> findByParentId(Long parentId) {
        List<ItemCat> list = catService.findByParentId(parentId);
        return list;
    }

    @RequestMapping("/add")
    public Result add(@RequestBody ItemCat itemCat) {
        try {
            catService.add(itemCat);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }

    @RequestMapping("/update")
    public Result update(@RequestBody ItemCat itemCat) {
        try {
            catService.update(itemCat);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    @RequestMapping("/findOne")
    public ItemCat findOne(Long id) {
        return catService.findOne(id);
    }

    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            catService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }

    }

    @RequestMapping("/findAll")
    public List<ItemCat> findAll() {
        List<ItemCat> list=catService.findAll();
        return list;

    }
}
