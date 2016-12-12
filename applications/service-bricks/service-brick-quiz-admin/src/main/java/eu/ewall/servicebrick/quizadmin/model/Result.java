package eu.ewall.servicebrick.quizadmin.model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "Results")
public class Result implements Serializable, Comparable<Result> {
	
	private static final long serialVersionUID = 1L;

	@Id
	private ObjectId id;

	private String username;
	private String topicId;
	private String topicName;
	private int numberOfCorrectAnswers;
	private int numberOfQuestionsPerQuiz;
	private float correctPercentage;
	private DateTime timestamp;
	
	@JsonCreator
	public Result(@JsonProperty("username") String username,
			@JsonProperty("topicId") String topicId,
			@JsonProperty("topicName") String topicName,
			@JsonProperty("numberOfCorrectAnswers") int numberOfCorrectAnswers,
			@JsonProperty("numberOfQuestionsPerQuiz") int numberOfQuestionsPerQuiz) {
		this.username = username;
		this.topicId = topicId;
		this.topicName = topicName;
		this.numberOfQuestionsPerQuiz = numberOfQuestionsPerQuiz;
		this.numberOfCorrectAnswers = numberOfCorrectAnswers;
		this.correctPercentage = (float)numberOfCorrectAnswers / numberOfQuestionsPerQuiz;
		this.timestamp = new DateTime();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public int getNumberOfCorrectAnswers() {
		return numberOfCorrectAnswers;
	}

	public int getNumberOfQuestionsPerQuiz() {
		return numberOfQuestionsPerQuiz;
	}

	public float getCorrectPercentage() {
		return correctPercentage;
	}

	public DateTime getTimestamp() {
		return timestamp;
	}

	@Override
	public int compareTo(Result compareResult) {
		DateTime compareTimestamp = compareResult.getTimestamp();
		return DateTimeComparator.getInstance().compare(compareTimestamp, this.timestamp);
	}

}
