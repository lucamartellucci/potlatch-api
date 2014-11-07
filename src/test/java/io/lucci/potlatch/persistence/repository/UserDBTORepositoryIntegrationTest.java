package io.lucci.potlatch.persistence.repository;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import io.lucci.potlatch.persistence.PersistenceConfig;
import io.lucci.potlatch.persistence.model.UserDBTO;

import java.text.SimpleDateFormat;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@ActiveProfiles(profiles = { "db-test-mysql" })
@ContextConfiguration(classes = { PersistenceConfig.class }, loader = AnnotationConfigContextLoader.class)
public class UserDBTORepositoryIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	final static Logger logger = LoggerFactory.getLogger(UserDBTORepositoryIntegrationTest.class);
	
    @Autowired
    private UserDBTORepository repo;
    
    @Test
    public final void testFindOne() throws Exception {
    	
    	executeSqlScript("classpath:db/gift.prepareDB.sql", false);
    	UserDBTO user = repo.findOne(1L);
    	assertThat(user,is(notNullValue()));
    	assertThat(user.getEmail(),is(equalTo("luca.martellucci@gmail.com")));
    	assertThat(user.getName(),is(equalTo("luca")));
    	assertThat(user.getGender(),is(equalTo("M")));
    	assertThat(user.getPassword(),is(equalTo("password01")));
		assertThat(new SimpleDateFormat("dd/MM/yyyy").format(user.getBirthdate()),is(equalTo("25/09/1978")));
		assertThat(user.getGifts().size(),is(equalTo(3)));
    	
    }

}
