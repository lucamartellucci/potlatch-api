package io.lucci.potlatch.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import io.lucci.potlatch.model.Gift;
import io.lucci.potlatch.service.GiftService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
    	
    	Gift gift = new Gift();
    	gift.setUuid("1");
		Mockito.when(giftService.getGiftByUuid("1")).thenReturn(gift);
    	
        this.mockMvc.perform( get( "/gift/1" ).accept( MediaType.parseMediaType( "application/json;charset=UTF-8" ) ) )
        .andExpect( status().isOk() )
        .andExpect( content().contentType( "application/json;charset=UTF-8" ) )
        .andDo( print() );
        
        
        

    }

	
	

}
