package eu.ewall.servicebrick.quizadmin.model;

import java.io.Serializable;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

//import com.fasterxml.jackson.annotation.JsonCreator;
//import com.fasterxml.jackson.annotation.JsonProperty;


@Document(collection="Topics")
public class Topic implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	private ObjectId id;
	
	private String username;
	private String language;
	private String topicName;
	private String icon;
	private int noQuestionsPerTopic;
	private int noQuestionsPerQuiz;
	private List<Question> questions;
	
	
	/*@PersistenceConstructor
	public Topic(String username, String language, String topicName, String icon, int noQuestionsPerTopic, int noQuestionsPerQuiz) {
		this(username, language, name, noQuestionsPerTopic, noQuestionsPerQuiz);
	}*/

	@JsonCreator
	public Topic(@JsonProperty("username") String username, 
			@JsonProperty("language") String language, @JsonProperty("topicName") String topicName, @JsonProperty("icon") String icon, 
			@JsonProperty("noQuestionsPerQuiz") int noQuestionsPerQuiz, @JsonProperty("questions") List<Question> questions) {
		this.username = username;
		this.language = language;
		this.topicName = topicName;
		this.icon = icon;
		this.noQuestionsPerTopic = 0;
		this.noQuestionsPerQuiz = noQuestionsPerQuiz;
		this.questions = questions;
	}
	

	public String getId() {
		return id.toString();
	}


	public void setId(ObjectId id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getLanguage() {
		return language;
	}


	public void setLanguage(String language) {
		this.language = language;
	}


	public String getTopicName() {
		return topicName;
	}


	public void setTopicName(String name) {
		this.topicName = name;
	}
	

	public String getIcon() {
		return icon;
	}

	
	public void setIcon(String icon) {
		this.icon = icon;
	}


	public int getNoQuestionsPerTopic() {
		return noQuestionsPerTopic;
	}


	public void setNoQuestionsPerTopic(int noQuestionsPerTopic) {
		this.noQuestionsPerTopic = noQuestionsPerTopic;
	}


	public int getNoQuestionsPerQuiz() {
		return noQuestionsPerQuiz;
	}


	public void setNoQuestionsPerQuiz(int noQuestionsPerQuiz) {
		this.noQuestionsPerQuiz = noQuestionsPerQuiz;
	}
	
	public List<Question> getQuestions() {
		return questions;
	}


	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	
}
