package eu.ewall.platform.idss.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public class UpgradeDatabaseActionTable extends DatabaseTableDef<DatabaseAction> {
	public static final String NAME = "_action_log";

	private static final int VERSION = 6;

	private static final String LOGTAG = UpgradeDatabaseActionTable.class
			.getSimpleName();

	public UpgradeDatabaseActionTable() {
		super(NAME, DatabaseAction.class, VERSION);
		addCompoundIndex(new DatabaseIndex("timeOrder", new String[] {
				"time", "order"
		}));
	}

	@Override
	public int upgradeTable(int version, Database db) throws DatabaseException {
		if (version == 0)
			return upgradeTableV0(db);
		else if (version == 1)
			return upgradeTableV1(db);
		else if (version == 2)
			return upgradeTableV2(db);
		else if (version == 3)
			return upgradeTableV3(db);
		else if (version == 4)
			return upgradeTableV4(db);
		else if (version == 5)
			return upgradeTableV5(db);
		else
			return 6;
	}
	
	private int upgradeTableV0(Database db) throws DatabaseException {
		List<Map<String,?>> maps = db.selectMaps(NAME, null, 0, null);
		db.delete(NAME, (DatabaseCriteria)null);
		db.dropColumn(NAME, "time");
		DatabaseColumnDef column = new DatabaseColumnDef("time",
				DatabaseType.LONG, true);
		db.addColumn(NAME, column);
		List<Map<String,Object>> insertMaps = new ArrayList<Map<String,Object>>();
		while (!maps.isEmpty()) {
			Map<String,Object> map = new LinkedHashMap<String,Object>(
					maps.remove(0));
			insertMaps.add(map);
		}
		db.insertMaps(NAME, insertMaps);
		return 1;
	}
	
	private int upgradeTableV1(Database db) throws DatabaseException {
		try {
			db.dropIndex(NAME, "time");
		} catch (DatabaseException ex) {}
		try {
			db.createIndex(NAME, new DatabaseIndex("table", new String[] {
					"table"
			}));
		} catch (DatabaseException ex) {}
		try {
			db.createIndex(NAME, new DatabaseIndex("timeOrder", new String[] {
					"time", "order"
			}));
		} catch (DatabaseException ex) {}
		return 2;
	}
	
	private int upgradeTableV2(Database db) throws DatabaseException {
		try {
			db.createIndex(NAME, new DatabaseIndex("userTable", new String[] {
					"user", "table"
			}));
		} catch (DatabaseException ex) {}
		return 3;
	}
	
	private int upgradeTableV3(Database db) throws DatabaseException {
		try {
			db.dropIndex(NAME, "user");
		} catch (DatabaseException ex) {}
		try {
			db.dropIndex(NAME, "table");
		} catch (DatabaseException ex) {}
		try {
			db.dropIndex(NAME, "userTable");
		} catch (DatabaseException ex) {}
		try {
			db.dropIndex(NAME, "timeOrder");
		} catch (DatabaseException ex) {}
		try {
			db.createIndex(NAME, new DatabaseIndex("userTableTimeOrder",
					new String[] { "user", "table", "time", "order" }));
		} catch (DatabaseException ex) {}
		return 4;
	}
	
	private int upgradeTableV4(Database db) throws DatabaseException {
		try {
			db.dropIndex(NAME, "userTableTimeOrder");
		} catch (DatabaseException ex) {}
		try {
			db.createIndex(NAME, new DatabaseIndex("syncIndex", new String[] {
					"table", "action", "source", "user", "time", "order"
			}));
		} catch (DatabaseException ex) {}
		return 5;
	}
	
	private int upgradeTableV5(Database db) throws DatabaseException {
		try {
			db.dropIndex(NAME, "syncIndex");
		} catch (DatabaseException ex) {}
		try {
			db.createIndex(NAME, new DatabaseIndex("timeOrder",
					new String[] { "time", "order" }));
		} catch (DatabaseException ex) {}
		DatabaseSort[] sort = new DatabaseSort[] {
				new DatabaseSort("time", true),
				new DatabaseSort("order", true)
		};
		List<Map<String,?>> actions = db.selectMaps(NAME, null, 10000, sort);
		Logger logger = AppComponents.getLogger(LOGTAG);
		while (!actions.isEmpty()) {
			Map<UserTable,List<Map<String,Object>>> buckets =
					new LinkedHashMap<UserTable,List<Map<String,Object>>>();
			for (Map<String,?> action : actions) {
				String user = (String)action.get("user");
				String table = (String)action.get("table");
				UserTable key = new UserTable(user, table);
				List<Map<String,Object>> keyActions = buckets.get(key);
				if (keyActions == null) {
					keyActions = new ArrayList<Map<String,Object>>();
					buckets.put(key, keyActions);
				}
				Map<String,Object> insertAction =
						new LinkedHashMap<String,Object>(action);
				keyActions.add(insertAction);
			}
			for (UserTable key : buckets.keySet()) {
				DatabaseActionTable table = DatabaseCache
						.getInstance().initActionTable(db, key.getUser(),
						key.getTable());
				List<Map<String,Object>> keyActions = buckets.get(key);
				db.insertMaps(table.getName(), keyActions);
				logger.info(String.format(
						"Inserted %d database actions for %s / %s",
						keyActions.size(), key.getUser(), key.getTable()));
			}
			Map<String,?> lastAction = actions.get(actions.size() - 1);
			long lastTime = (Long)lastAction.get("time");
			int lastOrder = (Integer)lastAction.get("order");
			DatabaseCriteria criteria = new DatabaseCriteria.Or(
				new DatabaseCriteria.LessThan("time", lastTime),
				new DatabaseCriteria.And(
					new DatabaseCriteria.Equal("time", lastTime),
					new DatabaseCriteria.LessEqual("order", lastOrder)
				)
			);
			db.delete(NAME, criteria);
			actions = db.selectMaps(NAME, null, 10000, sort);
		}
		return 6;
	}
}
