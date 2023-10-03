package com.ehzyil.service;

import com.ehzyil.model.vo.admin.BlogBackInfoVO;
import com.ehzyil.model.vo.front.BlogInfoVO;

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

    /**
     * 查看后台信息
     * @return
     */
    BlogBackInfoVO getBlogBackInfo();
}
