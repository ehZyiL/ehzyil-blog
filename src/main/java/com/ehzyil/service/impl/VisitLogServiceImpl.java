package com.ehzyil.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehzyil.domain.VisitLog;
import com.ehzyil.mapper.VisitLogMapper;
import com.ehzyil.service.IVisitLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
@Service
public class VisitLogServiceImpl extends ServiceImpl<VisitLogMapper, VisitLog> implements IVisitLogService {

    @Override
    public void saveVisitLog(VisitLog visitLog) {
        // 保存访问日志
        getBaseMapper().insert(visitLog);
    }
}
