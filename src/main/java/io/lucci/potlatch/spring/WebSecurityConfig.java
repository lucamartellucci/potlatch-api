package io.lucci.potlatch.spring;

import io.lucci.potlatch.web.security.SimpleUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;

@Configuration
@ComponentScan({ "io.lucci.potlatch.web.security" })
public class WebSecurityConfig {
	
		@Configuration
		@EnableWebSecurity
		protected static class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
			
			@Autowired
			private SimpleUserDetailsService userDetailsService;
			
			@Bean(name = "myAuthenticationManager")
			@Override
			public AuthenticationManager authenticationManagerBean() throws Exception {
				return super.authenticationManagerBean();
			}
			
			@Override
		    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
				auth.userDetailsService(userDetailsService);
			}
		}
		
		@Configuration
		@EnableResourceServer
		protected static class ResourceServer extends ResourceServerConfigurerAdapter {

			@Override
			public void configure(HttpSecurity http) throws Exception {
				
				http.csrf().disable();
				
				http.authorizeRequests().antMatchers("/oauth/token").anonymous();
				
				http.authorizeRequests()
					.antMatchers(HttpMethod.GET, "/**")
					.access("#oauth2.hasScope('read')");
				
				http.authorizeRequests()
					.antMatchers("/**")
					.access("#oauth2.hasScope('write')");
			}

		}

		@Configuration
		@EnableAuthorizationServer
		@Order(Ordered.LOWEST_PRECEDENCE - 100)
		protected static class OAuth2Config extends	AuthorizationServerConfigurerAdapter {
			
			private ClientDetailsService csvc;
			
			public OAuth2Config() throws Exception {
				
				csvc = new InMemoryClientDetailsServiceBuilder()
					.withClient("mobile")
						.authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
						.scopes("read","write").resourceIds("gift", "oauth2-resource")
					.and()
					.withClient("mobileReader")
						.authorities("ROLE_CLIENT")
						.scopes("read").resourceIds("gift", "oauth2-resource")
						.accessTokenValiditySeconds(3600)
					.and().build();
			}

			/**
			 * Return the list of trusted client information to anyone who asks for it.
			 */
			@Bean
			public ClientDetailsService clientDetailsService() throws Exception {
				return csvc;
			}

			@Autowired
			private AuthenticationManager authenticationManager;

			@Override
			public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
				endpoints.authenticationManager(authenticationManager);
			}

		}
}