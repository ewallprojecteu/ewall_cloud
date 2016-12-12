package eu.ewall.platform.idss.dao;

import java.util.LinkedHashMap;
import java.util.Map;

public class DatabaseLockCollection {
	private static final Map<String,Map<String,Object>> DB_TABLE_LOCKS =
			new LinkedHashMap<String,Map<String,Object>>();
	
	public static Object getLock(String database, String table) {
		synchronized (DB_TABLE_LOCKS) {
			Map<String,Object> tableLocks = DB_TABLE_LOCKS.get(database);
			if (tableLocks == null) {
				tableLocks = new LinkedHashMap<String,Object>();
				DB_TABLE_LOCKS.put(database, tableLocks);
			}
			Object lock = tableLocks.get(table);
			if (lock == null) {
				lock = new Object();
				tableLocks.put(table, lock);
			}
			return lock;
		}
	}
}
