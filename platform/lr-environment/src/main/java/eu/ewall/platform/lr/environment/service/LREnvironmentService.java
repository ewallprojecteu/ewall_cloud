package eu.ewall.platform.lr.environment.service;

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

import eu.ewall.platform.commons.datamodel.profile.UserRole;
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
import eu.ewall.platform.lr.environment.model.IlluminanceEvent;
import eu.ewall.platform.lr.environment.model.IlluminanceHistory;
import eu.ewall.platform.lr.environment.model.IlluminanceNotification;
import eu.ewall.platform.lr.environment.persistence.IlluminanceNotificationTable;

public class LREnvironmentService extends ScheduledService{
public static final String LOGTAG = "LREnvironmentService";
	
	private LRInputProvider inputProvider;
	private Logger logger;
	private DatabaseConnection dbConn = null;
	private String dbName;
	private static final int threshold[] = {0,0,0,0,0,0,0,0,750,1500,1500,1500,1500,1500,1500,1500,1500,1500,1000,500,500,500,500,0};
	
	private Messages msg = new Messages();
	
	private String messageGeneral = msg.getProp().getProperty("message.general");
	private String messageMorning = msg.getProp().getProperty("message.morning");
	
	public String getMessageGeneral() {
		return messageGeneral;
	}

	public void setMessageGeneral(String messageGeneral) {
		this.messageGeneral = messageGeneral;
	}

	public String getMessageMorning() {
		return messageMorning;
	}

	public void setMessageMorning(String messageMorning) {
		this.messageMorning = messageMorning;
	}

	public LREnvironmentService() {
		
	}
	
	public LREnvironmentService(String dbName) throws Exception {
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
		logger.debug("Start task: lifestyle reasoner (environment)");
		Database database = getDatabase(dbConn);
		List<IDSSUserProfile> users = inputProvider.getUsers();
		for (IDSSUserProfile user : users) {
			System.out.println(user.getRole()+" == "+UserRole.PRIMARY_USER);
			if(user.getRole().equals(UserRole.PRIMARY_USER.toString())) {
				System.out.println(user.getUsername());
				runUserTask(database, user);
			}
		}
		logger.debug("End task: lifestyle reasoner (environment)");
		
	}

	private void runUserTask(Database database, IDSSUserProfile user) throws IOException, RemoteMethodException, Exception {
		//17 april - newest data in db
		//TODO change to second line
		DateTime from = new DateTime(user.getTimeZone()).withDate(2016, 4, 16);
//		DateTime from = new DateTime(user.getTimeZone());
		DateTime to = new DateTime(from.plusDays(1));
		Double ilumAverage[][] = new Double[7][24];
		int counter[] = new int[24];
		
		for(int i=1;i<=7;i++) {
			IlluminanceHistory dayHistory =
				inputProvider.getIlluminanceData(user, from.minusDays(i), to.minusDays(i), "livingroom");
			
			//so far so good...
			
			//might have null in illuminance data.. have to work-around that
			//have to work-around "no data: reports
			//
			
			
			
			
			
			//illuminance in hours
//			System.out.println(dayHistory.getIlluminanceEvents().isEmpty());
			if(!dayHistory.getIlluminanceEvents().isEmpty()) {
				for(IlluminanceEvent event : dayHistory.getIlluminanceEvents()) {
//					System.out.println(event.getLocation());
						if(event.getIlluminance() != null ) {
							ilumAverage[7-i][event.getTimestamp().getHourOfDay()] 
									= event.getIlluminance();
							
//								System.out.println(event.getIlluminance());
							
							counter[event.getTimestamp().getHourOfDay()]++;
						}
				}
				
			
//				get mean average
				Double ilum[] = new Double[24];
//				for(int ct=0;ct<24;ct++) {
//					ilum[ct] = 0d;
//				}
				for(int hour=0; hour<24; hour++) {
					ilum[hour] = 0d;
					for(int day = 0; day<7;day++) {
						if(ilumAverage[day][hour] != null) {
							ilum[hour] += ilumAverage[day][hour];
						}
					}
					if(counter[hour] != 0)
						ilum[hour] /= counter[hour];
				}
				boolean morningNTF = false;
				boolean generalNTF = false;
//				compare with threshold here
				for(int hour=0; hour<24;hour++) {
					if(ilum[hour] != null) {
						generalNTF = true;
						if(ilum[hour] > threshold[hour]) {
							
								morningNTF = true;
							
								
							}
							
							System.out.println("At "+hour+" : "+ilum[hour]);
						}
				}
	//if one notification exists for the current user of the specified type
	//update with the new one
				Random rand = new Random();
				int  days = rand.nextInt(7) + 1;
				LocalDate sendDate1 = new LocalDate().plusDays(days);
				LocalDate sendDate2 = sendDate1.plusDays(rand.nextInt(8-days)+1);
				IlluminanceNotification notification;
				//send general every week if we have data and send morning every week if data is out of bounds
				
				if(morningNTF) {
					if((notification = getNotification(user.getUsername(), "morning")) != null) {
						notification.setNotificationMessage(messageMorning.replace("{user}", user.getFirstName()));
						notification.setSendDate1(sendDate1);
						notification.setSendDate2(sendDate2);
						database.update(IlluminanceNotificationTable.NAME, notification);
					} else {
						notification = new IlluminanceNotification();
						notification.setUsername(user.getUsername());
						notification.setNotificationType("morning");
						notification.setNotificationMessage(messageMorning.replace("{user}", user.getFirstName()));//TODO set real message
						notification.setSendDate1(sendDate1);
						notification.setSendDate2(sendDate2);
						database.insert(IlluminanceNotificationTable.NAME, notification);
					}
				}
				if(generalNTF) {
					if((notification = getNotification(user.getUsername(), "general")) != null) {
						notification.setNotificationMessage(messageGeneral.replace("{user}", user.getFirstName()));
						notification.setSendDate1(sendDate1);
						notification.setSendDate2(sendDate2);
						database.update(IlluminanceNotificationTable.NAME, notification);
					} else {
						notification = new IlluminanceNotification();
						notification.setUsername(user.getUsername());
						notification.setNotificationType("general");
						notification.setNotificationMessage(messageGeneral.replace("{user}", user.getFirstName()));//TODO set real message
						notification.setSendDate1(sendDate1);
						notification.setSendDate2(sendDate2);
						database.insert(IlluminanceNotificationTable.NAME, notification);
					}
				}
			}
		}
	}
	
	
	public IlluminanceNotification getNotification(String username, String type) throws IOException, DatabaseException {
		
		DatabaseConnection dbConn = openDatabaseConnection();
		try {
			Database database = getDatabase(dbConn);
			DatabaseCriteria criteria;
			
			criteria = new DatabaseCriteria.And(
					new DatabaseCriteria.Equal("username", username),
					new DatabaseCriteria.Equal("notificationType", type)
			);
			return database.selectOne(new IlluminanceNotificationTable(), criteria,
					null);
		} finally {
			closeDatabaseConnection(dbConn);
		}
	}
	
	private Database getDatabase(DatabaseConnection dbConn) throws DatabaseException {
		List<DatabaseTableDef<?>> tableDefs =
				new ArrayList<DatabaseTableDef<?>>();
		
		tableDefs.add(new IlluminanceNotificationTable());
		
		return dbConn.initDatabase(dbName, tableDefs, false);
	}

	public void deleteNotification(IlluminanceNotification ntf) throws IOException, DatabaseException {
		DatabaseConnection dbConn = openDatabaseConnection();
		try {
			Database database = getDatabase(dbConn);
//			DatabaseCriteria criteria;
//			DatabaseObjectMapper mapper = new DatabaseObjectMapper();
//			
//			criteria = new DatabaseCriteria.And(
//					new DatabaseCriteria.Equal("username", ntf.getUsername()),
//					new DatabaseCriteria.Equal("notificationType", ntf.getNotificationType())
//			);
			database.delete(IlluminanceNotificationTable.NAME, ntf);
		} finally {
			closeDatabaseConnection(dbConn);
		}
	}

	public void notificationSent(IlluminanceNotification ntf) throws IOException, DatabaseException {
		DatabaseConnection dbConn = openDatabaseConnection();
		try {
			Database database = getDatabase(dbConn);
//			DatabaseCriteria criteria;
//			DatabaseObjectMapper mapper = new DatabaseObjectMapper();
//			
//			criteria = new DatabaseCriteria.And(
//					new DatabaseCriteria.Equal("username", ntf.getUsername()),
//					new DatabaseCriteria.Equal("notificationType", ntf.getNotificationType())
//			);
			ntf.setTimesSent(1);
			database.update(IlluminanceNotificationTable.NAME, ntf);
		} finally {
			closeDatabaseConnection(dbConn);
		}
	}

}
