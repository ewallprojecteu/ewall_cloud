package eu.ewall.platform.commons.datamodel.calendar;

/**
 * The Class ExerciseEvent.
 */
public class ExerciseEvent extends Event {

	/** The type. */
	public ExerciseType type;

	/** The location. */
	public EventLocation location;

	/** The trainer. */
	public TrainerType trainer;

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public ExerciseType getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type
	 *            the new type
	 */
	public void setType(ExerciseType type) {
		this.type = type;
	}

	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public EventLocation getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 *
	 * @param location
	 *            the new location
	 */
	public void setLocation(EventLocation location) {
		this.location = location;
	}

	/**
	 * Gets the trainer.
	 *
	 * @return the trainer
	 */
	public TrainerType getTrainer() {
		return trainer;
	}

	/**
	 * Sets the trainer.
	 *
	 * @param trainer
	 *            the new trainer
	 */
	public void setTrainer(TrainerType trainer) {
		this.trainer = trainer;
	}

}
