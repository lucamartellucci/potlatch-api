package io.lucci.potlatch.api;

import java.lang.reflect.Type;
import java.util.Date;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.converter.GsonConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class GiftApiFactory {

	public GiftApi getSimpleGiftApi(final String endpointUrl){
		GiftApi giftApi = new RestAdapter.Builder()
				.setEndpoint(endpointUrl).setLogLevel(LogLevel.FULL)
				.setConverter(buildDateGsonConverter())
				.build()
				.create(GiftApi.class);
		return giftApi;
	}

	private GsonConverter buildDateGsonConverter() {
		final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
			@Override
			public Date deserialize(JsonElement json, Type type,
					JsonDeserializationContext ctx) throws JsonParseException {
				return new Date(json.getAsLong());
			}
		}).create();
		return new GsonConverter(gson);
	}
}
