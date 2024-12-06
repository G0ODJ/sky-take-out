package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    @ApiOperation(value = "上传文件接口")
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        log.info("上传文件为：{}", file);
        // 使用UUID进行命名

        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String objectName = UUID.randomUUID().toString() + extension;
        try {
            String path = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(path);
        } catch (IOException e) {
            log.error("文件上传失败;{}",e.getMessage());
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
