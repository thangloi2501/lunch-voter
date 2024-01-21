package com.gtech.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
@ComponentScan("com.gtech")
public class AppConfig {

  @Bean
  public ResourceBundleMessageSource messageSource() {
    ResourceBundleMessageSource source = new ResourceBundleMessageSource();
    source.setBasenames("messages/gtech");
    source.setUseCodeAsDefaultMessage(true);

    return source;
  }
}
