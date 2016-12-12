package eu.ewall.platform.lr.habits.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseConnection;
import eu.ewall.platform.idss.dao.DatabaseCriteria;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseFactory;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.service.RemoteMethodException;
import eu.ewall.platform.idss.service.ScheduledService;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.lr.habits.model.FunctioningActivityEvent;
import eu.ewall.platform.lr.habits.model.FunctioningActivityHistory;
import eu.ewall.platform.lr.habits.model.Habits;
import eu.ewall.platform.lr.habits.persistence.HabitsTable;
import eu.ewall.platform.lr.habits.math.DoubleVector;
import eu.ewall.platform.lr.habits.math.DenseDoubleVector;
import eu.ewall.platform.lr.habits.math.MeanShiftClustering;

public class LRHabitsService extends ScheduledService{
public static final String LOGTAG = "LRHabitsService";
	
	private LRInputProvider inputProvider;
	private Logger logger;
	private DatabaseConnection dbConn = null;
	private String dbName;

	
	public LRHabitsService() {
		
	}
	
	public LRHabitsService(String dbName) throws Exception {
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
		logger.debug("Start task: lifestyle reasoner (habits)");
		Database database = getDatabase(dbConn);
		List<IDSSUserProfile> users = inputProvider.getUsers();
		for (IDSSUserProfile user : users) {
			runUserTask(database, user);
		}
		logger.debug("End task: lifestyle reasoner (habits)");
		
	}

	private void runUserTask(Database database, IDSSUserProfile user) throws IOException, RemoteMethodException, Exception {
		//17 april - newest data in db
	//	DateTime from = new DateTime(user.getTimeZone()).withDate(2016, 4, 16);
		String[] activityNames = {"cooking", "sleeping", "showering", "entertaining", "sanitary_visits", "houseworks", "getting_out", "visits", "undefined"};
		int activityCntr =0;
		DateTime to = new DateTime(user.getTimeZone());
		DateTime from = new DateTime(to.minusDays(28));
		from = from.withHourOfDay(0);
		from = from.withMinuteOfHour(0);
		from = from.withSecondOfMinute(0);
		
		FunctioningActivityHistory	fourWeeksHistory = inputProvider.getFunctioningActivityData(user.getUsername(), from, to);
		
	    int i = fourWeeksHistory.getFunctioningActivityEvents().size();
		int j = 0;
		int dayOfWeek;
		
	    DateTime temp;
	 
	    
		for(dayOfWeek=1;dayOfWeek<8;dayOfWeek++)
		{
			String dow = null;
			if(dayOfWeek==1)
				dow = "MONDAY";
			if(dayOfWeek==2)
				dow = "TUESDAY";
			if(dayOfWeek==3)
				dow = "WEDNESDAY";
			if(dayOfWeek==4)
				dow = "THURSDAY";
			if(dayOfWeek==5)
				dow = "FRIDAY";
			if(dayOfWeek==6)
				dow = "SATURDAY";
			if(dayOfWeek==7)
				dow = "SUNDAY";
			j=0;
			List<DoubleVector> Data = new ArrayList<>();
			while(j<i)
			{
				
			if(fourWeeksHistory.getFunctioningActivityEvents().get(j).getTimestamp().getDayOfWeek()==dayOfWeek)
			{
				temp=fourWeeksHistory.getFunctioningActivityEvents().get(j).getTimestamp();
				//logger.info(" "+ user.getUsername() + temp + fourWeeksHistory.getFunctioningActivityEvents().get(j).getFunctioningActivity());
			    Data.add(new DenseDoubleVector(new double[] { temp.getMillisOfDay() }));
			 
			}
			j++;
		}
			if(!(Data.isEmpty()))
			{
			List<DoubleVector> centers = MeanShiftClustering.cluster(Data, 2000, 500, 2000, false);
			if(!centers.isEmpty())
			{
				Habits hbts = new Habits();;
				deleteHabits(user.getUsername());
				int cntr_habits =0;
				while(cntr_habits<centers.size())
				{
					hbts.setUsername(user.getUsername());
					hbts.setHabitDayOfWeek(dow);
					hbts.setHabitType(activityNames[activityCntr]);
					hbts.setHabitTimeOfDay(centers.get(cntr_habits).toString());
					setHabits(hbts);
					cntr_habits++;
				}
				
			}
			
			}
		
			
		}
	}
	
	
	public List<Habits> getHabits(String username) throws IOException, DatabaseException {
		
		DatabaseConnection dbConn = openDatabaseConnection();
		try {
			Database database = getDatabase(dbConn);
			DatabaseCriteria criteria;
			
			criteria = new DatabaseCriteria.Equal("username", username);
			return database.select(new HabitsTable(),criteria,50,null);
		} finally {
			closeDatabaseConnection(dbConn);
		}
	}
	
	private Database getDatabase(DatabaseConnection dbConn) throws DatabaseException {
		List<DatabaseTableDef<?>> tableDefs =
				new ArrayList<DatabaseTableDef<?>>();
		
		tableDefs.add(new HabitsTable());
		
		return dbConn.initDatabase(dbName, tableDefs, false);
	}

	public void deleteHabits(String username) throws IOException, DatabaseException {
		DatabaseConnection dbConn = openDatabaseConnection();
		try {
			Database database = getDatabase(dbConn);
			DatabaseCriteria criteria;
			criteria = new DatabaseCriteria.And(
					new DatabaseCriteria.Equal("username", username)
			);
			database.delete(HabitsTable.NAME, criteria);
		} finally {
			closeDatabaseConnection(dbConn);
		}
	}

	public void setHabits(Habits ctr) throws IOException, DatabaseException {
		DatabaseConnection dbConn = openDatabaseConnection();
		try {
			Database database = getDatabase(dbConn);
	
			database.insert(HabitsTable.NAME, ctr);
		} finally {
			closeDatabaseConnection(dbConn);
		}
	}

}
