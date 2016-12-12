package eu.ewall.platform.idss.dao;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DatabaseCache {
	private static final Object STATIC_LOCK = new Object();
	private static DatabaseCache instance = null;
	
	private Map<String,DatabaseCachedMetadata> databases =
			new LinkedHashMap<String,DatabaseCachedMetadata>();
	private SecureRandom random = new SecureRandom();
	
	public static DatabaseCache getInstance() {
		synchronized (STATIC_LOCK) {
			if (instance == null)
				instance = new DatabaseCache();
			return instance;
		}
	}
	
	private DatabaseCache() {
	}
	
	public void removeDatabase(String dbName) {
		synchronized (STATIC_LOCK) {
			databases.remove(dbName);
		}
	}
	
	public DatabaseActionTable initActionTable(Database db, String user,
			String table) throws DatabaseException {
		synchronized (STATIC_LOCK) {
			DatabaseCachedMetadata cache = getCachedMetadata(db);
			UserTable userTable = new UserTable(user, table);
			DatabaseActionTable actionTable = cache.actionTables.get(
					userTable);
			if (actionTable != null)
				return actionTable;
			DatabaseCriteria criteria = new DatabaseCriteria.And(
					new DatabaseCriteria.Equal("user", user),
					new DatabaseCriteria.Equal("table", table)
			);
			UserTableKey key = db.selectOne(new UserTableKeyTable(), criteria,
					null);
			if (key == null) {
				key = new UserTableKey();
				key.setUser(user);
				key.setTable(table);
				key.setKey(generateKey(db));
				db.insert(UserTableKeyTable.NAME, key);
			}
			actionTable = new DatabaseActionTable(key);
			db.initTable(actionTable);
			cache.actionTables.put(userTable, actionTable);
			return actionTable;
		}
	}
	
	public List<String> getMetaTables(Database db) {
		synchronized (STATIC_LOCK) {
			return getCachedMetadata(db).metaTables;
		}
	}
	
	public void setMetaTables(Database db, List<String> tables) {
		synchronized (STATIC_LOCK) {
			getCachedMetadata(db).metaTables = tables;
		}
	}
	
	public void addMetaTable(Database db, String table) {
		synchronized (STATIC_LOCK) {
			List<String> selectedTables = getMetaTables(db);
			if (selectedTables == null || selectedTables.contains(table))
				return;
			selectedTables.add(table);
			Collections.sort(selectedTables);
		}
	}
	
	public List<String> getTablesOnInit(Database db) {
		synchronized (STATIC_LOCK) {
			return getCachedMetadata(db).tablesOnInit;
		}
	}
	
	public void setTablesOnInit(Database db, List<String> tables) {
		synchronized (STATIC_LOCK) {
			getCachedMetadata(db).tablesOnInit = tables;
		}
	}
	
	public void addTableOnInit(Database db, String table) {
		synchronized (STATIC_LOCK) {
			List<String> tablesOnInit = getTablesOnInit(db);
			if (tablesOnInit == null || tablesOnInit.contains(table))
				return;
			tablesOnInit.add(table);
			Collections.sort(tablesOnInit);
		}
	}
	
	public List<String> getTableFields(Database db, String table) {
		synchronized (STATIC_LOCK) {
			DatabaseCachedMetadata cache = getCachedMetadata(db);
			return cache.tableFields.get(table);
		}
	}
	
	public void setTableFields(Database db, String table,
			List<String> fields) {
		synchronized (STATIC_LOCK) {
			DatabaseCachedMetadata cache = getCachedMetadata(db);
			cache.tableFields.put(table, fields);
		}
	}
	
	public void removeTable(Database db, String table) {
		synchronized (STATIC_LOCK) {
			DatabaseCachedMetadata cache = getCachedMetadata(db);
			List<UserTable> actionTableKeys = new ArrayList<UserTable>(
					cache.actionTables.keySet());
			for (UserTable key : actionTableKeys) {
				if (key.getTable().equals(table))
					cache.actionTables.remove(key);
			}
			if (cache.metaTables != null)
				cache.metaTables.remove(table);
			cache.tableFields.remove(table);
		}
	}
	
	private DatabaseCachedMetadata getCachedMetadata(Database db) {
		DatabaseCachedMetadata cache = databases.get(db.getName());
		if (cache == null) {
			cache = new DatabaseCachedMetadata();
			databases.put(db.getName(), cache);
		}
		return cache;
	}
	
	private String generateKey(Database db) throws DatabaseException {
		int randNum = random.nextInt();
		while (true) {
			String key = String.format("%08x", randNum);
			DatabaseCriteria criteria = new DatabaseCriteria.Equal("key", key);
			if (db.count(UserTableKeyTable.NAME, criteria) == 0)
				return key;
			randNum++;
		}
	}
	
	private class DatabaseCachedMetadata {
		public Map<UserTable,DatabaseActionTable> actionTables =
				new LinkedHashMap<UserTable,DatabaseActionTable>();
		public List<String> metaTables = null;
		public List<String> tablesOnInit = null;
		public Map<String,List<String>> tableFields =
				new LinkedHashMap<String,List<String>>();
	}
}
