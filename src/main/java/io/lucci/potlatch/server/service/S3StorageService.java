package io.lucci.potlatch.server.service;

import java.io.InputStream;
import java.net.URI;
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
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

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
	public URI buildGiftUri(StorageAction action, String uuid) throws StorageServiceException {
		
		AmazonS3 s3client = new AmazonS3Client(new BasicAWSCredentials(accessKeyId, secretAccessKey));
		
		try {
			logger.info("Generating pre-signed URL for gift [{}]", uuid);
			
			java.util.Date expiration = new java.util.Date();
			long milliSeconds = expiration.getTime();
			milliSeconds += 1000 * 60 * 60; // Add 1 hour.
			expiration.setTime(milliSeconds);
			
			String objectKey = String.format("%s.jpg", uuid);
			
			GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey);
			generatePresignedUrlRequest.setMethod(fromAction(action)); 
			generatePresignedUrlRequest.setExpiration(expiration);

			URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest); 
			return url.toURI();
			
		} catch (Exception e) {
			throw new StorageServiceException(new StringBuilder("Unable to generate a presigne URL for object [").append(uuid).append("]").toString(), e);
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
	public void storeGiftData(InputStream data, String uuid) throws StorageServiceException {
		
		AmazonS3 s3client = new AmazonS3Client(new BasicAWSCredentials(accessKeyId, secretAccessKey));
		try {
			PutObjectRequest request = new PutObjectRequest(bucketName, uuid, data, new ObjectMetadata());
			PutObjectResult response = s3client.putObject(request);
			logger.info("Uploaded object eTag is [{}]",response.getETag());
		} catch (Exception e) {
			throw new StorageServiceException(new StringBuilder("Unable to store object [").append(uuid).append("]").toString(), e);
		}
		
	}

	@Override
	public InputStream loadGiftData(String uuid) throws StorageServiceException {
		AmazonS3 s3client = new AmazonS3Client(new BasicAWSCredentials(accessKeyId, secretAccessKey));
		try {
			GetObjectRequest request = new GetObjectRequest(bucketName, uuid);
			S3Object object = s3client.getObject(request);
			logger.info("Downloaded object metadata is [{}]",object.getObjectMetadata());
			return object.getObjectContent();
		} catch (Exception e) {
			throw new StorageServiceException(new StringBuilder("Unable to download object [").append(uuid).append("]").toString(), e);
		}
	}

	
	
}
