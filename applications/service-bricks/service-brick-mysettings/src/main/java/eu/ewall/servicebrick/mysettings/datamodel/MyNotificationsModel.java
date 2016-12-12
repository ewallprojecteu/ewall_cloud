package eu.ewall.servicebrick.mysettings.datamodel;

/**
 * The Class SharingDataModel.
 */
public class MyNotificationsModel  {

    private String username;
	private Integer healthNotifications;
	private Integer houseNotifications;
	private Integer medicalNotifications;

	public MyNotificationsModel(String username) {
		this.username=username;
	}
	
	public MyNotificationsModel(String username,Integer healthNotifications, Integer houseNotifications, Integer medicalNotifications) {
		this.username=username;
		this.healthNotifications=healthNotifications;
		this.houseNotifications=houseNotifications;
		this.medicalNotifications=medicalNotifications;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public Integer getHealthNotifications() {
		return healthNotifications;
	}

	public void setHealthNotifications(Integer healthNotifications) {
		this.healthNotifications = healthNotifications;
	}
	
	public Integer getHouseNotifications() {
		return houseNotifications;
	}

	public void setHouseNotifications(Integer houseNotifications) {
		this.houseNotifications = houseNotifications;
	}
	
	public Integer getMedicalNotifications() {
		return medicalNotifications;
	}

	public void setMedicalNotifications(Integer medicalNotifications) {
		this.medicalNotifications = medicalNotifications;
	}
	
}
