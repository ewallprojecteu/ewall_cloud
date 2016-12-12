package eu.ewall.servicebrick.logicgames.model;

import java.io.Serializable;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection="Results0hn0")
public class Result0hn0 implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private ObjectId id;
	
	private String username;
	private int size;
	private int score;
	private int time;
	private int hintsUsed;
	private int undoUsed;
	
	@JsonCreator
	public Result0hn0(@JsonProperty("username") String username,
			@JsonProperty("size") int size, @JsonProperty("score") int score, 
			@JsonProperty("time") int time, @JsonProperty("hintsUsed") int hintsUsed, 
			@JsonProperty("undoUsed") int undoUsed){
		this.username = username;
		this.size = size;
		this.score = score;
		this.time = time;
		this.hintsUsed = hintsUsed;
		this.undoUsed = undoUsed;
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
	
	public int getSize(){
		return size;
	}
	
	public void setSize(int size){
		this.size = size;
	}

	public int getScore(){
		return score;
	}
	
	public void setScore(int score){
		this.score = score;
	}
	
	public int getTime(){
		return time;
	}
	
	public void setTime(int time){
		this.time = time;
	}
	
	public int getHintsUsed(){
		return hintsUsed;
	}
	
	public void setHintsUsed(int hintsUsed){
		this.hintsUsed = hintsUsed;
	}

	public int getUndoUsed(){
		return undoUsed;
	}
	
	public void setUndoUsed(int undoUsed){
		this.undoUsed = undoUsed;
	}
	
}
