package com.ehzyil.service;

import com.ehzyil.domain.BlogFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.dto.FolderDTO;
import com.ehzyil.model.vo.admin.FileVO;
import com.ehzyil.model.vo.front.PageResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    /**
     * 查看文件列表
     *
     * @param condition 查询条件
     * @return 文件列表
     */
    PageResult<FileVO> listFileVOList(ConditionDTO condition);

    /**
     * 上传文件
     *
     * @param file 文件
     * @param path 文件路径
     */
    void uploadFile(MultipartFile file, String path);

    /**
     * 创建文件夹
     *
     * @param folder 文件夹信息
     */
    void createFolder(FolderDTO folder);

    /**
     * 删除文件
     *
     * @param fileIdList 文件id列表
     */
    void deleteFile(List<Integer> fileIdList);

    /**
     * 下载文件
     *
     * @param fileId 文件id
     */
    void downloadFile(Integer fileId);
}
