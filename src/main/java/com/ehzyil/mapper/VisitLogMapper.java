package com.ehzyil.mapper;

import cn.hutool.core.date.DateTime;
import com.ehzyil.domain.VisitLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ehzyil.model.vo.admin.UserViewVO;
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
public interface VisitLogMapper extends BaseMapper<VisitLog> {
    /**
     * 获取7天用户访问结果
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 用户访问结果
     */
    List<UserViewVO> selectUserViewList(@Param("startTime") DateTime startTime, @Param("endTime") DateTime endTime);

}
