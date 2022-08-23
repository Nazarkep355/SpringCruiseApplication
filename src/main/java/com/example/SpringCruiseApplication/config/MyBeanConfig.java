package com.example.SpringCruiseApplication.config;

import com.example.SpringCruiseApplication.filter.UpdateUserFilter;
import com.example.SpringCruiseApplication.mail.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

@Configuration
@ComponentScan("com.example.SpringCruiseApplication")
@EnableScheduling
public class MyBeanConfig {
    @Bean
    public Sender sender() {
        return new Sender();
    }


}
