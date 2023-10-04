package com.ehzyil.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehzyil.domain.BlogFile;
import com.ehzyil.mapper.BlogFileMapper;
import com.ehzyil.service.IBlogFileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static com.ehzyil.constant.CommonConstant.FALSE;

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
}
