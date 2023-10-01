package com.ehzyil.service.impl;

import com.ehzyil.domain.OperationLog;
import com.ehzyil.mapper.OperationLogMapper;
import com.ehzyil.service.IOperationLogService;
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
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements IOperationLogService {

    @Override
    public void recordOperation(OperationLog operationLog) {
        getBaseMapper().insert(operationLog);
    }
}
