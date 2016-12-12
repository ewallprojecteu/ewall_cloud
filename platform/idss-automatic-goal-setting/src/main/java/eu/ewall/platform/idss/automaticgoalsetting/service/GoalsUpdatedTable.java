package eu.ewall.platform.idss.automaticgoalsetting.service;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.service.model.common.DataUpdated;

/**
 * Definition of the database table that can store {@link DataUpdated
 * DataUpdated} objects to indicate the last date for which the service
 * generated a goal.
 * 
 * @author Dennis Hofs (RRD)
 */
public class GoalsUpdatedTable extends DatabaseTableDef<DataUpdated> {
	private static final String NAME_SUFFIX = "_goals_updated";

	private static final int VERSION = 0;

	public GoalsUpdatedTable(GoalMainType type) {
		super(type.toString().toLowerCase() + NAME_SUFFIX, DataUpdated.class,
				VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db)
			throws DatabaseException {
		return 0;
	}
}
