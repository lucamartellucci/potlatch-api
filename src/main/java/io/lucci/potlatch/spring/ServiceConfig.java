package io.lucci.potlatch.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "io.lucci.potlatch.service", "io.lucci.potlatch.service.adapter" })
public class ServiceConfig {
	
}