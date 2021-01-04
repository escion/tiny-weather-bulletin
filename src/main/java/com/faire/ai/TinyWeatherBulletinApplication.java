package com.faire.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class TinyWeatherBulletinApplication {

    public static void main(String[] args){
        SpringApplication.run(TinyWeatherBulletinApplication.class, args);
    }
}
