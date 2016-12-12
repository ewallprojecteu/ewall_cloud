package eu.ewall.platform.userinteractionlogger;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;

public class UserKeyTable extends DatabaseTableDef<UserKey> {
	public static final String NAME = "userkeys";
	
	private static final int VERSION = 0;

	public UserKeyTable() {
		super(NAME, UserKey.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db)
			throws DatabaseException {
		return 0;
	}
}
