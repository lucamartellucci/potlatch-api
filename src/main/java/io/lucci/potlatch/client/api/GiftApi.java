package io.lucci.potlatch.client.api;

import io.lucci.potlatch.web.model.Gift;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;


public interface GiftApi {

	public static final String OAUTH_TOKEN_PATH = "/oauth/token";
	
	public static final String API_BASE_PATH = "/api/v1";
	public static final String GIFT_PATH = "/gift";

	@GET(API_BASE_PATH + GIFT_PATH + "/{id}")
	public Gift getGiftById(@Path("id") String id);
	
	@GET(API_BASE_PATH + GIFT_PATH)
	public List<Gift> getGifts();
	
	@GET(API_BASE_PATH + GIFT_PATH)
	public List<Gift> getGifts(@Query("page") Integer page, @Query("size") Integer size);
	
}
