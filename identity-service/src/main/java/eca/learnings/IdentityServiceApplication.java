package eca.learnings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.HashMap;

@SpringBootApplication
@EnableDiscoveryClient
public class IdentityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdentityServiceApplication.class, args);
	}
}
