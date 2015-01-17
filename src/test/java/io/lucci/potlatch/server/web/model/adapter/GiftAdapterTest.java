package io.lucci.potlatch.server.web.model.adapter;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import io.lucci.potlatch.server.persistence.model.GiftDBTO;
import io.lucci.potlatch.server.web.model.Gift;
import io.lucci.potlatch.server.web.model.adapter.GiftAdapter;

import org.junit.Test;

public class GiftAdapterTest {
	
	private GiftAdapter adapter = new GiftAdapter();
	
	@Test
	public void dbtoToToTest() {
		
		GiftDBTO giftDBTO = new GiftDBTO();
		giftDBTO.setId(1l);
		giftDBTO.setLiked(true);
		giftDBTO.setNumberOfLikes(4L);
		giftDBTO.setStatus("Active");
		giftDBTO.setTitle("Gift_1");
		giftDBTO.setDescription("Gift_1 description");
		giftDBTO.setUuid("filename");
		
		Gift gift = adapter.dbtoToTo(giftDBTO , false);
		assertThat(gift, is(notNullValue()));
		
		assertThat(gift.getId(),is(equalTo(giftDBTO.getId())));
		assertThat(gift.getImageFilename() ,is(equalTo(adapter.buildImageFilename(giftDBTO.getUuid()))));
		assertThat(gift.getLikedByMe() ,is(equalTo(true)));
		assertThat(gift.getNumberOfLikes() ,is(equalTo(giftDBTO.getNumberOfLikes())));
		assertThat(gift.getReportedByMe(), is(nullValue()));
		assertThat(gift.getStatus(), is(equalTo(giftDBTO.getStatus())));
		assertThat(gift.getTitle(), is(equalTo(giftDBTO.getTitle())));
		assertThat(gift.getDescription(), is(equalTo(giftDBTO.getDescription())));
		assertThat(gift.getUser(), is(nullValue()));
		
	}

}
