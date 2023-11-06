package com.ehzyil.utils;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.ehzyil.model.dto.MailDTO;
//TODO 待修复
public class AlarmUtil extends AppenderBase<ILoggingEvent> {
    private static final long INTERVAL = 10 * 1000 * 60;
    private long lastAlarmTime = 0;

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        if (canAlarm()) {
            System.out.println("hhhhhhhhhhhhhh");
            EmailUtil.sendMail(
                    MailDTO.builder().
                            toEmail(SpringUtils.getConfig("spring.mail.username", "495028518@qq.com")).
                            subject(iLoggingEvent.getLoggerName()).
                            content(iLoggingEvent.getFormattedMessage()).
                            build());
        }
    }

    private boolean canAlarm() {
        // 做一个简单的频率过滤,一分钟内只允许发送一条报警
        long now = System.currentTimeMillis();
        if (now - lastAlarmTime >= INTERVAL) {
            lastAlarmTime = now;
            return true;
        } else {
            return false;
        }
    }
}
