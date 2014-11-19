package io.lucci.potlatch.api;

import io.lucci.potlatch.model.Gift;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;


public interface GiftApi {

	public static final String TOKEN_PATH = "/oauth/token";
	public static final String GIFT_PATH = "/gift";

	@GET(GIFT_PATH + "/{id}")
	public Gift getGiftById(@Path("id") String id);
	
	@GET(GIFT_PATH)
	public List<Gift> getGifts();
	
	@GET(GIFT_PATH)
	public List<Gift> getGifts(@Query("page") Integer page, @Query("size") Integer size);
	
}
