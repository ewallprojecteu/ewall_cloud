package eu.ewall.platform.lr.mood.service;

import java.util.List;

import eu.ewall.platform.lr.mood.MoodDataItem;

public class Valence {
	private float valence;
	private int minusDays;
	private boolean empty;
	
	public Valence(List<MoodDataItem> data, int minusDays) {
		float valence =0f;
		
		if(data != null && !data.isEmpty()) {
			for(MoodDataItem item : data) {
				valence += item.getValence();
			}
			valence /= data.size();
			this.valence = valence;
			this.empty = false;
		} else {
			this.valence = 0f;
			this.empty = true;
		}
		this.minusDays = minusDays;
		
	}
	
	public int getMinusDays() {
		return minusDays;
	}
	public void setMinusDays(int minusDays) {
		this.minusDays = minusDays;
	}
	public boolean isEmpty() {
		return empty;
	}
	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	public float getValence() {
		return valence;
	}

	public void setValence(float valence) {
		this.valence = valence;
	}
}
