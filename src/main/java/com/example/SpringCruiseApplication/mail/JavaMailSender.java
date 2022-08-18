package com.example.SpringCruiseApplication.mail;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

public class JavaMailSender extends JavaMailSenderImpl {
    public JavaMailSender(String host, int port, String username,
                          String password, String protocol,
                          String auth, String enable,
                          String debug) {
        super();
        this.setHost(host);
        this.setPort(port);
        this.setUsername(username);
        this.setPassword(password);
        Properties properties = this.getJavaMailProperties();
        properties.put("mail.transport.protocol", protocol);
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.starttls.enable", enable);
        properties.put("mail.debug", debug);
    }
}
