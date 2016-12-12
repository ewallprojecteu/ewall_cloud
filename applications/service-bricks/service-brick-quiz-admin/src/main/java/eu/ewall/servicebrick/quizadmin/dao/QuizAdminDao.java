package eu.ewall.servicebrick.quizadmin.dao;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.multipart.MultipartFile;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.preferences.SystemPreferences;
import eu.ewall.servicebrick.quizadmin.model.Question;
import eu.ewall.servicebrick.quizadmin.model.Result;
import eu.ewall.servicebrick.quizadmin.model.Topic;


@Component
public class QuizAdminDao {
	
	private static final Logger log = LoggerFactory.getLogger(QuizAdminDao.class);
	protected MongoOperations mongoOps;
	private static final String GET_USER_PROFILE = "/users/";

	@Value("${profilingServer.url}")
	private String profilingServerUrl;
	
	@Resource
	private RestOperations ewallClient;
	
	@Resource
	protected ObjectStorage objectStorage;
	
	@Autowired
	public QuizAdminDao(MongoOperations mongoOps) {
		this.mongoOps = mongoOps;
	}
	
	@Cacheable(value = "serviceBrickQuizAdminCache", key = "'getUserProfile-'.concat(#userId)")	
	public User getUser(String userId) {
		try {
			log.info("Getting profile for user " + userId);
			User user = null;
			try {
				log.info("URL: " + profilingServerUrl+GET_USER_PROFILE+userId);
				user = ewallClient.getForObject(profilingServerUrl+GET_USER_PROFILE+userId, User.class);
			} catch (RestClientException e) {
				e.printStackTrace();
				log.info("RestClientException", e);
				return null;
			}
			
			return user;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("Exception", e);
		}
		return null;
		
	}
	
	public Topic getTopic(String username, String topicId){
		Query topicQuery = query(where("username").is(username).andOperator(
				Criteria.where("_id").is(topicId)));
		Topic topic = (Topic) mongoOps.findOne(topicQuery, Topic.class);
		
		if(topic == null) {
			topicQuery = query(where("_id").is(topicId));
			topic = (Topic) mongoOps.findOne(topicQuery, Topic.class);
		}
		
		return topic;
	}
	
	public List<Topic> getTopicsList(String username){
		
		User user = getUser(username);
		SystemPreferences sysPref = (SystemPreferences) user.getUserProfile().getUserPreferencesSubProfile().getSystemPreferences();
		String language = sysPref.getPreferredLanguage();
		
		//String language = "en";
		Query topicsQuery = query(where("username").is(username));
		Query demoTopicsQuery = query(where("username").is("all-users").andOperator(Criteria.where("language").is(language)));
		List<Topic> topics = (List<Topic>) mongoOps.find(topicsQuery, Topic.class);
		topics.addAll(mongoOps.find(demoTopicsQuery, Topic.class));
		
		return topics;
	}
	
	public void newTopic(String username, String language, String topicName, String icon, int noQuestionsPerQuiz){
		List<Question> questions = new ArrayList<Question>();
		Topic topic = new Topic(username, language, topicName, icon, noQuestionsPerQuiz, questions);
		mongoOps.insert(topic);
	}
	
	public void editTopic(String username, String language, String topicId, String newName, String newIcon, int noQuestionsPerQuiz){
		Query topicQuery = query(where("username").is(username).andOperator(Criteria.where("language").is(language).andOperator(
				Criteria.where("_id").is(topicId))));
		Topic topic = mongoOps.findOne(topicQuery, Topic.class);
		if (topic != null){
			topic.setIcon(newIcon);
			topic.setTopicName(newName);
			topic.setNoQuestionsPerQuiz(noQuestionsPerQuiz);
			mongoOps.save(topic);
		}
		else log.debug("Topic not found");
	}
	
	public void deleteTopic(String username, String topicId){
		Query questionsQuery = query(where("topicId").is(topicId));
		mongoOps.remove(questionsQuery, Question.class);
		
		Query topicQuery = query(where("username").is(username).andOperator(Criteria.where("_id").is(topicId)));
		mongoOps.remove(topicQuery, Topic.class);
	}
	
	public Question getQuestion(String username, String questionId){
		Query questionQuery = query(where("username").is(username).andOperator(
				Criteria.where("_id").is(questionId)));
		Question question = (Question) mongoOps.findOne(questionQuery, Question.class);
		return question;
	}
	
	public List<Question> getQuestionsPerTopic(String username, String topicId){
		Query questionsQuery = query(where("username").is(username).andOperator(
				Criteria.where("topicId").is(topicId)));
		Query demoQuestionsQuery = query(where("username").is("all-users").andOperator(
				Criteria.where("topicId").is(topicId)));
		List<Question> questions = (List<Question>) mongoOps.find(questionsQuery, Question.class);
		questions.addAll(mongoOps.find(demoQuestionsQuery, Question.class));
		return questions;
	}
	
	public void newQuestion(String username, String language, String topicId, String questionText,
			String answer1, String answer2, String answer3, String answer4,
			String answer1Picture, String answer2Picture, String answer3Picture, String answer4Picture,
			int correctAnswerId){
		Question question = new Question(username, language, topicId, questionText,
				answer1, answer2, answer3, answer4, 
				answer1Picture, answer2Picture, answer3Picture, answer4Picture, correctAnswerId);
		mongoOps.insert(question);
		
		Query topicQuery = query(where("username").is(username).andOperator(
				Criteria.where("_id").is(topicId)));
		Topic topic = mongoOps.findOne(topicQuery, Topic.class);
		log.info("Topic name: " + topic.getTopicName());
		int questionsPerTopic = topic.getNoQuestionsPerTopic();
		topic.setNoQuestionsPerTopic(questionsPerTopic + 1);
		List<Question> questions = topic.getQuestions();
		questions.add(question);
		mongoOps.save(topic);
	}
	
	public void editQuestion(String username, String language, String questionId, String topicId, String questionText, 
			String answer1, String answer2, String answer3, String answer4,
			String answer1Picture, String answer2Picture, String answer3Picture,  String answer4Picture,
			int correctAnswerId){
		Query questionQuery = query(where("username").is(username).andOperator(Criteria.where("_id").is(questionId)));
		Question question = (Question) mongoOps.findOne(questionQuery, Question.class);
		
		Query topicQuery = query(where("username").is(username).andOperator(Criteria.where("language").is(language).andOperator(
				Criteria.where("_id").is(topicId))));
		Topic topic = mongoOps.findOne(topicQuery, Topic.class);
		List<Question> questions = topic.getQuestions();
		Iterator<Question> it = questions.iterator();
		while (it.hasNext()) {
			Question q = it.next();
			if(q.getId().equals(questionId)){
				it.remove();
			}
		}
		mongoOps.save(topic);
		
		question.setUsername(username);
		question.setLanguage(language);
		question.setQuestionText(questionText);
		question.setAnswer1(answer1);
		question.setAnswer2(answer2);
		question.setAnswer3(answer3);
		question.setAnswer4(answer4);
		question.setAnswer1Picture(answer1Picture);
		question.setAnswer2Picture(answer2Picture);
		question.setAnswer3Picture(answer3Picture);
		question.setAnswer4Picture(answer4Picture);
		question.setCorrectAnswerId(correctAnswerId);
		mongoOps.save(question);
		
		questions.add(question);
		mongoOps.save(topic);
		
	}
	
	public void deleteQuestion(String username, String questionId){
		Query questionQuery = query(where("username").is(username).andOperator(Criteria.where("_id").is(questionId)));
		Question question = (Question) mongoOps.findOne(questionQuery, Question.class);
		mongoOps.remove(questionQuery, Question.class);
		
		Query topicQuery = query(where("username").is(username).andOperator(
				Criteria.where("_id").is(question.getTopicId())));
		Topic topic = mongoOps.findOne(topicQuery, Topic.class);
		topic.setNoQuestionsPerTopic(topic.getNoQuestionsPerTopic()-1);
		List<Question> questions = topic.getQuestions();
		
		Iterator<Question> it = questions.iterator();
		while (it.hasNext()) {
			Question q = it.next();
			if(q.getId().equals(questionId)){
				it.remove();
			}
		}
		mongoOps.save(topic);
	}
	
	public void uploadImage(String username, MultipartFile image, String imageName) throws IOException{
		objectStorage.uploadImage(username, image, imageName);
		objectStorage.close();
	}
	
	public InputStream getImageDao(String username, String imageName) throws IOException{
		InputStream is = objectStorage.getImage(username, imageName);
		objectStorage.close();
		return is;
	}
	public List<String> getImageList(String username) throws IOException{
		List<String> urls = new ArrayList<String>();
		urls = objectStorage.getImageList(username);
		return urls;
	}

	public void deleteImage(String username, String imageName) throws IOException {
		objectStorage.deleteImage(username, imageName);
		objectStorage.close();
	}
	

}
