package eu.ewall.platform.lr.habits.persistence;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.lr.habits.model.Habits;

public class HabitsTable extends DatabaseTableDef<Habits>{
	
	public static final String NAME = "habits";
	private static final int VERSION = 0;

	public HabitsTable() {
		super(NAME, Habits.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db) throws DatabaseException {
		return 0;
	}
}
