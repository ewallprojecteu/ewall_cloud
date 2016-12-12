package eu.ewall.servicebrick.quizadmin.dao;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Component
@Profile("prod-aws")
public class AmazonS3 implements Closeable, ObjectStorage {
	
	private AmazonS3Client s3;
	
	private static final Logger log = LoggerFactory.getLogger(AmazonS3.class);
	
	@Value("${objectStorage.endpoint}")
	private String endpoint;
	
	@Value("${amazon.accessKeyId}")
	private String accessKeyId;

	@Value("${amazon.secretAccessKey}")
	private String secret_access_key;
	
	@Value("${amazon.bucketName}")
	private String bucketName;
	
	@PostConstruct
	public void init() {
		log.info("Using Amazon S3 as Object Storage");
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKeyId, secret_access_key);
		s3 = new AmazonS3Client(awsCreds);
		s3.setEndpoint("s3-eu-central-1.amazonaws.com");
			    
	}
	
	
	public void uploadImage(String username, MultipartFile image, String imageName) throws IOException {
		String fileName = username + "/" + imageName;
		try {
			log.info("Uploading " + imageName + " image for user " + username);
			byte [] byteArr = image.getBytes();
			InputStream stream = new ByteArrayInputStream(byteArr);
			
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType(image.getContentType());
			objectMetadata.setContentLength(byteArr.length);
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, stream, objectMetadata);
			s3.putObject(putObjectRequest);
			
		} catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException");
            System.out.println("Error Message: " + ace.getMessage());
        }
		
	}

	
	public InputStream getImage(String username, String imageName) throws IOException{
		String fileName = username + "/" + imageName;
		S3Object s3object = s3.getObject(new GetObjectRequest(bucketName, fileName));
		InputStream is = (InputStream) s3object.getObjectContent();
		log.info("Downloading image " + imageName + " for user " + username);
		return is;
	}

	
	public List<String> getImageList(String username){
		List<String> urls = new ArrayList<String>();
        ObjectListing objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix(username));
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            String str = objectSummary.getKey().toString();
            String fileName = str.substring(str.indexOf("/") + 1);
    		String path = endpoint + username + "/images/" + fileName;
    		if ((!fileName.equals("")) && (!fileName.endsWith("icon"))){ 
    			urls.add(path);
    		}
        }
        log.info("Found " + urls.size() + " images for user " + username);
		return urls;
	}
	

	public void listContainers() {
		 System.out.println("  Listing buckets");
         for (Bucket bucket : s3.listBuckets()) {
             System.out.println(" - " + bucket.getName());
         }
         System.out.println();
	}

	
	public void deleteImage(String username, String imageName) throws IOException{
		String fileName = username + "/" + imageName;
		s3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
		log.info("Deleting " + imageName + " for user " + username);
	}
	

	public void close() throws IOException {
		
	}
}