package eu.ewall.platform.reasoner.activitycoach.service;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage;

/**
 * Definition of the database table that can store {@link PhysicalActivityMotivationalMessage
 * MotivationalMessage} objects.
 * 
 * @author Dennis Hofs
 */
public class MotivationalMessageTable
extends DatabaseTableDef<PhysicalActivityMotivationalMessage> {
	protected static final String NAME = "messages";
	
	private static final int VERSION = 1;

	public MotivationalMessageTable() {
		super(NAME, PhysicalActivityMotivationalMessage.class, VERSION);
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
