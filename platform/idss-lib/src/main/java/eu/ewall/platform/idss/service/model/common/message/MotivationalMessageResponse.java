package eu.ewall.platform.idss.service.model.common.message;

import eu.ewall.platform.idss.service.model.common.ServiceResponse;

public class MotivationalMessageResponse
extends ServiceResponse<PhysicalActivityMotivationalMessage> {
	public MotivationalMessageResponse() {
	}

	public MotivationalMessageResponse(PhysicalActivityMotivationalMessage value) {
		setValue(value);
	}
}
