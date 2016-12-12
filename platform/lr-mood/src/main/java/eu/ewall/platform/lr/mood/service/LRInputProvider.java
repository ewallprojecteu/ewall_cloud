package eu.ewall.platform.lr.mood.service;

import java.io.IOException;
import java.util.List;

import org.joda.time.LocalDate;

import eu.ewall.platform.idss.service.RemoteMethodException;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.lr.mood.MoodDataItem;

public interface LRInputProvider {

	List<IDSSUserProfile> getUsers()
			throws IOException, RemoteMethodException, Exception;
	
	List<MoodDataItem> getMoodDataItem(IDSSUserProfile user, LocalDate date)
			throws IOException, RemoteMethodException, Exception;
}
