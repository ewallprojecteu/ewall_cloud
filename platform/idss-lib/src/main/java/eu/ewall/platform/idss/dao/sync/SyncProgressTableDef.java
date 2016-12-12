package eu.ewall.platform.idss.dao.sync;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;

/**
 * This is a special database table that is used for synchronisation with a
 * remote database. It keeps track of what data has already been synchronised
 * from the remote database to the local database. In the database it's stored
 * in a table with name "_sync_progress".
 *
 * @author Dennis Hofs (RRD)
 */
public class SyncProgressTableDef extends DatabaseTableDef<SyncProgress> {
	public static final String NAME = "_sync_progress";

	private static final int VERSION = 0;

	public SyncProgressTableDef() {
		super(NAME, SyncProgress.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db) throws DatabaseException {
		return 0;
	}
}
