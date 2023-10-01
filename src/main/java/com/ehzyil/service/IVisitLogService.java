package com.ehzyil.service;

import com.ehzyil.domain.VisitLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface IVisitLogService extends IService<VisitLog> {
    /**
     * 保存访问日志
     *
     * @param visitLog 访问日志信息
     */
    void saveVisitLog(VisitLog visitLog);
}
