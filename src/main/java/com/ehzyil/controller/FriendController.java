package com.ehzyil.controller;


import com.ehzyil.model.vo.FriendVO;
import com.ehzyil.model.vo.Result;
import com.ehzyil.service.IFriendService;
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
@RestController
public class FriendController {

    @Autowired
    private IFriendService friendService;

    /**
     * 查看友链列表
     *
     * @return {@link Result < FriendVO >} 友链列表
     */
//    @VisitLogger(value = "友链")
    @ApiOperation(value = "查看友链列表")
    @GetMapping("/friend/list")
    public Result<List<FriendVO>> listFriendVO() {
        return Result.success(friendService.listFriendVO());
    }

}
