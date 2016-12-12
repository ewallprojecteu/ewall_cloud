package eu.ewall.platform.userinteractionlogger.model;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.service.model.common.DataUpdated;

public class LRSourceUpdatedTable extends DatabaseTableDef<DataUpdated> {
	public static final String NAME = "uil_lr_source_updates";
	
	private static final int VERSION = 0;

	public LRSourceUpdatedTable() {
		super(NAME, DataUpdated.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db)
			throws DatabaseException {
		return 0;
	}
}
