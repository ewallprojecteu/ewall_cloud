package eu.ewall.platform.reasoner.activitycoach.service.dialogue;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.service.model.common.dialogue.DialogueInstance;

public class DialogueInstanceTable extends DatabaseTableDef<DialogueInstance> {
	public static final String NAME = "dialogue_instances";
	
	private static final int VERSION = 0;

	public DialogueInstanceTable() {
		super(NAME, DialogueInstance.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db)
			throws DatabaseException {
		return 0;
	}
}
