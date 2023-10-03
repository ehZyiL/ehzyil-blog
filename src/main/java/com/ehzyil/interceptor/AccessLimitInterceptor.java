package com.ehzyil.interceptor;

import com.alibaba.fastjson2.JSON;
import com.ehzyil.annotation.AccessLimit;
import com.ehzyil.model.vo.Result;
import com.ehzyil.service.RedisService;
import com.ehzyil.utils.IpUtils;
import com.ehzyil.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisService redisService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean result = true;


        // Handler 是否为 HandlerMethod 实例
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            //方法上没有访问控制的注解，直接通过
            if (accessLimit != null) {
                int seconds = accessLimit.seconds();
                int maxCount = accessLimit.maxCount();

                String ipAddress = IpUtils.getIpAddress(request);
                String method = request.getMethod();
                String requestURI = request.getRequestURI();
                String key = ipAddress + ":" + method + ":" + requestURI;
                try {
                    Long count = redisService.incr(key, 1L);
                    //第一次访问的话 设置过期时间
                    if (Objects.nonNull(count) && count == 1) {
                        redisService.setExpire(key, seconds, TimeUnit.SECONDS);
                    } else if (count > maxCount) {
                        //超过访问次数
                        WebUtils.renderString(response, JSON.toJSONString(Result.fail(accessLimit.msg())));
                        log.warn(key + "请求次数超过每" + seconds + "秒" + maxCount + "次");
                        result = false;
                    }
                } catch (Exception e) {
                    log.error("redis错误: " + e.getMessage());
                    result = false;
                }
            }
        }
        return result;
    }


}