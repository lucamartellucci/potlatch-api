package io.lucci.potlatch.client.api;

import io.lucci.potlatch.client.model.Gift;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;


public interface GiftApi {

	public static final String PARAM_DATA = "data";
	public static final String PARAM_FILE = "file";
	public static final String PARAM_PARENT_ID = "parentUuid";
	public static final String PARAM_SIZE = "size";
	public static final String PARAM_PAGE = "page";
	public static final String PARAM_ID = "id";

	public static final String PATH_OAUTH_TOKEN = "/oauth/token";
	public static final String PATH_BASE = "/api/v1";
	public static final String PATH_GIFT = PATH_BASE + "/gift";
	public static final String PATH_GIFT_ID = PATH_GIFT + "/{id}";
	public static final String PATH_GIFT_DATA = PATH_GIFT_ID + "/data";

	@GET(PATH_GIFT_ID)
	public Gift getGiftById(@Path(PARAM_ID) String id);
	
	@GET(PATH_GIFT)
	public List<Gift> getGifts();
	
	@GET(PATH_GIFT)
	public PaginatorResult<Gift> getGifts(@Query(PARAM_PAGE) Integer page, @Query(PARAM_SIZE) Integer size);
	
	@POST(PATH_GIFT)
	public Gift createGift(@Body Gift gift );
	
	@POST(PATH_GIFT)
	public Gift createChainedGift(@Body Gift gift, @Query(PARAM_PARENT_ID) String parentUuid );

	@Multipart
	@POST(PATH_GIFT_DATA)
	public Gift setGiftData(@Path(PARAM_ID) String id, @Part(PARAM_FILE) TypedFile file);
	
	@Streaming
	@GET(PATH_GIFT_DATA)
	public Response getGiftData(@Path(PARAM_ID) String id);
	
}
