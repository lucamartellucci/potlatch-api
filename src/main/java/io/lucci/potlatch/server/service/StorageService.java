package io.lucci.potlatch.server.service;

import java.io.InputStream;
import java.net.URL;

public interface StorageService {
	
	public enum StorageAction {
		READ, WRITE
	}
	
	public URL buildGiftUrl(StorageAction action, String uuid) throws StorageServiceException;	
	
	public void storeGiftData(InputStream data, String uuid) throws StorageServiceException;
	
	public InputStream loadGiftData(String uuid) throws StorageServiceException;

}
