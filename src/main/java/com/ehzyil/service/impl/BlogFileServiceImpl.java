package com.ehzyil.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehzyil.domain.BlogFile;
import com.ehzyil.exception.ServiceException;
import com.ehzyil.mapper.BlogFileMapper;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.dto.FolderDTO;
import com.ehzyil.model.vo.admin.FileVO;
import com.ehzyil.model.vo.front.PageResult;
import com.ehzyil.service.IBlogFileService;
import com.ehzyil.strategy.context.UploadStrategyContext;
import com.ehzyil.utils.FileUtils;
import com.ehzyil.utils.PageUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Objects;

import static com.ehzyil.constant.CommonConstant.FALSE;
import static com.ehzyil.constant.CommonConstant.TRUE;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
@Service
public class BlogFileServiceImpl extends ServiceImpl<BlogFileMapper, BlogFile> implements IBlogFileService {
    /**
     * 本地路径
     */
    @Value("${upload.local.path}")
    private String localPath;

    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    public void uploadToDB(MultipartFile file, String path, String md5, String extName, String url) {
        //查询文件是否存在
        BlogFile existFile = getBaseMapper().selectOne(new LambdaQueryWrapper<BlogFile>()
                .select(BlogFile::getId, BlogFile::getFilePath)
                .eq(BlogFile::getFileName, md5)
        );
        // 保存文件信息
        if (Objects.isNull(existFile)) {
            //不存在插入
            BlogFile newFile = BlogFile.builder()
                    .fileUrl(url)
                    .fileName(md5)
                    .filePath(path)
                    .extendName(extName)
                    .fileSize((int) file.getSize())
                    .isDir(FALSE)
                    .build();
            getBaseMapper().insert(newFile);
        } else {
            //存在更新
            String newFilePath = existFile.getFilePath();
            //不包含原路径就添加新路径
            if (!existFile.getFilePath().contains(path)) {
                existFile.setFilePath(newFilePath + "," + path);
            }
            getBaseMapper().updateById(existFile);
        }
    }

    @Override
    public PageResult<FileVO> listFileVOList(ConditionDTO condition) {
        // 查询文件数量
        Long count = getBaseMapper().selectCountByCase(condition.getFilePath().substring(1));

        if (count == 0) {
            return new PageResult<>();
        }
        // 查询文件列表
        List<FileVO> fileVOList = getBaseMapper().selectFileVOList(PageUtils.getLimit(), PageUtils.getSize(),
                condition.getFilePath().substring(1));
        return new PageResult<>(fileVOList, count);
    }

    @Override
    public void uploadFile(MultipartFile file, String path) {
        String uploadPath = "/".equals(path) ? path : path + "/";
        // 上传文件
        uploadStrategyContext.executeUploadStrategy(file, uploadPath);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createFolder(FolderDTO folder) {
        String fileName = folder.getFileName();
        String filePath = folder.getFilePath();
        // 判断目录是否存在
        BlogFile blogFile = getBaseMapper().selectOne(new LambdaQueryWrapper<BlogFile>()
                .select(BlogFile::getId)
                .eq(BlogFile::getFilePath, folder.getFilePath())
                .eq(BlogFile::getFileName, fileName));
        Assert.isNull(blogFile, "目录已存在");
        // 创建目录
        File directory = new File(localPath + filePath + "/" + fileName);
        if (FileUtils.mkdir(directory)) {
            BlogFile newBlogFile = BlogFile.builder()
                    .fileName(fileName)
                    .filePath(filePath)
                    .isDir(TRUE)
                    .build();
            getBaseMapper().insert(newBlogFile);
        } else {
            throw new ServiceException("创建目录失败");
        }
    }
    //TODO 待优化
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteFile(List<Integer> fileIdList) {
        List<BlogFile> blogFiles = getBaseMapper().selectList(new LambdaQueryWrapper<BlogFile>()
                .select(BlogFile::getFileName, BlogFile::getFilePath, BlogFile::getExtendName, BlogFile::getIsDir)
                .in(BlogFile::getId, fileIdList));
        // 删除数据库中的文件信息
        getBaseMapper().deleteBatchIds(fileIdList);
        blogFiles.forEach(blogFile -> {
            File file;
            String fileName = localPath + blogFile.getFilePath() + "/" + blogFile.getFileName();
            if (blogFile.getIsDir().equals(TRUE)) {
                // 删除数据库中剩余的子文件
                String filePath = blogFile.getFilePath() + blogFile.getFileName();
                getBaseMapper().delete(new LambdaQueryWrapper<BlogFile>().eq(BlogFile::getFilePath, filePath));
                // 删除目录
                file = new File(fileName);
                if (file.exists()) {
                    FileUtils.deleteFile(file);
                }
            } else {
                // 删除文件
                file = new File(fileName + "." + blogFile.getExtendName());
                if (file.exists()) {
                    file.delete();
                }
            }
        });
    }


    @Override
    public void downloadFile(Integer fileId) {

    }
}
