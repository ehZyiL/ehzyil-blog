package com.ehzyil.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置文件
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitmqProperties {

    /**
     * 主机
     */
    private String host;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String passport;

    /**
     * 路径
     */
    private String virtualhost;

    /**
     * 连接池大小
     */
    private Integer poolSize;

    /**
     * 开关 false-关闭，true-打开
     */
    private Boolean switchFlag;
}
