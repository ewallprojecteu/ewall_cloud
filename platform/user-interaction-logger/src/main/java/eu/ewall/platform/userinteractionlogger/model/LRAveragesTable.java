package eu.ewall.platform.userinteractionlogger.model;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.service.model.common.AverageDataParameter;

public class LRAveragesTable extends DatabaseTableDef<AverageDataParameter> {
	public static final String NAME = "uil_lr_averages";
	
	private static final int VERSION = 0;

	public LRAveragesTable() {
		super(NAME, AverageDataParameter.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db)
			throws DatabaseException {
		return 0;
	}
}
