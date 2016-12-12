package eu.ewall.servicebrick.quizadmin.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.maven.wagon.util.IoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import eu.ewall.servicebrick.common.dao.ProfilingServerDao;
import eu.ewall.servicebrick.quizadmin.dao.QuizAdminDao;
import eu.ewall.servicebrick.quizadmin.model.Question;
import eu.ewall.servicebrick.quizadmin.model.Result;
import eu.ewall.servicebrick.quizadmin.model.Topic;

@RestController
public class QuizAdminController {

	private static final Logger log = LoggerFactory.getLogger(QuizAdminController.class);
	
	private QuizAdminDao quizAdminDao;

	
	@Autowired
	public QuizAdminController(ProfilingServerDao profilingServerDao, QuizAdminDao quizAdminDao) {
		this.quizAdminDao = quizAdminDao;
	}
	
	@RequestMapping(value = "/v1/{username}/topics/{topicId}", method = RequestMethod.GET)
	public Topic getTopic(@PathVariable String username,
			@PathVariable String topicId){
		Topic topic = quizAdminDao.getTopic(username, topicId);
		return topic;
	}
	
	@RequestMapping(value = "/v1/{username}/topics", method = RequestMethod.GET)
	public List<Topic> getTopicsList(@PathVariable String username){
		List<Topic> topics = quizAdminDao.getTopicsList(username);
		return topics;
	}
	
	@RequestMapping(value = "/v1/{username}/topics", method = RequestMethod.POST)
	public ResponseEntity<String> newTopic(@PathVariable String username, @RequestBody Topic topic){
		quizAdminDao.newTopic(username, topic.getLanguage(), topic.getTopicName(), topic.getIcon(), topic.getNoQuestionsPerQuiz());
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/v1/{username}/topics/{topicId}", method = RequestMethod.POST)
	public ResponseEntity<String> editTopic(@PathVariable String username, @RequestBody Topic topic){
		quizAdminDao.editTopic(username, topic.getLanguage(), topic.getId(), topic.getTopicName(), topic.getIcon(), topic.getNoQuestionsPerQuiz());
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/v1/{username}/topics/{topicId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteTopic(@PathVariable String username,
			@PathVariable String topicId){
		quizAdminDao.deleteTopic(username, topicId);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/v1/{username}/questions/{questionId}", method = RequestMethod.GET)
	public Question getQuestion(@PathVariable String username,
			@PathVariable String questionId){
		Question question = quizAdminDao.getQuestion(username, questionId);
		return question;
	}
	
	@RequestMapping(value = "/v1/{username}/topics/{topicId}/questions", method = RequestMethod.GET)
	public List<Question> getQuestionsPerTopic(@PathVariable String username,
			@PathVariable String topicId){
		List<Question> questions = quizAdminDao.getQuestionsPerTopic(username, topicId);
		return questions;
	}
	
	@RequestMapping(value = "/v1/{username}/questions", method = RequestMethod.POST)
	public ResponseEntity<String> newQuestion(@PathVariable String username, @RequestBody Question question){
		quizAdminDao.newQuestion(username, question.getLanguage(), question.getTopicId(), question.getQuestionText(),
				question.getAnswer1(), question.getAnswer2(), question.getAnswer3(), question.getAnswer4(),
				question.getAnswer1Picture(), question.getAnswer2Picture(), question.getAnswer3Picture(), question.getAnswer4Picture(),
				question.getCorrectAnswerId());
		return new ResponseEntity<String>(HttpStatus.OK);
		
	};
	
	@RequestMapping(value = "/v1/{username}/questions/{questionId}", method = RequestMethod.POST)
	public ResponseEntity<String> editQuestion(@PathVariable String username, @RequestBody Question question){
		quizAdminDao.editQuestion(username, question.getLanguage(), question.getId(), question.getTopicId(), question.getQuestionText(),
				question.getAnswer1(), question.getAnswer2(), question.getAnswer3(), question.getAnswer4(),
				question.getAnswer1Picture(), question.getAnswer2Picture(), question.getAnswer3Picture(), question.getAnswer4Picture(),
				question.getCorrectAnswerId());
		return new ResponseEntity<String>(HttpStatus.OK);
	}
			
	
	@RequestMapping(value = "/v1/{username}/questions/{questionId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteQuestion(@PathVariable String username,
			@PathVariable String questionId){
		quizAdminDao.deleteQuestion(username, questionId);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/v1/{username}/images", method = RequestMethod.POST)
	public ResponseEntity<String> uploadImage(@PathVariable String username,
			@RequestParam(value="imageName", required=true) String imageName,
			@RequestParam(value="image", required = true) MultipartFile image) throws IOException{
		quizAdminDao.uploadImage(username, image, imageName);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/v1/{username}/images/{imageName}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody byte[] getImage(@PathVariable String username,
			@PathVariable(value="imageName") String imageName) throws IOException{
		InputStream is = quizAdminDao.getImageDao(username, imageName);
		return IoUtils.toByteArray(is);
	}
	
	@RequestMapping(value = "/v1/{username}/images", method = RequestMethod.GET)
	public @ResponseBody List<String> getImageList(@PathVariable String username) throws IOException{
		List<String> urls = quizAdminDao.getImageList(username);
		return urls;
	}
	
	@RequestMapping(value = "/v1/{username}/images/{imageName}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteImage(@PathVariable String username,
			@PathVariable String imageName) throws IOException{
		quizAdminDao.deleteImage(username, imageName);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	

	
}
