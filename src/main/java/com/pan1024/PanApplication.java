package com.pan1024;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @ClassName: PanApplication
 * @Date: 2019/6/6
 * @describe:
 */
@EnableScheduling
@SpringBootApplication
public class PanApplication {

    public static void main(String[] args){
        SpringApplication.run(PanApplication.class);
    }
}
