package com.ehzyil.mapper;

import com.ehzyil.domain.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.admin.TaskBackVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface TaskMapper extends BaseMapper<Task> {
    /**
     * 查询定时任务数量
     *
     * @param condition 条件
     * @return 数量
     */
    Long countTaskBackVO(@Param("condition") ConditionDTO condition);

    /**
     * 查询定时任务列表
     *
     * @param limit     页码
     * @param size      大小
     * @param condition 条件
     * @return 定时任务列表
     */
    List<TaskBackVO> selectTaskBackVO(@Param("limit") Long limit, @Param("size") Long size, @Param("condition") ConditionDTO condition);

}
