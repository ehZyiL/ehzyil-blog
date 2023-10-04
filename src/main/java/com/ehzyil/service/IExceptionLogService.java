package com.ehzyil.service;

import com.ehzyil.domain.ExceptionLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.front.PageResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface IExceptionLogService extends IService<ExceptionLog> {
    /**
     * 保存异常日志
     *
     * @param exceptionLog 异常日志信息
     */
    void recordException(ExceptionLog exceptionLog);

    /**
     * 查看异常日志列表
     *
     * @param condition 条件
     * @return 日志列表
     */
    PageResult<ExceptionLog> listExceptionLog(ConditionDTO condition);
}
