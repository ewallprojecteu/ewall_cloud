package eu.ewall.servicebrick.logicgames.dao;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import eu.ewall.servicebrick.common.dao.TimeZoneDaoSupport;
import eu.ewall.servicebrick.common.time.TimeZoneContext;
import eu.ewall.servicebrick.common.time.UserTimeZoneProvider;
import eu.ewall.servicebrick.logicgames.model.Result;
import eu.ewall.servicebrick.logicgames.model.Result0hn0;
import eu.ewall.servicebrick.logicgames.model.ResultMemoryGame;
import eu.ewall.servicebrick.logicgames.model.ResultMemoryTest;
import eu.ewall.servicebrick.logicgames.model.StatisticItem;
import eu.ewall.servicebrick.logicgames.model.Statistics;
import eu.ewall.servicebrick.logicgames.model.Statistics.DBAggregation;

@Component
public class LogicGamesDao extends TimeZoneDaoSupport {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	private static final String GET_USER_PROFILE = "/users/";

	@Value("${profilingServer.url}")
	private String profilingServerUrl;
	
	@Resource
	private RestOperations ewallClient;
	
	
	@Autowired
	public LogicGamesDao(MongoOperations mongoOps, TimeZoneContext timeZoneCtx, 
			UserTimeZoneProvider userTimeZoneProvider) {
		super(mongoOps, timeZoneCtx, userTimeZoneProvider);
	}
	
	public void newResult0hn0(String username, int size, int score, int time, int hintsUsed, int undoUsed){
		Result0hn0 result0hn0 = new Result0hn0(username, size, score, time, hintsUsed, undoUsed);
		mongoOps.insert(result0hn0);
	}
	
	public void newResultMemoryGame(String username, int time, int size, int numberOfClicks){
		ResultMemoryGame resultMemoryGame = new ResultMemoryGame(username, time, size, numberOfClicks);
		mongoOps.insert(resultMemoryGame);
	}
	
	public void newResultMemoryTest(String username, int time, int size, int corrects){
		ResultMemoryTest resultMemoryTest = new ResultMemoryTest(username, time, size, corrects);
		mongoOps.insert(resultMemoryTest);
	}
	
	public void addResult(String username, int time, int size, int corrects){
		ResultMemoryTest resultMemoryTest = new ResultMemoryTest(username, time, size, corrects);
		mongoOps.insert(resultMemoryTest);
	}

	public Result addResult(String username, String gameName, String type,
			String level, Integer nrOfMoves, Integer nrOfAnswersOk,
			String topicId, Integer elapsedTimeSecs, Integer nrOfLevels, Integer minMoves, Integer minElapsedTimeSecs, Integer nrOfQuestions,
			boolean completed) {
		Result result = new Result(username, gameName, type, level, nrOfMoves, nrOfAnswersOk, topicId, elapsedTimeSecs, nrOfLevels, minMoves, minElapsedTimeSecs, nrOfQuestions, completed);
		mongoOps.insert(result);
		return result;
		
	}

	public void updateResult(String resultId, Integer nrOfMoves, Integer minMoves, Integer nrOfAnswersOk, Integer nrOfQuestions,
			Integer elapsedTimeSecs, Integer minElapsedTimeSecs, Boolean completed) {
		Update update = new Update();
		Double performance = null;
		if(nrOfMoves != null && minMoves != null) {
			update.set("nrOfMoves", nrOfMoves);			
			if(nrOfMoves>=minMoves){ //If the game was "resolved" we can rely on the nrOfMoves metric wrt the minMoves
				performance = ((double)minMoves/(double)nrOfMoves)*100;
			} else {// else, we consider the percentage of moves taken
				performance = ((double)nrOfMoves/(double)minMoves)*100;
			}
		}
		if(nrOfAnswersOk != null && nrOfQuestions!=null) {
			update.set("nrOfAnswersOk", nrOfAnswersOk);
			performance = ((double)nrOfAnswersOk/(double)nrOfQuestions)*100;
		}
		if(elapsedTimeSecs != null){
			update.set("elapsedTimeSecs", elapsedTimeSecs);
			if(minElapsedTimeSecs != null) {
				performance = ((double)minElapsedTimeSecs/(double)elapsedTimeSecs)*100;				
			}
		}
		if(performance != null) {
			if(performance.compareTo(100d)>0) {
				performance = 100d;
			}
			update.set("performance", performance);			
		}
		if(completed != null)
			update.set("completed", completed);
		mongoOps.upsert(
					query(where("id").is(resultId)), 
					update,
					Result.class);
	}

	public Result getResult(String resultId) {
		Criteria criteria = where("id").is(resultId);
		Query query = query(criteria);
		return mongoOps.findOne(query, Result.class);
	}

	public List<Result> getLatestResults(String gameName, String username,
			Integer latestResults) {

		Criteria criteria = where("username").is(username).and("gameName").is(gameName);
		Query query = query(criteria).with(new Sort(Sort.Direction.DESC, "timestamp")).limit(latestResults);
		return findWithUserTimeZone(query, Result.class, username);
	}

	public List<Result> getResultsInTimeInterval(String gameName,
			String username, DateTime from, DateTime to) {
		
		Criteria criteria = where("username").is(username).and("gameName").is(gameName);
		criteria = criteria.andOperator(Criteria.where("timestamp").gte(from), Criteria.where("timestamp").lt(to));
		Query query = query(criteria).with(new Sort(Sort.Direction.ASC, "timestamp"));
		return findWithUserTimeZone(query, Result.class, username);
	}

	public List<Result> getResultsByGameNameInTimeInterval(String gameName,
			DateTime from, DateTime to) {
		
		Criteria criteria = where("gameName").is(gameName);
		criteria = criteria.andOperator(Criteria.where("timestamp").gte(from), Criteria.where("timestamp").lt(to));
		Query query = query(criteria).with(new Sort(Sort.Direction.ASC, "timestamp"));
		return mongoOps.find(query, Result.class);
	}

	public List<Result> getLatestResultsByGameName(String gameName,
			Integer latestResults) {
		
		Criteria criteria = where("gameName").is(gameName);
		Query query = query(criteria).with(new Sort(Sort.Direction.DESC, "timestamp")).limit(latestResults);
		return mongoOps.find(query, Result.class);
	}

	public List<Result> getResultsByUsernameInTimeInterval(String username,
			DateTime from, DateTime to) {
		
		Criteria criteria = where("username").is(username);
		criteria = criteria.andOperator(Criteria.where("timestamp").gte(from), Criteria.where("timestamp").lt(to));
		Query query = query(criteria).with(new Sort(Sort.Direction.ASC, "timestamp"));
		return mongoOps.find(query, Result.class);
	}

	public List<Result> getLatestResultsByUsername(String username,
			Integer latestResults) {
		
		Criteria criteria = where("username").is(username);
		Query query = query(criteria).with(new Sort(Sort.Direction.DESC, "timestamp")).limit(latestResults);
		return mongoOps.find(query, Result.class);
	}

	public Statistics getUsageStatistics(String username, DateTime from, DateTime to, Boolean splitByGame, DBAggregation aggregation) {
		Criteria criteria = where("username").is(username);
		criteria = criteria.andOperator(Criteria.where("timestamp").gte(from), Criteria.where("timestamp").lt(to));
		List<String> gameNames = mongoOps.getCollection("GameResults").distinct("gameName");

		Statistics statistics = new Statistics(username, from, to, aggregation);
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
		switch (aggregation) {
			case DAY:
				if(splitByGame){
					for(String gameName : gameNames){
						log.info("gameName: " + gameName);
						Criteria criteria2 = where("username").is(username);
						criteria2 = criteria2.andOperator(Criteria.where("timestamp").gte(from), Criteria.where("timestamp").lt(to));
						criteria2.and("gameName").is(gameName);
						GroupByResults<StatisticItem> groupedResults = mongoOps.group(criteria2, "GameResults", GroupBy.keyFunction("function(doc) {return {  date: new Date((doc.timestamp.getMonth()+1)+\"-\"+doc.timestamp.getDate()+\"-\"+doc.timestamp.getFullYear())}}").initialDocument("{ rounds: 0 , timeSpent : 0, abandoned: 0, completed: 0, avgPerformance: 0}")
								.reduceFunction("function( curr, result ) { result.rounds += 1; result.timeSpent += curr.elapsedTimeSecs; if(!curr.completed) {result.abandoned +=1} else { result.completed +=1; result.avgPerformance += curr.performance}}")
								.finalizeFunction("function(result) {if(result.completed>0) {result.avgPerformance = Math.round(result.avgPerformance / result.completed);} else {result.avgPerformance = 0;}}"), StatisticItem.class);
	
						TreeMap<String, StatisticItem> myTreeMap = new TreeMap<String, StatisticItem>();
						Iterator myIterator = groupedResults.iterator();
						while(myIterator.hasNext()){
							StatisticItem item = (StatisticItem)myIterator.next(); 
							myTreeMap.put(item.getDate().toString(dtf), item);
						}
						
						DateTime newFrom = new DateTime(from.getYear(), from.getMonthOfYear(), from.getDayOfMonth(), 0, 0);
						while(newFrom.isBefore(to)){
							if(myTreeMap.get(newFrom.toString(dtf))==null) {
								myTreeMap.put(newFrom.toString(dtf), new StatisticItem(newFrom, 0, 0, 0, 0, 0d));
							}
							newFrom = newFrom.plusDays(1);
						}
						statistics.putStatistic(gameName, new ArrayList(myTreeMap.values()));
					}
				} else {					
					Criteria criteria2 = where("username").is(username);
					criteria2 = criteria2.andOperator(Criteria.where("timestamp").gte(from), Criteria.where("timestamp").lt(to));
					GroupByResults<StatisticItem> groupedResults = mongoOps.group(criteria2, "GameResults", GroupBy.keyFunction("function(doc) {return {  date: new Date((doc.timestamp.getMonth()+1)+\"-\"+doc.timestamp.getDate()+\"-\"+doc.timestamp.getFullYear())}}").initialDocument("{ rounds: 0 , timeSpent : 0, abandoned: 0, completed: 0, avgPerformance: 0}")
							.reduceFunction("function( curr, result ) { result.rounds += 1; result.timeSpent += curr.elapsedTimeSecs; if(!curr.completed) {result.abandoned +=1} else { result.completed +=1; result.avgPerformance += curr.performance}}")
							.finalizeFunction("function(result) {if(result.completed>0) {result.avgPerformance = Math.round(result.avgPerformance / result.completed);} else {result.avgPerformance = 0;}}"), StatisticItem.class);

					TreeMap<String, StatisticItem> myTreeMap = new TreeMap<String, StatisticItem>();
					Iterator myIterator = groupedResults.iterator();
					while(myIterator.hasNext()){
						StatisticItem item = (StatisticItem)myIterator.next(); 
						myTreeMap.put(item.getDate().toString(dtf), item);
					}
					
					DateTime newFrom = new DateTime(from.getYear(), from.getMonthOfYear(), from.getDayOfMonth(), 0, 0);
					while(newFrom.isBefore(to)){
						if(myTreeMap.get(newFrom.toString(dtf))==null) {
							myTreeMap.put(newFrom.toString(dtf), new StatisticItem(newFrom, 0, 0, 0, 0, 0d));
						}
						newFrom = newFrom.plusDays(1);
					}
					statistics.putStatistic("overall", new ArrayList(myTreeMap.values()));
				}
				break;
			case MONTH:
				if(splitByGame){
					for(String gameName : gameNames){
						log.info("gameName: " + gameName);
						Criteria criteria2 = where("username").is(username);
						criteria2 = criteria2.andOperator(Criteria.where("timestamp").gte(from), Criteria.where("timestamp").lt(to));
						criteria2.and("gameName").is(gameName);
						GroupByResults<StatisticItem> groupedResults = mongoOps.group(criteria2, "GameResults", GroupBy.keyFunction("function(doc) {return {  date: new Date((doc.timestamp.getMonth()+1)+'-01-'+doc.timestamp.getFullYear())}}").initialDocument("{ rounds: 0 , timeSpent : 0, abandoned: 0, completed: 0, avgPerformance: 0}")
								.reduceFunction("function( curr, result ) { result.rounds += 1; result.timeSpent += curr.elapsedTimeSecs; if(!curr.completed) {result.abandoned +=1} else { result.completed +=1; result.avgPerformance += curr.performance}}")
								.finalizeFunction("function(result) {if(result.completed>0) {result.avgPerformance = Math.round(result.avgPerformance / result.completed);} else {result.avgPerformance = 0;}}"), StatisticItem.class);
	
						TreeMap<String, StatisticItem> myTreeMap = new TreeMap<String, StatisticItem>();
						Iterator myIterator = groupedResults.iterator();
						while(myIterator.hasNext()){
							StatisticItem item = (StatisticItem)myIterator.next(); 
							myTreeMap.put(item.getDate().toString(dtf), item);
						}
						
						DateTime newFrom = new DateTime(from.getYear(), from.getMonthOfYear(), 1, 0, 0);
						while(newFrom.isBefore(to)){
							if(myTreeMap.get(newFrom.toString(dtf))==null) {
								myTreeMap.put(newFrom.toString(dtf), new StatisticItem(newFrom, 0, 0, 0, 0, 0d));
							}
							newFrom = newFrom.plusMonths(1);
						}
						statistics.putStatistic(gameName, new ArrayList(myTreeMap.values()));
					} 
				} else {
					Criteria criteria2 = where("username").is(username);
					criteria2 = criteria2.andOperator(Criteria.where("timestamp").gte(from), Criteria.where("timestamp").lt(to));
					GroupByResults<StatisticItem> groupedResults = mongoOps.group(criteria2, "GameResults", GroupBy.keyFunction("function(doc) {return {  date: new Date((doc.timestamp.getMonth()+1)+'-01-'+doc.timestamp.getFullYear())}}").initialDocument("{ rounds: 0 , timeSpent : 0, abandoned: 0, completed: 0, avgPerformance: 0}")
							.reduceFunction("function( curr, result ) { result.rounds += 1; result.timeSpent += curr.elapsedTimeSecs; if(!curr.completed) {result.abandoned +=1} else { result.completed +=1; result.avgPerformance += curr.performance}}")
							.finalizeFunction("function(result) {if(result.completed>0) {result.avgPerformance = Math.round(result.avgPerformance / result.completed);} else {result.avgPerformance = 0;}}"), StatisticItem.class);

					TreeMap<String, StatisticItem> myTreeMap = new TreeMap<String, StatisticItem>();
					Iterator myIterator = groupedResults.iterator();
					while(myIterator.hasNext()){
						StatisticItem item = (StatisticItem)myIterator.next(); 
						myTreeMap.put(item.getDate().toString(dtf), item);
					}
					
					DateTime newFrom = new DateTime(from.getYear(), from.getMonthOfYear(), 1, 0, 0);
					while(newFrom.isBefore(to)){
						if(myTreeMap.get(newFrom.toString(dtf))==null) {
							myTreeMap.put(newFrom.toString(dtf), new StatisticItem(newFrom, 0, 0, 0, 0, 0d));
						}
						newFrom = newFrom.plusMonths(1);
					}
					statistics.putStatistic("overall", new ArrayList(myTreeMap.values()));
					
				}
				break;
				
			case YEAR:
				if(splitByGame){
					for(String gameName : gameNames){
						log.info("gameName: " + gameName);
						Criteria criteria2 = where("username").is(username);
						criteria2 = criteria2.andOperator(Criteria.where("timestamp").gte(from), Criteria.where("timestamp").lt(to));
						criteria2.and("gameName").is(gameName);
						GroupByResults<StatisticItem> groupedResults = mongoOps.group(criteria2, "GameResults", GroupBy.keyFunction("function(doc) {return {  date: new Date('01-01-'+doc.timestamp.getFullYear())}}").initialDocument("{ rounds: 0 , timeSpent : 0, abandoned: 0, completed: 0, avgPerformance: 0}")
								.reduceFunction("function( curr, result ) { result.rounds += 1; result.timeSpent += curr.elapsedTimeSecs; if(!curr.completed) {result.abandoned +=1} else { result.completed +=1; result.avgPerformance += curr.performance}}")
								.finalizeFunction("function(result) {if(result.completed>0) {result.avgPerformance = Math.round(result.avgPerformance / result.completed);} else {result.avgPerformance = 0;}}"), StatisticItem.class);
						
						TreeMap<String, StatisticItem> myTreeMap = new TreeMap<String, StatisticItem>();
						Iterator myIterator = groupedResults.iterator();
						while(myIterator.hasNext()){
							StatisticItem item = (StatisticItem)myIterator.next(); 
							myTreeMap.put(item.getDate().toString(dtf), item);
						}
						
						DateTime newFrom = new DateTime(from.getYear(), 1, 1, 0, 0);
						while(newFrom.isBefore(to)){
							if(myTreeMap.get(newFrom.toString(dtf))==null) {
								myTreeMap.put(newFrom.toString(dtf), new StatisticItem(newFrom, 0, 0, 0, 0, 0d));
							}
							newFrom = newFrom.plusYears(1);
						}
						statistics.putStatistic(gameName, new ArrayList(myTreeMap.values()));
					}
				} else {
					Criteria criteria2 = where("username").is(username);
					criteria2 = criteria2.andOperator(Criteria.where("timestamp").gte(from), Criteria.where("timestamp").lt(to));
					GroupByResults<StatisticItem> groupedResults = mongoOps.group(criteria2, "GameResults", GroupBy.keyFunction("function(doc) {return {  date: new Date('01-01-'+doc.timestamp.getFullYear())}}").initialDocument("{ rounds: 0 , timeSpent : 0, abandoned: 0, completed: 0, avgPerformance: 0}")
							.reduceFunction("function( curr, result ) { result.rounds += 1; result.timeSpent += curr.elapsedTimeSecs; if(!curr.completed) {result.abandoned +=1} else { result.completed +=1; result.avgPerformance += curr.performance}}")
							.finalizeFunction("function(result) {if(result.completed>0) {result.avgPerformance = Math.round(result.avgPerformance / result.completed);} else {result.avgPerformance = 0;}}"), StatisticItem.class);
					
					TreeMap<String, StatisticItem> myTreeMap = new TreeMap<String, StatisticItem>();
					Iterator myIterator = groupedResults.iterator();
					while(myIterator.hasNext()){
						StatisticItem item = (StatisticItem)myIterator.next(); 
						myTreeMap.put(item.getDate().toString(dtf), item);
					}
					
					DateTime newFrom = new DateTime(from.getYear(), 1, 1, 0, 0);
					while(newFrom.isBefore(to)){
						if(myTreeMap.get(newFrom.toString(dtf))==null) {
							myTreeMap.put(newFrom.toString(dtf), new StatisticItem(newFrom, 0, 0, 0, 0, 0d));
						}
						newFrom = newFrom.plusYears(1);
					}
					statistics.putStatistic("overall", new ArrayList(myTreeMap.values()));					
				}
				break;
				
			case NONE:
				if(splitByGame){
					for(String gameName : gameNames){
						log.info("gameName: " + gameName);
						Criteria criteria2 = where("username").is(username);
						criteria2 = criteria2.andOperator(Criteria.where("timestamp").gte(from), Criteria.where("timestamp").lt(to));
						criteria2.and("gameName").is(gameName);
						GroupByResults<StatisticItem> groupedResults = mongoOps.group(criteria2, "GameResults", GroupBy.key("username").initialDocument("{ rounds: 0 , timeSpent : 0, abandoned: 0, completed: 0, avgPerformance: 0}")
								.reduceFunction("function( curr, result ) { result.rounds += 1; result.timeSpent += curr.elapsedTimeSecs; if(!curr.completed) {result.abandoned +=1} else { result.completed +=1; result.avgPerformance += curr.performance}}")
								.finalizeFunction("function(result) {if(result.completed>0) {result.avgPerformance = Math.round(result.avgPerformance / result.completed);} else {result.avgPerformance = 0;}}"), StatisticItem.class);
						
						TreeMap<String, StatisticItem> myTreeMap = new TreeMap<String, StatisticItem>();
						Iterator myIterator = groupedResults.iterator();
						while(myIterator.hasNext()){
							StatisticItem item = (StatisticItem)myIterator.next(); 
							myTreeMap.put(from.toString(dtf), item);
						}
						
						statistics.putStatistic(gameName, new ArrayList(myTreeMap.values()));
					}
				} else {
					Criteria criteria2 = where("username").is(username);
					criteria2 = criteria2.andOperator(Criteria.where("timestamp").gte(from), Criteria.where("timestamp").lt(to));
					GroupByResults<StatisticItem> groupedResults = mongoOps.group(criteria2, "GameResults", GroupBy.key("username").initialDocument("{ rounds: 0 , timeSpent : 0, abandoned: 0, completed: 0, avgPerformance: 0}")
							.reduceFunction("function( curr, result ) { result.rounds += 1; result.timeSpent += curr.elapsedTimeSecs; if(!curr.completed) {result.abandoned +=1} else { result.completed +=1; result.avgPerformance += curr.performance}}")
							.finalizeFunction("function(result) {if(result.completed>0) {result.avgPerformance = Math.round(result.avgPerformance / result.completed);} else {result.avgPerformance = 0;}}"), StatisticItem.class);
	
					TreeMap<String, StatisticItem> myTreeMap = new TreeMap<String, StatisticItem>();
					Iterator myIterator = groupedResults.iterator();
					while(myIterator.hasNext()){
						StatisticItem item = (StatisticItem)myIterator.next(); 
						myTreeMap.put(from.toString(dtf), item);
					}
					
					statistics.putStatistic("overall", new ArrayList(myTreeMap.values()));
				}

			default:
					break;
				
		}
	
		
	
		return statistics;
	}


}
