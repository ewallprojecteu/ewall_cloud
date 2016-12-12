package eu.ewall.platform.reasoner.sleep.service;

import java.util.List;

import eu.ewall.platform.idss.service.model.common.ServiceResponse;

public class PSQIDataResponse extends ServiceResponse<List<PSQIDataOutput>>{
	public PSQIDataResponse() {
	}

	public PSQIDataResponse(List<PSQIDataOutput> value) {
		setValue(value);
	}

}
