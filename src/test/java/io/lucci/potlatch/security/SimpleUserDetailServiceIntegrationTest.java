package io.lucci.potlatch.security;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import io.lucci.potlatch.spring.PersistenceConfig;
import io.lucci.potlatch.spring.SecurityConfig;
import io.lucci.potlatch.spring.ServiceConfig;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.support.AnnotationConfigContextLoader;


@ActiveProfiles(profiles = { "db-test-mysql" })
@ContextConfiguration(classes = { PersistenceConfig.class, SecurityConfig.class, ServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
public class SimpleUserDetailServiceIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	final static Logger logger = LoggerFactory.getLogger(SimpleUserDetailServiceIntegrationTest.class);

    @Autowired
    private SimpleUserDetailService userDetailService;
    
    @Test
    public void testLoadUserByUsername() throws Exception {
    	executeSqlScript("file:src/test/resource/db/gift.prepareDB.sql", false);
    	UserDetails userDetails = userDetailService.loadUserByUsername("luca");
    	assertThat(userDetails, is(notNullValue()));
    	assertThat(userDetails.getAuthorities(), hasSize(2));
    	for (GrantedAuthority authority : userDetails.getAuthorities()) {
			assertThat(authority.getAuthority(), anyOf(equalTo("ADMIN"),equalTo("USER")));
		}
    }
    


}
