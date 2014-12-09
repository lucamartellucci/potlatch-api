package io.lucci.potlatch.server.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import io.lucci.potlatch.server.service.exception.GiftNotFoundExcetption;
import io.lucci.potlatch.server.spring.PersistenceConfig;
import io.lucci.potlatch.server.spring.ServiceConfig;
import io.lucci.potlatch.server.web.model.Gift;
import io.lucci.potlatch.server.web.model.GiftBuilder;
import io.lucci.potlatch.server.web.model.User;
import io.lucci.potlatch.server.web.model.UserBuilder;

import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@ActiveProfiles(profiles = { "test" })
@ContextConfiguration(classes = { PersistenceConfig.class, ServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
public class GiftServiceIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	final static Logger logger = LoggerFactory.getLogger(GiftServiceIntegrationTest.class);

    @Autowired
    private GiftService giftService;
    
    @Before
    public void setup() {
    	executeSqlScript("file:src/test/resources/db/gift.prepareDB.sql", false);
    }

    @Test
    public final void testLikeGift() throws Exception {
    	Long numberOfLikes = giftService.likeGift("a6c1e839-d390-4b37-9837-dee63b3cffd9", 1L);
    	assertThat(numberOfLikes, is(equalTo(1L)));
    	
    	Gift updatedGift = giftService.getGiftByUuid("a6c1e839-d390-4b37-9837-dee63b3cffd9");
    	assertThat(updatedGift.getNumberOfLikes(), is(equalTo(1L)));
    }
    
    @Test(expected=GiftNotFoundExcetption.class)
    public final void testLikeGiftThatNotExist() throws Exception {
    	giftService.likeGift("a6c1e839-d390-4b37-9837-dee63b3cffd1", 1L);
    	fail("Should throw a GiftNotFoundException");
    }
    
    @Test
    public void testCreateGift() throws Exception {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	User user = UserBuilder.user()
    			.withId(1L)
    			.withEmail("luca.martellucci@gmail.com")
    			.withUsername("luca")
    			.withPassword("password")
    			.withBirthdate(sdf.parse("1978-09-25"))
    			.withGender("M").withRefreshInterval(60L)
    			.withBlockInappropriate(true)
    			.withRoles("ADMIN,USER")
    			.build();
    	
    	String title = "title001";
		String description = "desc001";
		Gift gift = GiftBuilder.gift().withTitle(title)
    			.withDescription(description)
    			.withUser(user)
    			.build();
		
		Gift savedGift = giftService.createGift(gift , null, user);
		logger.info("saved gift is {}", savedGift);
		
		assertThat(savedGift, is(notNullValue()));
		assertThat(savedGift.getTitle(), is(equalTo(title)));
		assertThat(savedGift.getDescription(), is(equalTo(description)));
		assertThat(savedGift.getUri(), is(notNullValue()));
		assertThat(savedGift.getUri(), is(notNullValue()));
		assertThat(savedGift.getTimestamp(), is(notNullValue()));
		assertThat(savedGift.getStatus(), is(equalTo(Gift.GiftStatus.ready_for_upload.toString())));
		assertThat(savedGift.getChainMaster(), is(equalTo(Boolean.TRUE)));
		
		savedGift = giftService.createGift(gift , 1L, user);
		logger.info("saved gift is {}", savedGift);
		
		assertThat(savedGift, is(notNullValue()));
		assertThat(savedGift.getTitle(), is(equalTo(title)));
		assertThat(savedGift.getDescription(), is(equalTo(description)));
		assertThat(savedGift.getUri(), is(notNullValue()));
		assertThat(savedGift.getUri(), is(notNullValue()));
		assertThat(savedGift.getTimestamp(), is(notNullValue()));
		assertThat(savedGift.getStatus(), is(equalTo(Gift.GiftStatus.ready_for_upload.toString())));
		assertThat(savedGift.getChainMaster(), is(equalTo(Boolean.FALSE)));
    }

}
