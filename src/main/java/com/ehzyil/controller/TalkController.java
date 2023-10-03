package com.ehzyil.controller;


import com.ehzyil.enums.LikeTypeEnum;
import com.ehzyil.model.vo.front.PageResult;
import com.ehzyil.model.vo.front.Result;
import com.ehzyil.model.vo.front.TalkVO;
import com.ehzyil.service.ITalkService;
import com.ehzyil.strategy.context.LikeStrategyContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
@Api(tags = "说说模块")
@RestController
public class TalkController {

    @Autowired
    private ITalkService talkService;

    @Autowired
    private LikeStrategyContext likeStrategyContext;

    /**
     * 查看首页说说
     *
     * @return {@link Result<String>}
     */
    @ApiOperation(value = "查看首页说说")
    @GetMapping("/home/talk")
    public Result<List<String>> listTalkHome() {
        return Result.success(talkService.listTalkHome());
    }

    /**
     * 查看说说列表
     *
     * @return {@link Result<TalkVO>}
     */
    @ApiOperation(value = "查看说说列表")
    @GetMapping("/talk/list")
    public Result<PageResult<TalkVO>> listTalkVO(@RequestParam("current") Long current, @RequestParam("size") Long size) {
        return Result.success(talkService.listTalkVO(current, size));
    }

    @ApiOperation(value = "根据id查看说说")
    @GetMapping("/talk/{talkId}")
    public Result<TalkVO> getTalkById(@PathVariable("talkId") Long talkId) {
        return Result.success(talkService.getTalkById(talkId));
    }

    @ApiOperation(value = "点赞说说")
    @PostMapping("/talk/{talkId}/like")
    public Result<?> likeArticle(@PathVariable("talkId") Integer talkId) {
        likeStrategyContext.executeLikeStrategy(LikeTypeEnum.TALK, talkId);
        return Result.success();
    }

}
