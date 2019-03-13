package com.wf.ew.plugin.scheduler;

import java.io.IOException;
import java.util.Properties;

import com.wf.ew.EasyWebApplication;
import org.quartz.Scheduler;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 持久化Quartz
 */
//@Configuration
public class SchedulerConfig {

    @Bean(name="SchedulerFactory")
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setQuartzProperties(quartzProperties());
        return factory;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        //在quartz.properties中的属性被读取并注入后再初始化对象
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        Properties quartzProperties = propertiesFactoryBean.getObject();

        //读取application.properties配置
        ConfigurableApplicationContext context = SpringApplication.run(EasyWebApplication.class);
        ConfigurableEnvironment environment = context.getEnvironment();

        quartzProperties.setProperty("org.quartz.dataSource.qzDS.driver", environment.getProperty("spring.datasource.driver-class-name"));
        quartzProperties.setProperty("org.quartz.dataSource.qzDS.URL", environment.getProperty("spring.datasource.url"));
        quartzProperties.setProperty("org.quartz.dataSource.qzDS.user", environment.getProperty("spring.datasource.username"));
        quartzProperties.setProperty("org.quartz.dataSource.qzDS.password", environment.getProperty("spring.datasource.password"));
        quartzProperties.setProperty("org.quartz.dataSource.qzDS.maxConnection", "10");
        return quartzProperties;
    }

    /*
     * quartz初始化监听器
     */
    @Bean
    public QuartzInitializerListener executorListener() {
        return new QuartzInitializerListener();
    }

    /*
     * 通过SchedulerFactoryBean获取Scheduler的实例
     */
    @Bean(name="Scheduler")
    public Scheduler scheduler() throws IOException {
        return schedulerFactoryBean().getScheduler();
    }

}
