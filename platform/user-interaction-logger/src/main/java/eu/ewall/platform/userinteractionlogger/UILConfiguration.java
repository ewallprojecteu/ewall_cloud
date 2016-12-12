package eu.ewall.platform.userinteractionlogger;

public class UILConfiguration {
	
	private String profilingServerUrl;
	private String mongoDBHost;
	private int mongoDBPort;
	private String mongoDBName;
	
	// ---------- Getters ---------- //
	
	public String getProfilingServerUrl() {
		return profilingServerUrl;
	}
	
	public String getMongoDBHost() {
		return mongoDBHost;
	}
	
	public int getMongoDBPort() {
		return mongoDBPort;
	}
	
	public String getMongoDBName() {
		return mongoDBName;
	}
	
	// ---------- Setters ---------- //
	
	public void setProfilingServerUrl(String profilingServerUrl) {
		this.profilingServerUrl = profilingServerUrl;
	}
	
	public void setMongoDBHost(String mongoDBHost) {
		this.mongoDBHost = mongoDBHost;
	}
	
	public void setMongoDBPort(int mongoDBPort) {
		this.mongoDBPort = mongoDBPort;
	}
	
	public void setMongoDBName(String mongoDBName) {
		this.mongoDBName = mongoDBName;
	}

}
