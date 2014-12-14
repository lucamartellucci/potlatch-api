package io.lucci.potlatch.server.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import io.lucci.potlatch.server.config.PersistenceConfig;
import io.lucci.potlatch.server.config.ServiceConfig;
import io.lucci.potlatch.server.web.model.Gift;
import io.lucci.potlatch.server.web.model.Gift.GiftStatus;
import io.lucci.potlatch.server.web.model.GiftBuilder;
import io.lucci.potlatch.server.web.model.User;
import io.lucci.potlatch.server.web.model.UserBuilder;

import java.io.File;
import java.io.FileInputStream;
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
	private static final String CHAIN_MASTER = null;

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
		assertThat(savedGift.getUuid(), is(notNullValue()));
		assertThat(savedGift.getUri(), is(notNullValue()));
		
		// store data
		try (FileInputStream in = new FileInputStream(new File(this.getClass().getResource("/images/lollipop.jpg").getFile()));) {
			doNothing().when(storageService).storeObject(in, buildGiftObjectName(gift));
			
			// do test
			savedGift = giftManager.setGiftData(savedGift.getUuid(), in);
			
			verify(storageService).storeObject(in, buildGiftObjectName(savedGift));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
    	
		assertThat(savedGift.getStatus().toString(), is(equalTo(GiftStatus.active.toString())));
		
    }
    
    @Test
    public final void testCreateGiftAndSetDataFailure() throws Exception {

    	Gift gift = buildGift();
    	User user = buildUser();
    	
    	// create a gift
		Gift savedGift = giftManager.createGift(gift, CHAIN_MASTER, user);
		
		assertThat(savedGift.getStatus().toString(), is(equalTo(GiftStatus.ready_for_upload.toString())));
		assertThat(savedGift.getUuid(), is(notNullValue()));
		assertThat(savedGift.getUri(), is(notNullValue()));
		assertThat(savedGift.getTitle(), is("title001"));
		assertThat(savedGift.getDescription(), is("desc001"));
		
		// store data
		try (FileInputStream in = new FileInputStream(new File(this.getClass().getResource("/images/lollipop.jpg").getFile()));) {
			
			doThrow(new StorageServiceException()).when(storageService).storeObject(in, buildGiftObjectName(gift));
			savedGift = giftManager.setGiftData(savedGift.getUuid(), in);
		} catch (StorageServiceException e) {
			
			List<Map<String, Object>> result = jdbcTemplate.query( "select * from gift where uuid = "+ savedGift.getUuid(), new ColumnMapRowMapper() );
            assertThat( result.size(), is( 1 ) );
            assertThat( result.get( 0 ).get( "title" ).toString(), is( "title001" ) );
            assertThat( result.get( 0 ).get( "description" ).toString(), is( "desc001" ) );
            assertThat( result.get( 0 ).get( "status" ).toString(), is( equalTo(GiftStatus.ready_for_upload.toString())) );

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
    	
    }

	private String buildGiftObjectName(Gift gift) {
		return String.format("%s.jpg", gift.getUuid());
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
