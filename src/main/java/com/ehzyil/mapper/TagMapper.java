package com.ehzyil.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ehzyil.domain.Tag;
import com.ehzyil.model.vo.front.TagOptionVO;
import com.ehzyil.model.vo.front.TagVo;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface TagMapper extends BaseMapper<Tag> {


    /**
     * 前端查询标签列表
     *
     * @return
     */
    List<TagVo> selectTagVO();

    /**
     * 查询标签列表
     *
     * @return 标签列表
     */
    List<TagOptionVO> selectTagOptionList();

}
