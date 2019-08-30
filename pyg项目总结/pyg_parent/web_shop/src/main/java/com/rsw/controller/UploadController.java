package com.rsw.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.rsw.pojo.entity.Result;
import com.rsw.pojo.template.TypeTemplate;
import com.rsw.service.TypeTemplateService;
import com.rsw.util.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequestMapping("/upload")
@RestController
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;

    @RequestMapping("/uploadFile")
    public Result upload(MultipartFile file){
        //1、取文件的完整名称
        String fileName = file.getOriginalFilename();
        try {
            //2、创建一个 FastDFS 的客户端
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:fastDFS/fdfs_client.conf");
            //3、执行上传处理
            String path = fastDFSClient.uploadFile(file.getBytes(), file.getOriginalFilename(), file.getSize());
            //4、拼接返回的 url 和 ip 地址，拼装成完整的 url
            String url = FILE_SERVER_URL + path;
            return new Result(true,url);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "上传失败");
        }
    }



}
