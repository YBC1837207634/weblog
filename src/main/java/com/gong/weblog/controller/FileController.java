package com.gong.controller;

import cn.hutool.crypto.SecureUtil;
import com.gong.enums.BusinessType;
import com.gong.annotation.Log;
import com.gong.common.ResponseStatus;
import com.gong.entity.manage.FileEntity;
import com.gong.vo.Result;
import com.gong.service.manage.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.UUID;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Autowired
    private FileService fileService;

    @Value("${my-config.dir-name}")
    private String basePath;

    @Value("${my-config.base-url}")
    private String baseUrl;

    /**
     * 文件上传接口： http://localhost:8080/upload
     * @param file
     * @return
     * @throws IOException
     */
    @Log(title = "上传文件", businessType = BusinessType.INSERT)
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        if (file == null || file.getSize() == 0 || !StringUtils.hasText(file.getOriginalFilename())) {
            return Result.error(ResponseStatus.BAD_REQUEST, "上传的文件不符合要求！");
        }
        String suffix = "";
        String fileName = "";
        String originalFilename = file.getOriginalFilename();
        int i = originalFilename.lastIndexOf(".");
        // 没有后缀名
        if (i != -1) {
            suffix = originalFilename.substring(i);
        }
        // 如果没有就创建文件夹
        checkDir();
        // md5 加密
        InputStream inputStream = file.getInputStream();
        String md5 = SecureUtil.md5(inputStream);
        // 查看数据库中是否存储该文件
        FileEntity fileEntity = fileService.getByMd5(md5);
        if (fileEntity == null) {
            // 生成 uuid
            UUID uuid = UUID.randomUUID();
            fileName = uuid + suffix;
            fileEntity = new FileEntity();
            fileEntity.setFileName(fileName);
            fileEntity.setSize(file.getSize());
            fileEntity.setType(file.getContentType());
            fileEntity.setMd5(md5);
            fileEntity.setUrl(baseUrl + "file/" + fileName);
            // 保存到本地
            file.transferTo(new File(basePath + fileName));
            // 保存到数据库中
            fileService.saveOne(fileEntity);
        }
        // 返回名称
        return Result.success(fileEntity.getUrl());
    }

    /**
     * 文件下载接口： http://localhost:8080/{fileName}
     * @param fileName
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileName}")
    public void download(@PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {
        log.info(fileName);
        FileEntity byFileName = fileService.getByFileName(fileName);
        if (byFileName == null) {
            throw new FileNotFoundException("没有找到" + fileName);
        }
        // 清空response
        response.reset();
        response.setCharacterEncoding("UTF-8");
        //attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
        // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
        response.addHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.addHeader("Content-Length", "" + byFileName.getSize());
        response.addHeader("content-type", byFileName.getType());
        try (FileInputStream inputStream = new FileInputStream(basePath + fileName);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
        ) {
            // 每次读取1MB的数据
            byte[] bytes = new byte[1024];
            while(inputStream.read(bytes) != -1) {
                // 写入到响应流中
                bufferedOutputStream.write(bytes);
            }
            bufferedOutputStream.flush();
        }  catch (FileNotFoundException ex) {
            // 处理数据库中有文件，但是本地却没有
            log.warn("数据库文件信息和本地不符！");
            // 从数据库中删除
            fileService.removeOne(byFileName.getId());
            // 出现异常抛给全局异常处理器
            throw ex;
        }
    }

    public void checkDir() {
        File file = new File(basePath);
        if (!file.exists())
            file.mkdir();
    }

}
