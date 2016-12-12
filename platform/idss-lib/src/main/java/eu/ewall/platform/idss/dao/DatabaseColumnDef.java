package eu.ewall.platform.idss.dao;

/**
 * The definition of a database column.
 * 
 * @author Dennis Hofs (RRD)
 */
public class DatabaseColumnDef {
	private String name;
	private DatabaseType type;
	private DatabaseType elemType = null;
	private Class<? extends DatabaseObject> objClass = null;
	private boolean index = false;
	
	/**
	 * Constructs a new instance. For types {@link DatabaseType#LIST LIST} and
	 * {@link DatabaseType#MAP MAP}, you must call {@link
	 * #setElemType(DatabaseType) setElemType()}. For types {@link
	 * DatabaseType#OBJECT OBJECT} and {@link DatabaseType#OBJECT_LIST
	 * OBJECT_LIST}, you must call {@link #setObjClass(Class) setObjClass()}.
	 * 
	 * @param name the column name
	 * @param type the data type
	 */
	public DatabaseColumnDef(String name, DatabaseType type) {
		this.name = name;
		this.type = type;
	}
	
	/**
	 * Constructs a new instance. For primitive types (not {@link
	 * DatabaseType#LIST LIST}, {@link DatabaseType#MAP MAP}, {@link
	 * DatabaseType#OBJECT OBJECT} or {@link DatabaseType#OBJECT_LIST
	 * OBJECT_LIST}) you may set "index" to true, so the database will create
	 * index to make select queries run faster. For types {@link
	 * DatabaseType#LIST LIST} and {@link DatabaseType#MAP MAP}, you must call
	 * {@link #setElemType(DatabaseType) setElemType()}. For types {@link
	 * DatabaseType#OBJECT OBJECT} and {@link DatabaseType#OBJECT_LIST
	 * OBJECT_LIST}, you must call {@link #setObjClass(Class) setObjClass()}.
	 * 
	 * @param name the column name the column name
	 * @param type the data type the data type
	 * @param index true if the database should create an index, false
	 * otherwise
	 */
	public DatabaseColumnDef(String name, DatabaseType type, boolean index) {
		this.name = name;
		this.type = type;
		this.index = index;
	}
	
	/**
	 * Returns the column name.
	 * 
	 * @return the column name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the data type.
	 * 
	 * @return the data type
	 */
	public DatabaseType getType() {
		return type;
	}

	/**
	 * Returns the database type of the elements in {@link DatabaseType#LIST
	 * LIST} or {@link DatabaseType#MAP MAP}. For other types this should be
	 * null. The element type should not be {@link DatabaseType#LIST LIST},
	 * {@link DatabaseType#MAP MAP}, {@link DatabaseType#OBJECT OBJECT} or
	 * {@link DatabaseType#OBJECT_LIST OBJECT_LIST}.
	 * 
	 * @return the element type or null
	 */
	public DatabaseType getElemType() {
		return elemType;
	}

	/**
	 * Sets the database type of the elements in {@link DatabaseType#LIST
	 * LIST} or {@link DatabaseType#MAP MAP}. For other types this should be
	 * null. The element type should not be {@link DatabaseType#LIST LIST},
	 * {@link DatabaseType#MAP MAP}, {@link DatabaseType#OBJECT OBJECT} or
	 * {@link DatabaseType#OBJECT_LIST OBJECT_LIST}.
	 * 
	 * @param elemType the element type
	 */
	public void setElemType(DatabaseType elemType) {
		this.elemType = elemType;
	}

	/**
	 * Returns the class of database object in {@link DatabaseType#OBJECT
	 * OBJECT} or {@link DatabaseType#OBJECT_LIST OBJECT_LIST}. For other types
	 * this should be null.
	 *
	 * @return the object class or null
	 */
	public Class<? extends DatabaseObject> getObjClass() {
		return objClass;
	}

	/**
	 * Sets the class of database object in {@link DatabaseType#OBJECT OBJECT}
	 * or {@link DatabaseType#OBJECT_LIST OBJECT_LIST}. For other types this
	 * should be null.
	 *
	 * @param objClass the object class
	 */
	public void setObjClass(Class<? extends DatabaseObject> objClass) {
		this.objClass = objClass;
	}

	/**
	 * Returns whether the database should create an index on this column to
	 * make select queries run faster. This is only applicable for primitive
	 * data types (not {@link DatabaseType#LIST LIST}, {@link DatabaseType#MAP
	 * MAP}, {@link DatabaseType#OBJECT OBJECT} or {@link
	 * DatabaseType#OBJECT_LIST OBJECT_LIST}). The default is false.
	 * 
	 * @return true if the database should create an index, false otherwise
	 */
	public boolean isIndex() {
		return index;
	}

	/**
	 * Sets whether the database should create an index on this column to make
	 * select queries run faster. This is only applicable for primitive data
	 * types (not {@link DatabaseType#LIST LIST}, {@link DatabaseType#MAP MAP},
	 * {@link DatabaseType#OBJECT OBJECT} or {@link DatabaseType#OBJECT_LIST
	 * OBJECT_LIST}). The default is false.
	 * 
	 * @param index true if the database should create an index, false
	 * otherwise
	 */
	public void setIndex(boolean index) {
		this.index = index;
	}
}
