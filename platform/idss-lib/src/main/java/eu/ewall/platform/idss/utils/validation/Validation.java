package eu.ewall.platform.idss.utils.validation;

/**
 * This class can validate values with respect to certain criteria.
 * 
 * @author Dennis Hofs (RRD)
 */
public class Validation {
	
	/**
	 * Validates the length of a string.
	 * 
	 * @param s the string
	 * @param minLen the minimum length (&lt;= 0 if no minimum)
	 * @param maxLen the maximum length (&tl;= 0 if no maximum)
	 * @return the string
	 * @throws ValidationException if the string length is invalid
	 */
	public static String validateStringLength(String s, int minLen, int maxLen)
			throws ValidationException {
		if (minLen <= 0 && maxLen <= 0)
			return s;
		String error;
		if (minLen > 0 && maxLen > 0) {
			error = String.format("String length not between %d and %d: %s",
					minLen, maxLen, s);
		} else if (minLen > 0) {
			error = String.format("String length less than %d: %s", minLen, s);
		} else {
			error = String.format("String length greater than %d: %s",
					maxLen, s);
		}
		if (s.length() < minLen || (maxLen > 0 && s.length() > maxLen))
			throw new ValidationException(error);
		return s;
	}

	/**
	 * Validates the string against a regular expression.
	 * 
	 * @param s the string
	 * @param regex the regular expression
	 * @return the string
	 * @throws ValidationException if the string does not match the regular
	 * expression
	 */
	public static String validateStringRegex(String s, String regex)
			throws ValidationException {
		if (!s.matches(regex)) {
			throw new ValidationException(String.format(
					"String does not match regular expression %s: %s",
					regex, s));
		}
		return s;
	}
	
	/**
	 * Validates that an integer is in the specified range.
	 * 
	 * @param n the integer
	 * @param min the minimum value (null if no minimum)
	 * @param max the maximum value (null if no maximum)
	 * @return the integer
	 * @throws ValidationException if the integer is not in the specified range
	 */
	public static int validateIntRange(int n, Integer min, Integer max)
			throws ValidationException {
		if (min == null && max == null)
			return n;
		String error;
		if (min != null && max != null) {
			error = String.format("Integer not between %d and %d: %d",
					min, max, n);
		} else if (min != null) {
			error = String.format("Integer less than %d: %d", min, n);
		} else {
			error = String.format("Integer greater than %d: %d", max, n);
		}
		if ((min != null && n < min) || (max != null && n > max))
			throw new ValidationException(error);
		return n;
	}
	
	/**
	 * Validates that a long is in the specified range.
	 * 
	 * @param n the long
	 * @param min the minimum value (null if no minimum)
	 * @param max the maximum value (null if no maximum)
	 * @return the long
	 * @throws ValidationException if the long is not in the specified range
	 */
	public static long validateLongRange(long n, Long min, Long max)
			throws ValidationException {
		if (min == null && max == null)
			return n;
		String error;
		if (min != null && max != null) {
			error = String.format("Long not between %d and %d: %d",
					min, max, n);
		} else if (min != null) {
			error = String.format("Long less than %d: %d", min, n);
		} else {
			error = String.format("Long greater than %d: %d", max, n);
		}
		if ((min != null && n < min) || (max != null && n > max))
			throw new ValidationException(error);
		return n;
	}
	
	/**
	 * Validates that a string is a valid email address.
	 * 
	 * @param email the email address
	 * @return the email address
	 * @throws ValidationException if the email address is invalid
	 */
	public static String validateEmail(String email)
			throws ValidationException {
		int localDomainSep = email.lastIndexOf('@');
		if (localDomainSep == -1)
			throw new ValidationException("Character '@' not found");
		String localPart = email.substring(0, localDomainSep);
		String domainPart = email.substring(localDomainSep + 1);
		
		// validate local part
		String allowedLocalChars = "[A-Za-z0-9!#$%&'*+\\-/=?^_`.{|}~]+";
		if (!localPart.matches(allowedLocalChars)) {
			throw new ValidationException("Invalid local part");
		}

		// validate domain part
		String[] split = domainPart.split("\\.");
		if (split.length < 2) {
			throw new ValidationException(
					"Domain part does not consist of two or more labels");
		}
		for (int i = 0; i < split.length; i++) {
			String domainLabel = split[i];
			if (!domainLabel.matches("[A-Za-z0-9\\-]+") ||
					domainLabel.startsWith("-") || domainLabel.endsWith("-")) {
				throw new ValidationException("Invalid domain label: " +
						domainLabel);
			}
			if (i == split.length - 1 && domainLabel.matches("[0-9]+")) {
				throw new ValidationException("Invalid TLD: " + domainLabel);
			}
		}
		
		return email;
	}

	/**
	 * Validates that an object is not null.
	 * 
	 * @param obj the object
	 * @return the object
	 * @throws ValidationException if the object is null
	 */
	public static Object validateNotNull(Object obj)
			throws ValidationException {
		if (obj == null)
			throw new ValidationException("Value is null");
		return obj;
	}
}
