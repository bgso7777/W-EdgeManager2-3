package com.inswave.appplatform;

import com.inswave.appplatform.core.AppplatformSpringApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class DeployerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(AppplatformSpringApplication.class, args);
    }

    @Value("${wdeployer.repo.path}")
    private String deployerRepoPath;

    @PostConstruct
    public void postConstruct() {
        // jvm option으로 설정된 값을 우선함
        String path = System.getProperty("wdeployer.repo.path");
        if(path != null) {
            deployerRepoPath = path;
        }
        Config config = Config.getInstance();
        config.setDeployerRepoPath(deployerRepoPath);
    }
}
