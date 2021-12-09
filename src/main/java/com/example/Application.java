package com.example;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author dj
 */
@SpringBootApplication
@ServletComponentScan
@EnableMethodCache(basePackages = "com.example")
@EnableCreateCacheAnnotation
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
