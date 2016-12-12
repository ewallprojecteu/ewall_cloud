package eu.ewall.platform.idss.dao;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be added to a field in a {@link DatabaseObject
 * DatabaseObject} to indicate that it can be read from or written to the
 * database. The value specifies the database type. If this is {@link
 * DatabaseType#LIST LIST}, you must specify {@link #elemType() elemType()}.
 * 
 * <p>See {@link DatabaseObjectMapper DatabaseObjectMapper} for information
 * about the mapping between Java types and database types.</p>
 * 
 * @author Dennis Hofs (RRD)
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
@Documented
public @interface DatabaseField {
	
	/**
	 * The database type of this field.
	 * 
	 * @return the database type of this field
	 */
	DatabaseType value();

	/**
	 * True if the database should create an index on this field to make select
	 * queries run faster. The default is false. Compound indices can be
	 * defined in {@link DatabaseTableDef DatabaseTableDef}.
	 * 
	 * @return true if the database should create an index on this field, false
	 * otherwise
	 */
	boolean index() default false;
	
	/**
	 * The database type of the elements in {@link DatabaseType#LIST LIST} or
	 * {@link DatabaseType#MAP MAP}. This should not be evaluated for other
	 * field types. The element type should not be {@link DatabaseType#LIST
	 * LIST}, {@link DatabaseType#MAP MAP}, {@link DatabaseType#OBJECT OBJECT}
	 * or {@link DatabaseType#OBJECT_LIST OBJECT_LIST}.
	 * 
	 * @return the element type
	 */
	DatabaseType elemType() default DatabaseType.INT;
}
