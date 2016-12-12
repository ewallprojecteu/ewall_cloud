package eu.ewall.servicebrick.socializingmood.model;



import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitterResponse {

	@JsonProperty("username")
	private String username;
	
	@JsonProperty("text")
	private String text;
	
	@JsonProperty("createdAt")
	private Date createdAt;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public String toString() {
		return username+" "+createdAt+" "+text;
	}

}
