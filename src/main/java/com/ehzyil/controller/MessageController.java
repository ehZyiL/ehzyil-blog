package com.ehzyil.controller;


import com.ehzyil.model.dto.MessageDTO;
import com.ehzyil.model.vo.MessageVO;
import com.ehzyil.model.vo.Result;
import com.ehzyil.service.IMessageService;
import com.ehzyil.strategy.context.LikeStrategyContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
@RestController
@Api(tags = "留言模块")
public class MessageController {
    @Autowired
    private IMessageService messageService;



    @GetMapping("/message/list")
    @ApiOperation(value = "查询留言列表")
    public Result<List<MessageVO>> listTalkHome() {
        return Result.success(messageService.listTalkHome());
    }

    @PostMapping("/message/add")
    @ApiOperation(value = "添加留言")
    public Result<?> addMessage(@Validated @RequestBody MessageDTO message) {
        messageService.addMessage(message);
        return Result.success();
    }

}
