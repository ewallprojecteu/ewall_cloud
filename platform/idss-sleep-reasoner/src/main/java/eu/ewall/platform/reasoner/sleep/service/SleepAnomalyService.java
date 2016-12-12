package eu.ewall.platform.reasoner.sleep.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.DateTime;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseConnection;
import eu.ewall.platform.idss.dao.DatabaseCriteria;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseFactory;
import eu.ewall.platform.idss.dao.DatabaseObjectMapper;
import eu.ewall.platform.idss.dao.DatabaseSort;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.service.RemoteMethodException;
import eu.ewall.platform.idss.service.ScheduledService;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.reasoner.sleep.psqi.SleepClassifier;

public class SleepAnomalyService extends ScheduledService{

public static final String LOGTAG = "SleepService";
	
	private InputProvider inputProvider;
	private Logger logger;
	private DatabaseConnection dbConn = null;
	private String dbName;

	public SleepAnomalyService(String dbName) throws Exception {
		this.dbName = dbName;
		AppComponents components = AppComponents.getInstance();
		ILoggerFactory logFactory = components.getComponent(
				ILoggerFactory.class);
		logger = logFactory.getLogger(LOGTAG);
		DatabaseFactory dbFactory = components.getComponent(
				DatabaseFactory.class);
		dbConn = dbFactory.connect();
		logger.info("Hello");
	}
	
	public void setInputProvider(InputProvider inputProvider) {
		this.inputProvider = inputProvider;
	}
	
	@Override
	public void runTask() throws Exception {
		logger.debug("Start task: idss (sleep)");
		Database database = getDatabase(dbConn);
		List<IDSSUserProfile> users = inputProvider.getUsers();
		for (IDSSUserProfile user : users) {
			runUserTask(database, user);
		}
		logger.debug("End task: idss (sleep)");
		
	}

	private void runUserTask(Database database, IDSSUserProfile user) throws IOException, RemoteMethodException, Exception {

		SleepClassifier detection = 
				new SleepClassifier(inputProvider.getInbed(user, new LocalDate()),
				inputProvider.getInactivity(user, new LocalDate()));
		
		PSQIDataOutput data = new PSQIDataOutput();
			data.setUser(user.getUsername());
			data.setDate(new LocalDate());
			data.setOverallSleepQuality(detection.OverallSleepQuality());
			data.setSleepLatency(detection.SleepLatency());
			data.setSleepDuration(detection.SleepDuration());
			data.setSleepEfficiency(detection.SleepEfficiency());
			data.setSleepDisturbance(detection.SleepDisturbance());
			data.setNeedMedicine(detection.NeedMedicine());
			data.setDayDysfunction(detection.DayDysfunction());
		
		database.insert(PSQIDataTable.NAME, data);
	}
	
	public PSQIDataResponse getPSQI(String userid, DateTime from, DateTime to) 
			throws DatabaseException, IOException {
		DatabaseConnection dbConn = openDatabaseConnection();
		try {
			Database database = getDatabase(dbConn);
			DatabaseCriteria criteria;
			DatabaseObjectMapper mapper = new DatabaseObjectMapper();
			
			criteria = new DatabaseCriteria.And(
					new DatabaseCriteria.Equal("user", userid),
					new DatabaseCriteria.GreaterEqual("date",
							(String)mapper.toPrimitiveDatabaseValue(from,
							DatabaseType.DATE)),
					new DatabaseCriteria.LessEqual("date",
							(String)mapper.toPrimitiveDatabaseValue(to,
							DatabaseType.DATE))
			);
			DatabaseSort[] sort = new DatabaseSort[] {
					new DatabaseSort("date", false)
			};
			
			return new PSQIDataResponse(database.select(new PSQIDataTable(), 
					criteria, 0, sort));
		} finally {
			closeDatabaseConnection(dbConn);
		}
	}
	
	private Database getDatabase(DatabaseConnection dbConn) throws DatabaseException {
		List<DatabaseTableDef<?>> tableDefs =
				new ArrayList<DatabaseTableDef<?>>();
		tableDefs.add(new PSQIDataTable());
		
		return dbConn.initDatabase(dbName, tableDefs, false);
	}

	
}
