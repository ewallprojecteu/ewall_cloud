package eu.ewall.platform.idss.service.model.common.message.representation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage;

public class MessageRepresentation extends AbstractDatabaseObject {
	@JsonIgnore
	private String id;
	
	@DatabaseField(value=DatabaseType.TEXT)
	private String greetingText = null;
	
	@DatabaseField(value= DatabaseType.TEXT)
	private String feedbackText = null;

	@DatabaseField(value=DatabaseType.TEXT)
	private String argumentText = null;

	@DatabaseField(value=DatabaseType.TEXT)
	private String suggestionText = null;

	@DatabaseField(value=DatabaseType.TEXT)
	private String reinforcementText = null;
	
	@DatabaseField(value=DatabaseType.TEXT)
	private String synchronizeSensorText = null;
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	public String getGreetingText() {
		return greetingText;
	}
	
	public void setGreetingText(String greetingText) {
		this.greetingText = greetingText;
	}
	
	public String getFeedbackText() {
		return feedbackText;
	}

	public void setFeedbackText(String feedbackText) {
		this.feedbackText = feedbackText;
	}

	public String getArgumentText() {
		return argumentText;
	}

	public void setArgumentText(String argumentText) {
		this.argumentText = argumentText;
	}

	public String getSuggestionText() {
		return suggestionText;
	}

	public void setSuggestionText(String suggestionText) {
		this.suggestionText = suggestionText;
	}

	public String getReinforcementText() {
		return reinforcementText;
	}

	public void setReinforcementText(String reinforcementText) {
		this.reinforcementText = reinforcementText;
	}
	
	/**
	 * Returns the textual representation selected for the message's SynchronizeSensor secondary intention.
	 * @return the textual representation selected for the message's SynchronizeSensor secondary intention.
	 */
	public String getSynchronizeSensorText() {
		return synchronizeSensorText;
	}
	
	/**
	 * Sets the textual representation selected for the message's SynchronizeSensor secondary intention.
	 * @param synchronizeSensorText the textual representation selected for the message's SynchronizeSensor secondary intention.
	 */
	public void setSynchronizeSensorText(String synchronizeSensorText) {
		this.synchronizeSensorText = synchronizeSensorText;		
	}
	
	/**
	 * Builds the textual (string) representation of the motivational part of this
	 * {@link PhysicalActivityMotivationalMessage} (greeting, feedback and reinforcement).
	 * TODO: Implement dynamic, smart ordering of representation components.
	 * @return a textual representation of the motivational part of this {@link PhysicalActivityMotivationalMessage}.
	 */
	public String getMotivationalComponentText() {
		StringBuilder builder = new StringBuilder();
		
		if(greetingText != null) {
			appendText(builder, greetingText);
		}
		
		if (feedbackText != null) {
			appendText(builder, feedbackText);
		}
		
		if(reinforcementText != null) {
			appendText(builder, reinforcementText);
		}
		return builder.toString();
	}
	
	/**
	 * Builds the textual (string) representation of the suggestion part of this
	 * {@link PhysicalActivityMotivationalMessage} (argument, suggestion).
	 * TODO: Implement dynamic, smart ordering of representation components.
	 * @return a textual representation of the suggestion part of this {@link PhysicalActivityMotivationalMessage}.
	 */
	public String getSuggestionComponentText() {
		
		StringBuilder builder = new StringBuilder();
		
		if(argumentText != null) {
			appendText(builder, argumentText);
		}
		
		if(suggestionText != null) {
			appendText(builder, suggestionText);
		}
		
		if(synchronizeSensorText != null) {
			appendText(builder, synchronizeSensorText);
		}
		
		return builder.toString();
	}
	
	private void appendText(StringBuilder builder, String text) {
		if (text == null)
			return;
		text = text.trim();
		if (text.length() == 0)
			return;
		if (builder.length() > 0) {
			Pattern endPunctPattern = Pattern.compile("\\p{Punct}$");
			Matcher m = endPunctPattern.matcher(builder);
			if (!m.find())
				builder.append(".");
			builder.append(" ");
		}
		builder.append(text);
	}
	
	/**
	 * Returns a {@link String} representation of this {@link MessageRepresenation}
	 * by concatenating all individual message components together.
	 * @return a complete String representation of this {@link MessageRepresentation}.
	 */
	public String getCompleteMessage() {
		StringBuilder builder = new StringBuilder();
		
		if(greetingText != null) {
			appendText(builder, greetingText);
		}
		
		if (feedbackText != null) {
			appendText(builder, feedbackText);
		}
		
		if(argumentText != null) {
			appendText(builder, argumentText);
		}
		
		if(suggestionText != null) {
			appendText(builder, suggestionText);
		}
		
		if(reinforcementText != null) {
			appendText(builder, reinforcementText);
		}
		
		if(synchronizeSensorText != null) {
			appendText(builder, synchronizeSensorText);
		}
		
		return builder.toString();
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if(greetingText != null) {
			appendText(builder,greetingText+" [greeting]");
		}
		
		if(feedbackText != null) {
			appendText(builder,feedbackText+" [feedback]");
		}
		
		if(reinforcementText != null) {
			appendText(builder,reinforcementText+" [reinforcement]");
		}
		
		if(argumentText != null) {
			appendText(builder,argumentText+" [argument]");
		}
		
		if(suggestionText != null) {
			appendText(builder,suggestionText+" [suggestion]");
		}
		
		if(synchronizeSensorText != null) {
			appendText(builder,synchronizeSensorText+" [synchronizeSensor]");
		}
		
		return builder.toString();
	}
}
