package eu.ewall.platform.idss.service.model.state.context;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;

/**
 * Definition of the database table that can store {@link ContextModel
 * ContextModel} objects.
 * 
 * @author Dennis Hofs (RRD)
 */
public class ContextModelTable extends DatabaseTableDef<ContextModel> {
	public static final String NAME = "contextmodel";
	
	private static final int VERSION = 0;

	public ContextModelTable() {
		super(NAME, ContextModel.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db)
			throws DatabaseException {
		return 0;
	}
}
