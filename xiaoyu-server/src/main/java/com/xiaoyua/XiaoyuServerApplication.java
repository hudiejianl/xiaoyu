package com.xiaoyua;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class XiaoyuServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(XiaoyuServerApplication.class, args);
	}

}
