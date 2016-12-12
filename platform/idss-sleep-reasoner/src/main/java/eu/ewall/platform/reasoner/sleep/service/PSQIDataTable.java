package eu.ewall.platform.reasoner.sleep.service;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;

public class PSQIDataTable extends DatabaseTableDef<PSQIDataOutput>{

	public static final String NAME = "psqi";
	private static final int VERSION = 0;
	
	public PSQIDataTable() {
		super(NAME, PSQIDataOutput.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db) throws DatabaseException {
		return 0;
	}

}
