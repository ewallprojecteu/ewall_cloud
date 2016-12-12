package eu.ewall.platform.idss.wellbeingads.model.physicalactivity;

/**
 * The Enum AggregationUnit.
 */
public enum AggregationUnit {
	
	/** The hour. */
	HOUR("h"),
	
	/** The day. */
	DAY("d"),
	
	/** The week. */
	WEEK("wk"),
	
	/** The month. */
	MONTH("mo");

	/** The symbol. */
	private String symbol;
	
	/**
	 * Instantiates a new aggregation unit.
	 *
	 * @param symbol the symbol
	 */
	private AggregationUnit(String symbol) {
		this.symbol = symbol;
	}
	
	/**
	 * Symbol.
	 *
	 * @return the string
	 */
	public String symbol() {
		return symbol;
	}
	
	/**
	 * From.
	 *
	 * @param symbol the symbol
	 * @return the aggregation unit
	 */
	public static AggregationUnit from(String symbol) {
		if (HOUR.symbol.equals(symbol)) {
			return HOUR;
		} else if (DAY.symbol.equals(symbol)) {
			return DAY;
		} else if (WEEK.symbol.equals(symbol)) {
			return WEEK;
		} else if (MONTH.symbol.equals(symbol)) {
			return MONTH;
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return symbol;
	}
	
}
