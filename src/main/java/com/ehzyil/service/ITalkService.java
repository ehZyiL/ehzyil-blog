package com.ehzyil.service;

import com.ehzyil.domain.Talk;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.dto.TalkDTO;
import com.ehzyil.model.vo.admin.TalkBackInfoVO;
import com.ehzyil.model.vo.admin.TalkBackVO;
import com.ehzyil.model.vo.front.PageResult;
import com.ehzyil.model.vo.front.TalkVO;
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

    /**
     * 查看后台说说列表
     *
     * @param condition 条件
     * @return {@link PageResult<TalkBackVO>} 说说列表
     */
    PageResult<TalkBackVO> listTalkBackVO(ConditionDTO condition);

    /**
     * 添加说说
     *
     * @param talk 说说
     */
    void addTalk(TalkDTO talk);

    /**
     * 删除说说
     *
     * @param talkId 说说id
     */
    void deleteTalk(Integer talkId);

    /**
     * 修改说说
     *
     * @param talk 说说
     */
    void updateTalk(TalkDTO talk);

    /**
     * 编辑说说
     *
     * @param talkId 说说id
     * @return 说说
     */
    TalkBackInfoVO editTalk(Integer talkId);
    /**
     * 上传说说图片
     *
     * @param file 文件
     * @return 说说图片地址
     */
    String uploadTalkCover(MultipartFile file);
}
