package eu.ewall.platform.lr.physicalactivity.service;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.service.model.common.ActivityMeasure;
import eu.ewall.platform.idss.service.model.common.AverageActivity;

/**
 * Definition of the database table that can store {@link AverageActivity
 * AverageActivity} objects.
 * 
 * @author Dennis Hofs (RRD)
 */
public class AverageActivityTable extends DatabaseTableDef<AverageActivity> {
	public static final String NAME_PREFIX = "averages_";
	private static final int VERSION = 0;

	/**
	 * Constructs a new averages table for the specified activity measure.
	 * 
	 * @param measure the activity measure
	 */
	public AverageActivityTable(ActivityMeasure measure) {
		super(NAME_PREFIX + measure.toString().toLowerCase(),
				AverageActivity.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db)
			throws DatabaseException {
		return VERSION;
	}
}
