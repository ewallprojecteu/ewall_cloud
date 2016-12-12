package eu.ewall.platform.idss.service.model.common;

import java.util.List;

public class MoodDataResponse extends ServiceResponse<List<MoodData>>{
	public MoodDataResponse() {
	}

	public MoodDataResponse(List<MoodData> value) {
		setValue(value);
	}
}
