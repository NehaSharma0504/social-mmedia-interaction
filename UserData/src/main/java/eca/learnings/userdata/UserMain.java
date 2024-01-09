package eca.learnings.userdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class UserMain {
    public static void main(String[] args) {
        SpringApplication.run(UserMain.class, args);
    }
}