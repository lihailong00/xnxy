package com.lee.xnxydev.controller;

import com.lee.xnxydev.aop.LogAnnotation;
import com.lee.xnxydev.pojo.VO.ResponseResult;
import com.lee.xnxydev.util.QiNiuUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 将图片上传至七牛云
 * @author 晓龙coding
 */
@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadController {
    @Resource
    private QiNiuUtil qiNiuUtil;

    @PostMapping("/image")
//    @LogAnnotation(module="上传", operator="上传图片")
    String uploadPictureCloud(MultipartFile file) {
        long size = file.getSize();
        log.info("文件大小：" + size);
        // 截取文件名，并用新的文件名替换
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        int startIndex = originalFilename.lastIndexOf('.');
        String ext = originalFilename.substring(startIndex);
        String randomName = UUID.randomUUID().toString().replace("-", "");
        String newFilename = randomName + ext;

        // 判断是否上传成功
        boolean success = qiNiuUtil.upload(file, newFilename);
        if (success) {
            return qiNiuUtil.getUrl() + newFilename;
        }
        // 图片上传失败，返回空串
        return "";
    }
}