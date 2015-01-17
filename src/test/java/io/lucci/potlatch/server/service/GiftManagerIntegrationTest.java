package io.lucci.potlatch.server.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import io.lucci.potlatch.server.config.PersistenceConfig;
import io.lucci.potlatch.server.config.ServiceConfig;
import io.lucci.potlatch.server.service.StorageService.StorageAction;
import io.lucci.potlatch.server.service.exception.GiftNotFoundExcetption;
import io.lucci.potlatch.server.web.model.Gift;
import io.lucci.potlatch.server.web.model.Gift.GiftStatus;
import io.lucci.potlatch.server.web.model.GiftBuilder;
import io.lucci.potlatch.server.web.model.User;
import io.lucci.potlatch.server.web.model.UserBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@ActiveProfiles(profiles = { "test" })
@ContextConfiguration(classes = { PersistenceConfig.class, ServiceConfig.class, GiftManagerTestConfig.class }, loader = AnnotationConfigContextLoader.class)
public class GiftManagerIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	final static Logger logger = LoggerFactory.getLogger(GiftManagerIntegrationTest.class);
	private static final Long CHAIN_MASTER = null;

    @Autowired
    private GiftManager giftManager;
    
    @Autowired
    private StorageService storageService;
    
    @Before
    public void setup(){
    	executeSqlScript("file:src/test/resources/db/gift.prepareDB.sql", false);
    	reset(storageService);
    }

    @Test
    public final void testCreateGiftAndSetData() throws Exception {

    	Gift gift = buildGift();
    	User user = buildUser();
    	
    	// create a gift
		Gift savedGift = giftManager.createGift(gift, CHAIN_MASTER, user);
		
		assertThat(savedGift.getStatus().toString(), is(equalTo(GiftStatus.ready_for_upload.toString())));
		assertThat(savedGift.getId(), is(notNullValue()));
		
		// store data
		try (FileInputStream in = new FileInputStream(new File(this.getClass().getResource("/images/lollipop.jpg").getFile()));) {
			doNothing().when(storageService).storeGiftData(in, savedGift.getImageFilename());
			when(storageService.buildGiftUrl(eq(StorageAction.READ), anyString())).thenReturn(new URL("https://myurl.net/image.jpg"));
			
			// do test
			savedGift = giftManager.setGiftData(savedGift.getId(), in);
			
			verify(storageService).storeGiftData(in, extractUuid(savedGift.getImageFilename()));
			verify(storageService).buildGiftUrl(eq(StorageAction.READ), anyString());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
    	
		assertThat(savedGift.getStatus().toString(), is(equalTo(GiftStatus.active.toString())));
		
    }
    
    private String extractUuid(String imageFilename) {
		return imageFilename.split("\\.")[0];
	}

	@Test
    public final void testCreateGiftAndSetDataFailure() throws Exception {

    	Gift gift = buildGift();
    	User user = buildUser();
    	
    	// create a gift
		Gift savedGift = giftManager.createGift(gift, CHAIN_MASTER, user);
		
		assertThat(savedGift.getStatus().toString(), is(equalTo(GiftStatus.ready_for_upload.toString())));
		assertThat(savedGift.getId(), is(notNullValue()));
		assertThat(savedGift.getTitle(), is("title001"));
		assertThat(savedGift.getDescription(), is("desc001"));
		
		// store data
		try (FileInputStream in = new FileInputStream(new File(this.getClass().getResource("/images/lollipop.jpg").getFile()));) {
			
			doThrow(new StorageServiceException()).when(storageService).storeGiftData(in, extractUuid(savedGift.getImageFilename()));
			savedGift = giftManager.setGiftData(savedGift.getId(), in);
		} catch (StorageServiceException e) {
			
			List<Map<String, Object>> result = jdbcTemplate.query( "select * from gift where id = "+ savedGift.getId(), new ColumnMapRowMapper() );
            assertThat( result.size(), is( 1 ) );
            assertThat( result.get( 0 ).get( "title" ).toString(), is( "title001" ) );
            assertThat( result.get( 0 ).get( "description" ).toString(), is( "desc001" ) );
            assertThat( result.get( 0 ).get( "status" ).toString(), is( equalTo(GiftStatus.ready_for_upload.toString())) );

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
    	
    }
    
    @Test
    public final void testLikeGift() throws Exception {
    	
    	when(storageService.buildGiftUrl(eq(StorageAction.READ), anyString())).thenReturn(new URL("https://myurl.net/image.jpg"));
    	
    	Long numberOfLikes = giftManager.likeGift(3L, 1L);
    	assertThat(numberOfLikes, is(equalTo(1L)));
    	
    	Gift updatedGift = giftManager.getGiftById(3L);
    	assertThat(updatedGift.getNumberOfLikes(), is(equalTo(1L)));
    	verify(storageService).buildGiftUrl(eq(StorageAction.READ), anyString());	
    	
    }
    
    @Test(expected=GiftNotFoundExcetption.class)
    public final void testLikeGiftThatNotExist() throws Exception {
    	giftManager.likeGift(23L, 1L);
    	fail("Should throw a GiftNotFoundException");
    }
    
    @Test
    public void testCreateGift() throws Exception {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	User user = UserBuilder.user()
    			.withId(1L)
    			.withEmail("luca.martellucci@gmail.com")
    			.withUsername("luca")
    			.withPassword("password")
    			.withBirthdate(sdf.parse("1978-09-25"))
    			.withGender("M").withRefreshInterval(60L)
    			.withBlockInappropriate(true)
    			.withRoles("ADMIN,USER")
    			.build();
    	
    	String title = "title001";
		String description = "desc001";
		Gift gift = GiftBuilder.gift().withTitle(title)
    			.withDescription(description)
    			.withUser(user)
    			.build();
		
		Gift savedGift = giftManager.createGift(gift , null, user);
		logger.info("saved gift is {}", savedGift);
		
		assertThat(savedGift, is(notNullValue()));
		assertThat(savedGift.getTitle(), is(equalTo(title)));
		assertThat(savedGift.getDescription(), is(equalTo(description)));
		assertThat(savedGift.getImageFilename(), is(notNullValue()));
		assertThat(savedGift.getImageUrl(), is(nullValue()));
		assertThat(savedGift.getTimestamp(), is(notNullValue()));
		assertThat(savedGift.getStatus(), is(equalTo(Gift.GiftStatus.ready_for_upload.toString())));
		assertThat(savedGift.getChainMaster(), is(equalTo(Boolean.TRUE)));
		
		savedGift = giftManager.createGift(gift , savedGift.getId(), user);
		logger.info("saved gift is {}", savedGift);
		
		assertThat(savedGift, is(notNullValue()));
		assertThat(savedGift.getTitle(), is(equalTo(title)));
		assertThat(savedGift.getDescription(), is(equalTo(description)));
		assertThat(savedGift.getImageFilename(), is(notNullValue()));
		assertThat(savedGift.getImageUrl(), is(nullValue()));
		assertThat(savedGift.getTimestamp(), is(notNullValue()));
		assertThat(savedGift.getStatus(), is(equalTo(Gift.GiftStatus.ready_for_upload.toString())));
		assertThat(savedGift.getChainMaster(), is(equalTo(Boolean.FALSE)));
		
    }
    
    @Test
    public final void testFindAllGifts() throws Exception {
		User user = UserBuilder.user().withId(1L).withBlockInappropriate(Boolean.TRUE).build();
		
		when(storageService.buildGiftUrl(eq(StorageAction.READ), anyString())).thenReturn(new URL("https://myurl.net/image.jpg"));
		
		List<Gift> gifts = giftManager.getGifts(user, null).getResult();
		logger.debug("Found gifts: {}", gifts);
		
		assertThat(gifts, hasSize(2));
		assertThat(gifts.get(0).getTitle(), is("title_1"));
		assertThat(gifts.get(0).getDescription(), is("description_1"));
		assertThat(gifts.get(0).getChainMaster(), is(true));
		
		verify(storageService, times(2)).buildGiftUrl(eq(StorageAction.READ), anyString());	
    }

	private User buildUser() {
		User user = UserBuilder.user()
    			.withId(1L)
    			.withEmail("luca.martellucci@gmail.com")
    			.withUsername("luca")
    			.build();
		return user;
	}

	private Gift buildGift() {
		Gift gift = GiftBuilder.gift()
    			.withTitle("title001")
    			.withDescription("desc001")
    			.build();
		return gift;
	}
    

}
