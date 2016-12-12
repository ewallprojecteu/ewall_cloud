package eu.ewall.servicebrick.logicgames.model;

import java.io.Serializable;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection="ResultsMemoryGame")
public class ResultMemoryGame implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private ObjectId id;
	
	private String username;
	private int time;
	private int size;
	private int numberOfClicks;
	
	@JsonCreator
	public ResultMemoryGame(@JsonProperty("username") String username, @JsonProperty("time") int time, @JsonProperty("size") int size, @JsonProperty("numberOfClicks") int numberOfClicks){
		this.username = username;
		this.time = time;
		this.size = size;
		this.numberOfClicks = numberOfClicks;
	}
	
	public String getId(){
		return id.toString();
	}
	
	public void setId(ObjectId id){
		this.id = id;
	}
	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public int getTime(){
		return time;
	}
	
	public void setTime(int time){
		this.time = time;
	}
	
	public int getSize(){
		return size;
	}
	
	public void setSize(int size){
		this.size = size;
	}
	
	public int getNumberOfClicks(){
		return numberOfClicks;
	}
	
	public void setNumberOfClicks(int numberOfClicks){
		this.numberOfClicks = numberOfClicks;
	}
}
