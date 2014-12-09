package io.lucci.potlatch.server.service;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GiftManagerTestConfig {
	
	@Bean
    public StorageService storageService() {
        return Mockito.mock( StorageService.class );
    }

}
