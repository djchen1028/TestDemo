package com.bc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileConfig implements WebMvcConfigurer {
//    private String filePath = "E:/Java_Project/myCrawler_CsvFiles/";
    private String filePath = "/Users/dj_chen/IdeaProjects/WebFiles/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("config....");
        registry.addResourceHandler("/files/**").addResourceLocations( "file:"+filePath);
    }
}
