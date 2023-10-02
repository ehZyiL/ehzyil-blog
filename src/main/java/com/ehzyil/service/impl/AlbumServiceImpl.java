package com.ehzyil.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ehzyil.domain.Album;
import com.ehzyil.mapper.AlbumMapper;
import com.ehzyil.model.vo.AlbumVO;
import com.ehzyil.service.IAlbumService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}
