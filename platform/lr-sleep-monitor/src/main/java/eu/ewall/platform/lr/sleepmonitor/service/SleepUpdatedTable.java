package eu.ewall.platform.lr.sleepmonitor.service;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.service.model.common.DataUpdated;

/**
 * Definition of the database table that can store {@link DataUpdated
 * DataUpdated} objects to indicate until what date sleep data has been
 * retrieved and stored in the local database. It specifies the date following
 * the last retrieved night.
 * 
 * @author Dennis Hofs (RRD)
 */
public class SleepUpdatedTable extends DatabaseTableDef<DataUpdated> {
	public static final String NAME = "sleep_updated";
	
	private static final int VERSION = 0;

	public SleepUpdatedTable() {
		super(NAME, DataUpdated.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db) throws DatabaseException {
		return 0;
	}
}
