package com.chao.domain;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@MapperScan({"com.chao.domain.dao"})
@EnableDiscoveryClient
public class ProjectDomainApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectDomainApplication.class,args);
        System.out.println("domain启动成功！！！");
    }
}
