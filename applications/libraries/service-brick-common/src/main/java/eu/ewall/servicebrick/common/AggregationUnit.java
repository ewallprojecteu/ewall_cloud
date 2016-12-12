package eu.ewall.servicebrick.common;

public enum AggregationUnit {
	
	HOUR("h"),
	DAY("d"),
	WEEK("wk"),
	MONTH("mo");

	private String symbol;
	
	private AggregationUnit(String symbol) {
		this.symbol = symbol;
	}
	
	public String symbol() {
		return symbol;
	}
	
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

	@Override
	public String toString() {
		return symbol;
	}
	
}
