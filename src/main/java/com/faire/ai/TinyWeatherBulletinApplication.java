package com.faire.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class TinyWeatherBulletinApplication {

    public static void main(String[] args){
        SpringApplication.run(TinyWeatherBulletinApplication.class, args);
    }
}
