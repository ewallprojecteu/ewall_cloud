package eu.ewall.platform.reasoner.activitycoach.service.messages

/**
 * A MessageStyle object consists of a message text and a set of style
 * properties. The possible styles are defined by the lang* and lang*Pref
 * properties of UserModel. At the moment of writing:
 * 
 * <p><ul>
 * <li>formality: 0 (informal) .. 1 (formal)</li>
 * <li>length: 0 (short) .. 1 (long)</li>
 * <li>suggestive: 0 (imperative) .. 1 (suggestive)</li>
 * <li>honorifics: 0 (tu) or 1 (vos)</li>
 * </ul></p>
 * 
 * <p>Any style property can be left undefined.</p>
 * 
 * @author Dennis Hofs (RRD)
 */
class MessageStyle {
	String message = ''
	Map<String,Float> styles = new LinkedHashMap<String,Float>()
	
	String toString() {
		message
	}
}
