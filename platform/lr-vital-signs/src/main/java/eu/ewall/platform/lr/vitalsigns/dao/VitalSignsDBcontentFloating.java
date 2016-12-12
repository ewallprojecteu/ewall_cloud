package eu.ewall.platform.lr.vitalsigns.dao;

public class VitalSignsDBcontentFloating extends VitalSignsDBcontent
{
	
	private int counterBP;
	
	private int counterHR;
	
	private int counterOS;
	
	public int getCounterBP() {
		return counterBP;
	}
	
	public int getCounterHR() {
		return counterHR;
	}
	
	public int getCounterOS() {
		return counterOS;
	}
	
	public void setCounterBP(int cnt) {
		this.counterBP = cnt;
	}
	
	public void setCounterHR(int cnt) {
		this.counterHR = cnt;
	}
	
	public void setCounterOS(int cnt) {
		this.counterOS = cnt;
	}

}
