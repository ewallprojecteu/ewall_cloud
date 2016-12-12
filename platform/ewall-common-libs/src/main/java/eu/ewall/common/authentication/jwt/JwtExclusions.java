package eu.ewall.common.authentication.jwt;

import java.util.List;

public class JwtExclusions {
	
	private List<String> exclusions;
	
	public JwtExclusions() {
	}
	
	public JwtExclusions(List<String> exclusions) {
		this.exclusions = exclusions;
	}
	
	public void setExclusions(List<String> exclusions){
		this.exclusions = exclusions;
	}
	
	public void addExclusion(String exclusion){
		this.exclusions.add(exclusion);
	}
	
	public void addExclusions(List<String> exclusions){
		this.exclusions.addAll(exclusions);
	}

	public List<String> getExclusions(){
		return this.exclusions;
	}
}
