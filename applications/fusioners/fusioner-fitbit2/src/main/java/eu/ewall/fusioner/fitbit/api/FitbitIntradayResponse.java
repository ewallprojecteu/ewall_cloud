package eu.ewall.fusioner.fitbit.api;

import java.util.List;

import eu.ewall.platform.idss.utils.json.JsonObject;

public class FitbitIntradayResponse extends JsonObject {
	private FitbitDaySample dayData;
	private List<FitbitIntradaySample> intradayData;

	public FitbitDaySample getDayData() {
		return dayData;
	}

	public void setDayData(FitbitDaySample dayData) {
		this.dayData = dayData;
	}

	public List<FitbitIntradaySample> getIntradayData() {
		return intradayData;
	}

	public void setIntradayData(List<FitbitIntradaySample> intradayData) {
		this.intradayData = intradayData;
	}
}
