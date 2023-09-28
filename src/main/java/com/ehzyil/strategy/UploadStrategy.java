package com.ehzyil.strategy;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author ehyzil
 * @Description 上传策略
 * @create 2023-09-2023/9/28-11:24
 */
public interface UploadStrategy {

    String uploadFile(MultipartFile file,String path);

}
