package com.ehzyil.mapper;

import com.ehzyil.domain.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ehzyil.model.vo.TagVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface TagMapper extends BaseMapper<Tag> {


    /**
     * 前端查询标签列表
     * @return
     */
    List<TagVo> selectTagVO();
}
