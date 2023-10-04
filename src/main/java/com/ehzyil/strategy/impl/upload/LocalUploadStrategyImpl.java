package com.ehzyil.strategy.impl.upload;

import com.ehzyil.exception.ServiceException;
import com.ehzyil.service.IBlogFileService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author ehyzil
 * @Description
 * @create 2023-09-2023/9/28-12:07
 */
@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
@Service("localUploadStrategyImpl")
public class LocalUploadStrategyImpl extends AbstractUploadStrategyImpl {
    /**
     * 访问url
     */
    @Value("${upload.local.url}")
    private String localUrl;


    /**
     * 本地路径
     */
    @Value("${upload.local.path}")
    private String localPath;

    /**
     * 本地项目端口
     */
    @Value("${server.port}")
    private Integer port;

    @Autowired
    private IBlogFileService blogFileService;
    //TODO 本地上传待优化
    @Override
    public void initClient() {
//win 本地
//        try {
//            localPath = ResourceUtils.getURL("classpath:").getPath() + "static/imgs/";
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            throw new ServiceException("文件不存在");
//        }

        // 判断目录是否存在
        File directory = new File(localPath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                throw new ServiceException("创建目录失败");
            }
        }

    }

    @Override
    public Boolean exists(String filePath) {
        return new File(localPath + filePath).exists();
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream) throws IOException {
        // 判断上传目录是否存在
        File directory = new File(localPath + path);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new ServiceException("创建目录失败");
            }
        }
        // 写入文件
        File file = new File(localPath + path + fileName);
        if (file.createNewFile()) {
            //使用缓冲流写入本地
            try (BufferedInputStream bis = new BufferedInputStream(inputStream);
                 BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
                byte[] bytes = new byte[4096];
                int length;
                while ((length = bis.read(bytes)) != -1) {
                    bos.write(bytes, 0, length);
                }
            }

        }
    }


    @Override
    public String getFileAccessUrl(String filePath) {
        return localPath + filePath;
    }

    @Override
    public void uploadToDB(MultipartFile file, String path, String md5, String extName, String url) {
        blogFileService.uploadToDB(file,path,md5,extName,url);
    }
}
