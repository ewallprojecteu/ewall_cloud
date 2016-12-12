package eu.ewall.platform.idss.dao;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.ewall.platform.idss.dao.listener.DatabaseListenerRepository;
import eu.ewall.platform.idss.dao.sync.SyncProgressTableDef;

import eu.ewall.platform.idss.utils.AppComponent;
import eu.ewall.platform.idss.utils.ReflectionUtils;

import eu.ewall.platform.idss.utils.datetime.VirtualClock;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The base class for a database that can store {@link DatabaseObject
 * DatabaseObject}s. It was designed so that it can be implemented for SQL
 * databases as well as other databases such as MongoDB.
 * 
 * <p>Instances can be obtained from a {@link DatabaseConnection
 * DatabaseConnection}, which can be obtained from the {@link DatabaseFactory
 * DatabaseFactory}. The factory can be configured as an {@link AppComponent
 * AppComponent}.</p>
 *
 * <p>The database can log actions for audit logging or for synchronisation
 * with a remote database. If you enable audit logging, every select, insert,
 * update and delete action is logged with the user name who performed the
 * action. If you enable synchronisation logging, every insert, update and
 * delete action is logged. The data associated with insert and update actions
 * can also be logged. This is always done if synchronisation logging is
 * enabled. With audit logging it's optional. Finally it can log the user name
 * to which the data is related. This is done automatically when the data table
 * has a column named "user". It can be used for partial synchronisation of the
 * data for one particular user only.</p>
 *
 * <p>It only logs actions on tables that are not reserved (start with an
 * underscore). The logs are written to the table _action_log (see {@link
 * DatabaseActionTable DatabaseActionTableDef}). You should normally enable
 * action logging after initialising the tables with {@link
 * #initTable(DatabaseTableDef) initTable()}. This is done by {@link
 * DatabaseConnection DatabaseConnection}.</p>
 * 
 * @author Dennis Hofs (RRD)
 */
public abstract class Database {
	private String name;
	
	private boolean auditLogEnabled = false;
	private String auditLogUser = null;
	private boolean auditLogData = false;
	private boolean syncEnabled = false;
	private boolean saveSyncedRemoteActions = true;

	////////////////////////////////////////////////////////////////////////////
	// flags to enable caching
	private boolean metaInitialised = false;
	private boolean databaseInitialised = false;
	
	/**
	 * Constructs a new database.
	 * 
	 * @param name the database name
	 */
	public Database(String name) {
		this.name = name;
	}

	/**
	 * Returns the database name.
	 * 
	 * @return the database name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns whether audit logging is enabled.
	 *
	 * @return true if audit logging is enabled, false otherwise
	 */
	public boolean isAuditLogEnabled() {
		return auditLogEnabled;
	}

	/**
	 * Returns the user name for audit logging.
	 *
	 * @return the user name for audit logging
	 */
	public String getAuditLogUser() {
		return auditLogUser;
	}

	/**
	 * Returns whether data is stored with insert and update actions at audit
	 * logging. If synchronisation logging is enabled, the data is always
	 * stored regardless of this parameter.
	 *
	 * @return true if data is stored at audit logging, false otherwise
	 */
	public boolean isAuditLogData() {
		return auditLogData;
	}

	/**
	 * Enables audit logging. The parameter "logData" determines whether data is
	 * stored with insert and update actions at audit logging. If
	 * synchronisation logging is enabled, the data is always stored regardless
	 * of this parameter.
	 *
	 * @param user the user name that should be saved with the database actions
	 * (the user who performs the actions)
	 * @param logData true if data should be stored
	 */
	public void enableAuditLog(String user, boolean logData) {
		this.auditLogEnabled = true;
		this.auditLogUser = user;
		this.auditLogData = logData;
	}

	/**
	 * Disables audit logging.
	 */
	public void disableAuditLog() {
		this.auditLogEnabled = false;
		this.auditLogUser = null;
		this.auditLogData = false;
	}

	/**
	 * Returns whether action logging is enabled for synchronization with
	 * a remote database.
	 *
	 * @return true if synchronization logging is enabled, false otherwise
	 */
	public boolean isSyncEnabled() {
		return syncEnabled;
	}

	/**
	 * Returns whether action logging should be enabled for synchronization
	 * with a remote database.
	 *
	 * @param syncEnabled true if synchronization logging is enabled, false
	 * otherwise
	 */
	public void setSyncEnabled(boolean syncEnabled) {
		this.syncEnabled = syncEnabled;
	}

	/**
	 * If synchronization logging is enabled (see {@link #isSyncEnabled()
	 * isSyncEnabled()}), this method returns whether database actions received
	 * from the remote database should be logged as well. If this is false,
	 * then only local database actions are logged. The default is true.
	 * 
	 * @return true if remote database actions are logged, false if only local
	 * database actions are logged
	 */
	public boolean isSaveSyncedRemoteActions() {
		return saveSyncedRemoteActions;
	}

	/**
	 * If synchronization logging is enabled (see {@link
	 * #setSyncEnabled(boolean) setSyncEnabled()}), this method determines
	 * whether database actions received from the remote database should be
	 * logged as well. If this is false, then only local database actions are
	 * logged. The default is true.
	 * 
	 * @param saveSyncedRemoteActions true if remote database actions are
	 * logged, false if only local database actions are logged
	 */
	public void setSaveSyncedRemoteActions(boolean saveSyncedRemoteActions) {
		this.saveSyncedRemoteActions = saveSyncedRemoteActions;
	}

	/**
	 * Returns whether initialisation of the database has been finished. This
	 * means that the database structure will not change anymore and related
	 * queries can be cached. This method just returns what has been set with
	 * {@link #setDatabaseInitialised(boolean) setDatabaseInitialised()}
	 * (default false). The {@link DatabaseConnection DatabaseConnection}
	 * controls this when you obtain a database from it.
	 *
	 * @return true if the initialisation of the database has been finished
	 */
	public boolean isDatabaseInitialised() {
		return databaseInitialised;
	}

	/**
	 * Sets whether initialisation of the database has been finished. This
	 * means that the database structure will not change anymore and related
	 * queries can be cached. The default is false. The {@link
	 * DatabaseConnection DatabaseConnection} controls this when you obtain a
	 * database from it.
	 *
	 * @param databaseInitialised true if the initialisation has been finished,
	 * false otherwise
	 */
	public void setDatabaseInitialised(boolean databaseInitialised) {
		this.databaseInitialised = databaseInitialised;
	}

	/**
	 * Initialises a table according to the specified definition. If the table
	 * does not exist, it will be created. If it exists, this method will get
	 * its version from the metadata table and then upgrade it, until it has the
	 * current version that is obtained from the definition. The current version
	 * will be written to the metadata table.
	 * 
	 * <p>This method automatically initialises the special metadata table as
	 * well. In fact, it will call itself and pass {@link TableMetadataTableDef
	 * TableMetadataTableDef}, which is handled specially. Users should not
	 * pass this class.</p>
	 *
	 * <p>When you have initialised the last table, you can call {@link
	 * #setDatabaseInitialised(boolean) setDatabaseInitialised()} to enable
	 * caching and get better performance. This is done automatically when you
	 * obtain a database from {@link DatabaseConnection
	 * DatabaseConnection}.</p>
	 * 
	 * @param tableDef the table definition
	 * @throws DatabaseException if a database error occurs
	 */
	public void initTable(DatabaseTableDef<?> tableDef)
			throws DatabaseException {
		if ((tableDef instanceof TableMetadataTableDef) && metaInitialised)
			return;
		TableMetadataTableDef metaDef = new TableMetadataTableDef();
		List<Class<? extends DatabaseTableDef<?>>> reservedTables =
				new ArrayList<Class<? extends DatabaseTableDef<?>>>();
		reservedTables.add(UserTableKeyTable.class);
		reservedTables.add(SyncProgressTableDef.class);
		List<String> tables = selectDbTablesOnInit();
		if (tableDef instanceof TableMetadataTableDef) {
			if (!tables.contains(tableDef.getName()))
				createTable(tableDef);
		} else if (!reservedTables.contains(tableDef.getClass()) &&
				!metaInitialised) {
			initTable(metaDef);
		}
		DatabaseCriteria criteria = new DatabaseCriteria.And(
				new DatabaseCriteria.Equal("table", tableDef.getName()),
				new DatabaseCriteria.Equal("key", TableMetadata.KEY_VERSION)
			);
		TableMetadata meta = selectOne(metaDef, criteria, null);
		if (meta == null) {
			if (tables.contains(tableDef.getName()))
				dropDbTable(tableDef.getName());
			createTable(tableDef);
		} else {
			int version = Integer.parseInt(meta.getValue());
			if (version < tableDef.getCurrentVersion()) {
				while (version < tableDef.getCurrentVersion()) {
					version = tableDef.upgradeTable(version, this);
				}
				meta.setValue(Integer.toString(version));
				update(metaDef.getName(), meta);
				criteria = new DatabaseCriteria.And(
						new DatabaseCriteria.Equal("table", tableDef.getName()),
						new DatabaseCriteria.Equal("key",
								TableMetadata.KEY_FIELDS)
				);
				meta = selectOne(metaDef, criteria, null);
				TableMetadata metaFields = getTableMetadataFields(tableDef);
				if (meta == null) {
					insert(metaDef.getName(), metaFields);
				} else {
					meta.setValue(metaFields.getValue());
					update(metaDef.getName(), meta);
				}
			}
		}
		if (tableDef instanceof TableMetadataTableDef) {
			for (Class<? extends DatabaseTableDef<?>> reservedClass :
					reservedTables) {
				DatabaseTableDef<?> reserved;
				try {
					reserved = reservedClass.newInstance();
				} catch (Exception ex) {
					throw new RuntimeException(
							"Can't initialise reserved table: " +
							reservedClass.getName());
				}
				initTable(reserved);
			}
			metaInitialised = true;
			initTable(new UpgradeDatabaseActionTable());
			List<UserTableKey> userTableKeys = select(new UserTableKeyTable(),
					null, 0, null);
			for (UserTableKey userTableKey : userTableKeys) {
				initTable(new DatabaseActionTable(userTableKey));
			}
		}
	}

	/**
	 * Creates a table for storing the specified type of objects. The table
	 * will have an ID column (primary key) and a column for each class field
	 * that is annotated with {@link DatabaseField DatabaseField}. The "id"
	 * field should not be annotated.
	 * 
	 * <p>This method is called from {@link #initTable(DatabaseTableDef)
	 * initTable()} if the table does not exist yet. It will write the current
	 * version to the metadata table.</p>
	 * 
	 * @param tableDef the table definition
	 * @throws DatabaseException if a database error occurs
	 */
	private void createTable(DatabaseTableDef<?> tableDef)
			throws DatabaseException {
		Class<? extends DatabaseObject> dataClass = tableDef.getDataClass();
		List<DatabaseColumnDef> cols = getDatabaseObjectColumns(dataClass);
		createTable(tableDef.getName(), cols);
		for (DatabaseIndex index : tableDef.getCompoundIndices()) {
			createIndex(tableDef.getName(), index);
		}
		TableMetadata meta = new TableMetadata();
		meta.setTable(tableDef.getName());
		meta.setKey(TableMetadata.KEY_VERSION);
		meta.setValue(Integer.toString(tableDef.getCurrentVersion()));
		insert(TableMetadataTableDef.NAME, meta);
		meta = getTableMetadataFields(tableDef);
		insert(TableMetadataTableDef.NAME, meta);
		DatabaseCache cache = DatabaseCache.getInstance();
		cache.addMetaTable(this, tableDef.getName());
		cache.addTableOnInit(this, tableDef.getName());
	}

	/**
	 * Returns a TableMetadata object with key "fields". The value is a JSON
	 * array with the field names, excluding "id".
	 *
	 * @param tableDef the table definition
	 * @return the metadata object with the table fields
	 */
	private TableMetadata getTableMetadataFields(DatabaseTableDef<?> tableDef) {
		TableMetadata meta = new TableMetadata();
		meta.setTable(tableDef.getName());
		meta.setKey(TableMetadata.KEY_FIELDS);
		List<String> fields = DatabaseFieldScanner.getDatabaseFieldNames(
				tableDef.getDataClass());
		ObjectMapper mapper = new ObjectMapper();
		try {
			meta.setValue(mapper.writeValueAsString(fields));
		} catch (JsonProcessingException ex) {
			throw new RuntimeException("Can't convert field names to JSON: " +
					ex.getMessage(), ex);
		}
		return meta;
	}

	/**
	 * Creates the database column definition for the specified database
	 * object class. This includes all class fields that are annotated with
	 * {@link DatabaseField DatabaseField} (this is not the id field).
	 *
	 * @param clazz the database object class
	 * @return the column definitions
	 */
	protected List<DatabaseColumnDef> getDatabaseObjectColumns(
			Class<? extends DatabaseObject> clazz) {
		List<DatabaseFieldSpec> fields =
				DatabaseFieldScanner.getDatabaseFields(clazz);
		List<DatabaseColumnDef> cols = new ArrayList<DatabaseColumnDef>();
		for (DatabaseFieldSpec fieldSpec : fields) {
			Field field = fieldSpec.getPropSpec().getField();
			DatabaseField annot = field.getAnnotation(DatabaseField.class);
			if (annot != null) {
				DatabaseColumnDef colDef = new DatabaseColumnDef(
						field.getName(), annot.value(), annot.index());
				if (annot.value() == DatabaseType.LIST ||
						annot.value() == DatabaseType.MAP) {
					colDef.setElemType(annot.elemType());
				} else if (annot.value() == DatabaseType.OBJECT) {
					colDef.setObjClass(field.getType().asSubclass(
							DatabaseObject.class));
				} else if (annot.value() == DatabaseType.OBJECT_LIST) {
					Class<?> objClass;
					if (field.getType().isArray()) {
						objClass = field.getType().getComponentType();
					} else {
						objClass = ReflectionUtils.getGenericListElementType(
								field);
					}
					colDef.setObjClass(objClass.asSubclass(
							DatabaseObject.class));
				}
				cols.add(colDef);
			}
		}
		return cols;
	}
	
	/**
	 * Creates a table with an ID column (primary key) and the specified list
	 * of columns.
	 * 
	 * @param table the table name (lower case)
	 * @param columns the columns to create (without the ID column)
	 * @throws DatabaseException if a database error occurs
	 */
	protected abstract void createTable(String table,
			List<DatabaseColumnDef> columns)
			throws DatabaseException;
	
	/**
	 * Creates an index on a table. Single-field indices can be defined in the
	 * {@link DatabaseColumnDef DatabaseColumnDef} and they are created
	 * automatically in {@link #initTable(DatabaseTableDef) initTable()} or
	 * {@link #createTable(String, List) createTable()}.
	 * 
	 * @param table the table name
	 * @param index the index
	 * @throws DatabaseException if a database error occurs
	 */
	public abstract void createIndex(String table, DatabaseIndex index)
			throws DatabaseException;
	
	/**
	 * Drops an index from a table.
	 * 
	 * @param table the table name
	 * @param name the index name
	 * @throws DatabaseException if a database error occurs
	 */
	public abstract void dropIndex(String table, String name)
			throws DatabaseException;

	/**
	 * Returns the names of the database fields for the specified table. This
	 * is read from the metadata table. It excludes field "id".
	 *
	 * @param table the table name
	 * @return the table fields
	 * @throws DatabaseException if a database error occurs
	 */
	public List<String> getTableFields(String table) throws DatabaseException {
		List<String> tableFields = DatabaseCache.getInstance().getTableFields(
				this, table);
		if (tableFields != null)
			return tableFields;
		DatabaseCriteria criteria = new DatabaseCriteria.And(
				new DatabaseCriteria.Equal("table", table),
				new DatabaseCriteria.Equal("key", TableMetadata.KEY_FIELDS)
		);
		TableMetadata meta = selectOne(new TableMetadataTableDef(), criteria,
				null);
		ObjectMapper mapper = new ObjectMapper();
		List<?> list;
		try {
			list = mapper.readValue(meta.getValue(), List.class);
		} catch (Exception ex) {
			throw new RuntimeException("Can't parse JSON array: " +
					ex.getMessage(), ex);
		}
		List<String> fields = new ArrayList<String>();
		for (Object item : list) {
			fields.add((String)item);
		}
		if (databaseInitialised)
			DatabaseCache.getInstance().setTableFields(this, table, fields);
		return fields;
	}
	
	/**
	 * Adds a column to the specified table.
	 * 
	 * @param table the table name (lower case)
	 * @param column the column definition
	 * @throws DatabaseException if a database error occurs
	 */
	public abstract void addColumn(String table, DatabaseColumnDef column)
			throws DatabaseException;
	
	/**
	 * Drops a column from the specified table. The ID column should never be
	 * dropped.
	 * 
	 * @param table the table name (lower case)
	 * @param column the name of the column to drop
	 * @throws DatabaseException if a database error occurs
	 */
	public abstract void dropColumn(String table, String column)
			throws DatabaseException;
	
	/**
	 * Renames a column in the specified table. The ID column should never be
	 * renamed.
	 * 
	 * @param table the table name (lower case)
	 * @param oldName the old column name
	 * @param newName the new column name
	 * @throws DatabaseException if a database error occurs
	 */
	public abstract void renameColumn(String table, String oldName,
			String newName) throws DatabaseException;
	
	/**
	 * Selects all tables that have been created using {@link
	 * #initTable(DatabaseTableDef) initTable()}, including reserved tables
	 * (whose name start with an underscore). It reads the table names from
	 * the metadata table. It creates the metadata table if it doesn't exist.
	 * The returned table names are ordered by name.
	 * 
	 * @return the table names (lower case)
	 * @throws DatabaseException if a database error occurs
	 */
	public List<String> selectTables() throws DatabaseException {
		List<String> selectedTables = DatabaseCache.getInstance()
				.getMetaTables(this);
		if (selectedTables != null)
			return selectedTables;
		TableMetadataTableDef metaDef = new TableMetadataTableDef();
		initTable(metaDef);
		List<TableMetadata> metas = select(metaDef, null, 0, null);
		List<String> tables = new ArrayList<String>();
		for (TableMetadata meta : metas) {
			if (!tables.contains(meta.getTable()))
				tables.add(meta.getTable());
		}
		Collections.sort(tables);
		DatabaseCache.getInstance().setMetaTables(this, tables);
		return new ArrayList<String>(tables);
	}
	
	/**
	 * Returns the database tables that existed when the database was first
	 * initialized and that were added later to the metadata table. The first
	 * set is defined at the start of the application, or when the database is
	 * created again, either because it was dropped with {@link
	 * DatabaseConnection#dropDatabase(String)
	 * DatabaseConnection.dropDatabase()} or because it was dropped manually
	 * outside the application.
	 * 
	 * <p>The returned list will not contain any subtables that were created
	 * after the database was first initialized.</p>
	 * 
	 * @return the database tables
	 * @throws DatabaseException if a database error occurs
	 */
	private List<String> selectDbTablesOnInit() throws DatabaseException {
		List<String> initTables = DatabaseCache.getInstance().getTablesOnInit(
				this);
		if (initTables != null)
			return initTables;
		initTables = selectDbTables();
		DatabaseCache.getInstance().setTablesOnInit(this, initTables);
		return initTables;
	}
	
	/**
	 * Selects the names of all tables, ordered by name. Some databases don't
	 * return empty tables.
	 * 
	 * @return the tables (lower case)
	 * @throws DatabaseException if a database error occurs
	 */
	protected abstract List<String> selectDbTables() throws DatabaseException;
	
	/**
	 * Drops the specified table. It also deletes related metadata and it
	 * deletes the table from the {@link DatabaseCache DatabaseCache}.
	 * 
	 * @param table the table name (lower case)
	 * @throws DatabaseException if a database error occurs
	 */
	public void dropTable(String table) throws DatabaseException {
		dropDbTable(table);
		DatabaseCriteria criteria = new DatabaseCriteria.Equal("table", table);
		delete(TableMetadataTableDef.NAME, criteria);
		List<UserTableKey> userTableKeys = select(new UserTableKeyTable(),
				criteria, 0, null);
		for (UserTableKey key : userTableKeys) {
			DatabaseActionTable actionTable = new DatabaseActionTable(key);
			dropTable(actionTable.getName());
		}
		delete(UserTableKeyTable.NAME, criteria);
		DatabaseCache.getInstance().removeTable(this, table);
	}

	/**
	 * Drops the specified table. Any related subtables should be deleted as
	 * well. It does not delete any related metadata or cached data.
	 * 
	 * @param table the table name (lower case)
	 * @throws DatabaseException if a database error occurs
	 */
	protected abstract void dropDbTable(String table) throws DatabaseException;
	
	/**
	 * Begins a transaction. Currently there is no rollback functionality, but
	 * a transaction can be used to speed up a sequence of write queries. At
	 * the end call {@link #commitTransaction() commitTransaction()}.
	 * 
	 * @throws DatabaseException if a database error occurs
	 */
	public abstract void beginTransaction() throws DatabaseException;

	/**
	 * Commits a transaction. This should be called after {@link
	 * #beginTransaction() beginTransaction()}.
	 * 
	 * @throws DatabaseException if a database error occurs
	 */
	public abstract void commitTransaction() throws DatabaseException;
	
	/**
	 * Inserts an object into a table. If the object ID is null, the database
	 * will generate an ID and set it in the object. This method writes the
	 * object fields that are annotated with {@link DatabaseField
	 * DatabaseField}. The "id" field should not be annotated.
	 *
	 * <p>If audit or sync logging is enabled and the table is not one of the
	 * reserved tables, this method will add the insert action to the table
	 * _action_log.</p>
	 *
	 * @param table the table name (lower case)
	 * @param value the object to insert
	 * @throws DatabaseException if a database error occurs
	 * @see DatabaseObjectMapper
	 */
	public void insert(String table, DatabaseObject value)
			throws DatabaseException {
		List<DatabaseObject> values = new ArrayList<DatabaseObject>();
		values.add(value);
		insert(table, values);
	}

	/**
	 * Inserts one or more objects into a table. If an object ID is null, the
	 * database will generate an ID and set it in the object. This method
	 * writes the object fields that are annotated with {@link DatabaseField
	 * DatabaseField}. The "id" field should not be annotated.
	 *
	 * <p>If audit or sync logging is enabled and the table is not one of the
	 * reserved tables, this method will add the insert action to the table
	 * _action_log.</p>
	 *
	 * @param table the table name (lower case)
	 * @param values the objects to insert
	 * @throws DatabaseException if a database error occurs
	 * @see DatabaseObjectMapper
	 */
	public void insert(String table, List<? extends DatabaseObject> values)
			throws DatabaseException {
		DatabaseObjectMapper mapper = new DatabaseObjectMapper();
		List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
		for (DatabaseObject value : values) {
			maps.add(mapper.objectToMap(value));
		}
		insertMaps(table, maps);
		Iterator<Map<String,Object>> mapIt = maps.iterator();
		Iterator<? extends DatabaseObject> objIt = values.iterator();
		while (mapIt.hasNext()) {
			Map<String,Object> map = mapIt.next();
			DatabaseObject obj = objIt.next();
			obj.setId((String)map.get("id"));
		}
	}

	/**
	 * Inserts one or more records into a table. Each record is specified as
	 * a map. The keys are the column names. The values should be obtained with
	 * {@link DatabaseObjectMapper DatabaseObjectMapper}. If the map has a key
	 * "id", it will be used as the ID. Otherwise the database should generate
	 * an ID and then set it in the map.
	 *
	 * <p>If audit or sync logging is enabled and the table is not one of the
	 * reserved tables, this method will add the insert action to the table
	 * _action_log with source "local". See also {@link
	 * #insertMaps(String, List, String) insert(table, values, source)}.</p>
	 *
	 * @param table the table name (lower case)
	 * @param values the records to insert
	 * @throws DatabaseException if a database error occurs
	 */
	public void insertMaps(String table, List<Map<String,Object>> values)
			throws DatabaseException {
		insertMaps(table, values, DatabaseAction.SOURCE_LOCAL);
	}

	/**
	 * Inserts one or more records into a table. Each record is specified as
	 * a map. The keys are the column names. The values should be obtained with
	 * {@link DatabaseObjectMapper DatabaseObjectMapper}. If the map has a key
	 * "id", it will be used as the ID. Otherwise the database should generate
	 * an ID and then set it in the map.
	 *
	 * <p>If audit or sync logging is enabled and the table is not one of the
	 * reserved tables, this method will add the insert action to the table
	 * _action_log with the specified source.</p>
	 *
	 * <p>You should normally call {@link #insertMaps(String, List)
	 * insertMaps(table, values)}. This method with source parameter is used
	 * by synchronisers.</p>
	 *
	 * @param table the table name (lower case)
	 * @param values the records to insert
	 * @param source the source of the action. See {@link
	 * DatabaseAction#setSource(String) DatabaseAction.setSource()}.
	 * @throws DatabaseException if a database error occurs
	 */
	public void insertMaps(String table, List<Map<String,Object>> values,
			String source) throws DatabaseException {
		doInsertMaps(table, values);
		boolean syncLog = syncEnabled && (
				source.equals(DatabaseAction.SOURCE_LOCAL) ||
				saveSyncedRemoteActions);
		if (!table.startsWith("_") && (auditLogEnabled || syncLog)) {
			writeDatabaseActions(table, DatabaseAction.Action.INSERT, values,
					values, source);
		}
		if (!table.startsWith("_")) {
			DatabaseListenerRepository.getInstance().notifyInsert(name, table,
					values);
		}
	}
	
	/**
	 * This method runs the insert action as described in {@link
	 * #insertMaps(String, List) insertMaps()} without action logging.
	 *
	 * @param table the table name (lower case)
	 * @param values the records to insert
	 * @throws DatabaseException if a database error occurs
	 */
	protected abstract void doInsertMaps(String table,
			List<Map<String,Object>> values) throws DatabaseException;

	/**
	 * Selects objects from a database table. This method returns a list of
	 * objects of the table's data class. It will write the "id" field and
	 * fields that are annotated with {@link DatabaseField DatabaseField}. The
	 * "id" field should not be annotated.
	 *
	 * <p>If audit logging is enabled and the table is not one of the reserved
	 * tables, this method will add the select action to the table
	 * _action_log.</p>
	 *
	 * @param table the table (lower case)
	 * @param criteria the criteria for the objects to return. This can be
	 * null.
	 * @param limit the maximum number of objects to return. Set this to 0 or
	 * less to get all records.
	 * @param sort the order in which the objects are returned. This can be
	 * null or an empty array if no sorting is needed.
	 * @param <T> the type of database object
	 * @return the objects
	 * @throws DatabaseException if a database error occurs
	 * @see DatabaseObjectMapper
	 */
	public <T extends DatabaseObject> List<T> select(DatabaseTableDef<T> table,
			DatabaseCriteria criteria, int limit, DatabaseSort[] sort)
			throws DatabaseException {
		List<Map<String,?>> maps = selectMaps(table.getName(), criteria, limit,
				sort);
		List<T> result = new ArrayList<T>();
		DatabaseObjectMapper mapper = new DatabaseObjectMapper();
		for (Map<String,?> map : maps) {
			result.add(mapper.mapToObject(map, table.getDataClass()));
		}
		return result;
	}
	
	/**
	 * Selects one object from a database table. If more than one object
	 * matches, it returns the first object according to the specified sort
	 * order. If no object matches, it returns null. This method returns an
	 * object of the table's data class. It will write the "id" field and
	 * fields that are annotated with {@link DatabaseField DatabaseField}. The
	 * "id" field should not be annotated.
	 *
	 * <p>If audit logging is enabled and the table is not one of the reserved
	 * tables, this method will add the select action to the table
	 * _action_log.</p>
	 *
	 * @param table the table (lower case)
	 * @param criteria the criteria for the object to return. This can be
	 * null.
	 * @param sort the order in which the objects are returned. This can be
	 * null or an empty array if no sorting is needed.
	 * @param <T> the type of database object
	 * @return the object or null
	 * @throws DatabaseException if a database error occurs
	 * @see DatabaseObjectMapper
	 */
	public <T extends DatabaseObject> T selectOne(DatabaseTableDef<T> table,
			DatabaseCriteria criteria, DatabaseSort[] sort)
			throws DatabaseException {
		List<T> list = select(table, criteria, 1, sort);
		if (list.isEmpty())
			return null;
		else
			return list.get(0);
	}
	
	/**
	 * Selects records from a database table. This method returns a list of
	 * data maps. Each map should at least have a key "id". The keys are the
	 * column names. In some databases the column names are in lower case, so
	 * they may not exactly match the field names of a {@link DatabaseObject
	 * DatabaseObject}.</p>
	 *
	 * <p>Users should normally not call this method but use one of the select()
	 * methods. This method may be used if no {@link DatabaseObject
	 * DatabaseObject} class is available. In particular this can happen during
	 * a database upgrade, when reading data from an old version of the table
	 * that doesn't match the latest {@link DatabaseObject DatabaseObject}
	 * class.</p>
	 *
	 * <p>If audit logging is enabled and the table is not one of the reserved
	 * tables, this method will add the select action to the table
	 * _action_log with source "local".</p>
	 *
	 * @param table the table name (lower case)
	 * @param criteria the criteria for the records to return. This can be
	 * null.
	 * @param limit the maximum number of records to return. Set this to 0 or
	 * less to get all records.
	 * @param sort the order in which the records are returned. This can be
	 * null or an empty array if no sorting is needed.
	 * @return the records (keys may be in lower case)
	 * @throws DatabaseException if a database error occurs
	 */
	public List<Map<String,?>> selectMaps(String table,
			DatabaseCriteria criteria, int limit, DatabaseSort[] sort)
			throws DatabaseException {
		List<Map<String,?>> result = doSelectMaps(table, criteria, limit, sort);
		if (!result.isEmpty() && !table.startsWith("_") && auditLogEnabled) {
			writeDatabaseActions(table, DatabaseAction.Action.SELECT, result,
					null, DatabaseAction.SOURCE_LOCAL);
		}
		return result;
	}

	/**
	 * This method runs the select action as described in {@link
	 * #selectMaps(String, DatabaseCriteria, int, DatabaseSort[]) selectMaps()}
	 * without action logging.
	 *
	 * @param table the table name (lower case)
	 * @param criteria the criteria for the records to return. This can be
	 * null.
	 * @param limit the maximum number of records to return. Set this to 0 or
	 * less to get all records.
	 * @param sort the order in which the records are returned. This can be
	 * null or an empty array if no sorting is needed.
	 * @return the records (keys may be in lower case)
	 * @throws DatabaseException if a database error occurs
	 */
	protected abstract List<Map<String,?>> doSelectMaps(String table,
			DatabaseCriteria criteria, int limit, DatabaseSort[] sort)
			throws DatabaseException;

	/**
	 * Counts the number of records in a table that match the specified
	 * criteria.
	 * 
	 * @param table the table name (lower case)
	 * @param criteria the criteria. This can be null.
	 * @return the number of records
	 * @throws DatabaseException if a database error occurs
	 */
	public abstract int count(String table, DatabaseCriteria criteria)
			throws DatabaseException;

	/**
	 * Updates the specified object in the database. The object should already
	 * be in the specified table and its ID should not be changed.
	 *
	 * <p>If audit or sync logging is enabled and the table is not one of the
	 * reserved tables, this method will add the update action to the table
	 * _action_log.</p>
	 *
	 * @param table the table name (lower case)
	 * @param object the object
	 * @throws DatabaseException if a database error occurs
	 */
	public void update(String table, DatabaseObject object)
			throws DatabaseException {
		DatabaseCriteria criteria = new DatabaseCriteria.Equal("id",
				object.getId());
		DatabaseObjectMapper mapper = new DatabaseObjectMapper();
		Map<String,Object> map = mapper.objectToMap(object);
		map.remove("id");
		update(table, criteria, map);
	}

	/**
	 * Updates all records that match the specified criteria. The keys in the
	 * map "values" are the columns that should be set. The values should be
	 * obtained with {@link DatabaseObjectMapper DatabaseObjectMapper}.
	 *
	 * <p>If audit or sync logging is enabled and the table is not one of the
	 * reserved tables, this method will add the update action to the table
	 * _action_log with source "local". See also {@link
	 * #update(String, DatabaseCriteria, Map, String)
	 * update(table, criteria, values, source)}.</p>
	 *
	 * @param table the table name (lower case)
	 * @param criteria the criteria for the records to update. This can be
	 * null.
	 * @param values the column values that should be set
	 * @throws DatabaseException if a database error occurs
	 */
	public void update(String table, DatabaseCriteria criteria,
			Map<String,?> values) throws DatabaseException {
		update(table, criteria, values, DatabaseAction.SOURCE_LOCAL);
	}

	/**
	 * Updates all records that match the specified criteria. The keys in the
	 * map "values" are the columns that should be set. The values should be
	 * obtained with {@link DatabaseObjectMapper DatabaseObjectMapper}.
	 *
	 * <p>If audit or sync logging is enabled and the table is not one of the
	 * reserved tables, this method will add the update action to the table
	 * _action_log with the specified source.</p>
	 *
	 * <p>You should normally call {@link #update(String, DatabaseCriteria, Map)
	 * update(table, criteria, values)}. This method with source parameter is
	 * used by synchronisers.</p>
	 *
	 * @param table the table name (lower case)
	 * @param criteria the criteria for the records to update. This can be
	 * null.
	 * @param values the column values that should be set
	 * @param source the source of the action. See {@link
	 * DatabaseAction#setSource(String) DatabaseAction.setSource()}.
	 * @throws DatabaseException if a database error occurs
	 */
	public void update(String table, DatabaseCriteria criteria,
			Map<String,?> values, String source) throws DatabaseException {
		doUpdate(table, criteria, values);
		boolean syncLog = syncEnabled && (
				source.equals(DatabaseAction.SOURCE_LOCAL) ||
				saveSyncedRemoteActions);
		if (!table.startsWith("_") && (auditLogEnabled || syncLog)) {
			List<? extends Map<String,?>> records = selectLogRecords(table,
					criteria);
			List<Map<String,?>> valueList = new ArrayList<Map<String,?>>();
			for (int i = 0; i < records.size(); i++) {
				valueList.add(values);
			}
			writeDatabaseActions(table, DatabaseAction.Action.UPDATE, records,
					valueList, source);
		}
		if (!table.startsWith("_")) {
			DatabaseListenerRepository.getInstance().notifyUpdate(name, table,
					criteria, values);
		}
	}

	/**
	 * This method runs the update action as described in {@link
	 * #update(String, DatabaseCriteria, Map) update()} without action logging.
	 *
	 * @param table the table name (lower case)
	 * @param criteria the criteria for the records to update. This can be
	 * null.
	 * @param values the column values that should be set
	 * @throws DatabaseException if a database error occurs
	 */
	protected abstract void doUpdate(String table, DatabaseCriteria criteria,
			Map<String,?> values) throws DatabaseException;

	/**
	 * Deletes the specified object in the database.
	 *
	 * <p>If audit or sync logging is enabled and the table is not one of the
	 * reserved tables, this method will add the delete action to the table
	 * _action_log.</p>
	 *
	 * @param table the table name (lower case)
	 * @param object the object
	 * @throws DatabaseException if a database error occurs
	 */
	public void delete(String table, DatabaseObject object)
			throws DatabaseException {
		DatabaseCriteria criteria = new DatabaseCriteria.Equal("id",
				object.getId());
		delete(table, criteria);
	}

	/**
	 * Deletes all records that match the specified criteria.
	 *
	 * <p>If audit or sync logging is enabled and the table is not one of the
	 * reserved tables, this method will add the delete action to the table
	 * _action_log with source "local". See also {@link
	 * #delete(String, DatabaseCriteria, String)
	 * delete(table, criteria, source)}.</p>
	 *
	 * @param table the table name (lower case)
	 * @param criteria the criteria. This can be null.
	 * @throws DatabaseException if a database error occurs
	 */
	public void delete(String table, DatabaseCriteria criteria)
			throws DatabaseException {
		delete(table, criteria, DatabaseAction.SOURCE_LOCAL);
	}

	/**
	 * Deletes all records that match the specified criteria.
	 *
	 * <p>If audit or sync logging is enabled and the table is not one of the
	 * reserved tables, this method will add the delete action to the table
	 * _action_log with the specified source.</p>
	 *
	 * <p>You should normally call {@link #delete(String, DatabaseCriteria)
	 * delete(table, criteria)}. This method with source parameter is used
	 * by synchronisers.</p>
	 *
	 * @param table the table name (lower case)
	 * @param criteria the criteria. This can be null.
	 * @param source the source of the action. See {@link
	 * DatabaseAction#setSource(String) DatabaseAction.setSource()}.
	 * @throws DatabaseException if a database error occurs
	 */
	public void delete(String table, DatabaseCriteria criteria, String source)
			throws DatabaseException {
		List<? extends Map<String,?>> records =
				new ArrayList<Map<String,String>>();
		boolean syncLog = syncEnabled && (
				source.equals(DatabaseAction.SOURCE_LOCAL) ||
				saveSyncedRemoteActions);
		if (!table.startsWith("_") && (auditLogEnabled || syncLog)) {
			records = selectLogRecords(table, criteria);
		}
		doDelete(table, criteria);
		if (!records.isEmpty() && !table.startsWith("_") &&
				(auditLogEnabled || syncLog)) {
			writeDatabaseActions(table, DatabaseAction.Action.DELETE, records,
					null, source);
		}
		if (!table.startsWith("_")) {
			DatabaseListenerRepository.getInstance().notifyDelete(name, table,
					criteria);
		}
	}
	
	/**
	 * Writes database actions to the {@link DatabaseActionTable
	 * DatabaseActionTable}s. The lists "records" and "values" must
	 * have the same length.
	 * 
	 * <p>A record should have the following keys:</p>
	 *
	 * <p><ul>
	 * <li>id (required): the record ID</li>
	 * <li>user (optional): if the record has a "user" field, its value should
	 * be set here. Otherwise it can be omitted or set to null.</li>
	 * </ul></p>
	 *
	 * <p>A value map should contain the data that is associated with the
	 * action. It will be written as a JSON object. For an insert, it contains
	 * the complete record including "id". For an update it contains the
	 * updated columns. For a select or delete, a value map is always null and
	 * you can set the entire "values" list to null.</p>
	 * 
	 * @param table the table where the action was written
	 * @param action the action
	 * @param records the affected records
	 * @param values the action data or null
	 * @param source the source
	 * @throws DatabaseException if a database error occurs
	 */
	private void writeDatabaseActions(String table,
			DatabaseAction.Action action,
			List<? extends Map<String,?>> records,
			List<? extends Map<String,?>> values, String source)
			throws DatabaseException {
		Object tableLock = DatabaseLockCollection.getLock(name, table);
		synchronized (tableLock) {
			List<DatabaseAction> actions = new ArrayList<DatabaseAction>();
			String currUser = null;
			DatabaseActionTable actionTable = null;
			Iterator<? extends Map<String,?>> recordIt = records.iterator();
			Iterator<? extends Map<String,?>> valuesIt = null;
			if (values != null)
				valuesIt = values.iterator();
			while (recordIt.hasNext()) {
				Map<String,?> record = recordIt.next();
				Map<String,?> data = null;
				if (valuesIt != null)
					data = valuesIt.next();
				String recUser = (String)record.get("user");
				if (actionTable == null ||
						((recUser == null) != (currUser == null)) ||
						(recUser != null && !recUser.equals(currUser))) {
					if (actionTable != null && actions.size() > 0)
						insert(actionTable.getName(), actions);
					actions.clear();
					currUser = recUser;
					actionTable = DatabaseCache.getInstance()
							.initActionTable(this, currUser, table);
				}
				addDatabaseAction(actionTable, actions, action, record, data,
						source);
			}
			if (actionTable != null && actions.size() > 0) {
				insert(actionTable.getName(), actions);
				DatabaseListenerRepository.getInstance()
						.notifyAddDatabaseActions(name, table, actions);
			}
		}
	}

	/**
	 * Creates a database action for logging and adds it to the specified list
	 * "actions". The list should contain actions that occurred earlier than
	 * this action and that have not been written to the database yet. The
	 * parameter "record" should have the following keys:
	 *
	 * <p><ul>
	 * <li>id (required): the record ID</li>
	 * <li>user (optional): if the record has a "user" field, its value should
	 * be set here. Otherwise it can be omitted or set to null.</li>
	 * </ul></p>
	 *
	 * <p>The parameter "data" should contain the data that is associated with
	 * the action. It will be written as a JSON object. For an insert, it
	 * contains the complete record including "id". For an update it contains
	 * the updated columns. For a select or delete, the data is null.</p>
	 *
	 * @param actionTable the database action table to which the action should
	 * eventually be written. This method does not write to the table.
	 * @param actions the list to which the action should be added
	 * @param action the action
	 * @param record the record
	 * @param data the data that is associated with the action
	 * @throws DatabaseException if a database error occurs
	 */
	private void addDatabaseAction(DatabaseActionTable actionTable,
			List<DatabaseAction> actions, DatabaseAction.Action action,
			Map<String,?> record, Map<String,?> data, String source)
			throws DatabaseException {
		VirtualClock clock = VirtualClock.getInstance();
		long now = clock.currentTimeMillis();
		String table = actionTable.getUserTableKey().getTable();
		DatabaseAction prevAction = null;
		for (int i = actions.size() - 1; i >= 0; i--) {
			DatabaseAction current = actions.get(i);
			if (current.getTable().equals(table)) {
				prevAction = current;
				break;
			}
		}
		if (prevAction == null) {
			DatabaseCriteria criteria = new DatabaseCriteria.Equal(
					"table", table);
			DatabaseSort[] sort = new DatabaseSort[] {
					new DatabaseSort("time", false),
					new DatabaseSort("order", false)
			};
			prevAction = selectOne(actionTable, criteria, sort);
		}
		int order = 0;
		if (prevAction != null && prevAction.getTime() >= now) {
			now = prevAction.getTime();
			order = prevAction.getOrder() + 1;
		}
		DatabaseAction dbAction = new DatabaseAction();
		dbAction.setTable(table);
		if (record.containsKey("user"))
			dbAction.setUser((String)record.get("user"));
		dbAction.setAction(action);
		dbAction.setRecordId((String)record.get("id"));
		if (data != null && (auditLogData || syncEnabled)) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			try {
				dbAction.setJsonData(mapper.writeValueAsString(data));
			} catch (JsonProcessingException ex) {
				throw new RuntimeException("Can't convert data to JSON: " +
						ex.getMessage(), ex);
			}
		}
		dbAction.setTime(now);
		dbAction.setOrder(order);
		dbAction.setSource(source);
		if (auditLogEnabled)
			dbAction.setAuthor(auditLogUser);
		Object sampleTime = record.get("utcTime");
		if (sampleTime != null && sampleTime instanceof Long)
			dbAction.setSampleTime((Long)sampleTime);
		actions.add(dbAction);
	}

	/**
	 * This method runs the delete action as described in {@link
	 * #delete(String, DatabaseCriteria) delete()} without action logging.
	 *
	 * @param table the table name (lower case)
	 * @param criteria the criteria. This can be null.
	 * @throws DatabaseException if a database error occurs
	 */
	protected abstract void doDelete(String table, DatabaseCriteria criteria)
			throws DatabaseException;

	/**
	 * Selects records that match the specified criteria. The result is used
	 * to log a database action. Each record should be a map with the following
	 * keys:
	 *
	 * <p><ul>
	 * <li>id (required): the record ID</li>
	 * <li>user (optional): if the record has a "user" field, its value should
	 * be set here. Otherwise it can be omitted or set to null.</li>
	 * <li>utcTime (optional): if the record has a "utcTime" field, its value
	 * should be set here. Otherwise it can be omitted or set to null.</li>
	 * </ul></p>
	 *
	 * the IDs of the records that match the specified critiera.
	 *
	 * @param table the table name (lower case)
	 * @param criteria the criteria. This can be null.
	 * @return the records
	 * @throws DatabaseException if a database error occurs
	 */
	protected abstract List<? extends Map<String,?>> selectLogRecords(
			String table, DatabaseCriteria criteria) throws DatabaseException;
}
