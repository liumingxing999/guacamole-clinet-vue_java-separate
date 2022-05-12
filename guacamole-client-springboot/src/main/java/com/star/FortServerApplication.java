package com.star;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class FortServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FortServerApplication.class, args);
    }

}
