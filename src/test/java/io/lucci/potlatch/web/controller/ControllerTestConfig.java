package io.lucci.potlatch.web.controller;

import io.lucci.potlatch.service.GiftService;
import io.lucci.potlatch.service.StorageService;

import java.util.Arrays;
import java.util.List;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebMvc
@ComponentScan ( "io.lucci.potlatch.web.controller" )
public class ControllerTestConfig extends WebMvcConfigurerAdapter {
	
	@Bean
    public GiftService giftService() {
        return Mockito.mock( GiftService.class );
    }
	
	@Bean
    public StorageService storageService() {
        return Mockito.mock( StorageService.class );
    }
	
	@Override
	public void addArgumentResolvers( List<HandlerMethodArgumentResolver> argumentResolvers ) {
        argumentResolvers.add( new UserArgumentResolverMock() );
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
	

}
