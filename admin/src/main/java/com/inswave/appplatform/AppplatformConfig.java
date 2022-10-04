package com.inswave.appplatform;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppplatformConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(1);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        Map<String, MediaType> mediaTypeMap = new HashMap<>();
        mediaTypeMap.put("json", MediaType.APPLICATION_JSON);
        configurer.mediaTypes(mediaTypeMap);
    }

    @Bean
    public ContentNegotiatingViewResolver contentNegotiatingViewResolver(ContentNegotiationManager contentNegotiationManager) {
        ContentNegotiatingViewResolver viewResolver = new ContentNegotiatingViewResolver();
        viewResolver.setContentNegotiationManager(contentNegotiationManager);
        return viewResolver;
    }

    @Bean
    public ServletRegistrationBean getServletRegistrationBean() throws IOException {
        ServletRegistrationBean websquareDispatcher = new ServletRegistrationBean(new websquare.http.DefaultRequestDispatcher());

        System.setProperty("WEBSQUARE_HOME",getWebSquareHomePath());
        websquareDispatcher.addUrlMappings("*.wq");
        return websquareDispatcher;
    }

    public String getWebSquareHomePath() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("websquare_home");
        String websquareHomePath =  classPathResource.getURL().getPath();

        return websquareHomePath;
    }


    //2020.09.07 common fileupload 이용한 CommonsMultipartResolver 빈설정
    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setDefaultEncoding("UTF-8");
        //commonsMultipartResolver.setMaxUploadSizePerFile(5*1024*1024);
        return commonsMultipartResolver;
    }
}
