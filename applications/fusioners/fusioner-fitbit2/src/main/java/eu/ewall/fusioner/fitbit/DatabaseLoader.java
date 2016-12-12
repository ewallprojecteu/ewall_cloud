package eu.ewall.fusioner.fitbit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import eu.ewall.fusioner.fitbit.model.ActivitySummaryTable;
import eu.ewall.fusioner.fitbit.model.FitbitAuthRequestTable;
import eu.ewall.fusioner.fitbit.model.FitbitLinkTable;
import eu.ewall.fusioner.fitbit.model.FitbitUpdateTable;
import eu.ewall.fusioner.fitbit.model.SleepSummaryTable;
import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseConnection;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseFactory;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.utils.AppComponents;

public class DatabaseLoader {
	public static DatabaseConnection connect() throws IOException {
		DatabaseFactory dbFactory = AppComponents.getInstance().getComponent(
				DatabaseFactory.class);
		return dbFactory.connect();
	}
	
	public static Database initDatabase(DatabaseConnection dbConn,
			String dbName) throws DatabaseException {
		List<DatabaseTableDef<?>> tables =
				new ArrayList<DatabaseTableDef<?>>();
		tables.add(new FitbitAuthRequestTable());
		tables.add(new FitbitLinkTable());
		tables.add(new FitbitUpdateTable());
		tables.add(new ActivitySummaryTable());
		tables.add(new SleepSummaryTable());
		return dbConn.initDatabase(dbName, tables, false);
	}
}
