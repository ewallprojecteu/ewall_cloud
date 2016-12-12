package eu.ewall.platform.lr.environment.service;

import java.io.IOException;
import java.util.List;

import org.joda.time.DateTime;

import eu.ewall.platform.idss.service.RemoteMethodException;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.lr.environment.model.IlluminanceHistory;

public interface LRInputProvider {

	List<IDSSUserProfile> getUsers()
			throws IOException, RemoteMethodException, Exception;
	
	IlluminanceHistory getIlluminanceData(IDSSUserProfile user, DateTime from, DateTime to, String location)
			throws IOException, RemoteMethodException, Exception;
}
