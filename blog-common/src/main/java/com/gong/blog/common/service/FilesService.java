package com.gong.blog.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gong.blog.common.entity.FileEntity;
import org.springframework.web.multipart.MultipartFile;

/**
* @author asus
* @description 针对表【files】的数据库操作Service
* @createDate 2023-10-27 15:17:41
*/
public interface FilesService extends IService<FileEntity> {
    void ossUpload(String objectName, byte[] content) throws Exception;

    String upload(MultipartFile file);


}
