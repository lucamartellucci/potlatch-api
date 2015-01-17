package io.lucci.potlatch.server.web.controller;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import io.lucci.potlatch.client.api.GiftApi;
import io.lucci.potlatch.client.api.GiftApiFactory;
import io.lucci.potlatch.client.model.Gift;
import io.lucci.potlatch.client.model.GiftBuilder;
import io.lucci.potlatch.client.model.PaginatorResult;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class GiftControllerLiveTest {
	
	private final String TEST_URL = "http://localhost:8080/potlatch";
	
	private final Logger logger = LoggerFactory.getLogger(GiftControllerLiveTest.class);
	
	private GiftApi securedGiftApi;
	
	private final String USERNAME = "luca";
	private final String PASSWORD = "password01";
	private final String CLIENT_ID = "mobile";
	
	@Before
	public void setup(){
		GiftApiFactory giftApiFactory = new GiftApiFactory(); 
		securedGiftApi=giftApiFactory.getSecuredGiftApi(TEST_URL, USERNAME, PASSWORD, CLIENT_ID);
	}
	
	@Test
	public void testRetrieveGiftById() throws Exception {
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Gift gift = securedGiftApi.getGiftById("f6aa4067-5b21-4d98-b172-307b557187f0");

		logger.debug("Retrieved gift is {}", gift);
		
		assertThat(gift,is(notNullValue()));
		assertThat(gift.getId(), is(equalTo(1L)));
		assertThat(gift.getDescription(), is(equalTo("description_1")));
		assertThat(gift.getNumberOfLikes(), is(equalTo(2L)));
		assertThat(gift.getStatus(), is(equalTo("active")));
		assertThat(df.format(gift.getTimestamp()), is(equalTo("2014-11-12 09:42:47")));
		assertThat(gift.getChainMaster(), is(equalTo(Boolean.TRUE)));
		assertThat(gift.getTitle(), is(equalTo("title_1")));
		assertThat(gift.getImageUrl(), is(equalTo("http://www.url1.it")));
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
			assertThat(gift.getId(), is(anyOf(
					equalTo(1L), 
					equalTo(2L)
				)));
		}
	}
	
	@Test
	public void testRetrievePagedGifts() throws Exception {
		Integer pageNumber = 0;
		Integer pageSize = 8;
		PaginatorResult<Gift> paginatorResult = securedGiftApi.getGifts(pageNumber, pageSize);
		assertThat(paginatorResult, is(notNullValue()));
		assertThat(paginatorResult.getCurrentPage(), is(equalTo(pageNumber)));
		assertThat(paginatorResult.getPageSize(), is(equalTo(pageSize)));
		assertThat(paginatorResult.getTotalPages(), is(greaterThan(1)));
		assertThat(paginatorResult.getResult(), is(notNullValue()));
		assertThat(paginatorResult.getResult().size(), is(equalTo(pageSize)));
		
		
		paginatorResult = securedGiftApi.getGifts(++pageNumber, pageSize);
		assertThat(paginatorResult.getPageSize(), is(equalTo(pageSize)));
		assertThat(paginatorResult.getTotalPages(), is(greaterThan(1)));
		assertThat(paginatorResult.getResult(), is(notNullValue()));
		assertThat(paginatorResult.getResult().size(), is(equalTo(1)));
	}
	
	@Test
	public void testSetGiftData() throws Exception {
		File file = new File(this.getClass().getResource("/images/lollipop.jpg").getFile());
		TypedFile giftData = new TypedFile("image/jpeg", file);
		Gift gift = securedGiftApi.setGiftData("c95265f8-e274-49ef-9970-b171dfadfb12", giftData );
		logger.info("Gift: {}", gift);
	}
	
	@Test
	public void testGetGiftData() throws Exception {
		FileOutputStream output = new FileOutputStream(new File("/home/luca/tmp/"+System.currentTimeMillis()+".jpg"));
		Response response = securedGiftApi.getGiftData("c95265f8-e274-49ef-9970-b171dfadfb12");
		IOUtils.copy(response.getBody().in(), output);
		output.flush();
		output.close();
	}
	
	@Test
	public void testCreateGift() throws Exception {
		
		Gift gift = GiftBuilder.gift().withTitle("I love the sun").withDescription("A really sunny day!").build();
		Gift newGift = securedGiftApi.createGift(gift);
		
		assertThat(newGift, is(notNullValue()));
		assertThat(newGift.getId(), is(notNullValue()));
		assertThat(newGift.getTitle(), is(equalTo("I love the sun")));
		assertThat(newGift.getDescription(), is(equalTo("A really sunny day!")));
		assertThat(newGift.getStatus(), is(equalTo(Gift.GiftStatus.ready_for_upload.toString())));
		assertThat(newGift.getImageUrl(), is(notNullValue()));
		assertThat(newGift.getImageUrl(), is(equalTo(new StringBuilder(TEST_URL)
			.append(GiftApi.PATH_GIFT)
			.append("/")
			.append(newGift.getId())
			.append("/data").toString())));
		assertTrue(newGift.getChainMaster().booleanValue());
		
	}
	
	@Test
	public void testCreateChainedGift() throws Exception {
		
		Gift gift = GiftBuilder.gift().withTitle("I love the sun").withDescription("A really sunny day!").build();
		Gift newGift = securedGiftApi.createChainedGift(gift, "f6aa4067-5b21-4d98-b172-307b557187f0");
		
		assertThat(newGift, is(notNullValue()));
		assertThat(newGift.getId(), is(notNullValue()));
		assertThat(newGift.getTitle(), is(equalTo("I love the sun")));
		assertThat(newGift.getDescription(), is(equalTo("A really sunny day!")));
		assertThat(newGift.getStatus(), is(equalTo(Gift.GiftStatus.ready_for_upload.toString())));
		assertThat(newGift.getImageUrl(), is(notNullValue()));
		assertThat(newGift.getImageUrl(), is(equalTo(new StringBuilder(TEST_URL)
			.append(GiftApi.PATH_GIFT)
			.append("/")
			.append(newGift.getId())
			.append("/data").toString())));
		assertFalse(newGift.getChainMaster().booleanValue());
	}
	
	
}
