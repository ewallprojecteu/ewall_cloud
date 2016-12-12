package eu.ewall.platform.reasoner.activitycoach.service.dialogues

import eu.ewall.platform.idss.service.exception.DialogueNotFoundException

DialogueRepresentation read(URL dslUrl) throws DialogueNotFoundException {
	def dsl = dslUrl.text
	GroovyShell groovy = new GroovyShell();
	def closure = groovy.evaluate "{ -> $dsl }"
	def dlg = new DialogueRepresentation()
	closure.delegate = dlg
	closure()
	dlg
}
