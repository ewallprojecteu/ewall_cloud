package eu.ewall.platform.lr.mood.service;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.service.model.common.MoodData;

public class MoodDataTable extends DatabaseTableDef<MoodData> {
	
	public static final String NAME = "user_mood";
	private static final int VERSION = 0;

	public MoodDataTable() {
		super(NAME, MoodData.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db) throws DatabaseException {
		return 0;
	}

}
