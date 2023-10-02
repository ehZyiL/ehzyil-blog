package com.ehzyil.controller;


import com.ehzyil.annotation.VisitLogger;
import com.ehzyil.model.vo.AlbumVO;
import com.ehzyil.model.vo.Result;
import com.ehzyil.service.IAlbumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
@Api(tags = "相册模块")
@RestController
public class AlbumController {

    @Autowired
    private IAlbumService albumService;

    /**
     * 查看相册列表
     *
     * @return {@link Result<AlbumVO> 相册列表
     */
    @VisitLogger(value = "相册")
    @ApiOperation(value = "查看相册列表")
    @GetMapping("/album/list")
    public Result<List<AlbumVO>> listAlbumVO() {
        return Result.success(albumService.listAlbumVO());
    }

}
