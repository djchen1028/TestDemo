package com.bc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ResourceBundle;

@Configuration
public class FileConfig implements WebMvcConfigurer {
//    private String filePath = "/Users/dj_chen/IdeaProjects/WebFiles/";
//    private String filePath = "F:/Documents/IdeaProjects/WebFiles/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("config....");
        ResourceBundle resource = ResourceBundle.getBundle("DBproperties");
        String FILEPATH = resource.getString("FILEPATH");
        registry.addResourceHandler("/files/**").addResourceLocations( "file:"+FILEPATH);
    }
}
