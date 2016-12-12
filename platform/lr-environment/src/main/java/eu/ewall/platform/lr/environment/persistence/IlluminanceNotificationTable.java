package eu.ewall.platform.lr.environment.persistence;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.lr.environment.model.IlluminanceNotification;

public class IlluminanceNotificationTable extends DatabaseTableDef<IlluminanceNotification>{
	
	public static final String NAME = "illuminance_notifications";
	private static final int VERSION = 0;

	public IlluminanceNotificationTable() {
		super(NAME, IlluminanceNotification.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db) throws DatabaseException {
		return 0;
	}
}
