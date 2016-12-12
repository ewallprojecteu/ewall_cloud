package eu.ewall.platform.userinteractionlogger.model;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseIndex;
import eu.ewall.platform.idss.dao.DatabaseTableDef;

public class UserInteractionLogTable extends
DatabaseTableDef<UserInteractionLog> {
	private static final String NAME_PREFIX = "userinteractionlog_";
	
	private static final int VERSION = 1;

	public UserInteractionLogTable(String userKey) {
		super(NAME_PREFIX + userKey, UserInteractionLog.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db)
			throws DatabaseException {
		if (version == 0)
			return upgradeTableV0(db);
		else
			return 1;
	}
	
	private int upgradeTableV0(Database db) throws DatabaseException {
		db.createIndex(getName(), new DatabaseIndex("dateString",
				new String[] { "dateString" }));
		return 1;
	}
}
