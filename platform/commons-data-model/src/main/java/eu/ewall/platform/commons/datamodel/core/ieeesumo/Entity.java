package eu.ewall.platform.commons.datamodel.core.ieeesumo;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * The universal class of individuals. The root node from IEEE SUMO (Suggested
 * Upper Merged Ontology) ontology: <a href="http://www.ontologyportal.org/"
 * >http://www.ontologyportal.org/</a>
 * 
 * 
 * @author eandgrg
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Entity extends ResourceSupport implements Serializable {

	/**
	 * Default serial version uid, will be inherited in all (sub)classes. No
	 * static or transient fields will undergo default serialization
	 */
	private static final long serialVersionUID = -564097306059764369L;

	/** unique identifier within eWall system setup. */
	@Id
	@JsonIgnore
	private String objectId;

	/**
	 * The Constructor.
	 */
	public Entity() {
		this.objectId = UUID.randomUUID().toString();
	}
	
	/**
	 * The Constructor.
	 *
	 * @param uuid the uuid
	 */
	public Entity(UUID uuid) {
		if (uuid != null)
			this.objectId = uuid.toString();
	}

	/**
	 * Gets the object id.
	 *
	 * @return unique identifier
	 */
	public String getObjectId() {
		return objectId;
	}

	/**
	 * Sets the object id.
	 *
	 * @param objectId the objectId to set
	 */
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	
	
	
	
	}