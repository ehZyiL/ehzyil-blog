package com.ehzyil.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ehyzil
 * @Description
 * @create 2023-11-2023/11/6-17:32
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "upload.local")
public class LocalProperties {

    /**
     * 存储绝对路径
     */
    private String absTmpPath;

    /**
     * 存储相对路径
     */
    private String webImgPath;

    /**
     * 上传文件的临时存储目录
     */
    private String tmpUploadPath;

    /**
     * 访问图片的host
     */
    private String cdnHost;


    public String buildImgUrl(String url) {
        if (!url.startsWith(cdnHost)) {
            return cdnHost + url;
        }
        return url;
    }
}