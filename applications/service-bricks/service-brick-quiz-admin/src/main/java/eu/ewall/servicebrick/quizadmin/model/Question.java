package eu.ewall.servicebrick.quizadmin.model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


@Document(collection="Questions")
public class Question implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	private ObjectId id;
	
	private String username;
	private String language;
	private String topicId;
	private String questionText;
	private String answer1;
	private String answer2;
	private String answer3;
	private String answer4;
	private String answer1Picture;
	private String answer2Picture;
	private String answer3Picture;
	private String answer4Picture;
	private int correctAnswerId;
	
	
	//@PersistenceConstructor
	/*@JsonCreator
	public Question(@JsonProperty("language") String language,
			@JsonProperty("topicId") String topicId, @JsonProperty("questionText") String questionText,
			@JsonProperty("answer1") String answer1, @JsonProperty("answer2") String answer2,
			@JsonProperty("answer3") String answer3, @JsonProperty("answer4") String answer4,
			@JsonProperty("answer1Picture") String answer1Picture, @JsonProperty("answer2Picture") String answer2Picture, 
			@JsonProperty("answer3Picture") String answer3Picture, @JsonProperty("answer4Picture") String answer4Picture, 
			@JsonProperty("correctAnswerId") int correctAnswerId) {
		this("all-users", language, topicId, questionText, answer1, answer2, answer3, answer4, answer1Picture, answer2Picture,
				answer3Picture, answer4Picture, correctAnswerId);
	}*/

	//@PersistenceConstructor
	@JsonCreator
	public Question(@JsonProperty("username") String username, @JsonProperty("language") String language,
			@JsonProperty("topicId") String topicId, @JsonProperty("questionText") String questionText,
			@JsonProperty("answer1") String answer1, @JsonProperty("answer2") String answer2,
			@JsonProperty("answer3") String answer3, @JsonProperty("answer4") String answer4,
			@JsonProperty("answer1Picture") String answer1Picture, @JsonProperty("answer2Picture") String answer2Picture, 
			@JsonProperty("answer3Picture") String answer3Picture, @JsonProperty("answer4Picture") String answer4Picture, 
			@JsonProperty("correctAnswerId") int correctAnswerId) {
		this.username = username;
		this.language = language;
		this.topicId = topicId;
		this.questionText = questionText;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = answer3;
		this.answer4 = answer4;
		this.answer1Picture = answer1Picture;
		this.answer2Picture = answer2Picture;
		this.answer3Picture = answer3Picture;
		this.answer4Picture = answer4Picture;
		this.correctAnswerId = correctAnswerId;
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

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getAnswer1() {
		return answer1;
	}

	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}

	public String getAnswer2() {
		return answer2;
	}

	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}

	public String getAnswer3() {
		return answer3;
	}

	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}

	public String getAnswer4() {
		return answer4;
	}

	public void setAnswer4(String answer4) {
		this.answer4 = answer4;
	}

	public String getAnswer1Picture() {
		return answer1Picture;
	}

	public void setAnswer1Picture(String answer1Picture) {
		this.answer1Picture = answer1Picture;
	}

	public String getAnswer2Picture() {
		return answer2Picture;
	}

	public void setAnswer2Picture(String answer2Picture) {
		this.answer2Picture = answer2Picture;
	}

	public String getAnswer3Picture() {
		return answer3Picture;
	}

	public void setAnswer3Picture(String answer3Picture) {
		this.answer3Picture = answer3Picture;
	}

	public String getAnswer4Picture() {
		return answer4Picture;
	}

	public void setAnswer4Picture(String answer4Picture) {
		this.answer4Picture = answer4Picture;
	}

	public int getCorrectAnswerId() {
		return correctAnswerId;
	}

	public void setCorrectAnswerId(int correctAnswerId) {
		this.correctAnswerId = correctAnswerId;
	}

}
