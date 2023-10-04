package com.ehzyil.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehzyil.domain.VisitLog;
import com.ehzyil.mapper.VisitLogMapper;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.front.PageResult;
import com.ehzyil.service.IVisitLogService;
import com.ehzyil.utils.PageUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
public class VisitLogServiceImpl extends ServiceImpl<VisitLogMapper, VisitLog> implements IVisitLogService {

    @Override
    public PageResult<VisitLog> listVisitLog(ConditionDTO condition) {

        // 查询访问日志数量
        Long count = getBaseMapper().selectCount(new LambdaQueryWrapper<VisitLog>()
                .like(StringUtils.hasText(condition.getKeyword()), VisitLog::getPage, condition.getKeyword())
        );
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询访问日志列表
        List<VisitLog> visitLogVOList = getBaseMapper().selectVisitLogList(PageUtils.getLimit(),
                PageUtils.getSize(), condition.getKeyword());
        return new PageResult<>(visitLogVOList, count);

    }

    @Override
    public void saveVisitLog(VisitLog visitLog) {
        // 保存访问日志
        getBaseMapper().insert(visitLog);
    }
}
