package com.ehzyil;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ehyzil
 * @Description
 * @create 2023-09-2023/9/25-19:48
 */
@SpringBootApplication
//@MapperScan("com.ehzyil.mapper")
public class BlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class);
    }
}
