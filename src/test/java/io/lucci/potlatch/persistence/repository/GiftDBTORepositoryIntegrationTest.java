package io.lucci.potlatch.persistence.repository;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import io.lucci.potlatch.persistence.model.GiftDBTO;
import io.lucci.potlatch.spring.PersistenceConfig;

import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@ActiveProfiles(profiles = { "db-test-mysql" })
@ContextConfiguration(classes = { PersistenceConfig.class }, loader = AnnotationConfigContextLoader.class)
public class GiftDBTORepositoryIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	final static Logger logger = LoggerFactory.getLogger(GiftDBTORepositoryIntegrationTest.class);

    @Autowired
    private GiftDBTORepository repo;
    
    @Test
    public final void testFindOne() throws Exception {
    	
    	executeSqlScript("file:src/test/resources/db/gift.prepareDB.sql", false);
    	GiftDBTO gift = repo.findOne(1L);
    	assertThat(gift,is(notNullValue()));
    	assertThat(gift.getTitle(), is(equalTo("title_1")));
    	assertThat(gift.getDescription(), is(equalTo("description_1")));
    	assertThat(gift.getStatus(), is(equalTo("active")));
    	assertThat(gift.getUser(),is(notNullValue()));
    	assertThat(gift.getUser().getEmail(),is("luca.martellucci@gmail.com"));
    	assertThat(gift.getUser().getUsername(),is("luca"));
		assertThat(new SimpleDateFormat("dd/MM/yyyy").format(gift.getUser().getBirthdate()),is("25/09/1978"));
		
		logger.info("{}", gift.getUserActions());
    	
    }
    
    @Test
    public final void testFindByUuid() throws Exception {
    	
    	executeSqlScript("file:src/test/resources/db/gift.prepareDB.sql", false);
    	GiftDBTO gift = repo.findByUuid("f6aa4067-5b21-4d98-b172-307b557187f0");
    	assertThat(gift,is(notNullValue()));
    	assertThat(gift.getTitle(), is(equalTo("title_1")));
    	assertThat(gift.getDescription(), is(equalTo("description_1")));
    	assertThat(gift.getStatus(), is(equalTo("active")));
    	assertThat(gift.getUser(),is(notNullValue()));
    	assertThat(gift.getUser().getEmail(),is("luca.martellucci@gmail.com"));
    	assertThat(gift.getUser().getUsername(),is("luca"));
		assertThat(new SimpleDateFormat("dd/MM/yyyy").format(gift.getUser().getBirthdate()),is("25/09/1978"));
		
		logger.info("{}", gift.getUserActions());
    	
    }
    
	@Test
	public final void testFindAllByUserId() throws Exception {

		executeSqlScript("file:src/test/resources/db/gift.prepareDB.sql", false);
		final long userId = 1L;
		List<GiftDBTO> gifts = repo.findAllByUserId(userId);
		assertThat(gifts.size(), is(equalTo(3)));

		for (GiftDBTO gift : gifts) {
			logger.info("Gift is: {}", gift);
		}
		
		GiftDBTO gift1 = gifts.get(0);
		assertThat(gift1.getId(), is(equalTo(1L)));
		assertThat(gift1.getLiked(), is(equalTo(Boolean.TRUE)));
		assertThat(gift1.getTitle(), is("title_1"));
		assertThat(gift1.getDescription(), is("description_1"));
		assertThat(gift1.getUuid(), is("f6aa4067-5b21-4d98-b172-307b557187f0"));
		assertThat(gift1.getUri(), is("http://www.url1.it"));
		assertThat(gift1.getStatus(), is("active"));
		assertThat(gift1.getUser(), is(notNullValue()));
		assertThat(gift1.getUser().getId(), is(1L));
		assertThat(gift1.getUser().getUsername(), is("luca"));
		assertThat(gift1.getUser().getEmail(), is("luca.martellucci@gmail.com"));
	}
	
	@Test
	public final void testFindAllByUserIdFilterInappropriate() throws Exception {

		executeSqlScript("file:src/test/resources/db/gift.prepareDB.sql", false);
		final long userId = 1L;
		List<GiftDBTO> gifts = repo.findAllByUserIdFilterInappropriate(userId);
		assertThat(gifts.size(), is(equalTo(2)));

		for (GiftDBTO gift : gifts) {
			logger.info("Gift is: {}", gift);
		}
		
		GiftDBTO gift1 = gifts.get(0);
		assertThat(gift1.getId(), is(equalTo(1L)));
		assertThat(gift1.getLiked(), is(equalTo(Boolean.TRUE)));
		assertThat(gift1.getTitle(), is("title_1"));
		assertThat(gift1.getDescription(), is("description_1"));
		assertThat(gift1.getUuid(), is("f6aa4067-5b21-4d98-b172-307b557187f0"));
		assertThat(gift1.getUri(), is("http://www.url1.it"));
		assertThat(gift1.getStatus(), is("active"));
		assertThat(gift1.getUser(), is(notNullValue()));
		assertThat(gift1.getUser().getId(), is(1L));
		assertThat(gift1.getUser().getUsername(), is("luca"));
		assertThat(gift1.getUser().getEmail(), is("luca.martellucci@gmail.com"));
	}
    
    @Test
    public final void testFindAllByUserIdPaged() throws Exception {
    	
    	executeSqlScript("file:src/test/resources/db/gift.prepareDB.sql", false);
    	final long userId = 1L;
    	PageRequest pageable = new PageRequest(0, 2);
		Page<GiftDBTO> pagedGifts = repo.findAllByUserId(userId, pageable);
		List<GiftDBTO> gifts = pagedGifts.getContent();
		
    	assertThat(gifts.size(), is(equalTo(2)));

    	GiftDBTO gift1 = gifts.get(0);
    	assertThat(gift1.getId(), is(equalTo(1L)));
    	assertThat(gift1.getLiked(), is(equalTo(Boolean.TRUE)));
    	
    	for (GiftDBTO gift : gifts) {
			logger.info("Gift is: {}", gift);
		}
    	
    	pageable = new PageRequest(1, 2);
		pagedGifts = repo.findAllByUserId(userId, pageable);
		
		gifts = pagedGifts.getContent();
    	assertThat(gifts.size(), is(equalTo(1)));

		
    }

}
