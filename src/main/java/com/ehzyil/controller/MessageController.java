package com.ehzyil.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ehzyil.annotation.OptLogger;
import com.ehzyil.model.dto.CheckDTO;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.dto.MessageDTO;
import com.ehzyil.model.vo.admin.MessageBackVO;
import com.ehzyil.model.vo.front.MessageVO;
import com.ehzyil.model.vo.front.PageResult;
import com.ehzyil.model.vo.front.Result;
import com.ehzyil.service.IMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ehzyil.enums.OptTypeConstant.DELETE;
import static com.ehzyil.enums.OptTypeConstant.UPDATE;

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

    /**
     * 查看后台留言列表
     *
     * @param condition 条件
     * @return {@link Result< MessageBackVO >} 留言列表
     */
    @ApiOperation(value = "查看后台留言列表")
    @SaCheckPermission("news:message:list")
    @GetMapping("/admin/message/list")
    public Result<PageResult<MessageBackVO>> listMessageBackVO(ConditionDTO condition) {
        return Result.success(messageService.listMessageBackVO(condition));
    }

    /**
     * 删除留言
     *
     * @param messageIdList 留言Id列表
     * @return {@link Result<>}
     */
    @OptLogger(value = DELETE)
    @ApiOperation(value = "删除留言")
    @SaCheckPermission("news:message:delete")
    @DeleteMapping("/admin/message/delete")
    public Result<?> deleteMessage(@RequestBody List<Integer> messageIdList) {
        messageService.removeByIds(messageIdList);
        return Result.success();
    }

    /**
     * 审核留言
     *
     * @param check 审核信息
     * @return {@link Result<>}
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "审核留言")
    @SaCheckPermission("news:message:pass")
    @PutMapping("/admin/message/pass")
    public Result<?> updateMessageCheck(@Validated @RequestBody CheckDTO check) {
        messageService.updateMessageCheck(check);
        return Result.success();
    }
}
