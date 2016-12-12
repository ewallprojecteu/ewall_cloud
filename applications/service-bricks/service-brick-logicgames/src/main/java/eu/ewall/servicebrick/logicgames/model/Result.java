package eu.ewall.servicebrick.logicgames.model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "GameResults")
public class Result implements Serializable, Comparable<Result> {
	
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String username;
	private String gameName;
	private String type;
	private String level;
	private Integer nrOfMoves;
	private Integer nrOfAnswersOk;
	private String topicId;
	private Integer elapsedTimeSecs;
	private Integer nrOfLevels;
	private Integer minMoves;
	private Integer minElapsedTimeSecs;
	private Integer nrOfQuestions;
	private Double performance = 0d;
	private Boolean completed;
	private DateTime timestamp;
	
	
	@JsonCreator
	public Result(@JsonProperty("username") String username,
			@JsonProperty("gameName") String gameName,
			@JsonProperty("type") String type,
			@JsonProperty("level") String level,
			@JsonProperty("nrOfMoves") Integer nrOfMoves,
			@JsonProperty("nrOfAnswersOk") Integer nrOfAnswersOk,
			@JsonProperty("topicId") String topicId,
			@JsonProperty("elapsedTimeSecs") Integer elapsedTimeSecs,
			@JsonProperty("nrOfLevels") Integer nrOfLevels,
			@JsonProperty("minMoves") Integer minMoves,
			@JsonProperty("minElapsedTimeSecs") Integer minElapsedTimeSecs,
			@JsonProperty("nrOfQuestions") Integer nrOfQuestions,
			@JsonProperty("completed") Boolean completed					
			) {
		this.username = username;
		this.gameName = gameName;
		this.type = type;
		this.level = level;
		this.nrOfMoves = nrOfMoves;
		this.nrOfAnswersOk = nrOfAnswersOk;
		this.topicId = topicId;
		this.elapsedTimeSecs = elapsedTimeSecs;
		this.nrOfLevels = nrOfLevels;
		this.minMoves = minMoves;
		this.minElapsedTimeSecs = minElapsedTimeSecs;
		this.nrOfQuestions = nrOfQuestions;
		this.completed = completed;
		this.timestamp = new DateTime();
		
		//Calculate performance
		if(completed) {
			if(nrOfMoves!=null && minMoves!=null) {
				this.performance=((double)minMoves/(double)nrOfMoves*100);	
			} else if(nrOfAnswersOk!=null && nrOfQuestions!=null){
				this.performance=((double)nrOfAnswersOk/(double)nrOfQuestions*100);											
			} else if(elapsedTimeSecs!=null && minElapsedTimeSecs!=null){
				this.performance=((double)minElapsedTimeSecs/(double)elapsedTimeSecs*100);						
			}
			if(this.performance.doubleValue()>100){
				this.performance=100d;
			}
		} else {
			this.performance = 0d;
		}
	}


	@Override
	public int compareTo(Result compareResult) {
		DateTime compareTimestamp = compareResult.getTimestamp();
		return DateTimeComparator.getInstance().compare(compareTimestamp, this.timestamp);
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getGameName() {
		return gameName;
	}


	public void setGameName(String gameName) {
		this.gameName = gameName;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getLevel() {
		return level;
	}


	public void setLevel(String level) {
		this.level = level;
	}


	public Integer getNrOfMoves() {
		return nrOfMoves;
	}


	public void setNrOfMoves(Integer nrOfMoves) {
		this.nrOfMoves = nrOfMoves;
	}


	public Integer getNrOfAnswersOk() {
		return nrOfAnswersOk;
	}


	public void setNrOfAnswersOk(Integer nrOfAnswersOk) {
		this.nrOfAnswersOk = nrOfAnswersOk;
	}


	public Integer getElapsedTimeSecs() {
		return elapsedTimeSecs;
	}


	public void setElapsedTimeSecs(Integer elapsedTimeSecs) {
		this.elapsedTimeSecs = elapsedTimeSecs;
	}


	public Integer getNrOfLevels() {
		return nrOfLevels;
	}


	public void setNrOfLevels(Integer nrOfLevels) {
		this.nrOfLevels = nrOfLevels;
	}


	public Integer getNrOfQuestions() {
		return nrOfQuestions;
	}


	public void setNrOfQuestions(Integer nrOfQuestions) {
		this.nrOfQuestions = nrOfQuestions;
	}


	public Boolean isCompleted() {
		return completed;
	}


	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}


	public DateTime getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public String getTopicId() {
		return topicId;
	}


	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}


	public Double getPerformance() {
		return performance;
	}


	public void setPerformance(Double performance) {
		this.performance = performance;
	}


	public Boolean getCompleted() {
		return completed;
	}


	public Integer getMinMoves() {
		return minMoves;
	}


	public void setMinMoves(Integer minMoves) {
		this.minMoves = minMoves;
	}


	public Integer getMinElapsedTimeSecs() {
		return minElapsedTimeSecs;
	}


	public void setMinElapsedTimeSecs(Integer minElapsedTimeSecs) {
		this.minElapsedTimeSecs = minElapsedTimeSecs;
	}



}
