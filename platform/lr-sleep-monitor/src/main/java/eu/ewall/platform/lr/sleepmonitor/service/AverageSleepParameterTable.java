package eu.ewall.platform.lr.sleepmonitor.service;

import java.util.List;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;

/**
 * Definition of the database table that can store {@link AverageSleepParameter
 * AverageSleepParameter} objects.
 * 
 * @author Dennis Hofs (RRD)
 */
public class AverageSleepParameterTable extends
DatabaseTableDef<AverageSleepParameter> {
	public static final String NAME = "averages";
	
	private static final int VERSION = 1;

	public AverageSleepParameterTable() {
		super(NAME, AverageSleepParameter.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db)
			throws DatabaseException {
		if (version == 0)
			return updateTableV0(db);
		else
			return 1;
	}
	
	private int updateTableV0(Database db) throws DatabaseException {
		List<String> tables = db.selectTables();
		if (tables.contains(AverageSleepParameterTable.NAME))
			db.dropTable(AverageSleepParameterTable.NAME);
		if (tables.contains(NightSleepDataTable.NAME))
			db.dropTable(NightSleepDataTable.NAME);
		if (tables.contains(SleepProcessedTable.NAME))
			db.dropTable(SleepProcessedTable.NAME);
		if (tables.contains(SleepUpdatedTable.NAME))
			db.dropTable(SleepUpdatedTable.NAME);
		if (tables.contains("night_sleep_periods"))
			db.dropTable("night_sleep_periods");
		return 1;
	}
}
