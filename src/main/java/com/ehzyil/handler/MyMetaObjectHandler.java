package com.ehzyil.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.ehzyil.enums.ZoneEnum.SHANGHAI;

/**
 * @author ehyzil
 * @Description mybatis plus自动填充
 * @create 2023-09-2023/9/26-20:39
 */
@Log4j2
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
//        log.info("start insert fill ....");
        this.strictInsertFill(metaObject,
                "createTime",
                LocalDateTime.class,
                LocalDateTime.now(ZoneId.of(SHANGHAI.getZone()))
        );
    }

    @Override
    public void updateFill(MetaObject metaObject) {
//        log.info("start update fill ....");
        this.strictUpdateFill(metaObject,
                "updateTime",
                LocalDateTime.class,
                LocalDateTime.now(ZoneId.of(SHANGHAI.getZone()))
        );
    }
}
