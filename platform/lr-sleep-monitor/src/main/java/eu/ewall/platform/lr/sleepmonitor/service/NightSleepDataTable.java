package eu.ewall.platform.lr.sleepmonitor.service;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;

public class NightSleepDataTable extends DatabaseTableDef<NightSleepData> {
	public static final String NAME = "night_sleep_data";
	
	private static final int VERSION = 0;

	public NightSleepDataTable() {
		super(NAME, NightSleepData.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db)
			throws DatabaseException {
		return 0;
	}
}
