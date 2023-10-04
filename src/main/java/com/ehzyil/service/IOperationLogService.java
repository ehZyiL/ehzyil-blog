package com.ehzyil.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.domain.OperationLog;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.admin.OperationLogVO;
import com.ehzyil.model.vo.front.PageResult;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface IOperationLogService extends IService<OperationLog> {
    /**
     * 查看操作日志列表
     *
     * @param condition 条件
     * @return 日志列表
     */
    PageResult<OperationLogVO> listOperationLogVO(ConditionDTO condition);

    /**
     * 保存操作日志
     *
     * @param operationLog 操作日志信息
     */
    void recordOperation(OperationLog operationLog);
}
