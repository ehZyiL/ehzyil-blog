package com.ehzyil.service;

import com.ehzyil.domain.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.front.ArticleConditionList;
import com.ehzyil.model.vo.front.TagVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface ITagService extends IService<Tag> {

    List<TagVo> listTagVO();

    /**
     * 查看标签下的文章
     * @param condition
     * @return
     */
    ArticleConditionList listArticleTag(ConditionDTO condition);
}
