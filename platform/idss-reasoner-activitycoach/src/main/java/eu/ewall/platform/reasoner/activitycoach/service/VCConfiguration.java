package eu.ewall.platform.reasoner.activitycoach.service;

/**
 * Global Configuration class for the Virtual Coach service.
 * 
 * @author Harm op den Akker (Roessingh Research and Development)
 */
public class VCConfiguration {

	private String serviceBaseUrl;
	private String idssCoreUrl;
	private String profilingServerUrl;
	private String eWalletUrl;
	
	private String mongoDBHost;
	private int mongoDBPort;
	private String mongoDBName;
	
	private boolean smartTimingEnabled;
	private int pammExpirationMinutes;
	private int dialogueExpirationMinutes;
	private double allowedGoalDeviation;
	private double extremeGoalDeviation;
	
	private String defaultDayStartTime;
	private String defaultDayEndTime;
	
	private int fixedTimingPAMMIntervalMinutes;
	
	private int interactionReminderMinimumAllowedMinutes;
	private int interactionReminderMaximumAllowedMinutes;
	private double interactionReminderCoolDownFactor;
	
	private int attributeUpdateTime;
	
	private long maximumAllowedPollingFrequency;
	
	// ----------------------------- //
	// ---------- GETTERS ---------- //
	// ----------------------------- //
	
	public String getServiceBaseUrl() {
		return serviceBaseUrl;
	}
	
	public String getIDSSCoreUrl() {
		return idssCoreUrl;
	}
	
	public String getProfilingServerUrl() {
		return profilingServerUrl;
	}
	
	public String getEWalletUrl() {
		return eWalletUrl;
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
	
	public boolean getSmartTimingEnabled() {
		return smartTimingEnabled;
	}
	
	public int getPAMMExpirationMinutes() {
		return pammExpirationMinutes;
	}
	
	public int getDialogueExpirationMinutes() {
		return dialogueExpirationMinutes;
	}
	
	public String getDefaultDayStartTime() {
		return defaultDayStartTime;
	}
	
	public String getDefaultDayEndTime() {
		return defaultDayEndTime;
	}
	
	public int getFixedTimingPAMMIntervalMinutes() {
		return fixedTimingPAMMIntervalMinutes;
	}
	
	public double getAllowedGoalDeviation() {
		return allowedGoalDeviation;
	}
	
	public double getExtremeGoalDeviation() {
		return extremeGoalDeviation;
	}
	
	public int getInteractionReminderMinimumAllowedMinutes() {
		return interactionReminderMinimumAllowedMinutes;
	}
	
	public int getInteractionReminderMaximumAllowedMinutes() {
		return interactionReminderMaximumAllowedMinutes;
	}
	
	public double getInteractionReminderCoolDownFactor() {
		return interactionReminderCoolDownFactor;
	}
	
	public int getAttributeUpdateTime() {
		return attributeUpdateTime;
	}
	
	public long getMaximumAllowedPollingFrequency() {
		return maximumAllowedPollingFrequency;
	}
	
	// ----------------------------- //
	// ---------- SETTERS ---------- //
	// ----------------------------- //
	
	public void setServiceBaseUrl(String serviceBaseUrl) {
		this.serviceBaseUrl = serviceBaseUrl;
	}
	
	public void setIDSSCoreUrl(String idssCoreUrl) {
		this.idssCoreUrl = idssCoreUrl;
	}
	
	public void setProfilingServerUrl(String profilingServerUrl) {
		this.profilingServerUrl = profilingServerUrl;
	}
	
	public void setEWalletUrl(String eWalletUrl) {
		this.eWalletUrl = eWalletUrl;
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
	
	public void setSmartTimingEnabled(boolean smartTimingEnabled) {
		this.smartTimingEnabled = smartTimingEnabled;
	}
	
	public void setPAMMExpirationMinutes(int pammExpirationMinutes) {
		this.pammExpirationMinutes = pammExpirationMinutes;
	}
	
	public void setDialogueExpirationMinutes(int dialogueExpirationMinutes) {
		this.dialogueExpirationMinutes = dialogueExpirationMinutes;
	}
	
	public void setDefaultDayStartTime(String defaultDayStartTime) {
		this.defaultDayStartTime = defaultDayStartTime;
	}
	
	public void setDefaultDayEndTime(String defaultDayEndTime) {
		this.defaultDayEndTime = defaultDayEndTime;
	}
	
	public void setFixedTimingPAMMIntervalMinutes(int fixedTimingPAMMIntervalMinutes) {
		this.fixedTimingPAMMIntervalMinutes = fixedTimingPAMMIntervalMinutes;
	}
	
	public void setAllowedGoalDeviation(double allowedGoalDeviation) {
		this.allowedGoalDeviation = allowedGoalDeviation;
	}
	
	public void setExtremeGoalDeviation(double extremeGoalDeviation) {
		this.extremeGoalDeviation = extremeGoalDeviation;
	}
	
	public void setInteractionReminderMinimumAllowedMinutes(int interactionReminderMinimumAllowedMinutes) {
		this.interactionReminderMinimumAllowedMinutes = interactionReminderMinimumAllowedMinutes;
		
	}
	
	public void setInteractionReminderMaximumAllowedMinutes(int interactionReminderMaximumAllowedMinutes) {
		this.interactionReminderMaximumAllowedMinutes = interactionReminderMaximumAllowedMinutes;
	}
	
	public void setInteractionReminderCoolDownFactor(double interactionReminderCoolDownFactor) {
		this.interactionReminderCoolDownFactor = interactionReminderCoolDownFactor;		
	}
	
	public void setAttributeUpdateTime(int attributeUpdateTime) {
		this.attributeUpdateTime = attributeUpdateTime;
	}
	
	public void setMaximumAllowedPollingFrequency(long maximumAllowedPollingFrequency) {
		this.maximumAllowedPollingFrequency = maximumAllowedPollingFrequency;
	}
}