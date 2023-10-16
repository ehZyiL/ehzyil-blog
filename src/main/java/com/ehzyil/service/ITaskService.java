package com.ehzyil.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.domain.Task;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.dto.StatusDTO;
import com.ehzyil.model.dto.TaskDTO;
import com.ehzyil.model.dto.TaskRunDTO;
import com.ehzyil.model.vo.PageResult;
import com.ehzyil.model.vo.TaskBackVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface ITaskService extends IService<Task> {

    /**
     * 查看定时任务列表
     *
     * @param condition 条件
     * @return 定时任务列表
     */
    PageResult<TaskBackVO> listTaskBackVO(ConditionDTO condition);

    /**
     * 添加定时任务
     *
     * @param task 定时任务
     */
    void addTask(TaskDTO task);

    /**
     * 修改定时任务
     *
     * @param task 定时任务信息
     */
    void updateTask(TaskDTO task);

    /**
     * 删除定时任务
     *
     * @param taskIdList 定时任务id集合
     */
    void deleteTask(List<Integer> taskIdList);

    /**
     * 修改定时任务状态
     *
     * @param taskStatus 定时任务状态
     */
    void updateTaskStatus(StatusDTO taskStatus);

    /**
     * 定时任务立即执行一次
     *
     * @param taskRun 定时任务
     */
    void runTask(TaskRunDTO taskRun);
}
