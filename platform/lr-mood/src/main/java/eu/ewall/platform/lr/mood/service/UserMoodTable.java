package eu.ewall.platform.lr.mood.service;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;

public class UserMoodTable extends DatabaseTableDef<UserMood>{
	public static final String NAME = "user_mood";
	private static final int VERSION = 0;

	public UserMoodTable() {
		super(NAME, UserMood.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db) throws DatabaseException {
		return 0;
	}

}
