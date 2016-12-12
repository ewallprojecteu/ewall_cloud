package eu.ewall.platform.idss.service.model.state.domain;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;

/**
 * This class models a physical activity state, presented using a variety
 * of different parameters.
 * 
 * @author Harm op den Akker (RRD)
 */
public class ActivityState extends AbstractDatabaseObject {
	
	@DatabaseField(value= DatabaseType.INT)
	private int steps;
	
	@DatabaseField(value= DatabaseType.INT)
	private int burnedCalories;
	
	@DatabaseField(value= DatabaseType.FLOAT)
	private float kilometers;
	
	/**
	 * Creates an instance of an empty {@link ActivityState}.
	 */
	public ActivityState() {}
	
	/**
	 * Creates an instance of a {@link ActivityState} presented by the number of steps
	 * taken, the number of calories burned, and the number of kilometers walked.
	 * @param steps the number of steps presented in this {@link ActivityState}.
	 * @param burnedCalories the number of burned calories presented in this {@link ActivityState}.
	 * @param kilometers the number of kilometers walked presented in this {@link ActivityState}.
	 */
	public ActivityState(int steps, int burnedCalories, float kilometers) {
		this.steps = steps;
		this.burnedCalories = burnedCalories;
		this.kilometers = kilometers;
	}
	
	/**
	 * Returns the number of steps presented in this {@link ActivityState}.
	 * @return the number of steps presented in this {@link ActivityState}.
	 */
	public int getSteps() {
		return steps;
	}
	
	/**
	 * Sets the number of steps presented in this {@link ActivityState}.
	 * @param steps the number of steps presented in this {@link ActivityState}.
	 */
	public void setSteps(int steps) {
		this.steps = steps;
	}
	
	/**
	 * Returns the number of burned calories presented in this {@link ActivityState}. 
	 * @return the number of burned calories presented in this {@link ActivityState}.
	 */
	public int getBurnedCalories() {
		return burnedCalories;
	}
	
	/**
	 * Sets the number of burned calories presented in this {@link ActivityState}.
	 * @param burnedCalories the number of burned calories presented in this {@link ActivityState}.
	 */
	public void setBurnedCalories(int burnedCalories) {
		this.burnedCalories = burnedCalories;
	}
	
	/**
	 * Returns the number of kilometers walked presented in this {@link ActivityState}.
	 * @return the number of kilometers walked presented in this {@link ActivityState}.
	 */
	public float getKilometers() {
		return kilometers;
	}
	
	/**
	 * Sets the number of kilometers walked presented in this {@link ActivityState}.
	 * @param kilometers the number of kilometers walked presented in this {@link ActivityState}.
	 */
	public void setKilometers(float kilometers) {
		this.kilometers = kilometers;
	}
}
