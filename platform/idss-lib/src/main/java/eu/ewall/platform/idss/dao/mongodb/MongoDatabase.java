package eu.ewall.platform.idss.dao.mongodb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseColumnDef;
import eu.ewall.platform.idss.dao.DatabaseCriteria;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseIndex;
import eu.ewall.platform.idss.dao.DatabaseSort;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.dao.TableMetadataTableDef;

/**
 * Implementation of {@link Database Database} for MongoDB. Instances of this
 * class can be obtained from a {@link MongoDatabaseFactory
 * MongoDatabaseFactory}.
 * 
 * @author Dennis Hofs (RRD)
 */
public class MongoDatabase extends Database {
	private DB database;
	
	/**
	 * Constructs a new instance.
	 * 
	 * @param name the database name
	 * @param db the Mongo database
	 */
	public MongoDatabase(String name, DB db) {
		super(name);
		this.database = db;
	}

	@Override
	protected void createTable(String table, List<DatabaseColumnDef> columns)
			throws DatabaseException {
		DBCollection coll = getCollection(table, false);
		for (DatabaseColumnDef col : columns) {
			if (col.isIndex())
				coll.createIndex(new BasicDBObject(col.getName(), 1));
		}
	}

	@Override
	public void createIndex(String table, DatabaseIndex index)
			throws DatabaseException {
		DBCollection coll = getCollection(table, false);
		BasicDBObject keys = new BasicDBObject();
		for (String field : index.getFields()) {
			keys.append(field, 1);
		}
		BasicDBObject options = new BasicDBObject("name", index.getName());
		try {
			coll.createIndex(keys, options);
		} catch (MongoException ex) {
			throw new DatabaseException("Can't create compound index \"" +
					index.getName() + "\": " + ex.getMessage(), ex);
		}
	}

	@Override
	public void dropIndex(String table, String name) throws DatabaseException {
		DBCollection coll = getCollection(table, false);
		MongoException exception = null;
		try {
			coll.dropIndex(name);
			return;
		} catch (MongoException ex) {
			exception = ex;
		}
		// exception != null
		// older versions of MongoDatabase created single-column indexes
		// with name "column_1" instead of "column"
		String altName = name + "_1";
		try {
			coll.dropIndex(altName);
		} catch (MongoException ex) {
			throw new DatabaseException("Can't drop index \"" + name +
					"\" or \"" + altName + "\": " + exception.getMessage(),
					exception);
		}
	}

	@Override
	public void addColumn(String table, DatabaseColumnDef column)
			throws DatabaseException {
		DBCollection coll = getCollection(table, false);
		if (column.isIndex()) {
			coll.createIndex(new BasicDBObject(column.getName(), 1),
					new BasicDBObject("name", column.getName()));
		}
		BasicDBObject query = new BasicDBObject();
		BasicDBObject update;
		if (column.getType() == DatabaseType.MAP) {
			update = new BasicDBObject(column.getName(), new BasicDBObject());
		} else if (column.getType() == DatabaseType.LIST || column.getType() ==
				DatabaseType.OBJECT_LIST) {
			update = new BasicDBObject(column.getName(), new BasicDBList());
		} else {
			update = new BasicDBObject(column.getName(), null);
		}
		BasicDBObject action = new BasicDBObject("$set", update);
		try {
			coll.update(query, action, false, true);
		} catch (MongoException ex) {
			throw new DatabaseException(String.format(
					"Can't add field \"%s\" to collection \"%s\" in database \"%s\"",
					column.getName(), table, getName()) + ": " + ex.getMessage(),
					ex);
		}
	}

	@Override
	public void dropColumn(String table, String column)
			throws DatabaseException {
		DBCollection coll = getCollection(table, false);
		dropColumnIndexes(coll, column);
		BasicDBObject query = new BasicDBObject();
		BasicDBObject update = new BasicDBObject(column, "");
		BasicDBObject action = new BasicDBObject("$unset", update);
		try {
			coll.update(query, action, false, true);
		} catch (MongoException ex) {
			throw new DatabaseException(String.format(
					"Can't remove field \"%s\" from collection \"%s\" in database \"%s\"",
					column, table, getName()) + ": " + ex.getMessage(),
					ex);
		}
	}

	@Override
	public void renameColumn(String table, String oldName, String newName)
			throws DatabaseException {
		DBCollection coll = getCollection(table, false);
		boolean hasIndex = dropColumnIndexes(coll, oldName);
		if (hasIndex)
			coll.createIndex(new BasicDBObject(newName, 1));
		BasicDBObject query = new BasicDBObject();
		BasicDBObject update = new BasicDBObject(oldName, newName);
		BasicDBObject action = new BasicDBObject("$rename", update);
		try {
			coll.update(query, action, false, true);
		} catch (MongoException ex) {
			throw new DatabaseException(String.format(
					"Can't rename field \"%s\" to \"%s\" in collection \"%s\" in database \"%s\"",
					oldName, newName, table, getName()) + ": " +
					ex.getMessage(), ex);
		}
	}

	/**
	 * Drops all indexes whose key involves the specified column.
	 *
	 * @param coll the collection
	 * @param column the column name
	 * @return true if an index was dropped, false otherwise
	 */
	private boolean dropColumnIndexes(DBCollection coll, String column) {
		List<DBObject> indexes = coll.getIndexInfo();
		boolean droppedIndex = false;
		for (DBObject index : indexes) {
			DBObject indexKey = (DBObject)index.get("key");
			if (indexKey.containsField(column)) {
				coll.dropIndex(indexKey);
				droppedIndex = true;
			}
		}
		return droppedIndex;
	}

	@Override
	protected List<String> selectDbTables() throws DatabaseException {
		Set<String> set;
		try {
			set = database.getCollectionNames();
		} catch (MongoException ex) {
			throw new DatabaseException(String.format(
					"Can't get collection names from database \"%s\"",
					getName()) + ": " + ex.getMessage(), ex);
		}
		List<String> result = new ArrayList<String>(set);
		Collections.sort(result);
		return result;
	}

	@Override
	protected void dropDbTable(String table) throws DatabaseException {
		DBCollection coll = getCollection(table, false);
		try {
			coll.drop();
		} catch (MongoException ex) {
			throw new DatabaseException(String.format(
					"Can't drop collection \"%s\" from database \"%s\"",
					table, getName()) + ": " + ex.getMessage(), ex);
		}
	}

	@Override
	public void beginTransaction() throws DatabaseException {
	}

	@Override
	public void commitTransaction() throws DatabaseException {
	}

	@Override
	protected void doInsertMaps(String table, List<Map<String, Object>> values)
			throws DatabaseException {
		DBCollection coll = getCollection(table, true);
		List<DBObject> docs = new ArrayList<DBObject>();
		for (Map<String,Object> val : values) {
			BasicDBObject doc = new BasicDBObject();
			List<String> keys = new ArrayList<String>(val.keySet());
			Collections.sort(keys);
			for (String key : keys) {
				if (key.equals("id")) {
					doc.append("_id", new ObjectId((String)val.get(key)));
				} else {
					doc.append(key, toDbObject(val.get(key)));
				}
			}
			docs.add(doc);
		}
		try {
			coll.insert(docs);
		} catch (MongoException ex) {
			throw new DatabaseException(String.format(
					"Can't insert documents into collection \"%s\" in database \"%s\"",
					table, getName()) + ": " + ex.getMessage(), ex);
		}
		Iterator<DBObject> docIt = docs.iterator();
		Iterator<Map<String,Object>> mapIt = values.iterator();
		while (docIt.hasNext()) {
			DBObject doc = docIt.next();
			Map<String,Object> map = mapIt.next();
			map.put("id", doc.get("_id").toString());
		}
	}

	@Override
	protected List<Map<String, ?>> doSelectMaps(String table,
			DatabaseCriteria criteria, int limit, DatabaseSort[] sort)
			throws DatabaseException {
		DBCollection coll = getCollection(table, true);
		MongoQueryBuilder builder = new MongoQueryBuilder();
		DBCursor cursor = null;
		try {
			cursor = coll.find(builder.buildCriteria(criteria));
			if (limit > 0)
				cursor.limit(limit);
			if (sort != null && sort.length > 0)
				cursor.sort(builder.buildSort(sort));
			List<Map<String,?>> result = new ArrayList<Map<String,?>>();
			while (cursor.hasNext()) {
				DBObject doc = cursor.next();
				Map<String,Object> map = new HashMap<String,Object>();
				for (String key : doc.keySet()) {
					if (key.equals("_id")) {
						map.put("id", doc.get(key).toString());
					} else {
						map.put(key, doc.get(key));
					}
				}
				result.add(map);
			}
			return result;
		} catch (MongoException ex) {
			throw new DatabaseException(String.format(
					"Can't find documents from collection \"%s\" in database \"%s\"",
					table, getName()) + ": " + ex.getMessage(), ex);
		} finally {
			if (cursor != null)
				cursor.close();
		}
	}

	@Override
	public int count(String table, DatabaseCriteria criteria)
			throws DatabaseException {
		DBCollection coll = getCollection(table, true);
		MongoQueryBuilder builder = new MongoQueryBuilder();
		try {
			return (int)coll.getCount(builder.buildCriteria(criteria));
		} catch (MongoException ex) {
			throw new DatabaseException(String.format(
					"Can't count documents in collection \"%s\" in database \"%s\"",
					table, getName()) + ": " + ex.getMessage(), ex);
		}
	}

	@Override
	protected void doUpdate(String table, DatabaseCriteria criteria,
			Map<String, ?> values) throws DatabaseException {
		DBCollection coll = getCollection(table, true);
		MongoQueryBuilder builder = new MongoQueryBuilder();
		DBObject query = builder.buildCriteria(criteria);
		BasicDBObject update = new BasicDBObject();
		List<String> keys = new ArrayList<String>(values.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			if (key.equals("id")) {
				update.append("_id", new ObjectId((String)values.get(key)));
			} else {
				update.append(key, toDbObject(values.get(key)));
			}
		}
		BasicDBObject action = new BasicDBObject();
		action.append("$set", update);
		try {
			coll.update(query, action, false, true);
		} catch (MongoException ex) {
			throw new DatabaseException(String.format(
					"Can't update documents in collection \"%s\" in database \"%s\"",
					table, getName()) + ": " + ex.getMessage(), ex);
		}
	}

	@Override
	protected void doDelete(String table, DatabaseCriteria criteria)
			throws DatabaseException {
		DBCollection coll = getCollection(table, true);
		MongoQueryBuilder builder = new MongoQueryBuilder();
		DBObject query = builder.buildCriteria(criteria);
		try {
			coll.remove(query);
		} catch (MongoException ex) {
			throw new DatabaseException(String.format(
					"Can't remove documents from collection \"%s\" in database \"%s\"",
					table, getName()) + ": " + ex.getMessage(), ex);
		}
	}
	
	/**
	 * Tries to get the collection with the specified name. This method can
	 * check whether the collection exists in the metadata table. This is
	 * recommended before any data query, because it could happen that a
	 * database has been cleared and then all data queries should fail until
	 * the database has been reinitialised. This method does not check for the
	 * metadata table itself.
	 * 
	 * @param name the collection name
	 * @param checkExists true if this method should check whether the
	 * collection exists in the metadata table
	 * @return the collection
	 * @throws DatabaseException if "checkExists" is true and the collection
	 * does not exist in the metadata table, or if a database error occurs
	 */
	private DBCollection getCollection(String name, boolean checkExists)
			throws DatabaseException {
		if (checkExists && !name.equals(TableMetadataTableDef.NAME) &&
				!selectTables().contains(name)) {
			throw new DatabaseException(String.format(
					"Collection \"%s\" not found in database \"%s\"",
					name, getName()));
		}
		try {
			return database.getCollection(name);
		} catch (MongoException ex) {
			throw new DatabaseException(String.format(
					"Can't get collection \"%s\" from database \"%s\": %s",
					name, getName(), ex.getMessage()), ex);
		}
	}
	
	/**
	 * Converts a map, list or primitive value to a value that can be put in
	 * a DBObject.
	 * 
	 * @param obj the map, list or primitive value
	 * @return the value for a DBObject
	 */
	private Object toDbObject(Object obj) {
		if (obj instanceof List) {
			List<?> list = (List<?>)obj;
			BasicDBList dbList = new BasicDBList();
			for (Object item : list) {
				dbList.add(toDbObject(item));
			}
			return dbList;
		} else if (obj instanceof Map) {
			Map<?,?> map = (Map<?,?>)obj;
			BasicDBObject dbMap = new BasicDBObject();
			for (Object key : map.keySet()) {
				dbMap.append(key.toString(), toDbObject(map.get(key)));
			}
			return dbMap;
		} else {
			return obj;
		}
	}

	@Override
	protected List<? extends Map<String,?>> selectLogRecords(String table,
			DatabaseCriteria criteria) throws DatabaseException {
		DBCollection coll = getCollection(table, true);
		MongoQueryBuilder builder = new MongoQueryBuilder();
		DBCursor cursor = null;
		try {
			BasicDBObject keys = new BasicDBObject();
			keys.append("_id", 1);
			cursor = coll.find(builder.buildCriteria(criteria), keys);
			List<Map<String,?>> records = new ArrayList<Map<String,?>>();
			while (cursor.hasNext()) {
				DBObject doc = cursor.next();
				Map<String,Object> record = new LinkedHashMap<String,Object>();
				record.put("id", doc.get("_id").toString());
				if (doc.containsField("user"))
					record.put("user", doc.get("user"));
				if (doc.containsField("utcTime"))
					record.put("utcTime", doc.get("utcTime"));
				records.add(record);
			}
			return records;
		} catch (MongoException ex) {
			throw new DatabaseException(String.format(
					"Can't find documents from collection \"%s\" in database \"%s\"",
					table, getName()) + ": " + ex.getMessage(), ex);
		} finally {
			if (cursor != null)
				cursor.close();
		}
	}
}
