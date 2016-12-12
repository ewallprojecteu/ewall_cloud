package eu.ewall.platform.idss.service.model.state.domain;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;

/**
 * Definition of the database table that can store {@link
 * PhysicalActivityStateModel PhysicalActivityStateModel} objects.
 * 
 * @author Dennis Hofs (RRD)
 */
public class PhysicalActivityStateModelTable extends DatabaseTableDef<PhysicalActivityStateModel> {
	public static final String NAME = "phys_act_state";
	
	private static final int VERSION = 1;

	public PhysicalActivityStateModelTable() {
		super(NAME, PhysicalActivityStateModel.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db) throws DatabaseException {
		return VERSION;
	}

}
