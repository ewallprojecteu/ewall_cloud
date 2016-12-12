package eu.ewall.servicebrick.physicalactivity.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import org.joda.time.DateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.data.mongodb.core.MongoOperations;



import eu.ewall.servicebrick.common.AggregationPeriod;
import eu.ewall.servicebrick.common.validation.ServiceBrickInputValidator;
import eu.ewall.servicebrick.physicalactivity.dao.MovementDao;
import eu.ewall.servicebrick.physicalactivity.model.Movement;
import eu.ewall.servicebrick.physicalactivity.model.FitBit_Movement;
import eu.ewall.servicebrick.physicalactivity.model.MovementHistory;


@RestController
@PropertySource("classpath:/service-brick-physicalactivity-${ewall.env:local}.properties")
public class MovementController {

	private static final Logger log = LoggerFactory.getLogger(MovementController.class);
	protected MongoOperations mongoOps;
	
	private ServiceBrickInputValidator inputValidator;
	private MovementDao movementDao;
	
	
	@Value("${source}")
	public int source;
	
	@Autowired
	public MovementController(ServiceBrickInputValidator inputValidator, MovementDao movementDao) {
		this.inputValidator = inputValidator;
		this.movementDao = movementDao;
	}

	
	
	@RequestMapping(value = "/v1/{username}/movement", method = RequestMethod.GET)
	public MovementHistory getMovement(
			@PathVariable String username,
			@RequestParam("from") DateTime from,
			@RequestParam("to") DateTime to,
			@RequestParam("aggregation") AggregationPeriod aggregation) {
		long start = System.currentTimeMillis();
		log.debug("Request for movement for " + username + " from " + from + " to " + to + 
		          " aggregated by " + aggregation);
		inputValidator.validateTimeInterval(username, from, to);
		AggregationPeriod daoAggregation = new AggregationPeriod(1, aggregation.getUnit());
		List<Movement> daoItems = new ArrayList<Movement>();
		List<FitBit_Movement> daoItemsFitBit = new ArrayList<FitBit_Movement>();
		
		if(source==0){
		daoItems = movementDao.readMovements(username, from, to, daoAggregation, Movement.class);
		List<Movement> finalItems;
		if (aggregation.getLength() > 1) {
			finalItems = mergeMovements(daoItems, from, aggregation);
		} else {
			finalItems = daoItems;
		}
		log.debug("getMovement() call took " + (System.currentTimeMillis() - start) + "ms");
		MovementHistory result = new MovementHistory(username, from, to, aggregation, finalItems);
		return result;
		}
		else
		{
		daoItemsFitBit = movementDao.readMovements(username, from, to, daoAggregation, FitBit_Movement.class);
		
		
	//	log.info(" "+daoItemsFitBit.size());
	//	log.info(" "+daoItemsFitBit.get(0).getSteps());
		List<FitBit_Movement> finalItemsFitBit;
		List<Movement> finalItems1 = new LinkedList<Movement>();
		
		if (aggregation.getLength() > 1) {
			finalItemsFitBit = mergeFitBitMovements(daoItemsFitBit, from, aggregation);
		} else {
			finalItemsFitBit = daoItemsFitBit;
		}
		
		int s=finalItemsFitBit.size();
		
		if(s>0)
		{
		
		for(int i=0;i<s;i++)
		{
			Movement temp = new Movement(username,from,to);
			temp.setAggregation(finalItemsFitBit.get(i).getAggregation());
			temp.setBurnedCalories(finalItemsFitBit.get(i).getBurnedCalories());
			temp.setFrom(finalItemsFitBit.get(i).getFrom());
			temp.setKilometers(finalItemsFitBit.get(i).getKilometers());
			temp.setTo(finalItemsFitBit.get(i).getTo());
			temp.setUsername(finalItemsFitBit.get(i).getUsername());
			temp.setSteps(finalItemsFitBit.get(i).getSteps());
			finalItems1.add(temp);
		}
		}
	/*	else
		{
			DateTime from_time = new DateTime(from);
			DateTime to_time = new DateTime(from);
			while(to_time.isBefore(to))
				{	
			Movement temp = new Movement(username,from_time,to_time);
			temp.setAggregation(aggregation);
			temp.setBurnedCalories(0);
			temp.setFrom(from);
			temp.setKilometers(0);
			temp.setTo(to);
			temp.setUsername(username);
			temp.setSteps(0);
			finalItems1.add(temp);
				}
		}*/
			
		
		MovementHistory result = new MovementHistory(username, from, to, aggregation, finalItems1);
		log.debug("getMovement() call took " + (System.currentTimeMillis() - start) + "ms");
		return result;
		}
	}
	
	private List<Movement> mergeMovements(List<Movement> sourceItems, DateTime intervalStart, AggregationPeriod aggregation) {
		List<Movement> mergedItems = new LinkedList<>();
		if (sourceItems.size() > 0) {
			DateTime firstFrom = sourceItems.get(0).getFrom();
			// Make sure intervalStart uses the same time zone as source items (endpoint input parameters have only an
			// offset instead of a full time zone) so the zone is preserved in the aligned movement
			intervalStart = intervalStart.withZone(firstFrom.getZone());
			Movement mergeDest = getAlignedMovement(firstFrom, intervalStart, aggregation);
			mergedItems.add(mergeDest);
			for (Movement sourceItem : sourceItems) {
				DateTime sourceFrom = sourceItem.getFrom();
				if (sourceFrom.compareTo(mergeDest.getTo()) >= 0) {
					mergeDest = getAlignedMovement(sourceFrom, mergeDest.getTo(), aggregation);
					mergedItems.add(mergeDest);
				}
				mergeDest.addSteps(sourceItem.getSteps());
				mergeDest.addKilometers(sourceItem.getKilometers());
				mergeDest.addBurnedCalories(sourceItem.getBurnedCalories());
			}
		}
		return mergedItems;
	}
	
	
	private Movement getAlignedMovement(DateTime date, DateTime alignStart, AggregationPeriod alignPeriod) {
		if (date.isBefore(alignStart)) {
			throw new IllegalArgumentException("date is before alignStart: " + date + ", " + alignStart);
		}
		DateTime movementFrom = alignStart;
		DateTime movementTo = alignPeriod.addTo(movementFrom);
		while (date.compareTo(movementTo) >= 0) {
			movementFrom = movementTo;
			movementTo = alignPeriod.addTo(movementTo);
		}
		return new Movement(movementFrom, movementTo);
	}
	
	private List<FitBit_Movement> mergeFitBitMovements(List<FitBit_Movement> sourceItems, DateTime intervalStart, AggregationPeriod aggregation) {
		List<FitBit_Movement> mergedItems = new LinkedList<>();
		if (sourceItems.size() > 0) {
			DateTime firstFrom = sourceItems.get(0).getFrom();
			// Make sure intervalStart uses the same time zone as source items (endpoint input parameters have only an
			// offset instead of a full time zone) so the zone is preserved in the aligned movement
			intervalStart = intervalStart.withZone(firstFrom.getZone());
			FitBit_Movement mergeDest = getAlignedFitBit_Movement(firstFrom, intervalStart, aggregation);
			mergedItems.add(mergeDest);
			for (FitBit_Movement sourceItem : sourceItems) {
				DateTime sourceFrom = sourceItem.getFrom();
				if (sourceFrom.compareTo(mergeDest.getTo()) >= 0) {
					mergeDest = getAlignedFitBit_Movement(sourceFrom, mergeDest.getTo(), aggregation);
					mergedItems.add(mergeDest);
				}
				mergeDest.addSteps(sourceItem.getSteps());
				mergeDest.addKilometers(sourceItem.getKilometers());
				mergeDest.addBurnedCalories(sourceItem.getBurnedCalories());
			}
		}
		return mergedItems;
	}
	
	
	private FitBit_Movement getAlignedFitBit_Movement(DateTime date, DateTime alignStart, AggregationPeriod alignPeriod) {
		if (date.isBefore(alignStart)) {
			throw new IllegalArgumentException("date is before alignStart: " + date + ", " + alignStart);
		}
		DateTime movementFrom = alignStart;
		DateTime movementTo = alignPeriod.addTo(movementFrom);
		while (date.compareTo(movementTo) >= 0) {
			movementFrom = movementTo;
			movementTo = alignPeriod.addTo(movementTo);
		}
		return new FitBit_Movement(movementFrom, movementTo);
	}
	
}
