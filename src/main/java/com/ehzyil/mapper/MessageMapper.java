package com.ehzyil.mapper;

import com.ehzyil.domain.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ehzyil.model.vo.MessageVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface MessageMapper extends BaseMapper<Message> {

    List<MessageVO> selectMessageVoList();
}
