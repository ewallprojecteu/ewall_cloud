package eu.ewall.platform.idss.automaticgoalsetting.service;

import eu.ewall.platform.idss.utils.exception.ParseException;

import eu.ewall.platform.idss.utils.xml.DoubleValueParser;
import eu.ewall.platform.idss.utils.xml.IntValueParser;
import eu.ewall.platform.idss.utils.xml.XMLDocumentParser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.xml.sax.SAXException;

/**
 * Configuration for the {@link IDSSAutomaticGoalSettingService
 * AutomaticGoalSettingService}.
 * 
 * @author Dennis Hofs (RRD)
 */
public class Configuration {
	private double incrementFactor = 0.2;
	private int roundingFactor = 100;
	
	/**
	 * Constructs a new configuration with default values.
	 */
	public Configuration() {
	}

	/**
	 * Returns the factor by which the average activity is increased to
	 * calculate the goal. This is the x in goalCategory = averageCategory +
	 * x * averageCategory. It should be between 0 and 1. The default is 0.2.
	 * 
	 * @return the increment factor
	 */
	public double getIncrementFactor() {
		return incrementFactor;
	}

	/**
	 * Sets the factor by which the average activity is increased to calculate
	 * the goal. This is the x in goalCategory = averageCategory +
	 * x * averageCategory. It should be between 0 and 1. The default is 0.2.
	 * 
	 * @param incrementFactor the increment factor
	 */
	public void setIncrementFactor(int incrementFactor) {
		this.incrementFactor = incrementFactor;
	}
	
	/**
	 * Returns the rounding factor for the goal. The service will round a
	 * calculated goal to the nearest number so that goal % roundingFactor = 0.
	 * The default is 100.
	 * 
	 * @return the rounding factor
	 */
	public int getRoundingFactor() {
		return roundingFactor;
	}

	/**
	 * Sets the rounding factor for the goal. The service will round a
	 * calculated goal to the nearest number so that goal % roundingFactor = 0.
	 * The default is 100.
	 * 
	 * @param roundingFactor the rounding factor
	 */
	public void setRoundingFactor(int roundingFactor) {
		this.roundingFactor = roundingFactor;
	}

	/**
	 * Parses the specified configuration file.
	 * 
	 * @param configFile the configuration file
	 * @return the configuration
	 * @throws IOException if a reading error occurs
	 * @throws ParseException if the XML content is invalid
	 */
	public static Configuration parse(File configFile) throws IOException,
	ParseException {
		return parse(configFile.toURI().toURL());
	}
	
	/**
	 * Parses the specified configuration file.
	 * 
	 * @param configFile the configuration file
	 * @return the configuration
	 * @throws IOException if a reading error occurs
	 * @throws ParseException if the XML content is invalid
	 */
	public static Configuration parse(URL configFile) throws IOException,
	ParseException {
		InputStream in = configFile.openStream();
		try {
			return parse(in);
		} finally {
			in.close();
		}
	}
	
	/**
	 * Parses the specified configuration file.
	 * 
	 * @param configFile the input steam from the configuration file
	 * @return the configuration
	 * @throws IOException if a reading error occurs
	 * @throws ParseException if the XML content is invalid
	 */
	public static Configuration parse(InputStream configFile) throws
	IOException, ParseException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(configFile);
			return parse(doc);
		} catch (ParserConfigurationException ex) {
			throw new ParseException("Can't create document builder: " +
					ex.getMessage(), ex);
		} catch (SAXException ex) {
			throw new ParseException("Can't parse config file: " +
					ex.getMessage(), ex);
		}
	}
	
	/**
	 * Parses the specified configuration document.
	 * 
	 * @param doc the configuration document
	 * @return the configuration
	 * @throws ParseException if the XML content is invalid
	 */
	public static Configuration parse(Document doc) throws ParseException {
		return parse(doc.getDocumentElement());
	}
	
	/**
	 * Parses the configuration from the specified element. This should be the
	 * root element of the configuration document or any element with the same
	 * format.
	 * 
	 * @param elem the element
	 * @return the configuration
	 * @throws ParseException if the XML content is invalid
	 */
	public static Configuration parse(Element elem) throws ParseException {
		Configuration config = new Configuration();
		XMLDocumentParser parser = new XMLDocumentParser();
		Double increment = parser.parseElemValue(elem, "incrementFactor",
				new DoubleValueParser(0.0, 1.0), null);
		if (increment != null)
			config.incrementFactor = increment;
		Integer rounding = parser.parseElemValue(elem, "roundingFactor",
				new IntValueParser(1, null), null);
		if (rounding != null)
			config.roundingFactor = rounding;
		return config;
	}
}
