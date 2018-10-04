package com.bridgelabz.fundoo.user.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {


	public void createFolder(String bucketName, String folderName);

	void deleteFolder(String folderName);

	void deleteFile(String folderName, String deleteFileName);

	String getFile(String folderName, String fileName);

	void uploadFile(String folderName, MultipartFile fileLocation);
}
