package com.inswave.appplatform.log;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ElasticsearchSearchProxyServletConfiguration implements EnvironmentAware {

  @Value("${proxy.elasticsearch.servlet_url}")
  private String servletUrl;
  @Value("${proxy.elasticsearch.target_url}")
  private String targetUrl;
  @Bean
  public ServletRegistrationBean servletRegistrationBean(){
    ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new ProxyServlet(), servletUrl);
    servletRegistrationBean.addInitParameter("targetUri", targetUrl);
    servletRegistrationBean.addInitParameter("log", "false");
    return servletRegistrationBean;
  }

  @Override
  public void setEnvironment(Environment environment) {
  }
}