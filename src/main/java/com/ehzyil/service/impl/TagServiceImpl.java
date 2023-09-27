package com.ehzyil.service.impl;

import com.ehzyil.domain.Tag;
import com.ehzyil.mapper.TagMapper;
import com.ehzyil.model.vo.TagVo;
import com.ehzyil.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {
    @Autowired
    private TagMapper tagMapper;
    @Override
    public List<TagVo> listTagVO() {
        return tagMapper.selectTagVO();
    }
}
