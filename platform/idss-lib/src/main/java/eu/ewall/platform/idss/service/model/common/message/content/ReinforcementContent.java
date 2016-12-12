package eu.ewall.platform.idss.service.model.common.message.content;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.service.model.type.PrimaryIntention;

/**
 * 
 * @author Harm op den Akker (RRD)
 */
public class ReinforcementContent extends AbstractDatabaseObject {
	
	@DatabaseField(value= DatabaseType.STRING)
	private PrimaryIntention reinforcementIntention = null;
	
	@DatabaseField(value= DatabaseType.INT)
	private boolean closeToGoal = false;
	
	@DatabaseField(value= DatabaseType.INT)
	private boolean isFunny = false;
		
	// ---------- CONSTRUCTORS ---------- //
	
	/**
	 * Creates an instance of a {@link ReinforcementContent} with given values for {@code intention}
	 * (the {@link PrimaryIntention} of this {@link eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage}), {@code closeToGoal} (whether
	 * or not the user is close to achieving his goal), and {@code isFunny} (whether or not the message
	 * should include a small joke).
	 * @param reinforcementIntention the {@link PrimaryIntention} of this {@link eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage}.
	 * @param closeToGoal whether or not the user is close to achieving his goal.
	 * @param isFunny whether or not the message should include a small joke.
	 */
	public ReinforcementContent(PrimaryIntention reinforcementIntention, boolean closeToGoal, boolean isFunny) {
		this.reinforcementIntention = reinforcementIntention;
		this.closeToGoal = closeToGoal;
		this.isFunny = isFunny;
		
	}
	
	/**
	 * Creates an instance of a {@link ReinforcementContent}, initialized with {@code null}/{@code false} values
	 * for all its parameters.
	 */
	public ReinforcementContent() {	}
	
	// ---------- GETTERS ---------- //
	
	/**
	 * Returns what the overall primary intention of this {@link eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage} was as either
	 * {@link PrimaryIntention#ENCOURAGE}, {@link PrimaryIntention#NEUTRAL}, or {@link PrimaryIntention#DISCOURAGE}.
	 * @return the overall primary intention of this {@link eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage}.
	 */
	public PrimaryIntention getReinforcementIntention() {
		return reinforcementIntention;
	}
	
	/**
	 * Returns {@code true} if the message should include a small joke, {@code false} otherwise.
	 * @return {@code true} if the message should include a small joke, {@code false} otherwise.
	 */
	public boolean getIsFunny() {
		return isFunny;
	}
	
	/**
	 * Returns {@code true} if the user is close to achieving his goal, {@code false} otherwise.
	 * @return {@code true} if the user is close to achieving his goal, {@code false} otherwise.
	 */
	public boolean getCloseToGoal() {
		return closeToGoal;
	}
	
	// ---------- SETTERS ---------- //
	
	/**
	 * Sets the {@link PrimaryIntention} for this {@link eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage}.
	 * @param reinforcementIntention the {@link PrimaryIntention} for this {@link eu.ewall.platform.idss.service.model.common.message.PhysicalActivityMotivationalMessage}.
	 */
	public void setReinforcementIntention(PrimaryIntention reinforcementIntention) {
		this.reinforcementIntention = reinforcementIntention;
	}
	
	/**
	 * Sets whether or not this {@link ReinforcementContent} should contain a 
	 * funny remark or a small joke.
	 * @param isFunny whether or not the message should contain a small joke.
	 */
	public void setIsFunny(boolean isFunny) {
		this.isFunny = isFunny;
	}
	
	/**
	 * Sets whether or not this {@link ReinforcementContent} should express that
	 * the user is close to achieving his goal.
	 * @param closeToGoal whether or not the user is close to achieving his goal.
	 */
	public void setCloseToGoal(boolean closeToGoal) {
		this.closeToGoal = closeToGoal;
	}

}
