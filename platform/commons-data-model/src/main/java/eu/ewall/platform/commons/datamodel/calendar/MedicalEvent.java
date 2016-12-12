package eu.ewall.platform.commons.datamodel.calendar;

/**
 * The Class MedicalEvent.
 */
public class MedicalEvent extends Event {

	/** The doctor name. */
	public String doctorName;

	/** The doctor phone. */
	public String doctorPhone;

	/** The location. */
	public String location;

	/** The med exam. */
	public Boolean medExam;

	/** The ins card. */
	public Boolean insCard;

	/** The other docs. */
	public String otherDocs;

	/** The transportation. */
	public String transportation;

	/**
	 * Gets the doctor name.
	 *
	 * @return the doctor name
	 */
	public String getDoctorName() {
		return doctorName;
	}

	/**
	 * Sets the doctor name.
	 *
	 * @param doctorName
	 *            the new doctor name
	 */
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	/**
	 * Gets the doctor phone.
	 *
	 * @return the doctor phone
	 */
	public String getDoctorPhone() {
		return doctorPhone;
	}

	/**
	 * Sets the doctor phone.
	 *
	 * @param doctorPhone
	 *            the new doctor phone
	 */
	public void setDoctorPhone(String doctorPhone) {
		this.doctorPhone = doctorPhone;
	}

	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 *
	 * @param location
	 *            the new location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Gets the med exam.
	 *
	 * @return the med exam
	 */
	public Boolean getMedExam() {
		return medExam;
	}

	/**
	 * Sets the med exam.
	 *
	 * @param medExam
	 *            the new med exam
	 */
	public void setMedExam(Boolean medExam) {
		this.medExam = medExam;
	}

	/**
	 * Gets the ins card.
	 *
	 * @return the ins card
	 */
	public Boolean getInsCard() {
		return insCard;
	}

	/**
	 * Sets the ins card.
	 *
	 * @param insCard
	 *            the new ins card
	 */
	public void setInsCard(Boolean insCard) {
		this.insCard = insCard;
	}

	/**
	 * Gets the other docs.
	 *
	 * @return the other docs
	 */
	public String getOtherDocs() {
		return otherDocs;
	}

	/**
	 * Sets the other docs.
	 *
	 * @param otherDocs
	 *            the new other docs
	 */
	public void setOtherDocs(String otherDocs) {
		this.otherDocs = otherDocs;
	}

	/**
	 * Gets the transportation.
	 *
	 * @return the transportation
	 */
	public String getTransportation() {
		return transportation;
	}

	/**
	 * Sets the transportation.
	 *
	 * @param transportation
	 *            the new transportation
	 */
	public void setTransportation(String transportation) {
		this.transportation = transportation;
	}

}
