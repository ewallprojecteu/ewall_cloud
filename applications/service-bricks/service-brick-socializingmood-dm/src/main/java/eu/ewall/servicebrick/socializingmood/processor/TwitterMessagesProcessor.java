   package eu.ewall.servicebrick.socializingmood.processor;


import java.util.Date;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestOperations;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.servicebrick.common.dao.ProfilingServerDao;
import eu.ewall.servicebrick.socializingmood.model.MoodTwitter;
import eu.ewall.servicebrick.socializingmood.model.TwitterResponse;
import eu.ewall.servicebrick.socializingmood.dao.MoodTwitterDao;


/**
 * Reads data from  VisualSensing at regular intervals and applies algorithms to determine Mood info.
 * The extrapolated data is saved on the DB for further retrieval by the service brick front end.
 */
@Component
public class TwitterMessagesProcessor {

	private static final Logger log = LoggerFactory.getLogger(VisualSensingProcessor.class);

	@Autowired
	private ProfilingServerDao profilingServerDao;
	@Autowired
	private MoodTwitterDao moodTwitterDao;
	
	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;
	
	@Autowired
	private Environment env;
	
	@Value("appServer.url")
	private String appServerurl;
	
	@Scheduled(initialDelayString="${processingTweets.delay}", fixedRateString="${processingTweets.interval}")
	public void tweetsProcessing() {		
		//get users from profiling server
		System.out.println("Crash here");
		User[] primaryUsers = profilingServerDao.getPrimaryUsers();
		
		//run getTweetsForUser for each user
		for(int it = 0; it< primaryUsers.length; it++) {
			try {
				//get tweets from fusioner-twitter
				TwitterResponse[] data = getTweetsForUser(primaryUsers[it]);
				
				//check if response is an empty array
				if(data.length == 0) continue;
			
			//split tweet text for each tweet
				for(int counter = 0;counter < data.length;counter++) {
					System.out.println(data[counter].toString());
					String[] splitted = data[counter].getText().split("\\s+");
					String value = "";
					float positive = 0f, negative = 0f;
					//get positive and negative score for each word
					for(int word = 0;word<splitted.length;word++) {
						value = env.getProperty(splitted[word]);
						if(value != null) {
							String[] split_value = value.split(",");
							positive+= Float.parseFloat(split_value[0]);
							negative+= Float.parseFloat(split_value[1]);
						}
					}
					//do something with result
					MoodTwitter item = new MoodTwitter(primaryUsers[it].getUsername(),null,new DateTime(data[counter].getCreatedAt()));
					Date local = new Date();
					if(positive>negative) {
						//save local time + positive status
						System.out.println(local+"positive");
						item.setSocializing("positive");
					} else if(positive<negative){
						//save local time + negative status
						System.out.println(local+"negative");
						item.setSocializing("negative");
					} else {
						//save local time + neutral status
						System.out.println(local+"neutral");
						item.setSocializing("neutral");
					}
					//save to database
					moodTwitterDao.insertEvent(item);
					log.info("Saving to database "+item.toString());
				}
			} catch(HttpServerErrorException e) {
				log.error(e.getMessage());
				continue;
			}
		}
	}
	
	public TwitterResponse[] getTweetsForUser(User user) throws HttpServerErrorException{
		//TODO set url in properties
		String url = appServerurl+"/fusioner-twitter/tweets/{userid}/timestamp" +
				"?from={from}&to={to}";
		//from time start to time end
		//get tweets for the last hour
		long interval = Long.parseLong(env.getProperty("processingTweets.timeInterval"));
		Date end = new Date();
		Date start = new Date(end.getTime() - interval);
		log.info("Getting tweets for: "+user.getUsername()+" -start: "+start+"end: "+end);
		TwitterResponse[] response;
		
		response = ewallClient.getForObject(url,
				TwitterResponse[].class, user.getUsername(),
				start.getTime(), end.getTime());
		
		return response;
	}

}
