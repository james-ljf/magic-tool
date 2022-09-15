package com.magictool.web.service;

import org.apache.axis.encoding.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

/**
 * 邮件发送模板
 *
 * @author lijf
 */
@Component
public class EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    private String base64Logo;

    /**
     * base64转化静态资源
     */
    public void initLogo(String path) {
        try {
            BufferedImage image = ImageIO.read(Objects.requireNonNull(EmailSender.class.getResourceAsStream("resource文件夹下静态资源路径")));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // 转换为gif，占用内存更小
            ImageIO.write(image, "gif", stream);
            base64Logo = path + Base64.encode(stream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送简单邮件的方法
     *
     * @param to      接收邮件方
     * @param title   邮件的标题
     * @param content 邮件的内容
     */
    @Async("asyncPromiseExecutor")
    public void sendSimpleEmail(String to, String title, String content) {
        //简单的邮件信息对象
        SimpleMailMessage message = new SimpleMailMessage();
        //设置发送给谁
        message.setTo(to);
        //设置从哪里发送
        message.setFrom("111111@qq.com");
        //设置邮件的标题
        message.setSubject(title);
        //设置邮件的内容
        message.setText(content);
        //发送邮件
        javaMailSender.send(message);
    }

    /**
     * 发送Html版本的验证码
     *
     * @param to           发送给谁
     * @param content      内容
     * @param title        标题
     * @param templateName 模板
     * @param path         图片路径
     */
    @Async("asyncPromiseExecutor")
    public void sendHtmlEmail(String to, String title, String content, String templateName, String path) {
        initLogo(path);
        //创建一个Thymeleaf的Context对象
        Context context = new Context();
        //设置参数
        context.setVariable("to", to);
        context.setVariable("logo", base64Logo);
        context.setVariable("content", content);
        //生成一个字符串类型的内容（将模板页面和上下文对象绑定）
        String c = templateEngine.process(templateName, context);

        //准备一个邮件信息对象
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            //设置从哪里发送
            helper.setFrom(new InternetAddress("11111@qq.com", "阿里巴巴官方", "UTF-8"));
            //设置发送给谁
            helper.setTo(to);
            //设置标题
            helper.setSubject(title);
            //设置内容
            helper.setText(c, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}