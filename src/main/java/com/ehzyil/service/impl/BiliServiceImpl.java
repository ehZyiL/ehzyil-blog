package com.ehzyil.service.impl;



import com.alibaba.fastjson2.JSON;
import com.ehzyil.service.BiliService;
import com.ehzyil.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author ehyzil
 * @Description B站图片接口实现类
 * @create 2023-09-2023/9/27-9:37
 */
@Service
public class BiliServiceImpl implements BiliService {

    @Value("${bili-url}")
    private String url;

    @Override
    public String uploadBiliPicture(MultipartFile file, String csrf, String sess) {
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Cookie", "SESSDATA=" + sess);
        // 上传图片
        String result = HttpClientUtils.uploadFileByHttpClient(url, csrf, headers, file);
        // 解析结果
        Object data = Objects.requireNonNull(JSON.parseObject(result)).get("data");
        String imageUrl = JSON.parseObject(data.toString()).get("image_url").toString();
        return imageUrl.replaceFirst("http", "https");
    }
}