package eu.ewall.platform.idss.service.model.state.interaction;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;

public class InteractionModelTable extends DatabaseTableDef<InteractionModel> {
	public static final String NAME = "interactionmodel";
	
	private static final int VERSION = 0;

	public InteractionModelTable() {
		super(NAME, InteractionModel.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db)
			throws DatabaseException {
		return 0;
	}
}
