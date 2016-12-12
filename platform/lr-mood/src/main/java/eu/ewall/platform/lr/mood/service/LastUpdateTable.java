package eu.ewall.platform.lr.mood.service;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;

public class LastUpdateTable extends DatabaseTableDef<LastUpdate> {
	
	public static final String NAME = "last_update";
	private static final int VERSION = 0;

	public LastUpdateTable() {
		super(NAME, LastUpdate.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db) throws DatabaseException {
		return 0;
	}

}
