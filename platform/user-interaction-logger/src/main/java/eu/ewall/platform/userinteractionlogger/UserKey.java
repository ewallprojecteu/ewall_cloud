package eu.ewall.platform.userinteractionlogger;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;

public class UserKey extends AbstractDatabaseObject {
	@DatabaseField(value=DatabaseType.STRING, index=true)
	private String user;

	@DatabaseField(value=DatabaseType.STRING)
	private String key;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
