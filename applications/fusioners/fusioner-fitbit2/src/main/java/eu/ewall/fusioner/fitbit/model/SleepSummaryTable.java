package eu.ewall.fusioner.fitbit.model;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;

public class SleepSummaryTable extends DatabaseTableDef<SleepSummary> {
	public static final String NAME = "sleep_summaries";
	
	private static final int VERSION = 0;

	public SleepSummaryTable() {
		super(NAME, SleepSummary.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db)
			throws DatabaseException {
		return 0;
	}
}
