package com.ehzyil.service;

import com.ehzyil.model.vo.BlogInfoVO;

/**
 * 博客业务接口
 *
 * @author ican
 **/
public interface BlogInfoService {
    /**
     * 上传访客信息
     */
    void report();

    /**
     * 查看博客信息
     *
     * @return 博客信息
     */
    BlogInfoVO getBlogInfo();

}
