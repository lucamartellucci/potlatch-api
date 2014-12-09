package io.lucci.potlatch.server.service;

import java.io.InputStream;
import java.net.URL;

public interface StorageService {
	
	public enum StorageAction {
		READ, WRITE
	}
	
	public URL prepareUrl(StorageAction action, String objectName) throws StorageServiceException;	
	
	public void storeObject(InputStream data, String objectName) throws StorageServiceException;
	
	public InputStream readObject(String objectName) throws StorageServiceException;

}