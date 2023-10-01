package com.ehzyil.aspect;

import com.ehzyil.annotation.VisitLogger;
import com.ehzyil.domain.VisitLog;
import com.ehzyil.manager.AsyncManager;
import com.ehzyil.manager.factory.AsyncFactory;
import com.ehzyil.utils.IpUtils;
import com.ehzyil.utils.UserAgentUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * @author ehyzil
 * @Description AOP记录访问日志
 * @create 2023-10-2023/10/1-21:24
 */


@Aspect
@Component
public class VisitLogAspect {
    /**
     * 定义切点，用于匹配被@VisitLogger注解标记的方法。
     */
    @Pointcut("@annotation(com.ehzyil.annotation.VisitLogger)")
    public void visitLogPointCut() {
    }

    //定义了一个连接点正常返回通知，即在被@VisitLogger注解标记的方法执行完成并返回结果后执行。
    @AfterReturning(value = "visitLogPointCut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // 获取操作
        VisitLogger visitLogger = method.getAnnotation(VisitLogger.class);
        // 获取request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        VisitLog visitLog = new VisitLog();
        String ipAddress = IpUtils.getIpAddress(request);
        String ipSource = IpUtils.getIpSource(ipAddress);
        // 解析browser和os
        Map<String, String> userAgentMap = UserAgentUtils.parseOsAndBrowser(request.getHeader("User-Agent"));
        visitLog.setIpAddress(ipAddress);
        visitLog.setIpSource(ipSource);
        visitLog.setOs(userAgentMap.get("os"));
        visitLog.setBrowser(userAgentMap.get("browser"));
        visitLog.setPage(visitLogger.value());

        // 保存到数据库
        AsyncManager.getInstance().execute(AsyncFactory.recordVisit(visitLog));
    }

}
