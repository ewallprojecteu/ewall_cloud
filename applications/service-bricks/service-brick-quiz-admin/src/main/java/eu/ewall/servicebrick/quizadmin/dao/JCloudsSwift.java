package eu.ewall.servicebrick.quizadmin.dao;

import static org.jclouds.io.Payloads.newByteArrayPayload;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.jclouds.ContextBuilder;
import org.jclouds.io.Payload;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.openstack.swift.v1.SwiftApi;
import org.jclouds.openstack.swift.v1.domain.Container;
import org.jclouds.openstack.swift.v1.domain.ObjectList;
import org.jclouds.openstack.swift.v1.domain.SwiftObject;
import org.jclouds.openstack.swift.v1.features.ContainerApi;
import org.jclouds.openstack.swift.v1.features.ObjectApi;
import org.jclouds.openstack.swift.v1.options.PutOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.io.Closeables;
import com.google.inject.Module;

@Component
@Profile("!prod-aws")
public class JCloudsSwift implements ObjectStorage{
	
	private SwiftApi swiftApi;
	
	private static final Logger log = LoggerFactory.getLogger(JCloudsSwift.class);

	@Value("${swift.containerName}")
	private String containerName;

	@Value("${objectStorage.endpoint}")
	private String endpoint;
	
	@Value("${swift.identity}")
	private String identity;
	
	@Value("${swift.credential}")
	String credential;
	
	@Value("${keystone.endpoint}")
	String keystone;
	
	@PostConstruct
	public void init() {
		Iterable<Module> modules = ImmutableSet.<Module>of(
				new SLF4JLoggingModule());

		String provider = "openstack-swift";
		swiftApi = ContextBuilder.newBuilder(provider)
				.endpoint(keystone)
				.credentials(identity, credential)
				.modules(modules)
				.buildApi(SwiftApi.class);
		log.info("Using OpenStack Swift as Object Storage");
	}

	public void uploadImage(String username, MultipartFile image, String imageName) throws IOException {
		String fileName = username + "/" + imageName;
		log.info("Uploading " + imageName + " image for user " + username);

		Multimap<String, String> headers = ArrayListMultimap.create();
		headers.put("Content-Type", "image/jpeg");

		ObjectApi objectApi = swiftApi.getObjectApi("regionOne", containerName);
		Payload payload = newByteArrayPayload(image.getBytes());
		objectApi.put(fileName, payload, PutOptions.Builder.headers(headers));
		System.out.println("  " + fileName);
	}

	public InputStream getImage(String username, String imageName) throws IOException{
		String fileName = username + "/" + imageName;

		ObjectApi objectApi = swiftApi.getObjectApi("regionOne", containerName);
		SwiftObject swiftObject = objectApi.get(fileName);
		InputStream is = swiftObject.getPayload().openStream();
		log.info("Downloading image " + imageName + " for user " + username);
		return is;
	}

	public List<String> getImageList(String username){
		ObjectApi objectApi = swiftApi.getObjectApi("regionOne", containerName);
		ObjectList items = objectApi.list();
		String str = null;
		List<String> urls = new ArrayList<String>();
		for(Object item : items){
			str = item.toString();	    	  
			String user = str.substring(17, 17 + username.length());
			String fileName = str.substring(18 + username.length(), str.indexOf(", uri"));
			if ((user.equals(username)) && (!fileName.endsWith("icon"))){
				String path = endpoint + username + "/images/" + fileName;
				urls.add(path);
			}
		}
		log.info("Found " + urls.size() + " images for user " + username);
		return urls;
	}

	public void listContainers() {
		System.out.println("  List Containers");

		ContainerApi containerApi = swiftApi.getContainerApi("regionOne");
		Set<Container> containers = containerApi.list().toSet();

		for (Container container : containers) {
			System.out.println("  " + container);
		}
	}

	public void deleteImage(String username, String imageName) throws IOException{
		String fileName = username + "/" + imageName;

		ObjectApi objectApi = swiftApi.getObjectApi("regionOne", containerName);
		objectApi.delete(fileName);
		log.info("Deleting " + imageName + " for user " + username);
		return;
	}

	public void close() throws IOException {
		Closeables.close(swiftApi, true);
	}
}