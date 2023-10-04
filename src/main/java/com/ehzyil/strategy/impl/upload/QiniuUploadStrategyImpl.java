package com.ehzyil.strategy.impl.upload;

import com.ehzyil.config.properties.QiniuProperties;
import com.ehzyil.mapper.BlogFileMapper;
import com.ehzyil.service.IBlogFileService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author ehyzil
 * @Description
 * @create 2023-09-2023/9/28-14:30
 */
@Slf4j
@Getter
@Setter
@Service("qiniuUploadStrategyImpl")
public class QiniuUploadStrategyImpl extends AbstractUploadStrategyImpl {

    @Autowired
    private QiniuProperties qiniuProperties;

    @Autowired
    private UploadManager uploadManager;

    @Autowired
    private BucketManager bucketManager;

    @Autowired
    private Auth auth;

    @Autowired
    private IBlogFileService blogFileService;

    @Override
    public void initClient() {

    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream) throws IOException {
        try {
            if (path.charAt(0) == '/') {
                path = path.substring(1);
            }
            // 上传图片文件
            Response res = uploadManager.put(inputStream, path + fileName, auth.uploadToken(qiniuProperties.getBucketName()), null, null);
            if (!res.isOK()) {
                throw new RuntimeException("上传七牛云出错：" + res.toString());
            }
        } catch (QiniuException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public Boolean exists(String filePath) {
        return false;
    }

    @Override
    public String getFileAccessUrl(String filePath) {
        return qiniuProperties.getUrl() + filePath;
    }

    @Override
    public void uploadToDB(MultipartFile file, String path, String md5, String extName, String url) {
        blogFileService.uploadToDB(file,path,md5,extName,url);
    }
}
