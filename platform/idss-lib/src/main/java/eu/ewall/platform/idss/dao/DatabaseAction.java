package eu.ewall.platform.idss.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class models a database action that can be logged for audit logging or
 * for synchronisation with a remote database.
 *
 * @author Dennis Hofs (RRD)
 */
public class DatabaseAction extends AbstractDatabaseObject {
	public static final String SOURCE_LOCAL = "local";
	
	@JsonIgnore
	private String id;

	@DatabaseField(value=DatabaseType.STRING)
	private String table;
	@DatabaseField(value=DatabaseType.STRING)
	private String user = null;
	@DatabaseField(value=DatabaseType.STRING)
	private Action action;
	@DatabaseField(value=DatabaseType.STRING)
	private String recordId;
	@DatabaseField(value=DatabaseType.TEXT)
	private String jsonData = null;
	// "sampleTime" and "time" are stored as LONG instead of ISOTIME so times
	// can be compared with GreaterThan / LessThan
	@DatabaseField(value=DatabaseType.LONG, index=true)
	private Long sampleTime = null;
	@DatabaseField(value=DatabaseType.LONG)
	private long time;
	@DatabaseField(value=DatabaseType.INT)
	private int order = 0;
	@DatabaseField(value=DatabaseType.STRING)
	private String source = SOURCE_LOCAL;
	@DatabaseField(value=DatabaseType.STRING)
	private String author = null;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the table on which the action was run.
	 *
	 * @return the table name
	 */
	public String getTable() {
		return table;
	}

	/**
	 * Sets the table on which the action was run.
	 *
	 * @param table the table name
	 */
	public void setTable(String table) {
		this.table = table;
	}

	/**
	 * Returns the user whose data was affected by this action. If the data does
	 * not belong to a user, this method returns null.
	 *
	 * <p>The user should be obtained from the "user" field of a database
	 * object. Objects without a "user" field don't belong to a user.</p>
	 *
	 * @return the user name or null
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the user whose data was affected by this action. If the data does
	 * not belong to a user, you should set this to null.
	 *
	 * <p>The user should be obtained from the "user" field of a database
	 * object. Objects without a "user" field don't belong to a user.</p>
	 *
	 * @param user the user name
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Returns the action.
	 *
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * Sets the action.
	 *
	 * @param action the action
	 */
	public void setAction(Action action) {
		this.action = action;
	}

	/**
	 * Returns the ID of the record that was affected by the action.
	 *
	 * @return the record ID
	 */
	public String getRecordId() {
		return recordId;
	}

	/**
	 * Sets the ID of the record that was affected by the action.
	 *
	 * @param recordId the record ID
	 */
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	/**
	 * Returns the data associated with the action, as a JSON object. For an
	 * insert, it contains the complete record including "id". For an update it
	 * contains the updated columns. For a select or delete, the data is null.
	 *
	 * @return the JSON data or null
	 */
	public String getJsonData() {
		return jsonData;
	}

	/**
	 * Sets the data associated with the action, as a JSON object. For an
	 * insert, it contains the complete record including "id". For an update it
	 * contains the updated columns. For a select or delete, the data is null.
	 *
	 * @param jsonData the JSON data or null
	 */
	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	/**
	 * If this is an action on a sample table, this method returns the time of
	 * the sample that was affected by the action. Otherwise it returns null.
	 * 
	 * @return the sample time or null
	 */
	public Long getSampleTime() {
		return sampleTime;
	}

	/**
	 * This method only applies for database actions on sample tables. It sets
	 * the time of the sample that was affected by the action.
	 * 
	 * @param sampleTime the sample time
	 */
	public void setSampleTime(Long sampleTime) {
		this.sampleTime = sampleTime;
	}

	/**
	 * Returns the time at which the action was run.
	 *
	 * @return the time at which the action was run
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Sets the time at which the action was run.
	 *
	 * @param time the time at which the action was run
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * Returns an order number. This can be used to order sequential actions
	 * that occurred within the same millisecond. Actions with another time
	 * or parallel actions may have the same number.
	 *
	 * @return the order number
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * Returns an order number. This can be used to order sequential actions
	 * that occurred within the same millisecond. Actions with another time
	 * or parallel actions may have the same number.
	 *
	 * @param order the order number
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * Returns the source of this action. This can be {@link #SOURCE_LOCAL
	 * SOURCE_LOCAL} (default) or the ID of a remote server. In the latter case,
	 * the action was added as a result of a synchronisation from the remote
	 * server.
	 *
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Sets the source of this action. This can be {@link #SOURCE_LOCAL
	 * SOURCE_LOCAL} (default) or the ID of a remote server. In the latter case,
	 * the action was added as a result of a synchronisation from the remote
	 * server.
	 *
	 * @param source the source
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * Returns the user who ran the action. This is only set for audit logging.
	 *
	 * @return the user name of the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Sets the user who ran the action. This should only be set for audit
	 * logging.
	 *
	 * @param author the user name of the author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * The possible actions.
	 */
	public enum Action {
		SELECT,
		INSERT,
		UPDATE,
		DELETE
	}
}
