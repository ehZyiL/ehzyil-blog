package com.ehzyil.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ehzyil.domain.ExceptionLog;
import com.ehzyil.mapper.ExceptionLogMapper;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.front.PageResult;
import com.ehzyil.service.IExceptionLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehzyil.utils.PageUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
public class ExceptionLogServiceImpl extends ServiceImpl<ExceptionLogMapper, ExceptionLog> implements IExceptionLogService {

    @Override
    public void recordException(ExceptionLog exceptionLog) {
        getBaseMapper().insert(exceptionLog);
    }

    @Override
    public PageResult<ExceptionLog> listExceptionLog(ConditionDTO condition) {
        // 查询异常日志数量
        Long count = getBaseMapper().selectCount(new LambdaQueryWrapper<ExceptionLog>()
                .like(StringUtils.hasText(condition.getOptModule()), ExceptionLog::getModule, condition.getOptModule())
                .or()
                .like(StringUtils.hasText(condition.getKeyword()), ExceptionLog::getDescription, condition.getKeyword())
        );
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询异常日志列表
        List<ExceptionLog> operationLogVOList = getBaseMapper().selectExceptionLogList(PageUtils.getLimit(),
                PageUtils.getSize(), condition);
        return new PageResult<>(operationLogVOList, count);
    }
}
