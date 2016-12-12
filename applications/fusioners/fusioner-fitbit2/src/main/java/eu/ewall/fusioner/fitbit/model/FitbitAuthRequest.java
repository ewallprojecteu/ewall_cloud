package eu.ewall.fusioner.fitbit.model;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;

public class FitbitAuthRequest extends AbstractDatabaseObject {
	@DatabaseField(value=DatabaseType.STRING)
	private String user;

	@DatabaseField(value=DatabaseType.STRING)
	private String stateCode;

	@DatabaseField(value=DatabaseType.LONG)
	private long requestTime;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public long getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
	}
}
