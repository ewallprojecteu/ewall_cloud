/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.ewallsystem;

import java.io.Serializable;

/**
 * The Class ActuatorCommand.
 */
public class ActuatorCommand implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3802038791974183306L;

	/** The room name. */
	private String roomName;

	/** The actuator name. */
	private String actuatorName;

	/** The command level. */
	private int commandValue;

	/** The actuator command type. */
	private ActuatorCommandType commandType;

	/**
	 * Instantiates a new actuator command.
	 */
	public ActuatorCommand() {
	}

	/**
	 * Instantiates a new actuator command.
	 *
	 * @param roomName
	 *            the room name
	 * @param actuatorName
	 *            the actuator name
	 * @param commandValue
	 *            the command value
	 * @param commandType
	 *            the command type
	 */
	public ActuatorCommand(String roomName, String actuatorName,
			int commandValue, ActuatorCommandType commandType) {
		this.roomName = roomName;
		this.actuatorName = actuatorName;
		this.commandValue = commandValue;
		this.commandType = commandType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (this.getClass() != obj.getClass())
		    return false;
		
		ActuatorCommand other = (ActuatorCommand) obj;
		return this.actuatorName.equals(other.actuatorName)
				&& this.roomName.equals(other.roomName)
				&& this.commandValue == other.commandValue
				&& this.commandType == other.commandType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return actuatorName.length() * 7 + commandValue * 11;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String
				.format("Actuator name: %s | room name: %s | command value: %s | command type: %s",
						actuatorName, roomName, commandValue, commandType);
	}

	/**
	 * Gets the room name.
	 *
	 * @return the room name
	 */
	public String getRoomName() {
		return roomName;
	}

	/**
	 * Sets the room name.
	 *
	 * @param roomName
	 *            the new room name
	 */
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	/**
	 * Gets the actuator name.
	 *
	 * @return the actuator name
	 */
	public String getActuatorName() {
		return actuatorName;
	}

	/**
	 * Sets the actuator name.
	 *
	 * @param actuatorName
	 *            the new actuator name
	 */
	public void setActuatorName(String actuatorName) {
		this.actuatorName = actuatorName;
	}

	/**
	 * Gets the command value.
	 *
	 * @return the commandValue
	 */
	public int getCommandValue() {
		return commandValue;
	}

	/**
	 * Sets the command value.
	 *
	 * @param commandValue
	 *            the commandValue to set
	 */
	public void setCommandValue(int commandValue) {
		this.commandValue = commandValue;
	}

	/**
	 * Gets the command type.
	 *
	 * @return the commandType
	 */
	public ActuatorCommandType getCommandType() {
		return commandType;
	}

	/**
	 * Sets the command type.
	 *
	 * @param commandType
	 *            the commandType to set
	 */
	public void setCommandType(ActuatorCommandType commandType) {
		this.commandType = commandType;
	}

}
