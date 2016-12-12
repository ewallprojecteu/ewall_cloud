package eu.ewall.platform.reasoner.activitycoach.service.messages

class MessageDatabaseReader {
	MessageDatabase read(URL dslUrl, stateModelRepository)
	throws Exception {
		def dsl = dslUrl.text
		GroovyShell groovy = new GroovyShell();
		def closure = groovy.evaluate "{ -> $dsl }"
		def db = new MessageDatabase(stateModelRepository)
		closure.delegate = db
		closure()
		db
	}
}
