package io.lucci.potlatch.server.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan({ "io.lucci.potlatch.server.service", "io.lucci.potlatch.server.web.model.adapter" })
public class ServiceConfig {
	
	@Configuration
    @Profile("production")
    @PropertySource("file:${CONFDIR}/potlatch/config/potlatch-service.properties")
    static class DbProductionProperties
    { }
    
    @Configuration
    @Profile("test")
    @PropertySource({"file:src/test/resources/service.properties"})
    static class DbTestProperties
    { }
	
	@Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
	
}