package com.ehzyil.controller;


import com.ehzyil.model.vo.PageResult;
import com.ehzyil.model.vo.Result;
import com.ehzyil.model.vo.TalkVO;
import com.ehzyil.service.ITalkService;
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
        return Result.success(talkService.listTalkVO(current,size));
    }

    @ApiOperation(value = "根据id查看说说")
    @GetMapping("/talk/{talkId}")
    public Result<TalkVO> getTalkById(@PathVariable("talkId") Long talkId) {
        return Result.success(talkService.getTalkById(talkId));
    }


}
