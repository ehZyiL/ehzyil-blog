package com.ehzyil.service;

import com.ehzyil.domain.Album;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.model.vo.AlbumVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface IAlbumService extends IService<Album> {
    /**
     * 查看相册列表
     * @return
     */
    List<AlbumVO> listAlbumVO();
}
