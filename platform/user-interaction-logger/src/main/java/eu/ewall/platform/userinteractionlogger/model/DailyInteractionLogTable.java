package eu.ewall.platform.userinteractionlogger.model;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;

public class DailyInteractionLogTable extends
DatabaseTableDef<DailyInteractionLog> {
	public static final String TABLE_NAME = "uil_daily_interactions";
	
	private static final int VERSION = 0;

	public DailyInteractionLogTable() {
		super(TABLE_NAME, DailyInteractionLog.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db)
			throws DatabaseException {
		return 0;
	}
}
