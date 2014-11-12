package io.lucci.potlatch.api;

import io.lucci.potlatch.model.Gift;
import retrofit.http.GET;
import retrofit.http.Path;


public interface GiftApi {

	public static final String TOKEN_PATH = "/oauth/token";
	public static final String GIFT_PATH = "/gift";

	@GET(GIFT_PATH + "/{id}")
	public Gift getGiftById(@Path("id") String id);
	
}
