package eu.ewall.platform.idss.automaticgoalsetting.service;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.service.model.common.ActivityGoal;
import eu.ewall.platform.idss.service.model.common.ActivityMeasure;

/**
 * Definition of the database table that can store {@link ActivityGoal
 * ActivityGoal} objects.
 * 
 * @author Dennis Hofs (RRD)
 */
public class ActivityGoalTable extends DatabaseTableDef<ActivityGoal> {
	public static final String NAME_PREFIX = "goals_";
	private static final int VERSION = 0;

	/**
	 * Constructs a new activity goal table for the specified activity measure.
	 * 
	 * @param measure the activity measure
	 */
	public ActivityGoalTable(ActivityMeasure measure) {
		super(NAME_PREFIX + measure.toString().toLowerCase(),
				ActivityGoal.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db)
			throws DatabaseException {
		return 0;
	}
}
