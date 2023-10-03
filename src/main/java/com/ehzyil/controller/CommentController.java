package com.ehzyil.controller;


import com.ehzyil.enums.LikeTypeEnum;
import com.ehzyil.model.dto.CommentDTO;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.front.*;
import com.ehzyil.service.ICommentService;
import com.ehzyil.strategy.context.LikeStrategyContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private LikeStrategyContext likeStrategyContext;

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
    @ApiOperation(value = "查看回复评论")
    @GetMapping("/comment/{commentId}/reply")
    public Result<List<ReplyVO>> listReply(@PathVariable("commentId") Integer commentId) {
        return Result.success(commentService.listReply(commentId));
    }


    @ApiOperation(value = "评论")
    @PostMapping("/comment/add")
    public Result<?> addComment(@RequestBody CommentDTO commentDTO) {
        commentService.addComment(commentDTO);
        return Result.success();
    }

    @ApiOperation(value = "点赞评论")
    @PostMapping("/comment/{commentId}/like")
    public Result<?> likeArticle(@PathVariable("commentId") Integer commentId) {
        likeStrategyContext.executeLikeStrategy(LikeTypeEnum.COMMENT, commentId);
        return Result.success();
    }
}
