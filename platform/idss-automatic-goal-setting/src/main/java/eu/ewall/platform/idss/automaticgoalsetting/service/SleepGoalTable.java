package eu.ewall.platform.idss.automaticgoalsetting.service;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.service.model.common.SleepGoal;
import eu.ewall.platform.idss.service.model.common.SleepGoalMeasure;

/**
 * Definition of a database table that can store {@link SleepGoal SleepGoal}
 * objects.
 * 
 * @author Dennis Hofs (RRD)
 */
public class SleepGoalTable extends DatabaseTableDef<SleepGoal> {
	public static final String NAME_PREFIX = "goals_";
	private static final int VERSION = 0;

	/**
	 * Constructs a new sleep goal table for the specified sleep measure.
	 * 
	 * @param measure the sleep measure
	 */
	public SleepGoalTable(SleepGoalMeasure measure) {
		super(NAME_PREFIX + measure.toString().toLowerCase(),
				SleepGoal.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db)
			throws DatabaseException {
		return 0;
	}
}
