package io.lucci.potlatch.spring;

import io.lucci.potlatch.web.controller.resolver.PageableArgumentResolver;
import io.lucci.potlatch.web.controller.resolver.UserArgumentResolver;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ComponentScan("io.lucci.potlatch.web.controller")
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

    private static final int MAX_UPLOAD_SIZE = 5242880;
    private static final int MAX_IN_MEMORY_SIZE = 5242880;

	public WebConfig() {
        super();
    }
    
    @Override
    public void addArgumentResolvers(List< HandlerMethodArgumentResolver > argumentResolvers) {
    	UserArgumentResolver personResolver = new UserArgumentResolver();
    	argumentResolvers.add(personResolver);
    	PageableArgumentResolver pageableResolver = new PageableArgumentResolver();
    	argumentResolvers.add(pageableResolver);
    }

    @Override
    public void configureMessageConverters( final List<HttpMessageConverter<?>> converters ) {

        final ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
        byteArrayHttpMessageConverter.setSupportedMediaTypes( Arrays.asList( MediaType.APPLICATION_OCTET_STREAM ) );
        converters.add( byteArrayHttpMessageConverter );

        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion( JsonInclude.Include.NON_NULL );

        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes( Arrays.asList( MediaType.APPLICATION_JSON ) );
        converter.setObjectMapper( mapper );
        converters.add( converter );

        super.configureMessageConverters( converters );
    }
    
    @Bean(name="multipartResolver")
    MultipartResolver multipartConfigElement() {
    	CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
    	multipartResolver.setMaxUploadSize(MAX_UPLOAD_SIZE);
    	multipartResolver.setMaxInMemorySize(MAX_IN_MEMORY_SIZE);
		return multipartResolver; 
    }
}