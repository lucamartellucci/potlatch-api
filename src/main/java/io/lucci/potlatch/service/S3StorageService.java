package io.lucci.potlatch.service;

import java.io.InputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

@Service
public class S3StorageService implements StorageService {
	
	private static Logger logger = LoggerFactory.getLogger(S3StorageService.class);
	
	@Value("${storage.bucketname}")
	private String bucketName;
	
	@Value("${storage.accesskey}")
	private String accessKeyId;
	
	@Value("${storage.secretkey}")
	private String secretAccessKey;
	
	
	@Override
	public URL prepareUrl(StorageAction action, String objectName) throws StorageServiceException {
		
		AmazonS3 s3client = new AmazonS3Client(new BasicAWSCredentials(accessKeyId, secretAccessKey));
		
		try {
			logger.info("Generating pre-signed URL for gift [{}]", objectName);
			
			java.util.Date expiration = new java.util.Date();
			long milliSeconds = expiration.getTime();
			milliSeconds += 1000 * 60 * 60; // Add 1 hour.
			expiration.setTime(milliSeconds);
			
			String objectKey = String.format("%s.jpg", objectName);
			
			GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey);
			generatePresignedUrlRequest.setMethod(fromAction(action)); 
			generatePresignedUrlRequest.setExpiration(expiration);

			URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest); 
			return url;
			
		} catch (Exception e) {
			throw new StorageServiceException(new StringBuilder("Unable to generate a presigne URL for object [").append(objectName).append("]").toString(), e);
		}
	}
	
	private HttpMethod fromAction(StorageAction action){
		switch (action) {
		case READ:
			return HttpMethod.GET;
		case WRITE:
			return HttpMethod.PUT;
		default:
			return HttpMethod.GET;
		}
	}

	@Override
	public void storeObject(InputStream data, String objectName) throws StorageServiceException {
		
		AmazonS3 s3client = new AmazonS3Client(new BasicAWSCredentials(accessKeyId, secretAccessKey));
		try {
			PutObjectRequest request = new PutObjectRequest(bucketName, objectName, data, new ObjectMetadata());
			PutObjectResult response = s3client.putObject(request);
			logger.info("Uploaded object eTag is [{}]",response.getETag());
		} catch (Exception e) {
			throw new StorageServiceException(new StringBuilder("Unable to store object [").append(objectName).append("]").toString(), e);
		}
		
	}

	
	
}
