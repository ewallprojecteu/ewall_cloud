package eu.ewall.platform.lr.physicalactivity.service;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseCriteria;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.service.model.common.DataUpdated;

/**
 * Definition of the database table that can store {@link DataUpdated
 * DataUpdated} objects to indicate until what date activity data has been
 * processed by the lifestyle reasoner to generate a weekly pattern. The date
 * is inclusive.
 * 
 * @author Dennis Hofs (RRD)
 */
public class ActivityProcessedTable extends DatabaseTableDef<DataUpdated> {
	public static final String NAME = "processed";
	
	private static final int VERSION = 1;

	public ActivityProcessedTable() {
		super(NAME, DataUpdated.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db) throws DatabaseException {
		if (version == 0)
			return upgradeTableV0(db);
		return 1;
	}
	
	private int upgradeTableV0(Database db) throws DatabaseException {
		db.delete(NAME, (DatabaseCriteria)null);
		return 1;
	}
}
