package com.ehzyil.service;

import com.ehzyil.domain.BlogFile;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface IBlogFileService extends IService<BlogFile> {

    public void uploadToDB(MultipartFile file, String path, String md5, String extName, String url);

}
