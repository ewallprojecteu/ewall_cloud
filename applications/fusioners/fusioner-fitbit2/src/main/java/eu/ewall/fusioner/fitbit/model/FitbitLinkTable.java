package eu.ewall.fusioner.fitbit.model;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;

public class FitbitLinkTable extends DatabaseTableDef<FitbitLink> {
	public static final String NAME = "fitbit_links";
	
	private static final int VERSION = 1;

	public FitbitLinkTable() {
		super(NAME, FitbitLink.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db)
			throws DatabaseException {
		if (version == 0)
			return upgradeTableV0(db);
		else
			return 1;
	}
	
	private int upgradeTableV0(Database db) throws DatabaseException {
		db.dropColumn(NAME, "dataSynced");
		return 1;
	}
}
