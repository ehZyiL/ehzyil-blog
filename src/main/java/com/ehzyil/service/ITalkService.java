package com.ehzyil.service;

import com.ehzyil.domain.Talk;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.model.vo.front.PageResult;
import com.ehzyil.model.vo.front.TalkVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface ITalkService extends IService<Talk> {
    /**
     * 首页说说
     * @return
     */
    List<String> listTalkHome();

    /**
     * 查询说说列表
     * @return
     */
    PageResult<TalkVO> listTalkVO( Long current, Long size);

    /**
     * 查看说说
     * @param talkId
     * @return
     */
    TalkVO getTalkById(Long talkId);
}
