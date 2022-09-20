package com.example.SpringCruiseApplication.config;

import com.example.SpringCruiseApplication.filter.UpdateUserFilter;
import com.example.SpringCruiseApplication.mail.Sender;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
//@ComponentScan("com.example.SpringCruiseApplication")
@EnableScheduling
public class MyBeanConfig {
    @Value("spring.datasource.url")
    private String url;
    @Value("db.driver")
    private String driver;

    @Autowired
    private DataSource dataSource;
    @Value("spring.datasource.username")
    private String username;
    @Value("spring.datasource.password")
    private String password;

    @Bean
    public Sender sender() {
        return new Sender();
    }

//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
//        LocalContainerEntityManagerFactoryBean em  = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(dataSource);
//        em.setJpaProperties(getHibernateProperties());
//        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//        em.setPackagesToScan("com.example.SpringCruiseApplication");
//        return em;
//
//    }
    public Properties getHibernateProperties(){
        Properties properties = new Properties();
        InputStream is =getClass().getClassLoader().getResourceAsStream("hibernate.properties");
        try{
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("cant find hibernate.properties",e);
        }
        return properties;
    }

//    @Bean
//    public DataSource dataSource(){
//        BasicDataSource dataSource = new BasicDataSource();
//        dataSource.setUrl(url);
//        dataSource.setDriverClassName(driver);
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//        return dataSource;
//
//    }
}
