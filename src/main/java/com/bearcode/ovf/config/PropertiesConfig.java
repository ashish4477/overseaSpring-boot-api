package com.bearcode.ovf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Created by leonid on 14.11.14.
 */
@Configuration
@PropertySource( "classpath:ovf.properties" )
public class PropertiesConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setIgnoreUnresolvablePlaceholders( true );
        configurer.setIgnoreResourceNotFound( true );
        return configurer;
    }

}
