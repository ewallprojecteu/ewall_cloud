package eu.ewall.platform.idss.dao;

/**
 * This class defines how a set of database records should be sorted on a
 * column when the records are retrieved. A list of instances indicates that
 * the records should first be sorted on the first column, then on the second
 * column and so on.
 * 
 * <p>Note that string comparisons are sensitive to case and diacritics. This
 * is normal in MongoDB and SQLite, but different than the default in
 * MySQL.</p>
 * 
 * @author Dennis Hofs (RRD)
 */
public class DatabaseSort {
	private String column;
	private boolean ascending;
	
	/**
	 * Constructs a new instance. Note that string comparisons are sensitive to
	 * case and diacritics. This is normal in MongoDB and SQLite, but different
	 * than the default in MySQL.
	 * 
	 * @param column the column name
	 * @param ascending true if the column should be sorted in ascending order,
	 * false if it should be sorted in descending order
	 */
	public DatabaseSort(String column, boolean ascending) {
		this.column = column;
		this.ascending = ascending;
	}
	
	/**
	 * Returns the column name.
	 * 
	 * @return the column name
	 */
	public String getColumn() {
		return column;
	}
	
	/**
	 * Returns true if the column should be sorted in ascending order, false if
	 * it should be sorted in descending order.
	 * 
	 * @return true if the column should be sorted in ascending order, false if
	 * it should be sorted in descending order
	 */
	public boolean isAscending() {
		return ascending;
	}
}
