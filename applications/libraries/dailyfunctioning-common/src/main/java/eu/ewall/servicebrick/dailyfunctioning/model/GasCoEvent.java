package eu.ewall.servicebrick.dailyfunctioning.model;

import org.joda.time.DateTime;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="gasCoEvents")
public class GasCoEvent extends GasEvent {

	private static final long serialVersionUID = 1L;

	@PersistenceConstructor
	public GasCoEvent(String username, DateTime timestamp, double gasValue,	String location) {
		super(username, timestamp, gasValue, location);
	}
	
}
