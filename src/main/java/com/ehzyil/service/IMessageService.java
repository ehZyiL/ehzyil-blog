package com.ehzyil.service;

import com.ehzyil.domain.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.model.dto.CheckDTO;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.dto.MessageDTO;
import com.ehzyil.model.vo.admin.MessageBackVO;
import com.ehzyil.model.vo.front.MessageVO;
import com.ehzyil.model.vo.front.PageResult;

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

    /**
     * 查看后台留言列表
     *
     * @param condition 条件
     * @return 后台留言列表
     */
    PageResult<MessageBackVO> listMessageBackVO(ConditionDTO condition);


    /**
     * 审核留言
     *
     * @param check 审核信息
     */
    void updateMessageCheck(CheckDTO check);
}
