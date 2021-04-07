package cn.syned.p2p;

import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling//开启定时器
@EnableDubboConfig
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
