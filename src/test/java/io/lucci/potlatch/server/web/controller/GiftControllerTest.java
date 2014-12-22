package io.lucci.potlatch.server.web.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import io.lucci.potlatch.server.service.GiftManager;
import io.lucci.potlatch.server.service.exception.GiftNotFoundExcetption;
import io.lucci.potlatch.server.web.controller.exception.ErrorCode;
import io.lucci.potlatch.server.web.model.Gift;
import io.lucci.potlatch.server.web.model.GiftBuilder;
import io.lucci.potlatch.server.web.model.PaginatorResult;
import io.lucci.potlatch.server.web.model.SimplePaginator;
import io.lucci.potlatch.server.web.model.User;
import io.lucci.potlatch.server.web.model.UserBuilder;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { ControllerTestConfig.class } )
@WebAppConfiguration
public class GiftControllerTest {
	
	private static Logger logger = LoggerFactory.getLogger(GiftControllerTest.class);

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private GiftManager giftManager;
    
    private MockMvc mockMvc;
    
    private User user;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup( this.wac ).build();
        user = buildUser();
        UserArgumentResolverMock.setUser(user);
        reset(giftManager);
    }
    
    @Test
    public void testFindById() throws Exception {
    	
		Gift gift = buildGift(user);
	
		when(giftManager.getGiftByUuid("1")).thenReturn(gift);
    	
        this.mockMvc.perform( get( "/api/v1/gift/1" ).accept( MediaType.parseMediaType( "application/json;charset=UTF-8" ) ) )
	        .andExpect( status().isOk() )
	        .andExpect( content().contentType( "application/json;charset=UTF-8" ) )
	        .andDo( print() )
	        .andExpect( jsonPath( "$.uuid" ).value( gift.getUuid() ) )
	        .andExpect( jsonPath( "$.title" ).value( gift.getTitle() ) )
	        .andExpect( jsonPath( "$.description" ).value( gift.getDescription() ) )
	        .andExpect( jsonPath( "$.timestamp" ).value( gift.getTimestamp().getTime() ) )
	        .andExpect( jsonPath( "$.chainMaster" ).value( gift.getChainMaster() ) )
	        .andExpect( jsonPath( "$.status" ).value( gift.getStatus() ) )
	        .andExpect( jsonPath( "$.numberOfLikes" ).value( gift.getNumberOfLikes().intValue() ) )
	        .andExpect( jsonPath( "$.reportedByMe" ).value( gift.getReportedByMe() ) )
	        .andExpect( jsonPath( "$.likedByMe" ).value( gift.getLikedByMe() ));
	        
        verify(giftManager).getGiftByUuid("1");
    }
    
    
    @Test
    public void testRetrieveGifts() throws Exception {
    	
		Gift gift = buildGift(user);
	
		SimplePaginator paginator = new SimplePaginator(0, 5);
		PaginatorResult<Gift> paginatorModel = new PaginatorResult<Gift>();
		paginatorModel.setResult(Arrays.asList(gift));
		paginatorModel.setCurrentPage(0);
		paginatorModel.setPageSize(5);
		paginatorModel.setTotalPages(1);
		
		when(giftManager.findAllGifts(user, paginator)).thenReturn(paginatorModel);
    	
        this.mockMvc.perform( get( "/api/v1/gift?page=0&size=5" ).accept( MediaType.parseMediaType( "application/json;charset=UTF-8" ) ) )
	        .andExpect( status().isOk() )
	        .andExpect( content().contentType( "application/json;charset=UTF-8" ) )
	        .andDo( print() )
	        .andExpect( jsonPath( "$.currentPage" ).value(0) )
	        .andExpect( jsonPath( "$.pageSize" ).value(5) )
	        .andExpect( jsonPath( "$.totalPages" ).value(1) )
	        .andExpect( jsonPath( "$.result", hasSize(1)) )
	        .andExpect( jsonPath( "$.result[0].uuid" ).value( gift.getUuid() ) )
	        .andExpect( jsonPath( "$.result[0].title" ).value( gift.getTitle() ) )
	        .andExpect( jsonPath( "$.result[0].description" ).value( gift.getDescription() ) )
	        .andExpect( jsonPath( "$.result[0].timestamp" ).value( gift.getTimestamp().getTime() ) )
	        .andExpect( jsonPath( "$.result[0].chainMaster" ).value( gift.getChainMaster() ) )
	        .andExpect( jsonPath( "$.result[0].status" ).value( gift.getStatus() ) )
	        .andExpect( jsonPath( "$.result[0].numberOfLikes" ).value( gift.getNumberOfLikes().intValue() ) )
	        .andExpect( jsonPath( "$.result[0].reportedByMe" ).value( gift.getReportedByMe() ) )
	        .andExpect( jsonPath( "$.result[0].likedByMe" ).value( gift.getLikedByMe() ));
	        
        verify(giftManager).findAllGifts(user, paginator);
    }
    
    @Test
    public void testFindByIdNotExistingGift() throws Exception {
    	
		final String errorMessage = "Unable to find the gift with id [2]";
		when(giftManager.getGiftByUuid("2")).thenThrow(new GiftNotFoundExcetption(errorMessage));
    	
        this.mockMvc.perform( get( "/api/v1/gift/2" ).accept( MediaType.parseMediaType( "application/json;charset=UTF-8" ) ) )
	        .andExpect( status().isNotFound() )
	        .andExpect( content().contentType( "application/json;charset=UTF-8" ) )
	        .andDo( print() )
	        .andExpect( jsonPath( "$.code" ).value( ErrorCode.RESOURCE_NOT_FOUND ) )
	        .andExpect( jsonPath( "$.message" ).value( errorMessage ) );
        
        verify(giftManager).getGiftByUuid("2");
    }
    
    @Test
    public void testCreateGift() throws Exception {
    	
		Gift gift = GiftBuilder.gift().withTitle("title001").withDescription("desc001").build();
		
		Gift savedGift = GiftBuilder.gift()
				.withTitle("title001")
				.withDescription("desc001")
				.withChainMaster(Boolean.TRUE)
				.withStatus(Gift.GiftStatus.ready_for_upload.toString())
				.withTimestamp(new Date())
				.withUri("http://localhost/potlatch:8080/api/v1/gift/f6aa4067-5b21-4d98-b172-307b557187f0/data")
				.withUuid("f6aa4067-5b21-4d98-b172-307b557187f0")
				.build();

		when(giftManager.createGift(gift, null, user)).thenReturn(savedGift);
		
        this.mockMvc.perform( post( "/api/v1/gift" ).accept( MediaType.parseMediaType( "application/json;charset=UTF-8" ) )
        		.content(GsonBuilderUtils.gsonBuilderWithBase64EncodedByteArrays().create().toJson(gift))
        		.contentType(MediaType.APPLICATION_JSON))
        	.andDo( print() )
	        .andExpect( status().isOk() )
	        .andExpect( content().contentType( "application/json;charset=UTF-8" ) )
	        .andExpect( jsonPath( "$.uuid" ).value( savedGift.getUuid() ) )
	        .andExpect( jsonPath( "$.title" ).value( savedGift.getTitle() ) )
	        .andExpect( jsonPath( "$.description" ).value( savedGift.getDescription() ) )
	        .andExpect( jsonPath( "$.timestamp" ).value( savedGift.getTimestamp().getTime() ) )
	        .andExpect( jsonPath( "$.chainMaster" ).value( savedGift.getChainMaster() ) )
	        .andExpect( jsonPath( "$.status" ).value( savedGift.getStatus() ) );
        
        verify(giftManager).createGift(gift, null, user);
    }
    
    
    
    

	private Gift buildGift(User user) {
		Gift gift = GiftBuilder.gift()
    			.withUuid("f6aa4067-5b21-4d98-b172-307b557187f0")
    			.withChainMaster(Boolean.TRUE)
    			.withDescription("Gift desc 1")
    			.withLikedByMe(Boolean.TRUE)
    			.withNumberOfLikes(33L)
    			.withReportedByMe(Boolean.FALSE)
    			.withStatus("Active")
    			.withTimestamp(new Date())
    			.withTitle("Gift 1")
    			.withUri("http://localhost/potlatch:8080/api/v1/gift/f6aa4067-5b21-4d98-b172-307b557187f0")
    			.withUser(user)
    			.build();
		return gift;
	}

	private User buildUser() {
		try {
			User user = UserBuilder.user()
				.withEmail("luca@gmail.com")
				.withGender("M")
				.withId(1L)
				.withPassword("password")
				.withBirthdate(new SimpleDateFormat("dd/MM/yyyy").parse("25/09/1978"))
				.withBlockInappropriate(Boolean.FALSE)
				.withRefreshInterval(60L)
				.withUsername("luca")
				.build();
			return user;
		} catch (Exception e) {
			logger.warn("Unable to create the user");
		}
		return null;
	}

	
	

}
