package eu.ewall.platform.reasoner.activitycoach.service.messages

/**
 * The base class for message content objects. It has subclasses for the
 * possible secondary intentions: FeedbackContent, ArgumentContent,
 * SuggestionContent, ReinforcementContent. Every content object should have a
 * unique ID and at least one message.
 * 
 * @author Dennis Hofs (RRD)
 */
class BaseRepresentation {
	
	/**
	 * The ID of this message content object.
	 */
	String id = ''
	
	/**
	 * The possible message representations, consisting of a message text and
	 * style properties. There should be at least one representation.
	 */
	List<MessageStyle> messages = []

	/**
	 * Defines whether this message is available from the current state.
	 */
	boolean available

	/**
	 * Sets the ID.
	 * 	
	 * @param id the ID
	 * @return this message content object
	 */
	def id(String id) {
		this.id = id
		this
	}
	
	/**
	 * Adds a message text without any style properties.
	 * 
	 * @param msg the message text
	 * @return the MessageStyle object
	 */
	def message(String msg) {
		def msgStyle = new MessageStyle(message: msg)
		messages << msgStyle
		msgStyle
	}

	/**
	 * Adds a message text with the specified style properties. See
	 * MessageStyle.
	 * 	
	 * @param msg the message text
	 * @param styles the style properties
	 * @return the MessageStyle object
	 */
	def message(String msg, Map<String,Float> styles) {
		def msgStyle = new MessageStyle(message: msg, styles: styles)
		messages << msgStyle
		msgStyle
	}

	/**
	 * Sets whether this feedback message is available from the current state.
	 * This can be a boolean value or a closure, which is evaluated
	 * immediately.
	 * 
	 * @param available the boolean value or closure
	 * @return this message content object
	 */
	def available(available) {
		if (available instanceof Closure) {
			available.delegate = this
			this.available = available()
		} else {
			this.available = available
		}
		this
	}
	
	String toString() {
		StringBuilder builder = new StringBuilder(
			"${getClass().simpleName} [id: $id, messages: [")
		def newline = System.properties['line.separator']
		if (!messages.empty)
			builder << "$newline    "
		builder << messages.join(",$newline    ")
		if (!messages.empty)
			builder << newline
		builder << ']]'
	}
}
