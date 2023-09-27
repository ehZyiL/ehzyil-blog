package com.ehzyil.controller;


import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.*;
import com.ehzyil.service.ICommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
@Api(tags = "评论模块")
@RestController
public class CommentController {

    @Autowired
    private ICommentService commentService;
    /**
     * 查看最新评论
     *
     * @return {@link List <RecentCommentVO>}
     */
    @ApiOperation(value = "查看最新评论")
    @GetMapping("/recent/comment")
    public Result<List<RecentCommentVO>> listRecentCommentVO() {
        return Result.success(commentService.listRecentCommentVO());
    }

    @ApiOperation(value = "查看评论")
    @GetMapping("/comment/list")
    public Result<PageResult<CommentVO>> listCommentVO(ConditionDTO conditionDTO) {
        return Result.success(commentService.listCommentVO(conditionDTO));
    }
}
