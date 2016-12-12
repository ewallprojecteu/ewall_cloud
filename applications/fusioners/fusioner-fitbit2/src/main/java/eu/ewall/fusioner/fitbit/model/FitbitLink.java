package eu.ewall.fusioner.fitbit.model;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;

public class FitbitLink extends AbstractDatabaseObject {
	
	@DatabaseField(value=DatabaseType.STRING)
	private String user;

	@DatabaseField(value=DatabaseType.TEXT)
	private String accessToken;

	@DatabaseField(value=DatabaseType.LONG)
	private long expires;

	@DatabaseField(value=DatabaseType.STRING)
	private String refreshToken;

	@DatabaseField(value=DatabaseType.STRING)
	private String tokenType;
	
	@DatabaseField(value=DatabaseType.LONG)
	private long linkTime;
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public long getExpires() {
		return expires;
	}

	public void setExpires(long expires) {
		this.expires = expires;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public long getLinkTime() {
		return linkTime;
	}

	public void setLinkTime(long linkTime) {
		this.linkTime = linkTime;
	}
}
