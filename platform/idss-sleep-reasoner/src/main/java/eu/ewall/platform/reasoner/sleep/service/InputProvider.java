package eu.ewall.platform.reasoner.sleep.service;

import java.io.IOException;
import java.util.List;

import org.joda.time.LocalDate;

import eu.ewall.platform.idss.service.RemoteMethodException;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.reasoner.sleep.InbedDataResponse;
import eu.ewall.platform.reasoner.sleep.InactivityDataResponse;

public interface InputProvider {
	
	List<IDSSUserProfile> getUsers()
			throws IOException, RemoteMethodException, Exception;
	
	InactivityDataResponse getInactivity(IDSSUserProfile user, LocalDate date)
			throws IOException, RemoteMethodException, Exception;
	
	InbedDataResponse getInbed(IDSSUserProfile user, LocalDate date)
			throws IOException, RemoteMethodException, Exception;
}
