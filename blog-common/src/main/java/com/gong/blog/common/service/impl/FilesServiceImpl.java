package com.gong.blog.common.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gong.blog.common.entity.FileEntity;
import com.gong.blog.common.exception.CUDException;
import com.gong.blog.common.exception.SystemException;
import com.gong.blog.common.mapper.FilesMapper;
import com.gong.blog.common.service.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

/**
* @author asus
* @description 针对表【files】的数据库操作Service实现
* @createDate 2023-10-27 15:17:40
*/
@Service
public class FilesServiceImpl extends ServiceImpl<FilesMapper, FileEntity> implements FilesService{


    @Value("${my-config.ossFilePath}")
    private String ossFilePath;

    @Value("${my-config.bucketName}")
    private String bucketName;

    @Autowired
    private FilesMapper filesMapper;

    @Override
    public void ossUpload(String objectName, byte[] content) throws Exception {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-shanghai.aliyuncs.com";
        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        // 填写Bucket名称，例如examplebucket。
//        String bucketName = "gwenstart";
        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
//        String objectName = "images/exampleobject.txt";
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);
        try {
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
            throw new CUDException("图片上传失败");
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
            throw new CUDException("图片上传失败");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    @Override
    public String upload(MultipartFile file) {
        if (file == null || file.isEmpty() || file.getSize() == 0 || !StringUtils.hasText(file.getOriginalFilename())) {
            throw new CUDException("上传的文件不符合要求！");

        }
        String suffix = "";
        String fileName = "";
        String originalFilename = file.getOriginalFilename();
        int i = originalFilename.lastIndexOf(".");
        // 没有后缀名
        if (i != -1) {
            suffix = originalFilename.substring(i);
        }
        // md5 加密
        String md5 = "";
        try (InputStream inputStream = file.getInputStream()) {
            md5 = SecureUtil.md5(inputStream);
        } catch (Exception e){
            e.printStackTrace();
            throw new SystemException("file error");
        }
        // 查看数据库中是否存储该文件
        LambdaQueryWrapper<FileEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FileEntity::getMd5, md5);
        FileEntity fileEntity = filesMapper.selectOne(queryWrapper);
        if (fileEntity == null) {
            // 生成 uuid
            UUID uuid = UUID.randomUUID();
            fileName = uuid + suffix;
            try {
                ossUpload(ossFilePath + fileName, file.getBytes());
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new CUDException("图片上传失败");
            }

            fileEntity = new FileEntity();
            fileEntity.setFileName(fileName);
            fileEntity.setSize(file.getSize());
            fileEntity.setType(file.getContentType());
            fileEntity.setMd5(md5);
//            fileEntity.setUrl(baseUrl + "file/" + fileName);
            fileEntity.setUrl(ossFilePath + fileName);
            // 保存到本地
//            file.transferTo(new File(basePath + fileName));
            // 保存到数据库中
            filesMapper.insert(fileEntity);

        }
        // 返回名称
        return fileEntity.getUrl();
    }


}




