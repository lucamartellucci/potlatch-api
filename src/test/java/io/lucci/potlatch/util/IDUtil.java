package io.lucci.potlatch.util;

import java.util.Random;
import java.util.UUID;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class IDUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(IDUtil.class);


    public final static String randomPositiveLongAsString() {
        return Long.toString(randomPositiveLong());
    }

    public final static String randomNegativeLongAsString() {
        return Long.toString(randomNegativeLong());
    }

    public final static long randomPositiveLong() {
        long id = new Random().nextLong() * 10000;
        id = (id < 0) ? (-1 * id) : id;
        return id;
    }

    public final static long randomNegativeLong() {
        long id = new Random().nextLong() * 10000;
        id = (id > 0) ? (-1 * id) : id;
        return id;
    }
    
    @Test
    public void generateUUID(){
    	for (int i = 0; i < 100; i++) {
    		logger.info("{}",UUID.randomUUID().toString());
		}
    }

}
