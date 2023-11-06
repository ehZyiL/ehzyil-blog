
package com.ehzyil.filter;

import cn.hutool.core.date.StopWatch;
import com.ehzyil.context.ReqInfoContext;
import com.ehzyil.utils.CrossUtil;
import com.ehzyil.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

/**
 * @author ehyzil
 * @Description
 * @create 2023-11-2023/11/5-19:16
 */
@Slf4j
@WebFilter(urlPatterns = "/*", filterName = "reqRecordFilter", asyncSupported = true)
public class ReqRecordFilter implements Filter {

    private static Logger REQ_LOG = LoggerFactory.getLogger("req");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        HttpServletRequest request = null;
        StopWatch stopWatch = new StopWatch("请求耗时");
        try {
            stopWatch.start("请求参数构建");
            request = this.initReqInfo((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
            stopWatch.stop();
            stopWatch.start("cors");
            CrossUtil.buildCors(request, (HttpServletResponse) servletResponse);
            stopWatch.stop();
            stopWatch.start("业务执行");
            filterChain.doFilter(request, servletResponse);
            stopWatch.stop();
        } finally {
            stopWatch.start("输出请求日志");
            ReqInfoContext.ReqInfo reqInfo = ReqInfoContext.getReqInfo();
            buildRequestLog(ReqInfoContext.getReqInfo(), request, System.currentTimeMillis() - start);
            ReqInfoContext.clear();
            stopWatch.stop();

            log.info("{} - cost:\n{}", request.getRequestURI(), stopWatch.prettyPrint(TimeUnit.MILLISECONDS));

        }

    }


    /**
     * 构建请求日志
     *
     * @param req
     * @param request
     * @param costTime
     */
    private void buildRequestLog(ReqInfoContext.ReqInfo req, HttpServletRequest request, long costTime) {
        if (req == null || isStaticURI(request)) {
            return;
        }

        StringBuilder msg = new StringBuilder();
        msg.append("method=").append(request.getMethod()).append("; ");
        if (StringUtils.isNotBlank(req.getReferer())) {
            msg.append("referer=").append(URLDecoder.decode(req.getReferer())).append("; ");
        }
        msg.append("remoteIp=").append(req.getClientIp());
        msg.append("; agent=").append(req.getUserAgent());

        if (req.getUserId() != null) {
            // 打印用户信息
            msg.append("; user=").append(req.getUserId());
        }

        msg.append("; uri=").append(request.getRequestURI());
        if (StringUtils.isNotBlank(request.getQueryString())) {
            msg.append('?').append(URLDecoder.decode(request.getQueryString()));
        }

        msg.append("; payload=").append(req.getPayload());
        msg.append("; cost=").append(costTime);
        REQ_LOG.info("{}", msg);
    }

    private HttpServletRequest initReqInfo(HttpServletRequest request, HttpServletResponse response) {
        if (isStaticURI(request)) {
            // 静态资源直接放行
            return request;
        }
        StopWatch stopWatch = new StopWatch("请求参数构建");
        try {

            stopWatch.start("请求基本信息");
            ReqInfoContext.ReqInfo reqInfo = new ReqInfoContext.ReqInfo();
            reqInfo.setHost(request.getHeader("host"));
            reqInfo.setPath(request.getPathInfo());
            if (reqInfo.getPath() == null) {
                String url = request.getRequestURI();
                int index = url.indexOf("?");
                if (index > 0) {
                    url = url.substring(0, index);
                }
                reqInfo.setPath(url);
            }
            reqInfo.setReferer(request.getHeader("referer"));
            reqInfo.setClientIp(IpUtils.getIpAddress(request));
            reqInfo.setUserAgent(request.getHeader("User-Agent"));
            reqInfo.setDeviceId(getOrInitDeviceId(request, response));

            ReqInfoContext.addReqInfo(reqInfo);

            request = this.wrapperRequest(request, reqInfo);
            stopWatch.stop();
        } catch (Exception e) {
            log.error("init reqInfo error!", e);
        } finally {
            log.info("{} -> 请求构建耗时: \n{}", request.getRequestURI(), stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        }

        return request;

    }

    /**
     * 判断是否是静态资源
     *
     * @param request
     * @return
     */

    private boolean isStaticURI(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return request == null
                || request.getRequestURI().endsWith("css")
                || request.getRequestURI().endsWith("js")
                || request.getRequestURI().endsWith("png")
                || request.getRequestURI().endsWith("ico")
                || request.getRequestURI().endsWith("svg")
                || request.getRequestURI().endsWith("min.js.map")
                || request.getRequestURI().endsWith("min.css.map");
    }

    private HttpServletRequest wrapperRequest(HttpServletRequest request, ReqInfoContext.ReqInfo reqInfo) {
        String method = request.getMethod();//
        if (!HttpMethod.POST.name().equalsIgnoreCase(request.getMethod())&&!HttpMethod.PUT.name().equalsIgnoreCase(request.getMethod())) {
            return request;
        }

        BodyReaderHttpServletRequestWrapper requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
        reqInfo.setPayload(requestWrapper.getBodyString());
        return requestWrapper;
    }


    /**
     * 初始化设备id
     *
     * @return
     */
    private String getOrInitDeviceId(HttpServletRequest request, HttpServletResponse response) {
//        String deviceId = request.getParameter("deviceId");
//        if (StringUtils.isNotBlank(deviceId) && !"null".equalsIgnoreCase(deviceId)) {
//            return deviceId;
//        }
//
//        Cookie device = SessionUtil.findCookieByName(request, LoginOutService.USER_DEVICE_KEY);
//        if (device == null) {
//            deviceId = UUID.randomUUID().toString();
//            if (response != null) {
//                response.addCookie(SessionUtil.newCookie(LoginOutService.USER_DEVICE_KEY, deviceId));
//            }
//            return deviceId;
//        }
//        return device.getValue();
        return null;
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}

