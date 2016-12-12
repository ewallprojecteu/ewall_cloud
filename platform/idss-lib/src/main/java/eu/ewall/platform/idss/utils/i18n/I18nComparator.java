package eu.ewall.platform.idss.utils.i18n;

import java.text.Collator;

import java.util.Comparator;
import java.util.Locale;

/**
 * This comparator can compare strings using a collator for a specified locale.
 * The collator has primary strength, which means that diacritics and case are
 * ignored.
 * 
 * @author Dennis Hofs (RRD)
 */
public class I18nComparator implements Comparator<String> {
	private Collator collator;

	/**
	 * Constructs a new comparator with the default locale.
	 */
	public I18nComparator() {
		this(null);
	}

	/**
	 * Constructs a new comparator with the specified locale. If the locale is
	 * null, it will use the default locale.
	 * 
	 * @param locale the locale or null
	 */
	public I18nComparator(Locale locale) {
		if (locale == null)
			collator = Collator.getInstance();
		else
			collator = Collator.getInstance(locale);
		collator.setStrength(Collator.PRIMARY);
	}

	@Override
	public int compare(String o1, String o2) {
		return collator.compare(o1, o2);
	}
}
