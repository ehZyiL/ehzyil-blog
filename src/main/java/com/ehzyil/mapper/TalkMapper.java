package com.ehzyil.mapper;

import com.ehzyil.domain.Talk;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ehzyil.model.vo.admin.TalkBackInfoVO;
import com.ehzyil.model.vo.admin.TalkBackVO;
import com.ehzyil.model.vo.front.TalkVO;
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

    /**
     * 查询后台说说列表
     *
     * @param limit  页码
     * @param size   大小
     * @param status 状态
     * @return 后台说说列表
     */
    List<TalkBackVO> selectTalkBackVO(@Param("limit") Long limit, @Param("size") Long size, @Param("status") Integer status);

    /**
     * 根据id查询后台说说
     *
     * @param talkId 说说id
     * @return 后台说说
     */
    TalkBackInfoVO selectTalkBackById(Integer talkId);
}
