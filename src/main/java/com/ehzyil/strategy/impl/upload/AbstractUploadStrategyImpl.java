package com.ehzyil.strategy.impl.upload;

import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ehzyil.domain.BlogFile;
import com.ehzyil.strategy.UploadStrategy;
import com.ehzyil.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static com.ehzyil.constant.CommonConstant.FALSE;
import static com.ehzyil.enums.FilePathEnum.CONFIG;

/**
 * @author ehyzil
 * @Description
 * @create 2023-09-2023/9/28-11:25
 */
public abstract class AbstractUploadStrategyImpl implements UploadStrategy {

    @Override
    public String uploadFile(MultipartFile file, String path) {

        try {

            // 判断文件大小是否超过2MB（2MB=2*1024*1024 bytes）
            if (file.getSize() > 2 * 1024 * 1024) {
                // 抛出文件大小超过限制的异常
                throw new ServiceException("文件大小不能超出2MB！");
            }

            //获取文件md5值
            String md5 = FileUtils.getMd5(file.getInputStream());
            //获取拓展名
            String extName = FileUtils.getExtension(file);
            // 重新生成文件名
            String fileName = md5 + "." + extName;

            //region 初始化
            initClient();
            //endregion


            // 判断文件是否已存在
            if (!exists(path + fileName)) {
                // 不存在则继续上传
                upload(path, fileName, file.getInputStream());
            }
            // 返回文件访问路径
            String url=getFileAccessUrl(path + fileName);

            uploadToDB(file, path, md5, extName, url);


            return url;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("文件上传失败");
        }

    }



    /**
     * 初始化客户端
     */
    public abstract void initClient();

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return {@link Boolean}
     */
    public abstract Boolean exists(String filePath);

    /**
     * 上传
     *
     * @param path        路径
     * @param fileName    文件名
     * @param inputStream 输入流
     * @throws IOException io异常
     */
    public abstract void upload(String path, String fileName, InputStream inputStream) throws IOException;

    /**
     * 获取文件访问url
     *
     * @param filePath 文件路径
     * @return {@link String} 文件url
     */
    public abstract String getFileAccessUrl(String filePath);

    public abstract  void uploadToDB(MultipartFile file, String path, String md5, String extName, String url);
}
