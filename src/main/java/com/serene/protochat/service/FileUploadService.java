package com.serene.protochat.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class FileUploadService {

	public void saveFile(String directory, String fileName,  MultipartFile file) {
		
		try {
			
			Path uploadPath = Paths.get(directory);
			
			if(!Files.exists(uploadPath)) {
				
				Files.createDirectories(uploadPath);
				
			}
			
			Path filepath = uploadPath.resolve(fileName);
			
			file.transferTo(filepath);
			
			this.createThumbnail(directory,fileName , file );
				
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private void createThumbnail(String directory, String fileName, MultipartFile file) throws IOException {
		
		Path thumbnailPath = Paths.get(directory + "/thumbnails");
		
		if(!Files.exists(thumbnailPath)) {
			
			Files.createDirectories(thumbnailPath);
			
		}
		
		Path thumbnailFilePath = thumbnailPath.resolve(fileName);
	
		if(!Files.exists(thumbnailPath)) {
			
			Files.createFile(thumbnailPath);
		}
		
		Thumbnails.of(file.getInputStream())
		.size(200, 200)
		.toFile(thumbnailFilePath.toFile());
		
		
	}
	
	public void removeDisplayPicture(String directory,String fileName) {
		
		try {
			
			Path displayPicturePath = Paths.get(directory + "/" + fileName);
			
			Path thumbnailPath = Paths.get(directory + "/thumbnails/" + fileName);
			
			Files.deleteIfExists(displayPicturePath);
			
			Files.deleteIfExists(thumbnailPath);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	

	
	
}
