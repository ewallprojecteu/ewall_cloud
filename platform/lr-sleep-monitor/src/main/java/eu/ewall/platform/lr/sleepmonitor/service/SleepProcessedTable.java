package eu.ewall.platform.lr.sleepmonitor.service;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.service.model.common.DataUpdated;

/**
 * Definition of the database table that can store {@link DataUpdated
 * DataUpdated} objects to indicate until what date sleep data has been
 * processed by the lifestyle reasoner to generate a weekly pattern. It
 * specifies the date following the last processed night.
 * 
 * @author Dennis Hofs (RRD)
 */
public class SleepProcessedTable extends DatabaseTableDef<DataUpdated> {
	public static final String NAME = "sleep_processed";
	
	private static final int VERSION = 0;

	public SleepProcessedTable() {
		super(NAME, DataUpdated.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db) throws DatabaseException {
		return 0;
	}
}
