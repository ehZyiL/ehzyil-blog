package com.ehzyil.mapper;

import com.ehzyil.domain.Photo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ehzyil.model.vo.admin.PhotoBackVO;
import com.ehzyil.model.vo.admin.PhotoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface PhotoMapper extends BaseMapper<Photo> {

    /**
     * 查询后台照片列表
     *
     * @param limit   页码
     * @param size    大小
     * @param albumId 相册id
     * @return 后台照片列表
     */
    List<PhotoBackVO> selectPhotoBackVOList(@Param("limit") Long limit, @Param("size") Long size, @Param("albumId") Integer albumId);

    /**
     * 查询照片列表
     *
     * @param albumId 相册id
     * @return 后台照片列表
     */
    List<PhotoVO> selectPhotoVOList(@Param("albumId") Integer albumId);
}
