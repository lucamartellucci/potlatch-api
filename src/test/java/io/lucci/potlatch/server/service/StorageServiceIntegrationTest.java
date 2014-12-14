package io.lucci.potlatch.server.service;

import static org.junit.Assert.assertNotNull;
import io.lucci.potlatch.server.config.PersistenceConfig;
import io.lucci.potlatch.server.config.ServiceConfig;
import io.lucci.potlatch.server.service.StorageService;
import io.lucci.potlatch.server.service.StorageService.StorageAction;

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

@ActiveProfiles(profiles = { "test" })
@ContextConfiguration(classes = { PersistenceConfig.class, ServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
public class StorageServiceIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	final static Logger logger = LoggerFactory.getLogger(StorageServiceIntegrationTest.class);

    @Autowired
    private StorageService storageService;

    @Test
    public void testGeneratePresignedURL() throws Exception {
    	URL url = storageService.prepareUrl(StorageAction.WRITE, "test-uuid");
    	logger.info("The presigned url is: {}", url);
    	assertNotNull(url);
    	uploadObject(url);
    }
    
    @Test
    public void testSaveObject() throws Exception {
    	FileInputStream in = new FileInputStream(new File(this.getClass().getResource("/images/lollipop.jpg").getFile()));
    	storageService.storeObject(in, "obj"+System.currentTimeMillis());
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
