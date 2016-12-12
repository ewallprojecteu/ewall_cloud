package eu.ewall.servicebrick.quizadmin.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ObjectStorage {
	public void init();
	public void uploadImage(String username, MultipartFile image, String imageName) throws IOException;
	public InputStream getImage(String username, String imageName) throws IOException;
	public List<String> getImageList(String username);
	public void listContainers();
	public void deleteImage(String username, String imageName) throws IOException;
	public void close() throws IOException;

}
