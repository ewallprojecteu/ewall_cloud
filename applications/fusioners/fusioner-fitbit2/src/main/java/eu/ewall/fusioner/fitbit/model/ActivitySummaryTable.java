package eu.ewall.fusioner.fitbit.model;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;

public class ActivitySummaryTable extends DatabaseTableDef<ActivitySummary> {
	public static final String NAME = "activity_summaries";
	
	private static final int VERSION = 0;
	
	public ActivitySummaryTable() {
		super(NAME, ActivitySummary.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db)
			throws DatabaseException {
		return 0;
	}
}
