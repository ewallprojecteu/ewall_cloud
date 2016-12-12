package eu.ewall.platform.reasoner.activitycoach.service.messages

/**
 * Database with message representation objects grouped by the various top-level message
 * types available:
 * <ul>
 *  <li>greetingRepresentations</li>
 * 	<li>feedbackRepresentations</li>
 * 	<li>argumentRepresentations</li>
 * 	<li>lifestyleSuggestionRepresentations</li>
 * 	<li>activitySuggestionRepresentations</li>
 * 	<li>reinforcementRepresentations</li>
 *  <li>synchronizeSensorRepresentations</li>
 * </ul>
 * 
 * @author Dennis Hofs (RRD)
 * @author Harm op den Akker (RRD)
 */
class MessageDatabase {
	
	/**
	 * Map from top-level message representation types to the corresponding {@link MessageRepresentationSet}.
	 */
	Map<String,MessageRepresentationSet> representationSets = new LinkedHashMap<String,MessageRepresentationSet>()
	
	def vcDataCollection
		
	MessageDatabase(vcDataCollection) {
		representationSets['greetingRepresentations'] = new MessageRepresentationSet()
		representationSets['feedbackRepresentations'] = new MessageRepresentationSet()
		representationSets['argumentRepresentations'] = new MessageRepresentationSet()
		representationSets['lifestyleSuggestionRepresentations'] = new MessageRepresentationSet()
		representationSets['activitySuggestionRepresentations'] = new MessageRepresentationSet()
		representationSets['reinforcementRepresentations'] = new MessageRepresentationSet()
		representationSets['synchronizeSensorRepresentations'] = new MessageRepresentationSet()
		this.vcDataCollection = vcDataCollection
	}

	/**
	 * Returns the UserModel from the StateModelRepository.
	 * 	
	 * @return the UserModel from the StateModelRepository
	 */
	def getUserModel() {
		vcDataCollection.userModel
	}
	
	/**
	 * Returns the ContextModel from the StateModelRepository.
	 * 
	 * @return the ContextModel from the StateModelRepository
	 */
	def getContext() {
		vcDataCollection.contextModel
	}
	
	/**
	 * Returns the InteractionModel from the StateModelRepository.
	 * 
	 * @return the InteractionModel from the StateModelRepository
	 */
	def getInteraction() {
		vcDataCollection.interactionModel
	}

	/**
	 * Returns the PhysicalActivityStateModel from the StateModelRepository.
	 * 
	 * @return the PhysicalActivityStateModel from the StateModelRepository
	 */
	def getPhysActState() {
		vcDataCollection.physicalActivityStateModel
	}
	
	/**
	 * Returns the {@link MessageRepresentationSet} containing 'greeting' messages.
	 * @return the {@link MessageRepresentationSet} containing 'greeting' messages.
	 */
	MessageRepresentationSet getGreetingRepresentations() {
		representationSets['greetingRepresentations']
	}
	
	/**
	 * Returns the {@link MessageRepresentationSet} containing 'feedback' messages.
	 * @return the {@link MessageRepresentationSet} containing 'feedback' messages.
	 */
	MessageRepresentationSet getFeedbackRepresentations() {
		representationSets['feedbackRepresentations']
	}
	
	/**
	 * Returns the {@link MessageRepresentationSet} containing 'argument' messages.
	 * @return the {@link MessageRepresentationSet} containing 'argument' messages.
	 */
	MessageRepresentationSet getArgumentRepresentations() {
		representationSets['argumentRepresentations']
	}
	
	/**
	 * Returns the {@link MessageRepresentationSet} containing 'lifestyleSuggestion' messages.
	 * @return the {@link MessageRepresentationSet} containing 'lifestyleSuggestion' messages.
	 */
	MessageRepresentationSet getLifestyleSuggestionRepresentations() {
		representationSets['lifestyleSuggestionRepresentations']
	}
	
	/**
	 * Returns the {@link MessageRepresentationSet} containing 'activitySuggestion' messages.
	 * @return the {@link MessageRepresentationSet} containing 'activitySuggestion' messages.
	 */
	MessageRepresentationSet getActivitySuggestionRepresentations() {
		representationSets['activitySuggestionRepresentations']
	}
	
	/**
	 * Returns the {@link MessageRepresentationSet} containing 'reinforcement' messages.
	 * @return the {@link MessageRepresentationSet} containing 'reinforcement' messages.
	 */
	MessageRepresentationSet getReinforcementRepresentations() {
		representationSets['reinforcementRepresentations']
	}
	
	/**
	 * Returns the {@link MessageRepresentationSet} containing 'synchronizeSensor' messages.
	 * @return the {@link MessageRepresentationSet} containing 'synchronizeSensor' messages.
	 */
	MessageRepresentationSet getSynchronizeSensorRepresentations() {
		representationSets['synchronizeSensorRepresentations']
	}
	
	/**
	 * Gets the {@link MessageRepresentationSet} for top level representation type 'greetingRepresentations'
	 * and evaluates the specified closure within the scope of that message set.
	 *
	 * @param closure the closure
	 * @return the {@link MessageRepresentationSet}.
	 */
	def greetingRepresentations(Closure closure) {
		addMessageRepresentationSet 'greetingRepresentations', closure
	}

	/**
	 * Gets the {@link MessageRepresentationSet} for top level representation type 'feedbackRepresentations'
	 * and evaluates the specified closure within the scope of that message set.
	 * 
	 * @param closure the closure
	 * @return the {@link MessageRepresentationSet}.
	 */
	def feedbackRepresentations(Closure closure) {
		addMessageRepresentationSet 'feedbackRepresentations', closure
	}
	
	/**
	 * Gets the {@link MessageRepresentationSet} for top level presentation type 'argumentPresentations'
	 * and evaluates the specified closure within the scope of that message set.
	 *
	 * @param closure the closure
	 * @return the {@link MessageRepresentationSet}.
	 */
	def argumentRepresentations(Closure closure) {
		addMessageRepresentationSet 'argumentRepresentations', closure
	}
	
	/**
	 * Gets the {@link MessageRepresentationSet} for top level representation type 'lifestyleSuggestionRepresentations'
	 * and evaluates the specified closure within the scope of that message set.
	 *
	 * @param closure the closure
	 * @return the {@link MessageRepresentationSet}.
	 */
	def lifestyleSuggestionRepresentations(Closure closure) {
		addMessageRepresentationSet 'lifestyleSuggestionRepresentations', closure
	}
	
	/**
	 * Gets the {@link MessageRepresentationSet} for top level representation type 'activitySuggestionRepresentations'
	 * and evaluates the specified closure within the scope of that message set.
	 *
	 * @param closure the closure
	 * @return the {@link MessageRepresentationSet}.
	 */
	def activitySuggestionRepresentations(Closure closure) {
		addMessageRepresentationSet 'activitySuggestionRepresentations', closure
	}
	
	/**
	 * Gets the {@link MessageRepresentationSet} for top level representation type 'reinforcementRepresentations'
	 * and evaluates the specified closure within the scope of that message set.
	 *
	 * @param closure the closure
	 * @return the {@link MessageRepresentationSet}.
	 */
	def reinforcementRepresentations(Closure closure) {
		addMessageRepresentationSet 'reinforcementRepresentations', closure
	}
	
	/**
	 * Gets the {@link MessageRepresentationSet} for top level representation type 'synchronizeSensorRepresentations'
	 * and evaluates the specified closure within the scope of that message set.
	 *
	 * @param closure the closure
	 * @return the {@link MessageRepresentationSet}.
	 */
	def synchronizeSensorRepresentations(Closure closure) {
		addMessageRepresentationSet 'synchronizeSensorRepresentations', closure
	}
	
	/**
	 * Gets the {@link MessageRepresentationSet} for the specified top level representation type and
	 * evaluates the specified closure within the scope of that message representation set.
	 * 
	 * @param topLevelRepresentations the top level representation type (feedbackRepresentations,
	 * argumentRepresentations, etc...).
	 * @param closure the closure.
	 * @return the {@link MessageRepresentationSet}.
	 */
	MessageRepresentationSet addMessageRepresentationSet(String topLevelRepresentations, Closure closure) {
		MessageRepresentationSet secondary = representationSets[topLevelRepresentations]
		closure.delegate = secondary
		closure()
		secondary
	}

	/**
	 * Finds the message {@link BaseRepresentation} object for a specified top level representation 
	 * type and a representation ID. If no such object is found, this method returns null.
	 * 
	 * @param topLevelRepresentations the top level representations type string ('feedbackRepresentations',
	 *  'argumentRepresentations', etc...).
	 * @param id the representation identifier.
	 * @return the message {@link BaseRepresentation} object or null if none is found.
	 */
	BaseRepresentation findRepresentation(String topLevelRepresentations, String id) {
		representationSets[topLevelRepresentations].findRepresentation(id)
	}
			
	String toString() {
		StringBuilder builder = new StringBuilder()
		def newline = System.properties['line.separator']
		builder << "${getClass().simpleName} ["
		def representations = [
			greetingRepresentations: greetingRepresentations,
			feedbackRepresentations: feedbackRepresentations,
			argumentRepresentations: argumentRepresentations,
			lifestyleSuggestionRepresentations: lifestyleSuggestionRepresentations,
			activitySuggestionRepresentations: activitySuggestionRepresentations,
			reinforcementRepresentations: reinforcementRepresentations,
			synchronizeSensorRepresentations: synchronizeSensorRepresentations
		]
		representations.each { key, value ->
			builder << newline
			builder << "    $key: "
			def prefix = "${value.getClass().simpleName} "
			def valStr = value.toString().substring(prefix.length()) 
			builder << valStr.readLines().join("$newline    ")
		}
		builder << "$newline]"
	}
}
