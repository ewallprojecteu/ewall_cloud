package eu.ewall.platform.reasoner.activitycoach.service.messages

/**
 * This message set contains message representation objects ({@link BaseRepresentation}) of
 * a specific type (greeting, feedback, argument, lifestyleSuggestion, activitySuggestion, reinforcement). The
 * MessageDatabase contains one SecondaryMessageSet for each of these top level representation sets.
 * 
 * @author Dennis Hofs (RRD)
 * @author Harm op den Akker (RRD)
 */
class MessageRepresentationSet {
	
	/**
	 * List of all {@link BaseRepresentation} items in this set.
	 */
	List<? extends BaseRepresentation> representations = new ArrayList<? extends BaseRepresentation>();

	/**
	 * Constructs an empty message set.
	 */
	MessageRepresentationSet() {
		representations[] = []
	}
	
	/**
	 * Returns the message representation objects in this {@link MessageRepresentationSet}.
	 * @return the message representation objects in this {@link MessageRepresentationSet}.
	 */
	List<? extends BaseRepresentation> getRepresentations() {
		representations
	}
	
	/**
	 * Adds a message representation object of type 'greeting' to this
	 * message set. It adds a new {@link GreetingRepresentation} object and evaluates the
	 * specified closure within the scope of that object.
	 *
	 * @param closure the closure
	 * @return the {@link GreetingRepresentation} object
	 */
	def greeting(Closure closure) {
		def greeting = new GreetingRepresentation()
		closure.delegate = greeting
		closure()
		representations << greeting
		greeting
	}
	
	
	/**
	 * Adds a message representation object of type 'feedback' to this
	 * message set. It adds a new {@link FeedbackRepresentation} object and evaluates the
	 * specified closure within the scope of that object.
	 * 
	 * @param closure the closure
	 * @return the {@link FeedbackRepresentation} object
	 */
	def feedback(Closure closure) {
		def feedback = new FeedbackRepresentation()
		closure.delegate = feedback
		closure()
		representations << feedback
		feedback
	}
	
	/**
	 * Adds a message representation object of type 'reinforcement' to this
	 * message set. It adds a new {@link ReinforcementRepresentation} object and evaluates the
	 * specified closure within the scope of that object.
	 *
	 * @param closure the closure
	 * @return the {@link ReinforcementRepresentation} object
	 */
	def reinforcement(Closure closure) {
		def reinforcement = new ReinforcementRepresentation()
		closure.delegate = reinforcement
		closure()
		representations << reinforcement
		reinforcement
	}
	
	/**
	 * Adds a message representation object of type 'synchronizeSensor' to this
	 * message set. It adds a new {@link SynchronizeSensorRepresentation} object and evaluates the
	 * specified closure within the scope of that object.
	 *
	 * @param closure the closure
	 * @return the {@link SynchronizeSensorRepresentation} object
	 */
	def synchronizeSensor(Closure closure) {
		def synchronizeSensor = new SynchronizeSensorRepresentation()
		closure.delegate = synchronizeSensor
		closure()
		representations << synchronizeSensor
		synchronizeSensor
	}
	
	def argument(Closure closure) {
		def argument = new ArgumentRepresentation()
		closure.delegate = argument
		closure()
		representations << argument
		argument
	}

	/**
	 * Finds the message representation {@link BaseRepresentation} object with the given
	 * representation identifier. If no such object is found, this method returns null.
	 * 	
	 * @param id the representation identifier.
	 * @return the {@link BaseRepresentation} object or null.
	 */
	BaseRepresentation findRepresentation(String id) {
		representations.find {
			it.id == id
		}
	}

	String toString() {
		StringBuilder builder = new StringBuilder()
		def newline = System.properties['line.separator']
		builder << "${getClass().simpleName} ["
		def representations = [
			feedback: feedback
		]
		representations.each { key, value ->
			builder << newline
			builder << "    $key: ["
			if (!value.empty)
				builder << newline
			def strs = value.collect {
				def prefix = "${it.getClass().simpleName} "
				def valStr = it.toString().substring(prefix.length())
				def indented = valStr.readLines().collect { "        $it" }
				indented.join(newline)
			}
			builder << strs.join(",$newline")
			if (!value.empty)
				builder << "$newline    "
			builder << ']'
		}
		builder << "$newline]"
	}
}
