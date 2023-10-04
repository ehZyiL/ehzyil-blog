package com.ehzyil.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ehzyil.domain.Album;
import com.ehzyil.domain.Photo;
import com.ehzyil.mapper.AlbumMapper;
import com.ehzyil.mapper.BlogFileMapper;
import com.ehzyil.mapper.PhotoMapper;
import com.ehzyil.model.dto.AlbumDTO;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.admin.AlbumBackVO;
import com.ehzyil.model.vo.front.AlbumVO;
import com.ehzyil.model.vo.front.PageResult;
import com.ehzyil.service.IAlbumService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehzyil.strategy.context.UploadStrategyContext;
import com.ehzyil.utils.BeanCopyUtils;
import com.ehzyil.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static com.ehzyil.enums.FilePathEnum.PHOTO;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album> implements IAlbumService {

    @Override
    public List<AlbumVO> listAlbumVO() {
        List<Album> albums = getBaseMapper().selectList(new LambdaQueryWrapper<Album>()
                .select(Album::getId, Album::getAlbumName, Album::getAlbumDesc, Album::getAlbumCover)
                .eq(Album::getStatus, "1"));

        List<AlbumVO> albumVOS = BeanUtil.copyToList(albums, AlbumVO.class);
        return albumVOS;
    }

    @Autowired
    private PhotoMapper photoMapper;

    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    @Autowired
    private BlogFileMapper blogFileMapper;

    @Override
    public PageResult<AlbumBackVO> listAlbumBackVO(ConditionDTO condition) {
        // 查询相册数量
        Long count = getBaseMapper().selectCount(new LambdaQueryWrapper<Album>()
                .like(StringUtils.hasText(condition.getKeyword()), Album::getAlbumName, condition.getKeyword()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询相册信息
        List<AlbumBackVO> albumList = getBaseMapper().selectAlbumBackVO(PageUtils.getLimit(), PageUtils.getSize(), condition.getKeyword());
        return new PageResult<>(albumList, count);
    }

    @Override
    public void addAlbum(AlbumDTO album) {
        // 相册是否存在
        Album existAlbum = getBaseMapper().selectOne(new LambdaQueryWrapper<Album>()
                .select(Album::getId)
                .eq(Album::getAlbumName, album.getAlbumName()));
        Assert.isNull(existAlbum, album.getAlbumName() + "相册已存在");
        // 添加新相册
        Album newAlbum = BeanCopyUtils.copyBean(album, Album.class);
        baseMapper.insert(newAlbum);
    }

    @Override
    public void deleteAlbum(Integer albumId) {
        // 查询照片数量
        Long count = photoMapper.selectCount(new LambdaQueryWrapper<Photo>()
                .eq(Photo::getAlbumId, albumId));
        Assert.isFalse(count > 0, "相册下存在照片");
        // 不存在照片则删除
        getBaseMapper().deleteById(albumId);
    }

    @Override
    public void updateAlbum(AlbumDTO album) {
        // 相册是否存在
        Album existAlbum = getBaseMapper().selectOne(new LambdaQueryWrapper<Album>()
                .select(Album::getId)
                .eq(Album::getAlbumName, album.getAlbumName()));
        Assert.isFalse(Objects.nonNull(existAlbum) && !existAlbum.getId().equals(album.getId()),
                album.getAlbumName() + "相册已存在");
        // 修改相册
        Album newAlbum = BeanCopyUtils.copyBean(album, Album.class);
        baseMapper.updateById(newAlbum);
    }

    @Override
    public AlbumDTO editAlbum(Integer albumId) {
        return getBaseMapper().selectAlbumById(albumId);
    }

    @Override
    public String uploadAlbumCover(MultipartFile file) {
        // 上传文件
       return uploadStrategyContext.executeUploadStrategy(file, PHOTO.getPath());
    }
}
