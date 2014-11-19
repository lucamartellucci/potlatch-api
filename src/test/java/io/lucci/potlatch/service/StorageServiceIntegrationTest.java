package io.lucci.potlatch.service;

import static org.junit.Assert.assertNotNull;
import io.lucci.potlatch.service.StorageService.StorageAction;
import io.lucci.potlatch.spring.PersistenceConfig;
import io.lucci.potlatch.spring.ServiceConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@ActiveProfiles(profiles = { "db-test-mysql" })
@ContextConfiguration(classes = { PersistenceConfig.class, ServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
public class StorageServiceIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	final static Logger logger = LoggerFactory.getLogger(StorageServiceIntegrationTest.class);

    @Autowired
    private StorageService storageService;

    @Test
    public final void testGeneratePresignedURL() throws Exception {
    	URL url = storageService.generatePresignedURL(StorageAction.WRITE, "test-uuid");
    	logger.info("The presigned url is: {}", url);
    	assertNotNull(url);
    	uploadObject(url);
    }
    
    private void uploadObject(URL url) throws IOException {
		HttpURLConnection connection=(HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("PUT");
		FileInputStream in = new FileInputStream(new File(this.getClass().getResource("/images/lollipop.jpg").getFile()));
		OutputStream out = connection.getOutputStream();
	    IOUtils.copy(in, connection.getOutputStream());
	    out.flush();
	    out.close();
		int responseCode = connection.getResponseCode();
		logger.info("Service returned response code [{}]", responseCode);
	}

}
