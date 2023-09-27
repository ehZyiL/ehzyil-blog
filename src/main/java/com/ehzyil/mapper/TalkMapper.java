package com.ehzyil.mapper;

import com.ehzyil.domain.Talk;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ehzyil.model.vo.TalkVO;
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
public interface TalkMapper extends BaseMapper<Talk> {

    List<TalkVO> selectTalkList(@Param("limit") Long limit, @Param("size") Long size);

}
