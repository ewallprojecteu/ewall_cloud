package eu.ewall.fusioner.fitbit.model;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;

public class FitbitUpdateTable extends DatabaseTableDef<FitbitUpdate> {
	public static final String NAME = "fitbit_updates";
	
	private static final int VERSION = 0;
	
	public FitbitUpdateTable() {
		super(NAME, FitbitUpdate.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db)
			throws DatabaseException {
		return 0;
	}
}
