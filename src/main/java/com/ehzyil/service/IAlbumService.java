package com.ehzyil.service;

import com.ehzyil.domain.Album;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.model.dto.AlbumDTO;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.admin.AlbumBackVO;
import com.ehzyil.model.vo.front.AlbumVO;
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
public interface IAlbumService extends IService<Album> {
    /**
     * 查看相册列表
     * @return
     */
    List<AlbumVO> listAlbumVO();

    /**
     * 查看后台相册列表
     *
     * @param condition 条件
     * @return 后台相册列表
     */
    PageResult<AlbumBackVO> listAlbumBackVO(ConditionDTO condition);

    /**
     * 添加相册
     *
     * @param album 相册
     */
    void addAlbum(AlbumDTO album);

    /**
     * 删除相册
     *
     * @param albumId 相册id
     */
    void deleteAlbum(Integer albumId);

    /**
     * 修改相册
     *
     * @param album 相册
     */
    void updateAlbum(AlbumDTO album);

    /**
     * 编辑相册
     *
     * @param albumId 相册id
     * @return 相册信息
     */
    AlbumDTO editAlbum(Integer albumId);


    /**
     * 上传相册封面
     *
     * @param file 文件
     * @return 相册封面地址
     */
    String uploadAlbumCover(MultipartFile file);
}
