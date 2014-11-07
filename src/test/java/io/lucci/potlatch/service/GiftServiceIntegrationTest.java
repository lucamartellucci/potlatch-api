package io.lucci.potlatch.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.lucci.potlatch.persistence.PersistenceConfig;
import io.lucci.potlatch.persistence.ServiceConfig;
import io.lucci.potlatch.persistence.model.GiftDBTO;

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
    	executeSqlScript("classpath:db/gift.prepareDB.sql", false);
    	Long numberOfLikes = giftService.likeGift(3L, 1L);
    	assertThat(numberOfLikes, is(equalTo(1L)));
    	
    	GiftDBTO updatedGift = giftService.getGiftById(3L);
    	assertThat(updatedGift.getNumberOfLikes(), is(equalTo(1L)));
    }

}
