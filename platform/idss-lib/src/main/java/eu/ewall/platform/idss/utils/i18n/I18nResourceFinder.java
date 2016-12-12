package eu.ewall.platform.idss.utils.i18n;

import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This class can find i18n resources. After construction you can set
 * properties with the set methods. Then call {@link #find() find()}. If it
 * returns true, you can get information about the found resource with the
 * get methods.
 * 
 * <p><b>Example</b></p>
 * 
 * <p>Find properties:</p>
 * 
 * <p><ul>
 * <li>baseName: "messages"</li>
 * <li>userLocales: ["nl_NL"]</li>
 * <li>honorifics: true</li>
 * <li>extension: "properties"</li>
 * </ul></p>
 * 
 * <p>It will try the following resources, in this order:</p>
 * 
 * <p><ul>
 * <li>messages_nl_NL_v.properties (if the locale contains a country; v = vos,
 * t = tu)</li>
 * <li>messages_nl_NL.properties (if the locale contains a country)</li>
 * <li>messages_nl_v.properties</li>
 * <li>messages_nl.properties</li>
 * <li>messages_v.properties</li>
 * <li>messages.properties</li>
 * </ul></p>
 * 
 * <p>If nothing is found, it will try the following locales: en_GB, en_US,
 * en.</p>
 * 
 * @author Dennis Hofs (RRD)
 */
public class I18nResourceFinder {
	private String baseName;
	private List<Locale> userLocales = new ArrayList<Locale>();
	private boolean honorifics = true;
	private String extension = "properties";
	private Class<?> loadClass = null;
	
	private Locale locale = null;
	private String name = null;
	private URL url = null;
	
	/**
	 * Constructs a new i18n resource finder.
	 * 
	 * @param baseName the base name of the resource file to find
	 */
	public I18nResourceFinder(String baseName) {
		this.baseName = baseName;
		this.userLocales.add(Locale.getDefault());
	}
	
	/**
	 * Sets the preferred locale of the user. If no resource is found for this
	 * locale, the resource finder will try en_GB, en_US or en. The default is
	 * the locale of the system.
	 * 
	 * @param userLocale the preferred locale of the user
	 * @see #setUserLocales(List)
	 */
	public void setUserLocale(Locale userLocale) {
		List<Locale> locales = new ArrayList<Locale>();
		locales.add(userLocale);
		setUserLocales(locales);
	}
	
	/**
	 * Sets the preferred locales of the user. If no resource is found for
	 * these locales, the resource finder will try en_GB, en_US or en. The
	 * default is the locale of the system.
	 * 
	 * @param userLocales the preferred locales of the user (at least one)
	 */
	public void setUserLocales(List<Locale> userLocales) {
		this.userLocales = userLocales;
	}

	/**
	 * Sets whether the resource should use honorifics. This is true for vos
	 * (u, vous, Sie) in tu-vos distinction, and false for tu (jij, tu, du).
	 * For languages without honorifics, such as English, there will be no
	 * resources with tu-vos designation, so the value of this property is not
	 * relevant. The default is true.
	 * 
	 * @param honorifics true if the resource should use honorifics, false
	 * otherwise
	 */
	public void setHonorifics(boolean honorifics) {
		this.honorifics = honorifics;
	}

	/**
	 * Sets the extension of the resource file to find. The default is
	 * "properties".
	 * 
	 * @param extension the extension of the resource file to find
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * Sets a class that should be used to find the resource. This means that
	 * the resource should be in the same package as the class. If null, it
	 * will try to find the resource in the root. The default is null.
	 * 
	 * @param loadClass the resource loading class
	 */
	public void setLoadClass(Class<?> loadClass) {
		this.loadClass = loadClass;
	}

	/**
	 * Tries to find a resource matching the specified properties. If a
	 * resource is found, this method will return true and you can get details
	 * with the get methods.
	 * 
	 * @return true if a resource was found, false otherwise
	 */
	public boolean find() {
		locale = null;
		name = null;
		url = null;
		List<Locale> prefLocales = new ArrayList<Locale>(userLocales);
		prefLocales.add(Locale.UK);
		prefLocales.add(Locale.US);
		prefLocales.add(Locale.ENGLISH);
		for (Locale locale : prefLocales) {
			if (findResource(locale))
				return true;
		}
		return false;
	}
	
	/**
	 * You can call this method after {@link #find() find()} returned true. It
	 * returns the locale of the found resource. This may be different than the
	 * preferred locale, because the resource finder also tries en_GB, en_US
	 * and en.
	 * 
	 * @return the locale of the found resource
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * You can call this method after {@link #find() find()} returned true. It
	 * returns the name of the found resource.
	 * 
	 * @return the name of the found resource
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * You can call this method after {@link #find() find()} returned true. It
	 * returns the URL to the found resource.
	 * 
	 * @return the URL to the found resource
	 */
	public URL getUrl() {
		return url;
	}
	
	/**
	 * Tries to find a resource matching the specified locale (and only that
	 * locale). If a resource is found, it will set the member variables
	 * "locale", "name" and "url" and it returns true.
	 * 
	 * @param locale the locale
	 * @return true if a matching resource was found, false otherwise
	 */
	private boolean findResource(Locale locale) {
		List<String> prefResources = new ArrayList<String>();
		if (locale.getCountry().length() > 0) {
			prefResources.add(String.format("%s_%s_%s_%s.%s", baseName,
					locale.getLanguage(), locale.getCountry(),
					honorifics ? "v" : "t", extension));
			prefResources.add(String.format("%s_%s_%s.%s", baseName,
					locale.getLanguage(), locale.getCountry(), extension));
		}
		prefResources.add(String.format("%s_%s_%s.%s", baseName,
				locale.getLanguage(), honorifics ? "v" : "t", extension));
		prefResources.add(String.format("%s_%s.%s", baseName,
				locale.getLanguage(), extension));
		prefResources.add(String.format("%s_%s.%s", baseName,
				honorifics ? "v" : "t", extension));
		prefResources.add(String.format("%s.%s", baseName, extension));
		for (String name : prefResources) {
			URL url;
			if (loadClass != null)
				url = loadClass.getResource(name);
			else
				url = getClass().getClassLoader().getResource(name);
			if (url != null) {
				this.locale = locale;
				this.name = name;
				this.url = url;
				return true;
			}
		}
		return false;
	}
}
