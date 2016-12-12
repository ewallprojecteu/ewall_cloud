package eu.ewall.platform.lr.mood.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.joda.time.LocalDate;
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
import eu.ewall.platform.idss.service.model.common.MoodDataResponse;
import eu.ewall.platform.idss.service.model.type.MoodCategory;
import eu.ewall.platform.idss.service.model.type.MoodTrend;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.lr.mood.MoodDataItem;

public class LRMoodService extends ScheduledService{

public static final String LOGTAG = "LRMoodService";
	
	private LRInputProvider inputProvider;
	private Logger logger;
	private DatabaseConnection dbConn = null;
	private String dbName;

	public LRMoodService(String dbName) throws Exception {
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
	
	public void setInputProvider(LRInputProvider inputProvider) {
		this.inputProvider = inputProvider;
	}
	
	@Override
	public void runTask() throws Exception {
		logger.debug("Start task: lifestyle reasoner (mood)");
		Database database = getDatabase(dbConn);
		List<IDSSUserProfile> users = inputProvider.getUsers();
		for (IDSSUserProfile user : users) {
			runUserTask(database, user);
		}
		logger.debug("End task: lifestyle reasoner (mood)");
		
	}

	private void runUserTask(Database database, IDSSUserProfile user) throws IOException, RemoteMethodException, Exception {
		//start from jan 1 2014 or the last update in last_update for the user
		
		LocalDate firstDate = new LocalDate(2015,1,1);
		//get last update
		DatabaseCriteria criteria = new DatabaseCriteria.Equal("user",
				user.getUsername());
		LastUpdate last = database.selectOne(
				new LastUpdateTable(), criteria, null);
		
		if(last == null) {
			//start from start date
			LocalDate date = new LocalDate(firstDate);
			while(date.compareTo(LocalDate.now()) < 0) {
				calculateMood(database, date, user);
				date = date.plusDays(1);
			}
		} else {
			//start from last update date
			LocalDate date = new LocalDate(last.getDate()).plusDays(1);
			while(date.compareTo(LocalDate.now()) < 0) {
				calculateMood(database, date, user);
				date = date.plusDays(1);
			}
		}
		
		
	}
	
	private LastUpdate getLastUpdate(String user) throws IOException, DatabaseException {
		DatabaseConnection dbConn = openDatabaseConnection();
		try {
			Database database = getDatabase(dbConn);
			DatabaseCriteria criteria = new DatabaseCriteria.Equal("user",
					user);
			return database.selectOne(new LastUpdateTable(), criteria,
					null);
		} finally {
			closeDatabaseConnection(dbConn);
		}
	}
	
	private void calculateMood(Database database, LocalDate date, IDSSUserProfile user) throws IOException, RemoteMethodException, Exception {
		
		List<MoodDataItem> data = inputProvider.getMoodDataItem(user, date);
		
		Valence today = new Valence(data,0);
		
		MoodTrend trend = defineTrend(date,user,today);
		
		System.out.println(trend.toString());
		
		if(!today.isEmpty()) {
			
			UserMood usermood= new UserMood();
			usermood.setDate(date);
			usermood.setUser(user.getUsername());			
			usermood.setTrend(trend);
			
			if(today.getValence() >= 0)
				usermood.setMood(MoodCategory.POSITIVE);
			else
				usermood.setMood(MoodCategory.NEGATIVE);
			 
			database.insert(UserMoodTable.NAME, usermood);
			
		}
		LastUpdate last = getLastUpdate(user.getUsername());
		LastUpdate update = new LastUpdate();
		
		if(last == null) {
			update.setDate(date);
			update.setUser(user.getUsername());
			database.insert(LastUpdateTable.NAME, update);
		} else {
			last.setDate(date);
			last.setUser(user.getUsername());
			database.update(LastUpdateTable.NAME, last);
		}
	}
	
	private MoodTrend defineTrend(LocalDate date, IDSSUserProfile user, Valence today) throws IOException, RemoteMethodException, Exception {
		
		Valence days4 = 
				new Valence(inputProvider.getMoodDataItem(user, date.minusDays(4)),4);
		Valence days3 = 
				new Valence(inputProvider.getMoodDataItem(user, date.minusDays(3)),3);
		Valence days2 = 
				new Valence(inputProvider.getMoodDataItem(user, date.minusDays(2)),2);
		Valence days1 = 
				new Valence(inputProvider.getMoodDataItem(user, date.minusDays(1)),1);
		
		List<Valence> valenceList = new ArrayList<Valence>();
		
		if(!days4.isEmpty())
			valenceList.add(days4);
		if(!days3.isEmpty())
			valenceList.add(days3);
		if(!days2.isEmpty())
			valenceList.add(days2);
		if(!days1.isEmpty())
			valenceList.add(days1);
		if(!today.isEmpty())
			valenceList.add(today);
		
		if(valenceList.isEmpty()) {
			return MoodTrend.NO_CHANGE;
		}
		
		Collections.sort(valenceList, new Comparator<Valence>(){
		     public int compare(Valence o1, Valence o2){
		        
		         return o2.getMinusDays() - o1.getMinusDays();
		     }
		});
		for(Valence test : valenceList) {
			System.out.println(test.getValence());
		}
		
		double result = 0;
		if(valenceList.size() == 5) {
			result = (valenceList.get(0).getValence()*-0.2)+
					(valenceList.get(1).getValence()*-0.1)+
					(valenceList.get(2).getValence()*0)+
					(valenceList.get(3).getValence()*0.1)+
					(valenceList.get(4).getValence()*0.2);
		} else if(valenceList.size() == 4) {
			result = (valenceList.get(0).getValence()*-0.3)+
					(valenceList.get(1).getValence()*-0.1)+
					(valenceList.get(2).getValence()*0.1)+
					(valenceList.get(3).getValence()*0.3);
		} else if(valenceList.size() == 3) {
			result = (valenceList.get(0).getValence()*-0.5)+
					(valenceList.get(1).getValence()*0)+
					(valenceList.get(2).getValence()*0.5);
		} else if(valenceList.size() == 2) {
			result = (valenceList.get(0).getValence()*-1)+
					(valenceList.get(1).getValence()*1);
		} else if(valenceList.size() == 1) {
			return MoodTrend.NO_CHANGE;
		} else {
			return MoodTrend.NO_CHANGE;
		}
		
		if(result > 0) {
			return MoodTrend.INCREASING;
		} else if (result < 0) {
			return MoodTrend.DECREASING;
		} else {
			return MoodTrend.NO_CHANGE;
		}
		
	}
	
	private Database getDatabase(DatabaseConnection dbConn) throws DatabaseException {
		List<DatabaseTableDef<?>> tableDefs =
				new ArrayList<DatabaseTableDef<?>>();
		tableDefs.add(new UserMoodTable());
		tableDefs.add(new LastUpdateTable());
		
		return dbConn.initDatabase(dbName, tableDefs, false);
	}

	public MoodDataResponse getMood4Period(String userid, LocalDate from, LocalDate to) 
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
			
			return new MoodDataResponse(database.select(new MoodDataTable(), 
					criteria, 0, sort));
		} finally {
			closeDatabaseConnection(dbConn);
		}
	}
}
