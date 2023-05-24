package com.example.eback;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling //开启定时任务
//@NacosPropertySource(dataId = "stock", autoRefreshed = true)
public class EBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(EBackApplication.class, args);
    }

}
