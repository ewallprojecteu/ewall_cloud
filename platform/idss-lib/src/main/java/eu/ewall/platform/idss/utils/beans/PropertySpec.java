package eu.ewall.platform.idss.utils.beans;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * The specification of a property in a JavaBeans-like object. A property may
 * be accessed by a public field or getter and setter methods. An instance
 * of this class can be obtained from the {@link PropertyScanner
 * PropertyScanner}.
 * 
 * @author Dennis Hofs (RRD)
 */
public class PropertySpec {
	private String name = null;
	private Field field = null;
	private boolean isPublic = false;
	private Method getMethod = null;
	private Method setMethod = null;

	/**
	 * Returns the field name.
	 * 
	 * @return the field name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the field name.
	 * 
	 * @param name the field name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the field. Note that it may not be public. See {@link
	 * #isPublic() isPublic()}.
	 * 
	 * @return the field
	 */
	public Field getField() {
		return field;
	}

	/**
	 * Sets the field.
	 * 
	 * @param field the field
	 */
	public void setField(Field field) {
		this.field = field;
	}

	/**
	 * Returns whether the field is public. If not, then use {@link
	 * #getGetMethod() getGetMethod()} and {@link #getSetMethod()
	 * getSetMethod()}.
	 * 
	 * @return true if the field is public
	 */
	public boolean isPublic() {
		return isPublic;
	}

	/**
	 * Sets whether the field is public. If not, you should set the get method
	 * and set method.
	 * 
	 * @param isPublic true if the field is public
	 */
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	/**
	 * Returns the get method if the field is not public.
	 * 
	 * @return the get method
	 */
	public Method getGetMethod() {
		return getMethod;
	}

	/**
	 * Sets the get method. This should be done if the field is not public.
	 * 
	 * @param getMethod the get method
	 */
	public void setGetMethod(Method getMethod) {
		this.getMethod = getMethod;
	}

	/**
	 * Returns the set method if the field is not public.
	 * 
	 * @return the set method
	 */
	public Method getSetMethod() {
		return setMethod;
	}

	/**
	 * Sets the set method. This should be done if the field is not public.
	 * 
	 * @param setMethod the set method
	 */
	public void setSetMethod(Method setMethod) {
		this.setMethod = setMethod;
	}
	
	@Override
	public String toString() {
		return "PropertySpec [name=" + name + "]";
	}
}
