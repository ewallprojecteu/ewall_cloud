package eu.ewall.platform.idss.service.model.common.message.content;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.service.model.type.Diagnosis;
import eu.ewall.platform.idss.service.model.type.GoalIntention;

/**
 * An {@link ArgumentContent} object models the {@link MessageContent} part related
 * to providing an argument for physical activity to the user.
 * 
 * @author Harm op den Akker (RRD)
 */
public class ArgumentContent extends AbstractDatabaseObject {
	
	@DatabaseField(value= DatabaseType.STRING)
	private Diagnosis specificDiagnosis;
	
	@DatabaseField(value=DatabaseType.STRING)
	private GoalIntention goalIntention;
	
	// ---------- CONSTRUCTORS ---------- //
	
	public ArgumentContent() { }
	
	public ArgumentContent(Diagnosis specificDiagnosis) {
		this.specificDiagnosis = specificDiagnosis;
	}
	
	public ArgumentContent(Diagnosis specificDiagnosis, GoalIntention goalIntention) {
		this.specificDiagnosis = specificDiagnosis;
		this.goalIntention = goalIntention;
	}
	
	// ---------- GETTERS ---------- //
	
	/**
	 * Returns the specific {@link Diagnosis} for which this {@link ArgumentContent} is valid,
	 * or {@code null} if the argument applies for all types of physical activity.
	 * @return the specific {@link Diagnosis} for which this {@link ArgumentContent} is valid.
	 */
	public Diagnosis getSpecificDiagnosis() {
		return specificDiagnosis;
	}
	
	public GoalIntention getGoalIntention() {
		return goalIntention;
	}
	
	// ---------- SETTERS ---------- //
	
	/**
	 * Sets the specific {@link Diagnosis} for which this argument is valid.
	 * @param specificDiagnosis the specific {@link Diagnosis} for which this argument is valid.
	 */
	public void setSpecificDiagnosis(Diagnosis specificDiagnosis) {
		this.specificDiagnosis = specificDiagnosis;
	}
	
	public void setGoalIntention(GoalIntention goalIntention) {
		this.goalIntention = goalIntention;
	}

}
 