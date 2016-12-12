package eu.ewall.platform.userinteractionlogger.model;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.service.model.common.DataUpdated;

public class LRWeekPatternUpdatedTable extends DatabaseTableDef<DataUpdated> {
	public static final String NAME = "uil_lr_week_pattern_updates";
	
	private static final int VERSION = 0;
	
	public LRWeekPatternUpdatedTable() {
		super(NAME, DataUpdated.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db)
			throws DatabaseException {
		return 0;
	}
}
