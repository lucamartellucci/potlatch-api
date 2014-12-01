package io.lucci.potlatch.web.controller;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import io.lucci.potlatch.client.api.GiftApi;
import io.lucci.potlatch.client.api.GiftApiFactory;
import io.lucci.potlatch.web.model.Gift;
import io.lucci.potlatch.web.model.GiftBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit.RetrofitError;

public class GiftControllerLiveTest {
	
	private final String TEST_URL = "http://localhost:8080/potlatch";
	
	private final Logger logger = LoggerFactory.getLogger(GiftControllerLiveTest.class);
	
	private GiftApi giftApi;
	private GiftApi securedGiftApi;
	
	private final String USERNAME = "luca";
	private final String PASSWORD = "password01";
	private final String CLIENT_ID = "mobile";
	
	@Before
	public void setup(){
		GiftApiFactory giftApiFactory = new GiftApiFactory(); 
		giftApi=giftApiFactory.getSimpleGiftApi(TEST_URL);
		securedGiftApi=giftApiFactory.getSecuredGiftApi(TEST_URL, USERNAME, PASSWORD, CLIENT_ID);
	}
	
	@Test
	public void testRetrieveGiftById() throws Exception {
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Gift gift = securedGiftApi.getGiftById("f6aa4067-5b21-4d98-b172-307b557187f0");

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
	
	@Test
	public void testRetrieveNotExistingGiftById() throws Exception {
		try {
			securedGiftApi.getGiftById("not_exist_gift_id");
		} catch (RetrofitError error) {
			assertThat(error.getResponse().getStatus(), is(equalTo(HttpStatus.SC_NOT_FOUND)));
			Object body = error.getBody();
			assertThat(body, is(notNullValue()));
			
		}
	}
	
	@Test
	public void testRetrieveGifts() throws Exception {
		List<Gift> gifts = securedGiftApi.getGifts();
		assertThat(gifts.size(), is(equalTo(2)));
		for (Gift gift : gifts) {
			assertThat(gift.getUuid(), is(anyOf(
					equalTo("f6aa4067-5b21-4d98-b172-307b557187f0"), 
					equalTo("5a4dcd02-1e6d-4c4d-a3a0-2e28cb631d21")
				)));
		}
	}
	
	@Test
	public void testRetrievePagedGifts() throws Exception {
		Integer pageNumber = 0;
		Integer pageSize = 1;
		List<Gift> gifts = securedGiftApi.getGifts(pageNumber, pageSize);
		assertThat(gifts.size(), is(equalTo(1)));
		assertThat(gifts.get(0).getUuid(), is(equalTo("f6aa4067-5b21-4d98-b172-307b557187f0")));
		
		gifts = securedGiftApi.getGifts(++pageNumber, pageSize);
		assertThat(gifts.size(), is(equalTo(1)));
		assertThat(gifts.get(0).getUuid(), is(equalTo("5a4dcd02-1e6d-4c4d-a3a0-2e28cb631d21")));
	}
	
	@Test
	public void testCreateGift() throws Exception {
		
		Gift gift = GiftBuilder.gift().withTitle("I love the sun").withDescription("A really sunny day!").build();
		Gift newGift = securedGiftApi.createGift(gift);
		
		assertThat(newGift, is(notNullValue()));
		assertThat(newGift.getUuid(), is(notNullValue()));
		assertThat(newGift.getTitle(), is(equalTo("I love the sun")));
		assertThat(newGift.getDescription(), is(equalTo("A really sunny day!")));
		assertThat(newGift.getStatus(), is(equalTo(Gift.GiftStatus.ready_for_upload.toString())));
		assertThat(newGift.getUri(), is(notNullValue()));
		assertThat(newGift.getUri(), is(equalTo(TEST_URL+GiftApi.API_BASE_PATH+GiftApi.GIFT_PATH+"/"+newGift.getUuid()+"/data")));
		assertTrue(newGift.getChainMaster().booleanValue());
		
	}
	
	@Test
	public void testCreateChainedGift() throws Exception {
		
		Gift gift = GiftBuilder.gift().withTitle("I love the sun").withDescription("A really sunny day!").build();
		Gift newGift = securedGiftApi.createChainedGift(gift, 1L);
		
		assertThat(newGift, is(notNullValue()));
		assertThat(newGift.getUuid(), is(notNullValue()));
		assertThat(newGift.getTitle(), is(equalTo("I love the sun")));
		assertThat(newGift.getDescription(), is(equalTo("A really sunny day!")));
		assertThat(newGift.getStatus(), is(equalTo(Gift.GiftStatus.ready_for_upload.toString())));
		assertThat(newGift.getUri(), is(notNullValue()));
		assertThat(newGift.getUri(), is(equalTo(TEST_URL+GiftApi.API_BASE_PATH+GiftApi.GIFT_PATH+"/"+newGift.getUuid()+"/data")));
		assertFalse(newGift.getChainMaster().booleanValue());
		
	}
	
	
}
