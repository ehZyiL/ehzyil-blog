package com.ehzyil.service;

import com.ehzyil.domain.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.dto.TagDTO;
import com.ehzyil.model.vo.admin.TagBackVO;
import com.ehzyil.model.vo.front.ArticleConditionList;
import com.ehzyil.model.vo.front.PageResult;
import com.ehzyil.model.vo.front.TagOptionVO;
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


    /**
     * 查看后台标签列表
     *
     * @param condition 条件
     * @return 后台标签列表
     */
    PageResult<TagBackVO> listTagBackVO(ConditionDTO condition);

    /**
     * 添加标签
     *
     * @param tag 标签
     */
    void addTag(TagDTO tag);

    /**
     * 删除标签
     *
     * @param tagIdList 标签id集合
     */
    void deleteTag(List<Integer> tagIdList);

    /**
     * 修改标签
     *
     * @param tag 标签
     */
    void updateTag(TagDTO tag);


    /**
     * 搜索文章标签
     *
     * @return 标签列表
     */
    List<TagOptionVO> listTagOption();
}
