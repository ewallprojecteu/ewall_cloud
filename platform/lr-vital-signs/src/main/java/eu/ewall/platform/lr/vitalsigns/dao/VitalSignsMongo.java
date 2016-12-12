/****************************************************************
 * Copyright 2015 Ss Cyril and Methodius University in Skopje
***************************************************************/

package eu.ewall.platform.lr.vitalsigns.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;




import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import eu.ewall.platform.lr.vitalsigns.dao.factory.MongoDBFactory;

/**
 * The Class NotificationMongo.
 * 
 */
@Component
@RestController
public class VitalSignsMongo {
	
	@Value("${mongo.dbname}")
	private String mongoDBName;

	/** The log. */
	private static Logger LOG = LoggerFactory.getLogger(VitalSignsMongo.class);
	/** The mongo ops. */
	public MongoOperations mongoOps;

	/** The Constant NOTIFICATIONS_COLLECTION. */
	public static final String VITALS_COLLECTION = "vitalSignsCollection";
	
	@Autowired
	private MongoDBFactory MongoDBFactory;
	

	/**
	 * Adds a new vital sings input in the DB hourly
	 *
	 * @param samples
	 *            the measurement samples
	 * @return the measurement sequence
	 */
	public int addMeasHour(VitalSignsDBcontent samples) 
	{

		//System.out.println("MongoDB name <<addVS into DB>> "+ mongoDBName);
		
		Date now = new Date();
		mongoOps = MongoDBFactory.getMongoOperations();
		Query query = new Query(Criteria.where("user").is(samples.getUser()).and("averInt").is(samples.getAverInt()).and("dateTime").lt(now));
		query.with(new Sort(Sort.Direction.DESC, "dateTime"));
		VitalSignsDBcontent sampleT = mongoOps.findOne(query, VitalSignsDBcontent.class, VITALS_COLLECTION);
		
		int countr = 0;
		
		if (sampleT != null)
		{
			countr = sampleT.getCounter();
		}
		
		Query queryS = new Query(Criteria.where("user").is(samples.getUser()).and("averInt").is(samples.getAverInt()).and("counter").is(countr+1));

		VitalSignsDBcontent sampleX = mongoOps.findOne(queryS, VitalSignsDBcontent.class, VITALS_COLLECTION);
		
		if (sampleX != null)
			{
			// update an old entry
			
			countr = sampleX.getCounter();
			samples.set_Id(sampleX.get_Id());
			samples.setCounter(sampleX.getCounter());
			mongoOps.save(samples, VITALS_COLLECTION);
			
			}
		else if(countr == 12) // the counter should restart
			{
			// update the first entry
			
			countr = 1;
			queryS = new Query(Criteria.where("user").is(samples.getUser()).and("averInt").is(samples.getAverInt()).and("counter").is(1));
			sampleX = mongoOps.findOne(queryS, VitalSignsDBcontent.class, VITALS_COLLECTION);
			samples.set_Id(sampleX.get_Id());
			samples.setCounter(sampleX.getCounter());
			mongoOps.save(samples, VITALS_COLLECTION);
				
			}
		else
			{
			
			//add a new entry
			countr =countr+1;
		    samples.setCounter(countr);	
		    mongoOps = MongoDBFactory.getMongoOperations();
		    mongoOps.insert(samples, VITALS_COLLECTION);
			//LOG.debug("New user with username " + user.getUsername()
			//		+ " added to mongodb.");
			}
			
			return countr;
	}
	
	/**
	 * Adds a new vital sings input in the DB daily
	 *
	 * @param samples
	 *            the measurement samples
	 * @return the measurement sequence
	 */
	public int addMeasDay(VitalSignsDBcontent samples) 
	{

		//System.out.println("MongoDB name <<addVS into DB>> "+ mongoDBName);
		
		Date now = new Date();
		mongoOps = MongoDBFactory.getMongoOperations();
		Query query = new Query(Criteria.where("user").is(samples.getUser()).and("averInt").is(samples.getAverInt()).and("dateTime").lt(now));
		query.with(new Sort(Sort.Direction.DESC, "dateTime"));
		VitalSignsDBcontent sampleT = mongoOps.findOne(query, VitalSignsDBcontent.class, VITALS_COLLECTION);
		
		int countr = 0;
		
		if (sampleT != null)
		{
			countr = sampleT.getCounter();
		}
		
		Query queryS = new Query(Criteria.where("user").is(samples.getUser()).and("averInt").is(samples.getAverInt()).and("counter").is(countr+1));

		VitalSignsDBcontent sampleX = mongoOps.findOne(queryS, VitalSignsDBcontent.class, VITALS_COLLECTION);
		
		if (sampleX != null)
			{
			// update an old entry
			
			countr = sampleX.getCounter();
			samples.set_Id(sampleX.get_Id());
			samples.setCounter(sampleX.getCounter());
			mongoOps.save(samples, VITALS_COLLECTION);
			
			}
		else if(countr == 7) // the counter should restart
			{
			// update the first entry
			
			countr = 1;
			queryS = new Query(Criteria.where("user").is(samples.getUser()).and("averInt").is(samples.getAverInt()).and("counter").is(1));
			sampleX = mongoOps.findOne(queryS, VitalSignsDBcontent.class, VITALS_COLLECTION);
			samples.set_Id(sampleX.get_Id());
			samples.setCounter(sampleX.getCounter());
			mongoOps.save(samples, VITALS_COLLECTION);
				
			}
		else
			{
			//add a new entry
			countr =countr+1;
		    samples.setCounter(countr);	
		    mongoOps = MongoDBFactory.getMongoOperations();
		    mongoOps.insert(samples, VITALS_COLLECTION);
			//LOG.debug("New user with username " + user.getUsername()
			//		+ " added to mongodb.");
			}
			
			return countr;
	}
	
	
	/**
	 * Adds a new vital sings input in the DB weekly
	 *
	 * @param samples
	 *            the measurement samples
	 * @return the measurement sequence
	 */
	public int addMeasWeek(VitalSignsDBcontent samples) 
	{

		//System.out.println("MongoDB name <<addVS into DB>> "+ mongoDBName);
		
		Date now = new Date();
		mongoOps = MongoDBFactory.getMongoOperations();
		Query query = new Query(Criteria.where("user").is(samples.getUser()).and("averInt").is(samples.getAverInt()).and("dateTime").lt(now));
		query.with(new Sort(Sort.Direction.DESC, "dateTime"));
		VitalSignsDBcontent sampleT = mongoOps.findOne(query, VitalSignsDBcontent.class, VITALS_COLLECTION);
		
		int countr = 0;
		
		if (sampleT != null)
		{
			countr = sampleT.getCounter();
		}
		
		Query queryS = new Query(Criteria.where("user").is(samples.getUser()).and("averInt").is(samples.getAverInt()).and("counter").is(countr+1));

		VitalSignsDBcontent sampleX = mongoOps.findOne(queryS, VitalSignsDBcontent.class, VITALS_COLLECTION);
		
		if (sampleX != null)
			{
			// update an old entry
			
			countr = sampleX.getCounter();
			samples.set_Id(sampleX.get_Id());
			samples.setCounter(sampleX.getCounter());
			mongoOps.save(samples, VITALS_COLLECTION);
			
			}
		else if(countr == 4) // the counter should restart
			{
			// update the first entry
			
			countr = 1;
			queryS = new Query(Criteria.where("user").is(samples.getUser()).and("averInt").is(samples.getAverInt()).and("counter").is(1));
			sampleX = mongoOps.findOne(queryS, VitalSignsDBcontent.class, VITALS_COLLECTION);
			samples.set_Id(sampleX.get_Id());
			samples.setCounter(sampleX.getCounter());
			mongoOps.save(samples, VITALS_COLLECTION);
				
			}
		else
			{
			//add a new entry
			countr =countr+1;
		    samples.setCounter(countr);	
		    mongoOps = MongoDBFactory.getMongoOperations();
		    mongoOps.insert(samples, VITALS_COLLECTION);
			//LOG.debug("New user with username " + user.getUsername()
			//		+ " added to mongodb.");
			}
			
			return countr;
	}
	
	
	/**
	 * Adds a new vital sings input in the DB monthly
	 *
	 * @param samples
	 *            the measurement samples
	 * @return the measurement sequence
	 */
	public int addMeasMonth(VitalSignsDBcontent samples) 
	{

		//System.out.println("MongoDB name <<addVS into DB>> "+ mongoDBName);
		
		Date now = new Date();
		mongoOps = MongoDBFactory.getMongoOperations();
		Query query = new Query(Criteria.where("user").is(samples.getUser()).and("averInt").is(samples.getAverInt()).and("dateTime").lt(now));
		query.with(new Sort(Sort.Direction.DESC, "dateTime"));
		VitalSignsDBcontent sampleT = mongoOps.findOne(query, VitalSignsDBcontent.class, VITALS_COLLECTION);
		
		int countr = 0;
		
		if (sampleT != null)
		{
			countr = sampleT.getCounter();
		}
		
		Query queryS = new Query(Criteria.where("user").is(samples.getUser()).and("averInt").is(samples.getAverInt()).and("counter").is(countr+1));

		VitalSignsDBcontent sampleX = mongoOps.findOne(queryS, VitalSignsDBcontent.class, VITALS_COLLECTION);
		
		if (sampleX != null)
			{
			// update an old entry
			
			countr = sampleX.getCounter();
			samples.set_Id(sampleX.get_Id());
			samples.setCounter(sampleX.getCounter());
			mongoOps.save(samples, VITALS_COLLECTION);
			
			}
		else if(countr == 12) // the counter should restart
			{
			// update the first entry
			
			countr = 1;
			queryS = new Query(Criteria.where("user").is(samples.getUser()).and("averInt").is(samples.getAverInt()).and("counter").is(1));
			sampleX = mongoOps.findOne(queryS, VitalSignsDBcontent.class, VITALS_COLLECTION);
			samples.set_Id(sampleX.get_Id());
			samples.setCounter(sampleX.getCounter());
			mongoOps.save(samples, VITALS_COLLECTION);
				
			}
		else
			{
			//add a new entry
			countr =countr+1;
		    samples.setCounter(countr);	
		    mongoOps = MongoDBFactory.getMongoOperations();
		    mongoOps.insert(samples, VITALS_COLLECTION);
			//LOG.debug("New user with username " + user.getUsername()
			//		+ " added to mongodb.");
			}
			
			return countr;
	}
	
	
	/**
	 * Adds a new vital sings input in the DB yearly
	 *
	 * @param samples
	 *            the measurement samples
	 */
	public int addMeasYear(VitalSignsDBcontent samples) 
	{

		//System.out.println("MongoDB name <<addVS into DB>> "+ mongoDBName);
		
		Date now = new Date();
		mongoOps = MongoDBFactory.getMongoOperations();
		Query query = new Query(Criteria.where("user").is(samples.getUser()).and("averInt").is(samples.getAverInt()).and("dateTime").lt(now));
		query.with(new Sort(Sort.Direction.DESC, "dateTime"));
		VitalSignsDBcontent sampleT = mongoOps.findOne(query, VitalSignsDBcontent.class, VITALS_COLLECTION);
		
		int countr = 0;
		
		if (sampleT != null)
		{
			countr = sampleT.getCounter();
		}
		
		countr =countr+1;
	    samples.setCounter(countr);	
	    mongoOps = MongoDBFactory.getMongoOperations();
	    mongoOps.insert(samples, VITALS_COLLECTION);
	    
	    return countr;
			
	}
	
	
	/**
	 * Adds a new vital sings input in the DB in a floating manner
	 *
	 * @param samples
	 *            the measurement samples
	 */
	public void addMeasFloating(VitalSignsDBcontentFloating samples) 
	{

		//System.out.println("MongoDB name <<addVS into DB>> "+ mongoDBName);
		
		Date now = new Date();
		mongoOps = MongoDBFactory.getMongoOperations();
		Query query = new Query(Criteria.where("user").is(samples.getUser()).and("averInt").is(samples.getAverInt()).and("dateTime").lt(now));
		query.with(new Sort(Sort.Direction.DESC, "dateTime"));
		VitalSignsDBcontentFloating sampleT = mongoOps.findOne(query, VitalSignsDBcontentFloating.class, VITALS_COLLECTION);
		
		int countr = 0;
		
		if (sampleT != null)
		{
			samples.set_Id(sampleT.get_Id());
			samples.setCounter(sampleT.getCounter()+1);
			
			if ((samples.getSbp() > -1) && (samples.getDbp() > -1))
			{
				samples.setSbp(averFloatingValue(sampleT.getSbp(),samples.getSbp(),sampleT.getCounterBP()));
				samples.setDbp(averFloatingValue(sampleT.getDbp(),samples.getDbp(),sampleT.getCounterBP()));
				samples.setCounterBP(sampleT.getCounterBP()+1);
			}
			else
			{
				samples.setSbp(sampleT.getSbp());
				samples.setDbp(sampleT.getDbp());
				samples.setCounterBP(sampleT.getCounterBP());
			}
			
			if ((samples.getHr() > -1) && (samples.getHrv() > -1))
			{
				samples.setHr(averFloatingValue(sampleT.getHr(),samples.getHr(),sampleT.getCounterHR()));
				samples.setHrv(averFloatingValue(sampleT.getHrv(),samples.getHrv(),sampleT.getCounterHR()));
				samples.setCounterHR(sampleT.getCounterHR()+1);
				
			}
			else
			{
				samples.setHr(sampleT.getHr());
				samples.setHrv(sampleT.getHrv());
				samples.setCounterHR(sampleT.getCounterHR());
			}
			
			if ((samples.getOs() > -1))
			{
				samples.setOs(averFloatingValue(sampleT.getOs(),samples.getOs(),sampleT.getCounterOS()));
				samples.setCounterOS(sampleT.getCounterOS()+1);
			}
			else
			{
				samples.setOs(sampleT.getOs());
				samples.setCounterOS(sampleT.getCounterOS());
			}
			
			mongoOps.save(samples, VITALS_COLLECTION);
			
		}
		else
		{
		    samples.setCounter(countr+1);	
		    
		    if ((samples.getSbp() == -1) || (samples.getDbp() == -1))
			{
		    	// store no meassurements
		    	samples.setSbp(0);
				samples.setDbp(0);
				samples.setCounterBP(0);
			}
			else
			{
				samples.setCounterBP(1);
			}
			
			if ((samples.getHr() == -1) || (samples.getHrv() == -1))
			{
				samples.setHr(0);
				samples.setHrv(0);
				samples.setCounterHR(0);
			}
			else
			{
				samples.setCounterHR(1);
			}
			
			if ((samples.getOs() == -1))
			{
				samples.setOs(0);
				samples.setCounterOS(0);
			}
			else
			{
				samples.setCounterOS(1);
			}
		    
		    mongoOps = MongoDBFactory.getMongoOperations();
		    mongoOps.insert(samples, VITALS_COLLECTION);
		}
			
	}
	
	
	/**
	 * Returns all measurements for the given user for a given time frame
	 *
	 * @param user
	 *            the user name
	 * @param  AvInt
	 * 			  the given time frame of interest (e.g. day, week, month, etc.)          
	 * @return List of measurement samples
	 */
	public List<VitalSignsDBcontent> getMeas(String user, String AvInt)
	{
		mongoOps = MongoDBFactory.getMongoOperations();
		Query query = new Query(Criteria.where("user").is(user).and("averInt").is(AvInt));
		query.with(new Sort(Sort.Direction.ASC, "_id"));
		return mongoOps.find(query, VitalSignsDBcontent.class,VITALS_COLLECTION);
	}
	
	/**
	 * Returns all measurements for the given user for a given time frame
	 *
	 * @param user
	 *            the user name
	 *   
	 * @return The floating measurement samples
	 */
	public VitalSignsDBcontentFloating getMeasFloating(String user)
	{
		mongoOps = MongoDBFactory.getMongoOperations();
		Query query = new Query(Criteria.where("user").is(user).and("averInt").is("floating"));
		return mongoOps.findOne(query, VitalSignsDBcontentFloating.class,VITALS_COLLECTION);
	}
	
	public int getLastSample(String user, String AvInt, Date Thr) 
	{
		mongoOps = MongoDBFactory.getMongoOperations();
		Query query = new Query(Criteria.where("user").is(user).and("averInt").is(AvInt).and("dateTime").lt(Thr));
		query.with(new Sort(Sort.Direction.DESC, "dateTime"));
		VitalSignsDBcontent sample = mongoOps.findOne(query, VitalSignsDBcontent.class, VITALS_COLLECTION);
		
		Query queryS = new Query(Criteria.where("user").is(user).and("averInt").is(AvInt).and("counter").is(sample.getCounter()+1));

		VitalSignsDBcontent sampleS = mongoOps.findOne(queryS, VitalSignsDBcontent.class, VITALS_COLLECTION);
		
		if (sampleS != null)
			return sampleS.getCounter();
		else
			return sample.getCounter();
	}
	
	public double averFloatingValue(double avg, double sample, int n)
	{
		double average = (float)(n*avg+sample)/(n+1);
				
	    return average;
	}

}
