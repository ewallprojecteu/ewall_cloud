package eu.ewall.servicebrick.dailyfunctioning.model;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.ewall.servicebrick.common.model.SensorEvent;

/**
 * 
 */
@Document(collection="visualSensingEvents")
public class VisualSensingEvent extends SensorEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private double tracked_faces;
	
	public VisualSensingEvent() {
		// TODO Auto-generated constructor stub
	}

	public VisualSensingEvent(DateTime timestamp, double tracked_faces) {
		this(null, timestamp, tracked_faces);
	}
	
	public VisualSensingEvent(String username, DateTime timestamp, double tracked_faces) {
		this(null, timestamp, tracked_faces, null);
	}

	public VisualSensingEvent(String username, DateTime timestamp, double tracked_faces, String location) {
		this.username = username;
		this.timestamp = timestamp;
		this.tracked_faces = tracked_faces;
		this.location=location;
	}

	public double getTrackedFaces() {
		return tracked_faces;
	}

	public void setTrackedFaces(double tracked_faces) {
		this.tracked_faces = tracked_faces;
	}

	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();	
		sb.append("timestamp: "  + timestamp.toDate() + ", username: " + username + ", tracked_faces: "  + tracked_faces + ", location: " + location);
		return sb.toString();
	}
}
