package io.lucci.potlatch.web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import io.lucci.potlatch.service.GiftService;
import io.lucci.potlatch.web.model.Gift;
import io.lucci.potlatch.web.model.GiftBuilder;
import io.lucci.potlatch.web.model.User;
import io.lucci.potlatch.web.model.UserBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private GiftService giftService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup( this.wac ).build();
    }
    
    @Test
    public void findByIdTest() throws Exception {
    	
		User user = buildUser();
		Gift gift = buildGift(user);
	
		when(giftService.getGiftByUuid("1")).thenReturn(gift);
    	
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
	        .andExpect( jsonPath( "$.likedByMe" ).value( gift.getLikedByMe() ) )
	        .andExpect( jsonPath( "$.user.id" ).value( user.getId().intValue() ) )
	        .andExpect( jsonPath( "$.user.email" ).value( user.getEmail() ) )
	        .andExpect( jsonPath( "$.user.username" ).value( user.getUsername() ) )
	        .andExpect( jsonPath( "$.user.password" ).value( user.getPassword() ) )
	        .andExpect( jsonPath( "$.user.birthdate" ).value( user.getBirthdate().getTime() ) )
	        .andExpect( jsonPath( "$.user.gender" ).value( user.getGender() ) )
	        .andExpect( jsonPath( "$.user.blockInappropriate" ).value( user.getBlockInappropriate() ) )
	        .andExpect( jsonPath( "$.user.refreshInterval" ).value( user.getRefreshInterval().intValue() ) );
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

	private User buildUser() throws ParseException {
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
	}

	
	

}
