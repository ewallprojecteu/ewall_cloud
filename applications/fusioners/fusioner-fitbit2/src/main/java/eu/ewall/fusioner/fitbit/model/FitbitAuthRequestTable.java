package eu.ewall.fusioner.fitbit.model;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;

public class FitbitAuthRequestTable extends
DatabaseTableDef<FitbitAuthRequest> {
	public final static String NAME = "fitbit_auth_requests";
	
	private final static int VERSION = 0;

	public FitbitAuthRequestTable() {
		super(NAME, FitbitAuthRequest.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db)
			throws DatabaseException {
		return 0;
	}

}
