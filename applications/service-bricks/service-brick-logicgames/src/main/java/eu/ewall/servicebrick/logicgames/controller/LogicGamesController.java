package eu.ewall.servicebrick.logicgames.controller;

import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.ewall.servicebrick.common.dao.ProfilingServerDao;
import eu.ewall.servicebrick.common.validation.ServiceBrickInputValidator;
import eu.ewall.servicebrick.logicgames.dao.LogicGamesDao;
import eu.ewall.servicebrick.logicgames.model.Result;
import eu.ewall.servicebrick.logicgames.model.ResultHistory;
import eu.ewall.servicebrick.logicgames.model.ResultId;
import eu.ewall.servicebrick.logicgames.model.Statistics;
import eu.ewall.servicebrick.logicgames.model.Statistics.DBAggregation;

@RestController
public class LogicGamesController {
	private static final Logger log = LoggerFactory.getLogger(LogicGamesController.class);
	
	private LogicGamesDao logicGamesDao;
	private ServiceBrickInputValidator inputValidator;
	
	@Autowired
	public LogicGamesController(ServiceBrickInputValidator inputValidator, ProfilingServerDao profilingServerDao, LogicGamesDao logicGamesDao){
		this.inputValidator = inputValidator;
		this.logicGamesDao = logicGamesDao;
	}
	
	@RequestMapping(value = "/v1/{username}/test", method = RequestMethod.GET)
	public String testing(@PathVariable String username){
		return "hello world";
	}
	
	@RequestMapping(value = "/v1/{username}/Result0hn0", method = RequestMethod.POST)
	public ResponseEntity<String> newResult0hn0(@PathVariable String username, 
			@RequestParam(value="size", required=true) int size, 
			@RequestParam(value="score", required=true) int score, 
			@RequestParam(value="time", required=true) int time, 
			@RequestParam(value="hintsUsed", required=true) int hintsUsed, 
			@RequestParam(value="undoUsed", required=true) int undoUsed){
		logicGamesDao.newResult0hn0(username, size, score, time, hintsUsed, undoUsed);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/v1/{username}/ResultMemoryGame", method = RequestMethod.POST)
	public ResponseEntity<String> newResultMemoryGame(@PathVariable String username,
			@RequestParam(value="time", required=true) int time, 
			@RequestParam(value="size", required=true) int size, 
			@RequestParam(value="numberOfClicks", required=true) int numberOfClicks){
		logicGamesDao.newResultMemoryGame(username, time, size, numberOfClicks);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/v1/{username}/ResultMemoryTest", method = RequestMethod.POST)
	public ResponseEntity<String> newResultMemoryTest(@PathVariable String username,
			@RequestParam(value="time", required=true) int time, 
			@RequestParam(value="size", required=true) int size,
			@RequestParam(value="corrects", required=true) int corrects){
		logicGamesDao.newResultMemoryTest(username, time, size, corrects);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/v1/result/{gameName}/{username}", method = RequestMethod.POST)
	public ResponseEntity<ResultId> addResult(@PathVariable String gameName, @PathVariable String username,			
			@RequestParam(value="type", required=true) String type, 
			@RequestParam(value="level", required=false) String level,
			@RequestParam(value="nrOfMoves", required=false) Integer nrOfMoves,
			@RequestParam(value="nrOfAnswersOk", required=false) Integer nrOfAnswersOk,
			@RequestParam(value="topicId", required=false) String topicId,
			@RequestParam(value="elapsedTimeSecs", required=true) Integer elapsedTimeSecs, 
			@RequestParam(value="nrOfLevels", required=false) Integer nrOfLevels, 
			@RequestParam(value="minMoves", required=false) Integer minMoves, 
			@RequestParam(value="minElapsedTimeSecs", required=false) Integer minElapsedTimeSecs, 
			@RequestParam(value="nrOfQuestions", required=false) Integer nrOfQuestions,
			@RequestParam(value="completed", required=true) boolean completed){
		Result result = logicGamesDao.addResult(username, gameName, type, level, nrOfMoves, nrOfAnswersOk, topicId, elapsedTimeSecs, nrOfLevels, minMoves, minElapsedTimeSecs, nrOfQuestions, completed);
		ResultId id = new ResultId(result.getId());
		return new ResponseEntity<ResultId>(id, HttpStatus.OK);
	}

	@RequestMapping(value = "/v1/result/{resultId}", method = RequestMethod.POST)
	public ResponseEntity<String> updateResult(@PathVariable String resultId, 			
			@RequestParam(value="nrOfMoves", required=false) Integer nrOfMoves,
			@RequestParam(value="minMoves", required=false) Integer minMoves,
			@RequestParam(value="nrOfAnswersOk", required=false) Integer nrOfAnswersOk,
			@RequestParam(value="nrOfQuestions", required=false) Integer nrOfQuestions,
			@RequestParam(value="elapsedTimeSecs", required=true) Integer elapsedTimeSecs, 
			@RequestParam(value="minElapsedTimeSecs", required=false) Integer minElapsedTimeSecs, 
			@RequestParam(value="completed", required=true) Boolean completed){
		logicGamesDao.updateResult(resultId, nrOfMoves, minMoves, nrOfAnswersOk, nrOfQuestions, elapsedTimeSecs, minElapsedTimeSecs, completed);
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/v1/result/{resultId}", method = RequestMethod.GET)
	public Result getResult(@PathVariable String resultId){
		Result result = logicGamesDao.getResult(resultId);	
		return result;
	}
	
	@RequestMapping(value = "/v1/result/{gameName}/{username}", method = RequestMethod.GET)
	public ResultHistory getResults(
			@PathVariable String gameName, @PathVariable String username,
			@RequestParam(value="from", required=false) DateTime from,
			@RequestParam(value="to", required=false) DateTime to, 
			@RequestParam(value="latestResults", required=false) Integer latestResults) {

		ResultHistory results = getResultHistory(gameName, username, from, to, latestResults);
		
		return results;
		
	}
	
	@RequestMapping(value = "/v1/result/game/{gameName}", method = RequestMethod.GET)
	public ResultHistory getResultsByGameName(
			@PathVariable String gameName, 
			@RequestParam(value="from", required=false) DateTime from,
			@RequestParam(value="to", required=false) DateTime to, 
			@RequestParam(value="latestResults", required=false) Integer latestResults) {

		ResultHistory results = getResultHistoryByGameName(gameName, from, to, latestResults);
		
		return results;
		
	}
	
	@RequestMapping(value = "/v1/result/user/{username}", method = RequestMethod.GET)
	public ResultHistory getResultsByUsername(
			@PathVariable String username, 
			@RequestParam(value="from", required=false) DateTime from,
			@RequestParam(value="to", required=false) DateTime to, 
			@RequestParam(value="latestResults", required=false) Integer latestResults) {

		ResultHistory results = getResultHistoryByUsername(username, from, to, latestResults);
		
		return results;
		
	}
	
	@RequestMapping(value = "/v1/statistics/user/{username}", method = RequestMethod.GET)
	public Statistics getStatisticsByUsername(
			@PathVariable String username, 
			@RequestParam(value="from", required=true) DateTime from,
			@RequestParam(value="to", required=true) DateTime to,
			@RequestParam(value="splitByGame", required=true) Boolean splitByGame,
			@RequestParam(value="aggregation", required=true) DBAggregation aggregation) {

		inputValidator.validateTimeInterval(username, from, to);
		Statistics results = getStatistics(username, from, to, splitByGame, aggregation);
		
		return results;
		
	}
	
	private Statistics getStatistics(String username, DateTime from, DateTime to, Boolean splitByGame, DBAggregation aggregation) {
		Statistics statistics = logicGamesDao.getUsageStatistics(username, from, to, splitByGame, aggregation);
		return statistics;
	}

	private ResultHistory getResultHistoryByGameName(String gameName,
			DateTime from, DateTime to, Integer latestResults) {
		if (from != null) {
			List<Result> results = logicGamesDao.getResultsByGameNameInTimeInterval(gameName,  from, to);
			return new ResultHistory(gameName, from, to, results);
		} else {
			List<Result> results = logicGamesDao.getLatestResultsByGameName(gameName, latestResults);
			return new ResultHistory(gameName, latestResults, results);
		}
	}

	private ResultHistory getResultHistoryByUsername(String username,
			DateTime from, DateTime to, Integer latestResults) {
		if (from != null) {
			inputValidator.validateTimeInterval(username, from, to);
			List<Result> results = logicGamesDao.getResultsByUsernameInTimeInterval(username,  from, to);
			return new ResultHistory(username, from, to, results);
		} else {
			inputValidator.validateLatestEvents(username, latestResults);
			List<Result> results = logicGamesDao.getLatestResultsByUsername(username, latestResults);
			return new ResultHistory(username, latestResults, results);
		}
	}

	public ResultHistory getResultHistory(String gameName, String username,
			DateTime from, DateTime to, Integer latestResults) {
		if (from != null) {
			inputValidator.validateTimeInterval(username, from, to);
			List<Result> results = logicGamesDao.getResultsInTimeInterval(gameName, username, from, to);
			return new ResultHistory(username, from, to, results);
		} else {
			inputValidator.validateLatestEvents(username, latestResults);
			List<Result> results = logicGamesDao.getLatestResults(gameName, username, latestResults);
			return new ResultHistory(username, latestResults, results);
		}
	}

}