package eu.ewall.platform.reasoner.activitycoach.service;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.service.model.common.PerformanceMetric;

public class PerformanceMetricTable extends DatabaseTableDef<PerformanceMetric> {

	protected static final String NAME = "performance-metrics";	
	private static final int VERSION = 1;

	
	public PerformanceMetricTable() {
		super(NAME, PerformanceMetric.class, VERSION);
	}


	@Override
	public int upgradeTable(int version, Database db) throws DatabaseException {
		if (version == 0)
			return upgradeTableV0(db);
		return 1;
	}
	
	private int upgradeTableV0(Database db) throws DatabaseException {
		db.dropTable(NAME);
		db.initTable(this);
		return VERSION;
	}
	
}