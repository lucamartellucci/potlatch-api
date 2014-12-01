package io.lucci.potlatch.service;

import io.lucci.potlatch.web.controller.exception.InternalServerErrorException;
import io.lucci.potlatch.web.controller.exception.ResourceNotFoundException;
import io.lucci.potlatch.web.model.Gift;

import java.io.InputStream;

import javax.transaction.Transactional;

public interface GiftManager {

	@Transactional
	public Gift setGiftData(String uuid, InputStream data) throws InternalServerErrorException, ResourceNotFoundException;
}
