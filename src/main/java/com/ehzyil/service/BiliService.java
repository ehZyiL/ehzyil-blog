package com.ehzyil.service;


import org.springframework.web.multipart.MultipartFile;

/**
 * @author ehyzil
 * @Description B站服务接口
 * @create 2023-09-2023/9/27-9:36
 */
public interface BiliService {

    /**
     * B站图片上传
     *
     * @param file 图片
     * @param csrf csrf
     * @param data data
     * @return 图片链接
     */
    String uploadBiliPicture(MultipartFile file, String csrf, String data);
}