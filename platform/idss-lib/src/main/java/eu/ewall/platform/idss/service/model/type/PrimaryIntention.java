package eu.ewall.platform.idss.service.model.type;

import eu.ewall.platform.idss.dao.DatabaseObject;

public enum PrimaryIntention implements DatabaseObject {
	NEUTRAL,
	ENCOURAGE,
	DISCOURAGE,
	WARNING;
	
	private String id;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;		
	}

}
