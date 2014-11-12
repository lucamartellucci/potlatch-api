package io.lucci.potlatch;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import io.lucci.potlatch.api.GiftApi;
import io.lucci.potlatch.api.GiftApiFactory;
import io.lucci.potlatch.model.Gift;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GiftLiveTest {
	
	private final String TEST_URL = "http://localhost:8080/potlatch";
	
	private final Logger logger = LoggerFactory.getLogger(GiftLiveTest.class);
	
	private GiftApi giftApi;
	
	@Before
	public void setup(){
		GiftApiFactory giftApiFactory = new GiftApiFactory(); 
		giftApi=giftApiFactory.getSimpleGiftApi(TEST_URL);
	}
	
	@Test
	public void testRetrieveGiftById() throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Gift gift = giftApi.getGiftById("f6aa4067-5b21-4d98-b172-307b557187f0");

		logger.debug("Retrieved gift is {}", gift);
		
		assertThat(gift,is(notNullValue()));
		assertThat(gift.getUuid(), is(equalTo("f6aa4067-5b21-4d98-b172-307b557187f0")));
		assertThat(gift.getDescription(), is(equalTo("description_1")));
		assertThat(gift.getNumberOfLikes(), is(equalTo(2L)));
		assertThat(gift.getStatus(), is(equalTo("active")));
		assertThat(df.format(gift.getTimestamp()), is(equalTo("2014-11-12 09:42:47")));
		assertThat(gift.getChainMaster(), is(equalTo(Boolean.TRUE)));
		assertThat(gift.getTitle(), is(equalTo("title_1")));
		assertThat(gift.getUri(), is(equalTo("http://www.url1.it")));
		assertThat(gift.getUser().getId(), is(equalTo(1L)));
	}

}
