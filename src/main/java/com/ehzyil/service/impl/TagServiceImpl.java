package com.ehzyil.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehzyil.domain.Tag;
import com.ehzyil.mapper.ArticleMapper;
import com.ehzyil.mapper.TagMapper;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.ArticleConditionList;
import com.ehzyil.model.vo.ArticleConditionVO;
import com.ehzyil.model.vo.TagVo;
import com.ehzyil.service.ITagService;
import com.ehzyil.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<TagVo> listTagVO() {
        return tagMapper.selectTagVO();
    }

    @Override
    public ArticleConditionList listArticleTag(ConditionDTO condition) {
        //查询符合条件的文章
        List<ArticleConditionVO> articleConditionVOS = articleMapper.listArticleByCondition(
                PageUtils.getLimit(), PageUtils.getSize(), condition);
        //查询标签名字
        String name = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                        .select(Tag::getTagName)
                        .eq(Tag::getId, condition.getTagId()))
                .getTagName();
        //构建对象返回
        return ArticleConditionList.builder()
                .articleConditionVOList(articleConditionVOS)
                .name(name)
                .build();
    }
}
