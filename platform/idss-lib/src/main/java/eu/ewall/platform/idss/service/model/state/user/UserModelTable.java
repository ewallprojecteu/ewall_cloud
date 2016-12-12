package eu.ewall.platform.idss.service.model.state.user;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;

/**
 * Definition of the database table that can store {@link UserModel UserModel}
 * objects.
 * 
 * @author Dennis Hofs (RRD)
 */
public class UserModelTable extends DatabaseTableDef<UserModel> {
	public static final String NAME = "usermodel";
	
	private static final int VERSION = 0;

	public UserModelTable() {
		super(NAME, UserModel.class, VERSION);
	}

	@Override
	public int upgradeTable(int version, Database db)
			throws DatabaseException {
		return 0;
	}
}
