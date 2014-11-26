package io.lucci.potlatch.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import io.lucci.potlatch.service.exception.GiftNotFoundExcetption;
import io.lucci.potlatch.spring.PersistenceConfig;
import io.lucci.potlatch.spring.ServiceConfig;
import io.lucci.potlatch.web.model.Gift;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@ActiveProfiles(profiles = { "db-test-mysql" })
@ContextConfiguration(classes = { PersistenceConfig.class, ServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
public class GiftServiceIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	final static Logger logger = LoggerFactory.getLogger(GiftServiceIntegrationTest.class);

    @Autowired
    private GiftService giftService;

    @Test
    public final void testLikeGift() throws Exception {
    	executeSqlScript("file:src/test/resources/db/gift.prepareDB.sql", false);
    	Long numberOfLikes = giftService.likeGift("a6c1e839-d390-4b37-9837-dee63b3cffd9", 1L);
    	assertThat(numberOfLikes, is(equalTo(1L)));
    	
    	Gift updatedGift = giftService.getGiftByUuid("a6c1e839-d390-4b37-9837-dee63b3cffd9");
    	assertThat(updatedGift.getNumberOfLikes(), is(equalTo(1L)));
    }
    
    @Test(expected=GiftNotFoundExcetption.class)
    public final void testLikeGiftThatNotExist() throws Exception {
    	executeSqlScript("file:src/test/resources/db/gift.prepareDB.sql", false);
    	giftService.likeGift("a6c1e839-d390-4b37-9837-dee63b3cffd1", 1L);
    	fail("Should throw a GiftNotFoundException");
    }

}
