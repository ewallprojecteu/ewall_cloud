package eu.ewall.platform.idss.utils;

import eu.ewall.platform.idss.utils.datetime.DateTimeUtils;

import eu.ewall.platform.idss.utils.exception.BuildException;
import eu.ewall.platform.idss.utils.exception.ParseException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.net.URL;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

/**
 * This class defines a set of application-wide components. They can be built
 * and configured from an XML file, similar to Java Beans. This class can also
 * create default instances on request.
 * 
 * <p>Components can be specified in an XML element "component". It should have
 * an attribute "class" with the qualified class name of the component. This
 * class will attempt to build the component with the default constructor of
 * that class. If there is no (accessible) default constructor, it will try
 * the static method getInstance().</p>
 * 
 * <p>The XML element "component" can have zero or more child elements
 * "attribute". The element should have two attributes: "name" and "value".
 * If the name is "myAttr", it will try to write the specified value to
 * member variable "myAttr". If the class does not have an accessible member
 * variable "myAttr", it will try the method setMyAttr().</p>
 * 
 * <p>The specified attribute value should be a string representation for the
 * type of attribute. This can be a primitive type, a date/time type (described
 * next) or a class that has a constructor that takes one string. You may
 * register additional types. This is described further below.</p>
 * 
 * <p>Date/time attributes can have one of the following types:</p>
 * 
 * <p><ul>
 * <li>long/Long (UNIX timestamp in milliseconds)</li>
 * <li>{@link Date Date}</li>
 * <li>{@link Instant Instant}</li>
 * <li>{@link Calendar Calendar}</li>
 * <li>{@link DateTime DateTime}</li>
 * <li>{@link LocalDate LocalDate}</li>
 * <li>{@link LocalTime LocalTime}</li>
 * <li>{@link LocalDateTime LocalDateTime}</li>
 * </ul></p>
 * 
 * <p>They are parsed using {@link DateTimeUtils#parseDateTime(String, Class)
 * parseDateTime()}</p>
 * 
 * <p>Components are built with one of the methods buildComponents(), which
 * take either an entire XML document (as a file, URL, input stream or parsed
 * document) or one XML element. If you pass an entire document, it will search
 * for "component" elements as direct children of the document's root element.
 * If you pass an XML element, it will search the direct children of that
 * element.</p>
 * 
 * <p>The method {@link #getComponent(Class) getComponent()} returns
 * components. If a component is requested that has not been built in
 * buildComponents(), it will try to create a default instance. It will try
 * the default constructor or static method getInstance(), without setting
 * any attributes.</p>
 *
 * <p><b>Register custom attribute types</b></p>
 *
 * <p>In addition to the default set of supported attribute types (primitive,
 * date/time, string constructor), you can register additional types. They
 * have higher priority than the default types, so you may even override those
 * types. To register a custom type, call {@link
 * #addAttributeMapping(AppComponentAttributeMapping)
 * addAttributeMapping()} before buildComponents(). If a field has a matching
 * class, the attribute mapping will be used to parse the string value of the
 * attribute from the XML element of the component.</p>
 * 
 * @author Dennis Hofs (RRD)
 */
public class AppComponents {
	private static AppComponents instance = null;
	private static final Object lock = new Object();

	/**
	 * Returns the singleton instance of this class.
	 * 
	 * @return the singleton instance of this class
	 */
	public static AppComponents getInstance() {
		synchronized (lock) {
			if (instance == null)
				instance = new AppComponents();
			return instance;
		}
	}

	/**
	 * This is a shortcut method to get a logger. It will try to get an app
	 * component of class {@link ILoggerFactory ILoggerFactory} from SLF4J.
	 * If it doesn't exist, it will get the default SLF4J factory (which is
	 * then added as an app component). Then it gets the logger with the
	 * specified name from that factory.
	 *
	 * @param name the logger name
	 * @return the logger
	 */
	public static Logger getLogger(String name) {
		ILoggerFactory factory = AppComponents.getInstance().getComponent(
				ILoggerFactory.class, LoggerFactory.getILoggerFactory());
		return factory.getLogger(name);
	}

	/**
	 * This is a shortcut method for getInstance().getComponent(). It tries to
	 * find a component of the specified class or a subclass. If no such
	 * component is found, it will try to create a default instance of the
	 * specified class. The new component will then be added to the collection
	 * so the same instance can be retrieved later.
	 *
	 * @param clazz the component class
	 * @param <T> the type of component
	 * @return the component
	 * @throws RuntimeException if a default instance cannot be created
	 */
	public static <T> T get(Class<T> clazz) throws RuntimeException {
		return getInstance().getComponent(clazz);
	}
	
	private Set<Object> components = new HashSet<Object>();
	private List<AppComponentAttributeMapping<?>> attrMappings =
			new ArrayList<AppComponentAttributeMapping<?>>();
	
	/**
	 * This private constructor is used in {@link #getInstance()
	 * getInstance()}.
	 */
	private AppComponents() {
	}

	/**
	 * Adds an attribute mapping. This registers a custom attribute type that
	 * can be used when the components are built from XML. If an attribute has
	 * a matching class, the mapping will be used to parse the string value from
	 * the XML element of a component.
	 *
	 * @param mapping the attribute mapping
	 * @param <T> the attribute class
	 */
	public <T> void addAttributeMapping(
			AppComponentAttributeMapping<T> mapping) {
		synchronized (lock) {
			attrMappings.add(mapping);
		}
	}
	
	/**
	 * Builds components from the specified XML configuration file. It searches
	 * for XML elements "component" as direct children of the root element in
	 * the XML document.
	 * 
	 * @param config the XML configuration file
	 * @throws IOException if an error occurs while reading the file
	 * @throws ParseException if the XML content is invalid
	 * @throws BuildException if an error occurs while building a component
	 */
	public void buildComponents(File config) throws IOException,
	ParseException, BuildException {
		buildComponents(config.toURI().toURL());
	}
	
	/**
	 * Builds components from the specified XML configuration file. It searches
	 * for XML elements "component" as direct children of the root element in
	 * the XML document.
	 * 
	 * @param config the URL of the XML configuration file
	 * @throws IOException if an error occurs while reading the file
	 * @throws ParseException if the XML content is invalid
	 * @throws BuildException if an error occurs while building a component
	 */
	public void buildComponents(URL config) throws IOException, ParseException,
	BuildException {
		InputStream in = config.openStream();
		try {
			buildComponents(in);
		} finally {
			in.close();
		}
	}
	
	/**
	 * Builds components from the specified XML configuration file. It searches
	 * for XML elements "component" as direct children of the root element in
	 * the XML document.
	 * 
	 * @param config the XML configuration file as an input stream
	 * @throws IOException if an error occurs while reading the file
	 * @throws ParseException if the XML content is invalid
	 * @throws BuildException if an error occurs while building a component
	 */
	public void buildComponents(InputStream config) throws IOException,
	ParseException, BuildException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
			throw new ParseException("Can't initialise XML parser: " +
					ex.getMessage());
		}
		Document doc;
		try {
			doc = builder.parse(config);
		} catch (SAXException ex) {
			throw new ParseException("Can't parse XML file: " +
					ex.getMessage(), ex);
		}
		buildComponents(doc);
	}
	
	/**
	 * Builds components from the specified XML configuration document. It
	 * searches for XML elements "component" as direct children of the root
	 * element in the document.
	 * 
	 * @param doc the XML configuration document
	 * @throws ParseException if the XML content is invalid
	 * @throws BuildException if an error occurs while building a component
	 */
	public void buildComponents(Document doc) throws ParseException,
	BuildException {
		buildComponents(doc.getDocumentElement());
	}
	
	/**
	 * Builds components from the specified XML element. It searches for XML
	 * elements "component" as direct children of the specified element.
	 * 
	 * @param elem the XML element
	 * @throws ParseException if the XML content is invalid
	 * @throws BuildException if an error occurs while building a component
	 */
	public void buildComponents(Element elem) throws ParseException,
	BuildException {
		NodeList children = elem.getElementsByTagName("component");
		for (int i = 0; i < children.getLength(); i++) {
			Element child = (Element)children.item(i);
			buildComponent(child);
		}
	}
	
	/**
	 * Builds a component from the specified "component" element.
	 * 
	 * @param elem the "component" element
	 * @throws ParseException if the XML content is invalid
	 * @throws BuildException if an error occurs while building a component
	 */
	private void buildComponent(Element elem) throws ParseException,
	BuildException {
		Attr attr = elem.getAttributeNode("class");
		if (attr == null) {
			throw new ParseException(
					"Element \"component\" does not have attribute \"class\"");
		}
		String className = attr.getValue();
		Class<?> compClass;
		try {
			compClass = Class.forName(className);
		} catch (ClassNotFoundException ex) {
			throw new BuildException("Class not found: " + className, ex);
		}
		NodeList children = elem.getElementsByTagName("attribute");
		Map<String,String> attributes = new HashMap<String,String>();
		for (int i = 0; i < children.getLength(); i++) {
			Element child = (Element)children.item(i);
			attr = child.getAttributeNode("name");
			if (attr == null) {
				throw new ParseException(
						"Element \"attribute\" does not have attribute \"name\"");
			}
			String attrName = attr.getValue();
			if (attrName.length() == 0)
				throw new ParseException("Attribute \"name\" is empty");
			attr = child.getAttributeNode("value");
			if (attr == null) {
				throw new ParseException(
						"Element \"attribute\" does not have attribute \"value\"");
			}
			String attrValue = attr.getValue();
			attributes.put(attrName, attrValue);
		}
		buildComponent(compClass, attributes);
	}
	
	/**
	 * Builds a component of the specified class, and then sets the specified
	 * attributes.
	 * 
	 * @param compClass the component class
	 * @param attrs the attributes
	 * @throws BuildException if an error occurs while building the component
	 */
	private void buildComponent(Class<?> compClass, Map<String,String> attrs)
	throws BuildException {
		Object component = getDefaultComponent(compClass);
		for (String attr : attrs.keySet()) {
			setComponentAttribute(component, attr, attrs.get(attr));
		}
		components.add(component);
	}
	
	/**
	 * Sets an attribute on the specified component.
	 * 
	 * @param component the component
	 * @param attr the attribute name
	 * @param value the string representation of the attribute value
	 * @throws BuildException if an error occurs while setting the attribute
	 */
	private void setComponentAttribute(Object component, String attr,
			String value) throws BuildException {
		Class<?> compClass = component.getClass();
		Field field = null;
		try {
			field = compClass.getField(attr);
		} catch (NoSuchFieldException ex) {
			// try set method
		}
		if (field != null) {
			setAttributeField(field, component, value);
			return;
		}
		String methodName = "set" + attr.substring(0, 1).toUpperCase() +
				attr.substring(1);
		Method method = null;
		Method[] methods = compClass.getMethods();
		for (Method m : methods) {
			if (!m.getName().equals(methodName))
				continue;
			if (m.getParameterTypes().length != 1)
				continue;
			method = m;
			break;
		}
		if (method == null) {
			throw new BuildException(String.format(
					"Class %s does not have field \"%s\" or method \"%s\"",
					compClass.getName(), attr, methodName));
		}
		setAttributeMethod(method, component, value);
	}

	/**
	 * Sets a value in an attribute field of a component. This is called from
	 * {@link #setComponentAttribute(Object, String, String)
	 * setComponentAttribute()} if the field is accessible.
	 *
	 * @param field the field
	 * @param component the component
	 * @param value the string representation of the attribute value
	 * @throws BuildException if the value can't be set
	 */
	private void setAttributeField(Field field, Object component, String value)
	throws BuildException {
		Class<?> type = field.getType();
		try {
			AppComponentAttributeMapping<?> mapping = null;
			for (AppComponentAttributeMapping<?> m : attrMappings) {
				if (type.isAssignableFrom(m.getClass())) {
					mapping = m;
					break;
				}
			}
			if (mapping != null) {
				field.set(component, mapping.parseValue(value));
			} else if (type == Boolean.TYPE) {
				field.setBoolean(component, Boolean.parseBoolean(value));
			} else if (type == Character.TYPE) {
				if (value.length() != 1) {
					throw new Exception("Invalid value for type char: \"" +
							value + "\"");
				}
				field.setChar(component, value.charAt(0));
			} else if (type == Byte.TYPE) {
				field.setByte(component, Byte.parseByte(value));
			} else if (type == Short.TYPE) {
				field.setShort(component, Short.parseShort(value));
			} else if (type == Integer.TYPE) {
				field.setInt(component, Integer.parseInt(value));
			} else if (type == Float.TYPE) {
				field.setFloat(component, Float.parseFloat(value));
			} else if (type == Double.TYPE) {
				field.setDouble(component, Double.parseDouble(value));
			} else if (type == Long.TYPE || type == Long.class) {
				Long longVal = null;
				try {
					longVal = Long.parseLong(value);
				} catch (NumberFormatException ex) {}
				try {
					if (longVal == null) {
						longVal = (Long)DateTimeUtils.parseDateTime(value,
								type);
					}
				} catch (ParseException ex) {}
				if (longVal == null) {
					throw new ParseException(
							"Value is not a long or a date/time: " +
									value);
				}
				field.set(component, longVal);
			} else if (type == Date.class || type == Instant.class ||
					type == Calendar.class || type == DateTime.class ||
					type == LocalDate.class || type == LocalTime.class ||
					type == LocalDateTime.class) {
				Object dateTime = DateTimeUtils.parseDateTime(value, type);
				field.set(component, dateTime);
			} else {
				Constructor<?> cstr = type.getConstructor(String.class);
				Object objVal = cstr.newInstance(value);
				field.set(component, objVal);
			}
		} catch (InvocationTargetException ex) {
			Throwable targetEx = ex.getTargetException();
			throw new BuildException("String constructor of class " +
					type.getName() + " throws exception: " +
					targetEx.getMessage(), targetEx);
		} catch (Exception ex) {
			throw new BuildException(String.format(
					"Can't set value \"%s\" to field \"%s\" with class %s",
					value, field.getName(), type.getName()) + ": " +
					ex.getMessage(), ex);
		}
	}

	/**
	 * Invokes a set method for a component attribute. This is called from
	 * {@link #setComponentAttribute(Object, String, String)
	 * setComponentAttribute()} if the field is not accessible directly.
	 *
	 * @param method the set method
	 * @param component the component
	 * @param value the string representation of the attribute value
	 * @throws BuildException if the value can't be set
	 */
	private void setAttributeMethod(Method method, Object component,
			String value) throws BuildException {
		Class<?> type = method.getParameterTypes()[0];
		try {
			AppComponentAttributeMapping<?> mapping = null;
			for (AppComponentAttributeMapping<?> m : attrMappings) {
				if (type.isAssignableFrom(m.getClass())) {
					mapping = m;
					break;
				}
			}
			if (mapping != null) {
				method.invoke(component, mapping.parseValue(value));
			} else if (type == Boolean.TYPE) {
				method.invoke(component, Boolean.parseBoolean(value));
			} else if (type == Character.TYPE) {
				if (value.length() != 1) {
					throw new Exception("Invalid value type char: \"" + value +
							"\"");
				}
				method.invoke(component, value.charAt(0));
			} else if (type == Byte.TYPE) {
				method.invoke(component, Byte.parseByte(value));
			} else if (type == Short.TYPE) {
				method.invoke(component, Short.parseShort(value));
			} else if (type == Integer.TYPE) {
				method.invoke(component, Integer.parseInt(value));
			} else if (type == Float.TYPE) {
				method.invoke(component, Float.parseFloat(value));
			} else if (type == Double.TYPE) {
				method.invoke(component, Double.parseDouble(value));
			} else if (type == Long.TYPE || type == Long.class) {
				Long longVal = null;
				try {
					longVal = Long.parseLong(value);
				} catch (NumberFormatException ex) {
				}
				try {
					if (longVal == null) {
						longVal = (Long) DateTimeUtils.parseDateTime(value,
								type);
					}
				} catch (ParseException ex) {
				}
				if (longVal == null) {
					throw new ParseException(
							"Value is not a long or a date/time: " +
									value);
				}
				method.invoke(component, longVal);
			} else if (type == Date.class || type == Instant.class ||
					type == Calendar.class || type == DateTime.class ||
					type == LocalDate.class || type == LocalTime.class ||
					type == LocalDateTime.class) {
				Object dateTime = DateTimeUtils.parseDateTime(value, type);
				method.invoke(component, dateTime);
			} else {
				Constructor<?> cstr = type.getConstructor(String.class);
				Object objVal;
				try {
					objVal = cstr.newInstance(value);
				} catch (InvocationTargetException ex) {
					Throwable targetEx = ex.getTargetException();
					throw new Exception("String constructor of class " +
							type.getName() + " throws exception: " +
							targetEx.getMessage(), targetEx);
				}
				method.invoke(component, objVal);
			}
		} catch (InvocationTargetException ex) {
			Throwable targetEx = ex.getTargetException();
			throw new BuildException("Method " + method.getName() +
					" of class " + component.getClass().getName() +
					" throws exception: " + targetEx.getMessage(), targetEx);
		} catch (Exception ex) {
			throw new BuildException(String.format(
					"Can't set value \"%s\" with method %s of class %s",
					value, method.getName(), component.getClass().getName()) +
					": " + ex.getMessage(), ex);
		}
	}
	
	/**
	 * Adds the specified component.
	 * 
	 * @param component the component
	 */
	public void addComponent(Object component) {
		synchronized (lock) {
			components.add(component);
		}
	}
	
	/**
	 * Returns whether there is a component of the specified class or a
	 * subclass.
	 * 
	 * @param clazz the component class
	 * @return true if there is a component, false otherwise
	 */
	public boolean hasComponent(Class<?> clazz) {
		return findComponent(clazz) != null;
	}
	
	/**
	 * Tries to find a component of the specified class or a subclass. If no
	 * such component is found, this method returns null.
	 * 
	 * @param clazz the component class
	 * @param <T> the type of component
	 * @return the component or null
	 */
	public <T> T findComponent(Class<T> clazz) {
		synchronized (lock) {
			for (Object comp : components) {
				Class<?> compClass = comp.getClass();
				if (clazz.isAssignableFrom(compClass))
					return clazz.cast(comp);
			}
			return null;
		}
	}
	
	/**
	 * Tries to find a component of the specified class or a subclass. If no
	 * such component is found, it will try to create a default instance of
	 * the specified class. The new component will then be added to the
	 * collection so the same instance can be retrieved later.
	 * 
	 * @param clazz the component class
	 * @param <T> the type of component
	 * @return the component
	 * @throws RuntimeException if a default instance cannot be created
	 */
	public <T> T getComponent(Class<T> clazz) throws RuntimeException {
		return getComponent(clazz, null);
	}

	/**
	 * Tries to find a component of the specified class or a subclass. If no
	 * such component is found, it will try to create a default instance of
	 * the specified class. The new component will be added to the collection so
	 * the same instance can be retrieved later.
	 *
	 * <p>If creating a default instance fails and a default value is specified,
	 * it will return that default value and also add it to the collection. If
	 * no default value is specified, it throws a runtime exception.</p>
	 *
	 * @param clazz the component class
	 * @param defaultVal the default value to return if there is no component of
	 * the specified class and no default instance can be created. Set this to
	 * null if you want a runtime exception in that case.
	 * @param <T> the type of component
	 * @return the component
	 * @throws RuntimeException if a default instance cannot be created and
	 * no default value is specified
	 */
	public <T> T getComponent(Class<T> clazz, T defaultVal) {
		synchronized (lock) {
			T comp = findComponent(clazz);
			if (comp != null)
				return comp;
			try {
				comp = getDefaultComponent(clazz);
				components.add(comp);
				return comp;
			} catch (BuildException ex) {
				if (defaultVal != null) {
					components.add(defaultVal);
					return defaultVal;
				} else {
					throw new RuntimeException(
							"Can't create default component of class " +
							clazz.getName() + ": " + ex.getMessage(), ex);
				}
			}
		}
	}
	
	/**
	 * Creates a default instance of a component of the specified class. It
	 * will try the default constructor or static method getInstance().
	 * 
	 * @param clazz the component class
	 * @return the default component
	 * @throws BuildException if the default component cannot be created
	 */
	private <T> T getDefaultComponent(Class<T> clazz) throws BuildException {
		boolean isAbstract = (clazz.getModifiers() & Modifier.ABSTRACT) != 0;
		Constructor<T> cstr = null;
		if (!isAbstract) {
			try {
				cstr = clazz.getConstructor();
			} catch (NoSuchMethodException ex) {
				// try getInstance()
			}
		}
		if (cstr != null) {
			try {
				return cstr.newInstance();
			} catch (InvocationTargetException ex) {
				Throwable targetEx = ex.getTargetException();
				throw new BuildException("Default constructor of class " +
						clazz.getName() + " throws exception: " +
						targetEx.getMessage(), targetEx);
			} catch (Exception ex) {
				throw new BuildException("Can't instantiate class " +
						clazz.getName() + " with default constructor: " +
						ex.getMessage(), ex);
			}
		}
		// class is abstract or default constructor is not available
		Method method;
		try {
			method = clazz.getMethod("getInstance");
		} catch (NoSuchMethodException ex) {
			throw new BuildException("Class " + clazz.getName() +
					" cannot be instantiated and does not have static method getInstance(): " +
					ex.getMessage(), ex);
		}
		boolean isStatic = (method.getModifiers() & Modifier.STATIC) != 0;
		if (!isStatic) {
			throw new BuildException("Class " + clazz.getName() +
					" cannot be instantiated and does not have static method getInstance()");
		}
		Object result;
		try { 
			result = method.invoke(null);
		} catch (InvocationTargetException ex) {
			Throwable targetEx = ex.getTargetException();
			throw new BuildException("Method getInstance() of class " +
					clazz.getName() + " throws exception: " +
					targetEx.getMessage(), targetEx);
		} catch (Exception ex) {
			throw new BuildException(
					"Can't invoke method getInstance() of class " +
					clazz.getName() + ": " + ex.getMessage(), ex);
		}
		if (result == null) {
			throw new BuildException("Method getInstance() of class " +
					clazz.getName() + " returns null");
		}
		try {
			return clazz.cast(result);
		} catch (ClassCastException ex) {
			throw new BuildException("Class " + result.getClass().getName() +
					" cannot be cast to " + clazz.getName());
		}
	}
}
