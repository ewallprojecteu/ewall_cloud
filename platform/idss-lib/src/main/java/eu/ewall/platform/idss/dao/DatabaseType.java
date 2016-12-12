package eu.ewall.platform.idss.dao;

/**
 * The possible types of database columns. See {@link DatabaseObjectMapper
 * DatabaseObjectMapper} for more details.
 * 
 * @author Dennis Hofs (RRD)
 */
public enum DatabaseType {
	BYTE,
	SHORT,
	INT,
	LONG,
	FLOAT,
	DOUBLE,
	STRING,
	TEXT,
	TEXT_MB4,
	DATE,
	TIME,
	DATETIME,
	ISOTIME,
	LIST,
	MAP,
	OBJECT,
	OBJECT_LIST
}
