package eu.ewall.platform.idss.service.model.common.message.intention;

import com.fasterxml.jackson.annotation.JsonIgnore;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage;
import eu.ewall.platform.idss.service.model.type.PrimaryIntention;
import eu.ewall.platform.idss.service.model.type.SecondaryIntention;

import java.util.ArrayList;
import java.util.List;

/**
 * The intention component of a {@link PhysicalActivityMotivationalMessage MotivationMessage}.
 * It consists of a primary intention (discourage, encourage, neutral) and at
 * least one secondary intention.<br />
 * {@link SecondaryIntention#REINFORCEMENT REINFORCEMENT} can only be used with primary
 * intention {@link PrimaryIntention#NEUTRAL NEUTRAL}.<br />
 * {@link SecondaryIntention#SUGGESTION SUGGESTION} cannot be used with primary
 * intention {@link PrimaryIntention#NEUTRAL NEUTRAL}.
 * 
 * @author Dennis Hofs (RRD)
 * @author Harm op den Akker (RRD)
 */
public class MessageIntention extends AbstractDatabaseObject {
	@JsonIgnore
	private String id;

	@DatabaseField(value= DatabaseType.STRING)
	private PrimaryIntention primaryIntention = null;
	
	@DatabaseField(value=DatabaseType.LIST, elemType=DatabaseType.STRING)
	private List<SecondaryIntention> secondaryIntentions = new ArrayList<SecondaryIntention>();
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the primary intention (discourage, encourage, neutral, synchronize).
	 * 
	 * @return the primary intention
	 */
	public PrimaryIntention getPrimaryIntention() {
		return primaryIntention;
	}

	/**
	 * Sets the primary intention (discourage, encourage, neutral, synchronize).
	 * 
	 * @param primary the primary intention
	 */
	public void setPrimaryIntention(PrimaryIntention primary) {
		this.primaryIntention = primary;
	}

	/**
	 * Returns the secondary intentions.
	 * 
	 * @return the secondary intentions
	 */
	public List<SecondaryIntention> getSecondaryIntentions() {
		return secondaryIntentions;
	}

	/**
	 * Sets the secondary intentions.
	 * 
	 * @param secondary the secondary intentions
	 */
	public void setSecondaryIntentions(List<SecondaryIntention> secondary) {
		this.secondaryIntentions = secondary;
	}
	
	/**
	 * Adds a secondary intention.
	 * 
	 * @param secondary the secondary intentions
	 */
	public void addSecondaryIntention(SecondaryIntention secondary) {
		this.secondaryIntentions.add(secondary);
	}
	
	/**
	 * Returns {@code true} if a secondary intention for "greeting" is present.
	 * @return {@code true} if a secondary intention for "greeting" is present.
	 */
	public boolean hasGreetingIntention() {
		for(SecondaryIntention secondaryIntention : secondaryIntentions) {
			if(secondaryIntention == SecondaryIntention.GREETING) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns {@code true} if a secondary intention for "feedback" is present.
	 * @return {@code true} if a secondary intention for "feedback" is present.
	 */
	public boolean hasFeedbackIntention() {
		for(SecondaryIntention secondaryIntention : secondaryIntentions) {
			if(secondaryIntention == SecondaryIntention.FEEDBACK) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns {@code true} if a secondary intention for "argument" is present.
	 * @return {@code true} if a secondary intention for "argument" is present.
	 */
	public boolean hasArgumentIntention() {
		for(SecondaryIntention secondaryIntention : secondaryIntentions) {
			if(secondaryIntention == SecondaryIntention.ARGUMENT) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns {@code true} if a secondary intention for "suggestion" is present.
	 * @return {@code true} if a secondary intention for "suggestion" is present.
	 */
	public boolean hasSuggestionIntention() {
		for(SecondaryIntention secondaryIntention : secondaryIntentions) {
			if(secondaryIntention == SecondaryIntention.SUGGESTION) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns {@code true} if a secondary intention for "reinforcement" is present.
	 * @return {@code true} if a secondary intention for "reinforcement" is present.
	 */
	public boolean hasReinforcementIntention() {
		for(SecondaryIntention secondaryIntention : secondaryIntentions) {
			if(secondaryIntention == SecondaryIntention.REINFORCEMENT) {
				return true;
			}
		}
		return false;
	}
	
	// ---------- Secondary intentions related to the "WARNING" primary intention
	
	/**
	 * Returns {@code true} if a secondary intention for "synchronize sensor" is present.
	 * This can only be true if the primary intention is set to {@link PrimaryIntention#WARNING}.
	 * @return {@code true} if a secondary intention for "synchronize sensor" is present.
	 */
	public boolean hasSynchronizeSensorIntention() {
		for(SecondaryIntention secondaryIntention: secondaryIntentions) {
			if(secondaryIntention == SecondaryIntention.SYNCHRONIZE_SENSOR) {
				return true;
			}
		}
		return false;
	}

}
