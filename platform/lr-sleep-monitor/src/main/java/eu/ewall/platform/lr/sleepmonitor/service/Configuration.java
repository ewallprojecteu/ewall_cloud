package eu.ewall.platform.lr.sleepmonitor.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import eu.ewall.platform.idss.utils.exception.ParseException;
import eu.ewall.platform.idss.utils.xml.BooleanValueParser;
import eu.ewall.platform.idss.utils.xml.SQLTimeValueParser;
import eu.ewall.platform.idss.utils.xml.XMLDocumentParser;

/**
 * Configuration for the {@link LRSleepMonitorService LRSleepMonitorService}.
 * 
 * @author Dennis Hofs (RRD)
 */
public class Configuration {
	private static final String DEFAULT_END_OF_NIGHT_TIME = "13:00:00";
	
	private boolean simulationMode = false;
	private LocalTime endOfNightTime;
	
	/**
	 * Constructs a new configuration with default values.
	 */
	public Configuration() {
		DateTimeFormatter formatter = DateTimeFormat.forPattern(
				"HH:mm:ss");
		endOfNightTime = formatter.parseLocalTime(DEFAULT_END_OF_NIGHT_TIME);
	}

	/**
	 * Returns the time when the night should be ended. The reasoner will
	 * process data from the past night after that time. The default is 13:00.
	 * 
	 * @return the end of night time
	 */
	public LocalTime getEndOfNightTime() {
		return endOfNightTime;
	}

	/**
	 * Sets the time when the night should be ended. The reasoner will process
	 * data from the past night after that time. The default is 13:00.
	 * 
	 * @param endOfNightTime the end of night time
	 */
	public void setEndOfNightTime(LocalTime endOfNightTime) {
		this.endOfNightTime = endOfNightTime;
	}

	/**
	 * Returns whether the reasoner should run in simulation mode. If true, it
	 * will not get sleep data from a service brick, but generate simulated
	 * data itself. The default is false.
	 * 
	 * @return true if the reasoner should run in simulation mode, false
	 * otherwise
	 */
	public boolean isSimulationMode() {
		return simulationMode;
	}

	/**
	 * Sets whether the reasoner should run in simulation mode. If true, it
	 * will not get sleep data from a service brick, but generate simulated
	 * data itself. The default is false.
	 * 
	 * @param simulationMode true if the reasoner should run in simulation
	 * mode, false otherwise
	 */
	public void setSimulationMode(boolean simulationMode) {
		this.simulationMode = simulationMode;
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
		LocalTime endOfNightTime = parser.parseElemValue(elem,
				"endOfNightTime", new SQLTimeValueParser(), null);
		if (endOfNightTime != null)
			config.endOfNightTime = endOfNightTime;
		Boolean simulationMode = parser.parseElemValue(elem, "simulationMode",
				new BooleanValueParser(), null);
		if (simulationMode != null)
			config.simulationMode = simulationMode;
		return config;
	}
}
