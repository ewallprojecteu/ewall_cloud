package eu.ewall.platform.lr.physicalactivity.service;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.service.model.common.ActivityMeasure;

/**
 * Definition of the database table that can store {@link DayActivity
 * DayActivity} objects.
 * 
 * @author Dennis Hofs (RRD)
 */
public class DayActivityTable extends DatabaseTableDef<DayActivity> {
	public static final String NAME_PREFIX = "activity_";
	private static final int VERSION = 0;

	/**
	 * Constructs a new day activity table for the specified activity measure.
	 * 
	 * @param measure the activity measure
	 */
	public DayActivityTable(ActivityMeasure measure) {
		super(NAME_PREFIX + measure.toString().toLowerCase(),
				DayActivity.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db)
			throws DatabaseException {
		return 0;
	}
}
