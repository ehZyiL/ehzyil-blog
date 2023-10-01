package com.ehzyil.config;

import com.ehzyil.config.properties.QiniuProperties;
import com.ehzyil.config.properties.ThreadPoolProperties;
import com.ehzyil.utils.SpringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.Target;
import java.util.concurrent.ScheduledExecutorService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ehyzil
 * @Description
 * @create 2023-10-2023/10/1-22:27
 */
@SpringBootTest
class ThreadPoolConfigTest {
    @Autowired
    ThreadPoolProperties threadPoolProperties;

    @Autowired
    QiniuProperties qiniuProperties;

    @Test
    void test(){
        System.out.println(qiniuProperties.toString());
        ScheduledExecutorService executor = SpringUtils.getBean("scheduledExecutorService");
        System.out.println(executor);
    }
}