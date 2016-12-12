package eu.ewall.platform.idss.service.model.state;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseFieldScanner;
import eu.ewall.platform.idss.dao.DatabaseFieldSpec;
import eu.ewall.platform.idss.dao.DatabaseObjectMapper;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.utils.beans.PropertySpec;
import eu.ewall.platform.idss.utils.datetime.VirtualClock;
import eu.ewall.platform.idss.utils.json.DateTimeFromIsoDateTimeDeserializer;
import eu.ewall.platform.idss.utils.json.IsoDateTimeSerializer;

/**
 * A state model consists of data attributes. Each attribute can be annotated
 * with a reliablility and the time when its value was last updated. A subclass
 * defines the attributes as member variables annotated with {@link
 * DatabaseField DatabaseField}, so the model can be serialised to a {@link
 * Database Database}. Normally these variables are private and the subclass
 * provides get and set methods. When you set an attribute, you should usually
 * annotate it with the source reliability and the update time. You can call
 * the attribute's set method followed by {@link
 * #setAttributeSourceReliability(String, Double)
 * setAttributeSourceReliability()} and {@link
 * #setAttributeUpdated(String, DateTime) setAttributeUpdated()}, or you can
 * call the shortcut method {@link
 * #setAttribute(String, Object, Double, DateTime) setAttribute()}.
 * 
 * <p>The source reliability defines how reliable the source is (sensor
 * quality, weather forecast). The value reliability of an attribute depends on
 * this source reliability and on its update time. The value of an attribute
 * that was updated longer ago is less reliable, but this can vary per
 * attribute as some attributes change much faster in time than others. The
 * method {@link #getReliabilityDecayTime(String) getReliabilityDecayTime()}
 * returns how long it takes before an attribute becomes completely
 * unreliable. The default implementation always returns {@link
 * #DEFAULT_RELIABILITY_DECAY_TIME DEFAULT_RELIABILITY_DECAY_TIME}, but
 * subclasses can override this method. The method {@link
 * #getValueReliability(String) getValueReliability()} uses all this
 * information to calculate the value reliability of an attribute.</p>
 * 
 * @author Dennis Hofs (RRD)
 */
public abstract class StateModel extends AbstractDatabaseObject {
	public static final int DEFAULT_RELIABILITY_DECAY_TIME = 7200000; // 2 hours
	
	@JsonIgnore
	private String id;
	
	@DatabaseField(value= DatabaseType.STRING)
	private String user;

	@DatabaseField(value=DatabaseType.ISOTIME)
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime created;

	@DatabaseField(value=DatabaseType.MAP, elemType=DatabaseType.FLOAT)
	private Map<String,Double> attributesSourceReliability =
			new LinkedHashMap<String,Double>();

	@DatabaseField(value=DatabaseType.MAP, elemType=DatabaseType.ISOTIME)
	@JsonSerialize(contentUsing=IsoDateTimeSerializer.class)
	@JsonDeserialize(contentUsing=DateTimeFromIsoDateTimeDeserializer.class)
	private Map<String,DateTime> attributesUpdated =
			new LinkedHashMap<String,DateTime>();
	
	@DatabaseField(value=DatabaseType.MAP, elemType=DatabaseType.ISOTIME)
	@JsonSerialize(contentUsing=IsoDateTimeSerializer.class)
	@JsonDeserialize(contentUsing=DateTimeFromIsoDateTimeDeserializer.class)
	private Map<String,DateTime> attributesRetrieved =
			new LinkedHashMap<String,DateTime>();
	
	// initialised at construction
	@JsonIgnore
	private List<DatabaseFieldSpec> attributes;
	@JsonIgnore
	private List<String> attributeNames;
	
	/**
	 * Constructs a new state model.
	 */
	public StateModel() {
		attributes = scanAttributes();
		attributeNames = new ArrayList<String>();
		for (DatabaseFieldSpec attrSpec : attributes) {
			attributeNames.add(attrSpec.getPropSpec().getName());
		}
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the names of all data attributes.
	 * 
	 * @return the attribute names
	 */
	public List<String> getAttributes() {
		return attributeNames;
	}
	
	/**
	 * Sets a data attribute's value, source reliability and update time.
	 * The value must be assignable to the attribute's type or it must be
	 * possible to convert it to that type from a JSON type.
	 * 
	 * @param attribute the attribute name
	 * @param value the attribute value
	 * @param sourceReliability the source reliability (0 is very unreliable,
	 * 1 is certain) or null
	 * @param updated the time when the value was last obtained or null
	 * @throws RuntimeException if the attribute does not exist or can't be
	 * written
	 */
	public void setAttribute(String attribute, Object value,
			Double sourceReliability, DateTime updated) {
		setAttributeValue(attribute, value);
		attributesSourceReliability.put(attribute, sourceReliability);
		attributesUpdated.put(attribute, updated);
	}
	
	/**
	 * Returns the value of the specified data attribute.
	 * 
	 * @param attribute the attribute name
	 * @return the attribute value
	 * @throws RuntimeException if the attribute does not exist or can't be
	 * read
	 */
	public Object getAttributeValue(String attribute) {
		DatabaseFieldSpec attrSpec = getAttribute(attribute);
		try {
			if (attrSpec.getPropSpec().isPublic())
				return attrSpec.getPropSpec().getField().get(this);
			else
				return attrSpec.getPropSpec().getGetMethod().invoke(this);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(String.format(
					"Can't access attribute \"%s\": %s",
					attribute, ex.getMessage()), ex);
		} catch (InvocationTargetException ex) {
			throw new RuntimeException(String.format(
					"Can't read attribute \"%s\": %s",
					attribute, ex.getMessage()), ex);
		}
	}
	
	/**
	 * Sets the value of the specified data attribute. The value must be
	 * assignable to the attribute's type or it must be possible to convert
	 * it to that type from a JSON type.
	 * 
	 * @param attribute the attribute name
	 * @param value the attribute value
	 * @throws RuntimeException if the attribute does not exist or can't be
	 * written
	 */
	public void setAttributeValue(String attribute, Object value) {
		DatabaseFieldSpec attrSpec = getAttribute(attribute);
		try {
			PropertySpec propSpec = attrSpec.getPropSpec();
			Class<?> propClass = propSpec.getField().getType();
			if (value != null && !propClass.isInstance(value)) {
				DatabaseObjectMapper mapper = new DatabaseObjectMapper();
				value = mapper.fromDatabaseValue(value, attrSpec.getDbField(),
						propSpec.getField());
			}
			if (propSpec.isPublic())
				propSpec.getField().set(this, value);
			else
				propSpec.getSetMethod().invoke(this, value);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(String.format(
					"Can't access attribute \"%s\": %s",
					attribute, ex.getMessage()), ex);
		} catch (InvocationTargetException ex) {
			throw new RuntimeException(String.format(
					"Can't write attribute \"%s\": %s",
					attribute, ex.getMessage()), ex);
		}
	}

	/**
	 * Returns the source reliability of the specified data attribute. If no
	 * source reliability was set, this method returns null.
	 * 
	 * @param attribute the attribute name
	 * @return the source reliability (0 is very unreliable, 1 is certain) or
	 * null
	 * @throws RuntimeException if the attribute does not exist
	 */
	public Double getAttributeSourceReliability(String attribute) {
		// get attribute to validate that it exists
		getAttribute(attribute);
		return attributesSourceReliability.get(attribute);
	}
	
	/**
	 * Sets the source reliability of the specified data attribute.
	 * 
	 * @param attribute the attribute name
	 * @param sourceReliability the source reliability (0 is very unreliable,
	 * 1 is certain) or null
	 * @throws RuntimeException if the attribute does not exist
	 */
	public void setAttributeSourceReliability(String attribute,
			Double sourceReliability) {
		// get attribute to validate that it exists
		getAttribute(attribute);
		attributesSourceReliability.put(attribute, sourceReliability);
	}
	
	/**
	 * Returns the time when the value of the specified data attribute was last
	 * obtained. If no update time was set, this method returns null.
	 * 
	 * @param attribute the attribute name
	 * @return the last update time or null
	 * @throws RuntimeException if the attribute does not exist
	 */
	public DateTime getAttributeUpdated(String attribute) {
		// get attribute to validate that it exists
		getAttribute(attribute);
		return attributesUpdated.get(attribute);
	}
	
	/**
	 * Sets the time when the value of the specified data attribute was last
	 * obtained.
	 * 
	 * @param attribute the attribute name
	 * @param updated the last update time or null
	 * @throws RuntimeException if the attribute does not exist
	 */
	public void setAttributeUpdated(String attribute, DateTime updated) {
		// get attribute to validate that it exists
		getAttribute(attribute);
		attributesUpdated.put(attribute, updated);
	}
	
	/**
	 * Returns the time when the value of the specified data attribute was last
	 * retrieved. If no update time was set, this method returns null.
	 * 
	 * @param attribute the attribute name
	 * @return the last retrieval time or null
	 * @throws RuntimeException if the attribute does not exist
	 */
	public DateTime getAttributeRetrieved(String attribute) {
		// get attribute to validate that it exists
		getAttribute(attribute);
		return attributesRetrieved.get(attribute);
	}
	
	public void setAttributeRetrieved(String attribute, DateTime retrieved) {
		// get attribute to validate that it exists
		getAttribute(attribute);
		attributesRetrieved.put(attribute, retrieved);
	}

	/**
	 * Returns the user name.
	 * 
	 * @return the user name
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the user name.
	 * 
	 * @param user the user name
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Returns the time when this state was created.
	 * 
	 * @return the time when this state was created
	 */
	public DateTime getCreated() {
		return created;
	}

	/**
	 * Sets the time when this state was created.
	 * 
	 * @param created the time when this state was created
	 */
	public void setCreated(DateTime created) {
		this.created = created;
	}

	/**
	 * Returns the source reliabilities of the data attributes. This method is
	 * used for database serialization. Usually it's better to call {@link
	 * #getAttributeSourceReliability(String) getAttributeSourceReliability()}.
	 * 
	 * @return the source reliabilities of the data attributes
	 */
	public Map<String, Double> getAttributesSourceReliability() {
		return attributesSourceReliability;
	}

	/**
	 * Sets the source reliabilities of the data attributes. This method is
	 * used for database serialization. Usually it's better to call {@link
	 * #setAttributeSourceReliability(String, Double)
	 * setAttributeSourceReliability()} or {@link
	 * #setAttribute(String, Object, Double, DateTime) setAttribute()}.
	 * 
	 * @param attributesSourceReliability the source reliabilities of the data
	 * attributes
	 */
	public void setAttributesSourceReliability(
			Map<String, Double> attributesSourceReliability) {
		this.attributesSourceReliability = attributesSourceReliability;
	}

	/**
	 * Returns the times when the value of a data attribute was obtained. This
	 * method is used for database serialization. Usually it's better to call
	 * {@link #getAttributeUpdated(String) getAttributeUpdated()}.
	 * 
	 * @return the attribute update times
	 */
	public Map<String, DateTime> getAttributesUpdated() {
		return attributesUpdated;
	}

	/**
	 * Sets the times when the value of a data attribute was obtained. This
	 * method is used for database serialization. Usually it's better to call
	 * {@link #setAttributeUpdated(String, DateTime) setAttributeUpdated()} or
	 * {@link #setAttribute(String, Object, Double, DateTime) setAttribute()}.
	 * 
	 * @param attributesUpdated the attribute update times
	 */
	public void setAttributesUpdated(Map<String, DateTime> attributesUpdated) {
		this.attributesUpdated = attributesUpdated;
	}
	
	/**
	 * Returns the times when the value of a data attribute was last retrieved.
	 * @return the times when the value of a data attribute was last retrieved.
	 */
	public Map<String,DateTime> getAttributesRetrieved() {
		return attributesRetrieved;
	}
	
	/**
	 * Sets the times when the value of a data attribute was last retrieved.
	 * @param attributesRetrieved the times when the value of a data attribute was last retrieved.
	 */
	public void setAttributesRetrieved(Map<String,DateTime> attributesRetrieved) {
		this.attributesRetrieved = attributesRetrieved;
	}

	/**
	 * Returns the reliability decay time for the specified attribute. This is
	 * the time it takes before the value of this attribute becomes completely
	 * unreliable. The default implementation always returns {@link
	 * #DEFAULT_RELIABILITY_DECAY_TIME DEFAULT_RELIABILITY_DECAY_TIME}, but
	 * subclasses can override this method.
	 * 
	 * <p>The reliability decay time is like a static property of the attribute
	 * and does not depend on the current value of this particular model
	 * instance.</p>
	 * 
	 * @param attribute the attribute name
	 * @return the reliability decay time
	 */
	public int getReliabilityDecayTime(String attribute) {
		return DEFAULT_RELIABILITY_DECAY_TIME;
	}
	
	/**
	 * Returns the current value reliability of the specified attribute. The
	 * value reliability is calculated from the source reliability ({@link
	 * #getAttributeSourceReliability(String)
	 * getAttributeSourceReliability()}), the update time ({@link
	 * #getAttributeUpdated(String) getAttributeUpdated()}) and the reliability
	 * decay time ({@link #getReliabilityDecayTime(String)
	 * getReliabilityDecayTime()}).
	 * 
	 * <p>This method multiplies the source reliability and the decay
	 * reliability. Because both the source reliability and the decay time may
	 * be undefined, it takes 1 as a default. This should be acceptable if
	 * either reliability is always undefined for this attribute.</p>
	 * 
	 * @param attribute the attribute name
	 * @return the value reliability
	 */
	public double getValueReliability(String attribute) {
		Double sourceRel = getAttributeSourceReliability(attribute);
		if (sourceRel == null)
			sourceRel = 1.0;
		double decayRel = 1;
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime();
		DateTime updated = getAttributeUpdated(attribute);
		if (updated != null) {
			double elapsed = updated.getMillis() - now.getMillis();
			double decayTime = getReliabilityDecayTime(attribute);
			if (elapsed > decayTime)
				decayRel = 0;
			else
				decayRel = 1 - elapsed / decayTime;
		}
		return sourceRel * decayRel;
	}
	
	/**
	 * Returns the data attribute with the specified name. If the attribute
	 * does not exist, it throws an {@link IllegalArgumentException
	 * IllegalArgumentException}. See also {@link #getAttributes()
	 * getAttributes()}.
	 * 
	 * @param name the attribute name
	 * @return the attribute
	 * @throws IllegalArgumentException if the attribute does not exist
	 */
	private DatabaseFieldSpec getAttribute(String name) {
		List<DatabaseFieldSpec> atts = attributes;
		for (DatabaseFieldSpec att : atts) {
			if (att.getPropSpec().getName().equals(name))
				return att;
		}
		throw new IllegalArgumentException(String.format(
				"Attribute \"%s\" not found", name));
	}
	
	/**
	 * Scans for all data attributes. That is all fields annotated with {@link
	 * DatabaseField DatabaseField} except and the fields that are defined in
	 * this base class.
	 * 
	 * @return the data attributes
	 */
	private List<DatabaseFieldSpec> scanAttributes() {
		List<DatabaseFieldSpec> fields =
				DatabaseFieldScanner.getDatabaseFields(getClass());
		List<DatabaseFieldSpec> attributes =
				new ArrayList<DatabaseFieldSpec>();
		for (DatabaseFieldSpec field : fields) {
			if (field.getPropSpec().getName().equals("created") ||
					field.getPropSpec().getName().equals("user") ||
					field.getPropSpec().getName().equals("attributesSourceReliability") ||
					field.getPropSpec().getName().equals("attributesUpdated")) {
				continue;
			}
			attributes.add(field);
		}
		return attributes;
	}
	
	/**
	 * Checks whether the data attribute values of this model equal those of
	 * the specified model. It only looks at the attributes returned by {@link
	 * #getAttributes() getAttributes()} and it ignores metadata.
	 * 
	 * @param other the other model
	 * @return true if the attribute values are equal, false otherwise
	 */
	public boolean isEqualAttributeValues(StateModel other) {
		for (String attr : attributeNames) {
			Object thisVal = getAttributeValue(attr);
			Object otherVal = other.getAttributeValue(attr);
			if ((thisVal == null) != (otherVal == null))
				return false;
			if (thisVal != null && !thisVal.equals(otherVal))
				return false;
		}
		return true;
	}

	/**
	 * Copies the data attribute values and metadata from this model into the
	 * specified model.
	 * 
	 * @param other the model to which the attributes should be written
	 */
	public void copyAttributesInto(StateModel other) {
		for (String att : attributeNames) {
			other.setAttribute(att, getAttributeValue(att),
					getAttributeSourceReliability(att),
					getAttributeUpdated(att));
		}
	}
}
