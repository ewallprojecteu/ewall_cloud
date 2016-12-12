package eu.ewall.platform.commons.datamodel.ewallsystem;

public class HeartbeatStatus {
	
	private Boolean onlineStatus;
	private Heartbeat lastHeartbeat;
	
	public HeartbeatStatus() {
		// TODO Auto-generated constructor stub
	}
	
	public HeartbeatStatus(Boolean onlineStatus, Heartbeat lastHeartbeat) {
		this.lastHeartbeat = lastHeartbeat;
		this.onlineStatus = onlineStatus;
	}
	
	public Boolean getOnlineStatus() {
		return onlineStatus;
	}
	public void setOnlineStatus(Boolean onlineStatus) {
		this.onlineStatus = onlineStatus;
	}
	public Heartbeat getLastHeartbeat() {
		return lastHeartbeat;
	}
	public void setLastHeartbeat(Heartbeat lastHeartbeat) {
		this.lastHeartbeat = lastHeartbeat;
	}
	
	

}
