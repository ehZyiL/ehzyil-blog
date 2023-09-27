package com.ehzyil.service;

import com.ehzyil.domain.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.model.dto.MessageDTO;
import com.ehzyil.model.vo.MessageVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface IMessageService extends IService<Message> {

    List<MessageVO> listTalkHome();

    /**
     * 添加留言
     *
     * @param message 留言
     */
    void addMessage(MessageDTO message);
}
