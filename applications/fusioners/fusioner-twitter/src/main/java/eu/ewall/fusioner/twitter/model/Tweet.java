package eu.ewall.fusioner.twitter.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tweet {

	private final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy.");

	private String username;
	private String text;
	private Date createdAt;

	@Override
	public String toString() {
		return String.format("Text: %s | created at: %s | by username: %s", text,
				SDF.format(createdAt), username);
	}

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
}
