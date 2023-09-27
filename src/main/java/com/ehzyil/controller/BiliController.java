package com.ehzyil.controller;

import com.ehzyil.model.vo.Result;
import com.ehzyil.service.BiliService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ehyzil
 * @Description
 * @create 2023-09-2023/9/27-9:38
 */
@RestController
@Api(tags = "b站服务模块")
public class BiliController {
    @Autowired
    private BiliService biliService;

    /**
     * B站图片上传
     *
     * @param file 文件
     * @return {@link Result <String>} 图片地址
     */
    @ApiOperation(value = "B站图片上传")
    @PostMapping("/bili/upload")
    public Result<String> biliUpload(@RequestParam("file_up") MultipartFile file,
                                     @RequestParam(value = "csrf") String csrf,
                                     @RequestParam(value = "data") String data) {
        return Result.success(biliService.uploadBiliPicture(file, csrf, data));
    }

}
