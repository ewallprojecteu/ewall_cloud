package eu.ewall.platform.idss.core.service;

public class IDSSCoreConfiguration {
	
	private String serviceBaseUrl;
	private String profilingServerUrl;
	private String automaticGoalSettingUrl;
	private String serviceBrickPhysicalActivityUrl;
	private String serviceBrickWeatherUrl;
	private String lifestyleReasonerSleepUrl;
	private String userInteractionLoggerUrl;
	private String eWalletServiceUrl;
	
	private String mongoDBHost;
	private int mongoDBPort;
	private String mongoDBName;
	
	// ----------------------------- //
	// ---------- GETTERS ---------- //
	// ----------------------------- //
	
	public String getServiceBaseUrl() {
		return serviceBaseUrl;
	}
		
	public String getProfilingServerUrl() {
		return profilingServerUrl;
	}
			
	public String getAutomaticGoalSettingUrl() {
		return automaticGoalSettingUrl;
	}
	
	public String getServiceBrickPhysicalActivityUrl() {
		return serviceBrickPhysicalActivityUrl;
	}
	
	public String getServiceBrickWeatherUrl() {
		return serviceBrickWeatherUrl;
	}
	
	public String getLifestyleReasonerSleepUrl() {
		return lifestyleReasonerSleepUrl;
	}
	
	public String getUserInteractionLoggerUrl() {
		return userInteractionLoggerUrl;
	}
	
	public String getEWalletServiceUrl() {
		return eWalletServiceUrl;
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
	
	
	// ----------------------------- //
	// ---------- SETTERS ---------- //
	// ----------------------------- //
	
	public void setServiceBaseUrl(String serviceBaseUrl) {
		this.serviceBaseUrl = serviceBaseUrl;
	}
	
	public void setProfilingServerUrl(String profilingServerUrl) {
		this.profilingServerUrl = profilingServerUrl;
	}
	
	public void setAutomaticGoalSettingUrl(String automaticGoalSettingUrl) {
		this.automaticGoalSettingUrl = automaticGoalSettingUrl;
	}
	
	public void setServiceBrickPhysicalActivityUrl(String serviceBrickPhysicalActivityUrl) {
		this.serviceBrickPhysicalActivityUrl = serviceBrickPhysicalActivityUrl;
	}
	
	public void setServiceBrickWeatherUrl(String serviceBrickWeatherUrl) {
		this.serviceBrickWeatherUrl = serviceBrickWeatherUrl;
	}
	
	public void setLifestyleReasonerSleepUrl(String lifestyleReasonerSleepUrl) {
		this.lifestyleReasonerSleepUrl = lifestyleReasonerSleepUrl;
	}
	
	public void setUserInteractionLoggerUrl(String userInteractionLoggerUrl) {
		this.userInteractionLoggerUrl = userInteractionLoggerUrl;
	}
	
	public void setEWalletServiceUrl(String eWalletServiceUrl) {
		this.eWalletServiceUrl = eWalletServiceUrl;
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
