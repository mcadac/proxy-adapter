package com.payu.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.*;
import org.springframework.context.*;
import org.springframework.scheduling.annotation.*;

import com.payu.client.*;


@SpringBootApplication(scanBasePackages = "com.payu.*")
@EnableFeignClients
@EnableScheduling
public class ProxyAdapterPayuApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context =
                SpringApplication.run(ProxyAdapterPayuApplication.class, args);

        Thread.sleep(60000);
        context.getBean(ProxyService.class).getLeader();
    }

}
