package com.gong.blog.manage.controller;

import com.gong.blog.common.annotation.Log;
import com.gong.blog.common.enums.BusinessType;
import com.gong.blog.common.service.FilesService;
import com.gong.blog.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Autowired
    private FilesService filesService;

    @Log(title = "上传文件", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<String> upload(MultipartFile file) {
        return Result.success(filesService.upload(file));
    }


}
