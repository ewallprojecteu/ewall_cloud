package eu.ewall.servicebrick.mysettings.datamodel;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class SharingDataModel.
 */

public class SharingData {

	
    private String username;
	private Boolean sharingFamily;
	private Boolean sharingGP;
    private List<String> sharingCaregivers = new ArrayList<String>();
    private List<String> caregiverNames = new ArrayList<String>();
	
	public SharingData(String username) {
		this.username=username;
	}
	
	public SharingData(String username, Boolean sharingFamily, Boolean sharingGP) {
		this.username=username;
		this.sharingFamily=sharingFamily;
		this.sharingGP=sharingGP;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public Boolean getSharingFamily() {
		return sharingFamily;
	}

	public void setSharingFamily(Boolean sharingFamily) {
		this.sharingFamily = sharingFamily;
	}

	public Boolean getSharingGP() {
		return sharingGP;
	}

	public void setSharingGP(Boolean sharingGP) {
		this.sharingGP = sharingGP;
	}
	
	public List< String > getSharingCaregivers ( )
	{
	    return sharingCaregivers;
	}
	
	
	public List< String > getCaregiverNames ( )
	{
	    return caregiverNames;
	}
	
	public void setSharingCaregivers(List<String> sharingCaregivers)
	{
		this.sharingCaregivers=sharingCaregivers;
	}
	
	public void setCaregiverNames(List<String> caregiverNames)
	{
		this.caregiverNames=caregiverNames;
	}

}
