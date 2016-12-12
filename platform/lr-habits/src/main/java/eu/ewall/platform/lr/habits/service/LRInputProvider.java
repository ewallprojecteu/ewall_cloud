package eu.ewall.platform.lr.habits.service;

import java.io.IOException;
import java.util.List;

import org.joda.time.DateTime;

import eu.ewall.platform.idss.service.RemoteMethodException;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.lr.habits.model.FunctioningActivityHistory;

public interface LRInputProvider {

	List<IDSSUserProfile> getUsers()
			throws IOException, RemoteMethodException, Exception;
	
	FunctioningActivityHistory getFunctioningActivityData(/*IDSSUserProfile*/ String user, DateTime from, DateTime to)
			throws IOException, RemoteMethodException, Exception;
}
