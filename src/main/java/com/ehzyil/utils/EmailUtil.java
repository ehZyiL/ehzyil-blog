package com.ehzyil.utils;


import cn.hutool.extra.spring.SpringUtil;
import com.ehzyil.model.dto.MailDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author YiHui
 * @date 2023/3/19
 */
@Slf4j
public class EmailUtil {
    private static volatile String from;

    public static String getFrom() {
        if (from == null) {
            synchronized (EmailUtil.class) {
                if (from == null) {
                    from = SpringUtils.getConfig("spring.mail.username", "495028518@qq.com");
                }
            }
        }
        return from;
    }




    public static void sendMail(MailDTO mailDTO) {
        try {
            JavaMailSender javaMailSender = SpringUtils.getBean(JavaMailSender.class);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            // 创建一个MimeMessageHelper对象，用于设置邮件的相关属性
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            // 创建一个Context对象，并将邮件内容的Map设置到Context中
            Context context = new Context();
            context.setVariables(mailDTO.getContentMap());
            // 设置发件人
            mimeMessageHelper.setFrom(getFrom());
            // 设置收件人
            mimeMessageHelper.setTo(mailDTO.getToEmail());
            // 设置邮件主题
            mimeMessageHelper.setSubject(mailDTO.getSubject());

            // 设置邮件内容为HTML格式，并将处理后的结果设置为邮件内容
            if (StringUtils.isNotBlank(mailDTO.getTemplate())) {
                // 使用模板引擎处理邮件模板，并得到处理后的结果
                TemplateEngine templateEngine = SpringUtils.getBean(TemplateEngine.class);
                String process = templateEngine.process(mailDTO.getTemplate(), context);
                mimeMessageHelper.setText(process, true);
            }else {
                mimeMessageHelper.setText(mailDTO.getContent(), true);
            }

            // 发送邮件
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


}
