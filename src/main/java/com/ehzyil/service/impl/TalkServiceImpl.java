package com.ehzyil.service.impl;

import com.ehzyil.domain.Talk;
import com.ehzyil.mapper.TalkMapper;
import com.ehzyil.service.ITalkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
@Service
public class TalkServiceImpl extends ServiceImpl<TalkMapper, Talk> implements ITalkService {

}
