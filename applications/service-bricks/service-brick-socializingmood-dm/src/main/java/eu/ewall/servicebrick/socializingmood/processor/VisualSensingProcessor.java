   package eu.ewall.servicebrick.socializingmood.processor;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import eu.ewall.platform.commons.datamodel.sensing.VisualSensing;
import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.EmotionalStateCategory;
import eu.ewall.servicebrick.common.dao.DataManagerUpdatesDao;
import eu.ewall.servicebrick.common.dao.ProfilingServerDao;
import eu.ewall.servicebrick.common.dao.SensorEventDao;
import eu.ewall.servicebrick.common.model.EmotionUpdate;
import eu.ewall.servicebrick.socializingmood.model.MoodUpdate;
import eu.ewall.servicebrick.socializingmood.dao.EmotionDao;
import eu.ewall.servicebrick.socializingmood.dao.MoodDao;


/**
 * Reads data from  VisualSensing at regular intervals and applies algorithms to determine Mood info.
 * The extrapolated data is saved on the DB for further retrieval by the service brick front end.
 */
@Component
public class VisualSensingProcessor {

	private static final Logger log = LoggerFactory.getLogger(VisualSensingProcessor.class);
	
	private static final int DEFAULT_MAX_DAYS_PER_REQUEST = 20;
	@Autowired
	private ProfilingServerDao profilingServerDao;
	@Autowired
	private EmotionDao emotionDao;
	@Autowired
	private MoodDao moodDao;
	@Autowired
	private DataManagerUpdatesDao dataManagerUpdatesDao;
	@Autowired
	private SensorEventDao sensorEventDao;
	
	@Value("${maxDaysPerRequest}")
	private String maxDaysPerRequestStr;

	@Value("${processing.startDateYYYYMMdd}")
	private String startDateYYYYMMdd;
	
    SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMdd");
	
	@Scheduled(initialDelayString="${processing.delay}", fixedRateString="${processing.interval}")
	public void process() {
		log.debug("Starting processing of visual sensing data");
		long start = System.currentTimeMillis();
		
		
		//Step 1: get primary users
		User[] primaryUsers = profilingServerDao.getPrimaryUsers();
		
		int nrOfUsers = primaryUsers.length;
		long nrOfMeasurements = 0;
		int maxDaysPerRequest;
		
		try {
			if(maxDaysPerRequestStr==null || Integer.parseInt(maxDaysPerRequestStr)<=0) {
				maxDaysPerRequest = DEFAULT_MAX_DAYS_PER_REQUEST;
			} else {
				maxDaysPerRequest = Integer.parseInt(maxDaysPerRequestStr);
			}
		} catch (NumberFormatException e1) {
			log.error("Wrong configuration value for maxDaysPerRequest: " + e1.getMessage());
			maxDaysPerRequest = DEFAULT_MAX_DAYS_PER_REQUEST;
		}
		
		DateTime dt = new DateTime();
		try {
			dt = new DateTime(sdf.parse(startDateYYYYMMdd));
		} catch (ParseException e1) {
			log.error("Error in configured starting date 'processing.startDateYYYYMMdd'. Setting to default (now)");
		}
		log.info(""
				+ "Socializing and Mood service brick: issuing requests for periods of " + maxDaysPerRequest + " days");
		
		//Step 2: cycle on primary users
		for(User primaryUser : primaryUsers){
			
			try {
				//Step 2a: for each one check last timestamp update
				EmotionUpdate emotionUpdate = null;
				try {
					emotionUpdate = dataManagerUpdatesDao.getMoodReadingByUsername(primaryUser.getUsername());
					if(emotionUpdate!=null){
						log.info("Got visual sensing reading for user " + emotionUpdate.getUsername());
					}
				} catch (Exception e) {
					log.debug("Exception in getting visual sensors reading for user " + primaryUser.getUsername(), e);
				} 
				
			
				if(emotionUpdate==null){
					//moodUpdate = new EmotionUpdate(primaryUser.getUsername(), new DateTime(0));
					//TODO: restore the commented one
					//emotionUpdate = new EmotionUpdate(primaryUser.getUsername(), new DateTime(2014, 9, 1, 0, 0));
					log.info("Starting date for indexing: " + dt);
				    emotionUpdate = new EmotionUpdate(primaryUser.getUsername(), dt);
				}

							
							
		
				//Step 2b: get new visual sensing data from profiling server
		     	VisualSensing[] visualMeasurements = profilingServerDao.getMoodData(primaryUser.getUsername(), emotionUpdate.getLastMoodUpdate().toDate(), new Date(), maxDaysPerRequest);
				nrOfMeasurements += (visualMeasurements.length);
			
				EmotionalStateCategory lastEmotion = EmotionalStateCategory.NEUTRAL ;
				double lastEmotionConf = 0;
				int lastTrack_id=0;
				double valence=0;
				double arousal=0;
				int cntr =0;
				for(VisualSensing visual : visualMeasurements)
				 {
					
										
					DateTime timestamp = new DateTime(visualMeasurements[cntr].getTimestamp());              
					cntr++;
			    double [][] activity_table ={{0,0},  // NEUTRALL
		                  				    {-0.2138, 0.2196},  // ANGER
		                  				    {-0.3106, -0.1039},  // CONTEMPT
		                  				    {-0.1384, 0.4169},  // DISGUST
		                  				    {-0.2923, 0.2493},  // FEAR
		                  				    {0.9458, -0.3247},  // HAPPINES
		                  				    {-0.4063, -0.7817},  // SADNESS
		                  				    {0.4154,  0.3245}}; // SURPRISE 



switch (visual.getEmotion())
{
  case NEUTRAL: {valence=activity_table[0][0]; arousal=activity_table[0][1]; break;}
  case ANGER: {valence=activity_table[1][0]; arousal=activity_table[1][1]; break;}
  case CONTEMPT: {valence=activity_table[2][0]; arousal=activity_table[2][1]; break;}
  case DISGUST: {valence=activity_table[3][0]; arousal=activity_table[3][1]; break;}
  case FEAR: {valence=activity_table[4][0]; arousal=activity_table[4][1]; break;}
  case HAPPINESS: {valence=activity_table[5][0]; arousal=activity_table[5][1]; break;}
  case SADNESS: {valence=activity_table[6][0]; arousal=activity_table[6][1]; break;}
  case SURPRISE: {valence=activity_table[7][0]; arousal=activity_table[7][1]; break;}	 
  default : {valence=0d; arousal=0d;}
}

    lastTrack_id = visual.getTrack_id();
	lastEmotion = visual.getEmotion();
	lastEmotionConf = visual.getEmotionConf();
	
	
	MoodUpdate calculated = new MoodUpdate(primaryUser.getUsername(),timestamp);
	
	calculated.setUsername(primaryUser.getUsername()); 
    calculated.setValence(valence);
    calculated.setArousal(arousal);
    calculated.setTimestamp(timestamp);

             
				
               moodDao.insertEvent(calculated);
				 }
				 
				if(visualMeasurements.length > 0){
					DateTime timestamp = new DateTime(visualMeasurements[visualMeasurements.length-1].getTimestamp());
					//Save the last track_id, Emotion, EmotionConf, valence and arousal
					emotionUpdate.setLastMoodUpdate(timestamp);
					emotionUpdate.setLastEmotion(lastEmotion);
					emotionUpdate.setLastTrack_id(lastTrack_id);
					emotionUpdate.setLastEmotionConf(lastEmotionConf);
					emotionUpdate.setLastValence(valence);
					emotionUpdate.setLastArousal(arousal);
					dataManagerUpdatesDao.updateMoodReading(emotionUpdate);
				}
				
			
			} catch (Exception e) {
				if(e instanceof HttpClientErrorException) {
					HttpStatus status = ((HttpClientErrorException)e).getStatusCode();
					if(status.equals(HttpStatus.NOT_FOUND)){
						log.error("HttpClientErrorException: User " + primaryUser.getUsername() + " has no sensing environment associated. Cannot index any data");		
					} else {
						log.error("HttpClientErrorException: " + e.getMessage());
					}
				} else {
					log.error("Exception: " + e.getMessage(), e);
				}
			}
			
			
		}
		log.info("Stats for visual data processor: duration " + ((double)(System.currentTimeMillis()-start)/1000)+ " ms.; managed " + nrOfUsers + " users; " + nrOfMeasurements + " nrOfMeasurements; ");
	}
	
}
