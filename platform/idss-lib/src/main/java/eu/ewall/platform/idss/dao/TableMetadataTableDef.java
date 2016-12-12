package eu.ewall.platform.idss.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a special database table that is used internally in {@link Database
 * Database}. It can define metadata about a database table (including itself).
 * Currently it stores a version number for each table, to support automatic
 * database upgrades. In the future it might define other metadata as well.
 * In the database it's stored in a table with name "_table_metadata".
 * 
 * @author Dennis Hofs (RRD)
 */
public class TableMetadataTableDef extends DatabaseTableDef<TableMetadata> {
	public static final String NAME = "_table_metadata";
	private static final int VERSION = 2;
	
	public TableMetadataTableDef() {
		super(NAME, TableMetadata.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db) throws DatabaseException {
		if (version == 0)
			return upgradeTableV0(db);
		else if (version == 1)
			return upgradeTableV1(db);
		else
			return 2;
	}

	public int upgradeTableV0(Database db) throws DatabaseException {
		List<Map<String,?>> maps = db.selectMaps(NAME, null, 0, null);
		List<Map<String,Object>> insertMaps =
				new ArrayList<Map<String,Object>>();
		for (Map<String,?> map : maps) {
			Map<String,Object> insertMap = new LinkedHashMap<String,Object>();
			for (String key : map.keySet()) {
				insertMap.put(key, map.get(key));
			}
			insertMaps.add(insertMap);
		}
		db.delete(NAME, (DatabaseCriteria)null);
		db.dropColumn(NAME, "value");
		db.addColumn(NAME, new DatabaseColumnDef("value", DatabaseType.TEXT));
		db.insertMaps(NAME, insertMaps);
		return 1;
	}
	
	public int upgradeTableV1(Database db) throws DatabaseException {
		List<Map<String,?>> maps = db.selectMaps(NAME, null, 0, null);
		List<Map<String,Object>> insertMaps =
				new ArrayList<Map<String,Object>>();
		for (Map<String,?> map : maps) {
			Map<String,Object> insertMap = new LinkedHashMap<String,Object>();
			for (String key : map.keySet()) {
				insertMap.put(key, map.get(key));
			}
			insertMaps.add(insertMap);
		}
		db.delete(NAME, (DatabaseCriteria)null);
		db.insertMaps(NAME, insertMaps);
		return 2;
	}
}
