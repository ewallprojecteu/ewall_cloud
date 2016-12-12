package eu.ewall.platform.idss.service.model.common.message.content;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;

/**
 * A {@link GreetingContent} object models the {@link MessageContent} part related
 * to the provision of a greeting to the user.
 * 
 * @author Harm op den Akker (RRD)
 */
public class GreetingContent extends AbstractDatabaseObject {
	
	@DatabaseField(value= DatabaseType.INT)
	private boolean isTimeBased = false;
	
	@DatabaseField(value= DatabaseType.INT)
	private boolean useFirstName = false;
	
	
	// ---------- GETTERS ---------- //
	
	/**
	 * Returns {@code true} if the greeting should be tailored to the time of day
	 * (i.e. daypart as morning, afternoon,...).
	 * @return {@code true} if the greeting should be tailored to the time of day.
	 */
	public boolean getIsTimeBased() {
		return isTimeBased;
	}
	
	/**
	 * Returns {@code true} if the greeting should be tailored to include the user's 
	 * first name in the greeting.
	 * @return {@code true} if the greeting should be tailored to include the user's 
	 * first name in the greeting, {@code false} otherwise.
	 */
	public boolean getUseFirstName() {
		return useFirstName;
	}
	
	// ---------- SETTERS ---------- //
	
	/**
	 * Sets whether the greeting should be tailored to the time of day (i.e. day part).
	 * @param isTimeBased {@code true} if the greeting should be tailored to the time of day.
	 */
	public void setIsTimeBased(boolean isTimeBased) {
		this.isTimeBased = isTimeBased;
	}
	
	/**
	 * Sets whether the greeting should be tailored to include the user's first name.
	 * @param useFirstName whether the greeting should be tailored to include the user's first name.
	 */
	public void setUseFirstName(boolean useFirstName) {
		this.useFirstName = useFirstName;
	}
}
