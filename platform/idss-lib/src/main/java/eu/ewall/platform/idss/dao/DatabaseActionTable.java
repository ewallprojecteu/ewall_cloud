package eu.ewall.platform.idss.dao;

import org.slf4j.Logger;

import eu.ewall.platform.idss.utils.AppComponents;

/**
 * This is a special database table that is used internally in {@link Database
 * Database}. It can log database actions for audit logging or for
 * synchronisation with a remote database. In the database it's stored in a
 * table with name "_action_log".
 *
 * @author Dennis Hofs (RRD)
 */
public class DatabaseActionTable extends DatabaseTableDef<DatabaseAction> {
	private static final String LOGTAG =
			DatabaseActionTable.class.getSimpleName();
	private static final String NAME_PREFIX = "_action_log_";

	private static final int VERSION = 1;
	
	private UserTableKey userTableKey;

	public DatabaseActionTable(UserTableKey userTableKey) {
		super(NAME_PREFIX + userTableKey.getKey(), DatabaseAction.class,
				VERSION);
		this.userTableKey = userTableKey;
		addCompoundIndex(new DatabaseIndex("timeOrder", new String[] {
				"time", "order"
		}));
	}
	
	public UserTableKey getUserTableKey() {
		return userTableKey;
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
		Logger logger = AppComponents.getLogger(LOGTAG);
		logger.info(String.format(
				"Add sampleTime to action table %s (%s/%s)",
				getName(), userTableKey.getUser(),
				userTableKey.getTable()));
		db.addColumn(getName(), new DatabaseColumnDef("sampleTime",
				DatabaseType.LONG, true));
		return 1;
	}
}
