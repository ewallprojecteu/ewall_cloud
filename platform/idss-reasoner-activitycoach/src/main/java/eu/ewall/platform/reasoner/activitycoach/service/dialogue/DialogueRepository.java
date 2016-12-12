package eu.ewall.platform.reasoner.activitycoach.service.dialogue;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;

import eu.ewall.platform.idss.service.exception.DialogueNotFoundException;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.idss.utils.i18n.I18nResourceFinder;
import eu.ewall.platform.reasoner.activitycoach.service.VCService;
import eu.ewall.platform.reasoner.activitycoach.service.dialogues.DialogueReader;
import eu.ewall.platform.reasoner.activitycoach.service.dialogues.DialogueRepresentation;

/**
 * A {@link DialogueRepository} contains the {@link DialogueRepresentation}s for a 
 * specific {@link Locale}.
 * 
 * @author Harm op den Akker, RRD
 * @author Dennis Hofs, RRD
 */
public class DialogueRepository {

	private Locale locale;
	private Map<String,DialogueRepresentation> dialogueRepresentations;
	private Logger logger;
	
	/**
	 * Creates an instance of a {@link DialogueRepository} for a  given {@link Locale}.
	 * @param locale the {@link Locale} in which language the dialogue representations will be available.
	 */
	public DialogueRepository(Locale locale) {
		this.locale = locale;
		dialogueRepresentations = new HashMap<String,DialogueRepresentation>();
		logger = AppComponents.getLogger(VCService.LOGTAG);
	}
	
	private void loadDialogue(String dialogueTypeId) throws DialogueNotFoundException {
		DialogueReader reader = new DialogueReader();
		I18nResourceFinder finder = new I18nResourceFinder("dialogue_" + dialogueTypeId);
		finder.setExtension("dsl");
		finder.setLoadClass(reader.getClass());
		finder.setUserLocale(locale);
		
		if (!finder.find()) {
			throw new DialogueNotFoundException("Resource dialogue_" + dialogueTypeId + ".dsl not found for language: '"+locale.toString()+"'");
		}
		
		DialogueRepresentation dialogueRepresentation = reader.read(finder.getUrl());
		dialogueRepresentations.put(dialogueTypeId, dialogueRepresentation);
		logger.info("Finished reading dialogue with dialogueTypeId " + dialogueTypeId + " for locale " + locale.toString() + " from " + finder.getUrl());
	}
	
	/**
	 * Returns the {@link DialogueRepresentation} associated with the given {@code dialogueTypeId} or throws
	 * an exception is the dialogue representation is not available in the {@link Locale} for this 
	 * {@link DialogueRepository}.
	 * 
	 * @param dialogueTypeId the unique identifier of the {@link DialogueRepresentation}.
	 * @return the {@link DialogueRepresentation} identified by the given {@code dialogueId}.
	 * @throws DialogueNotFoundException if the {@link DialogueRepresentation} is not available in the
	 * {@link Locale} of this {@link DialogueRepository}.
	 */
	public DialogueRepresentation getDialogue(String dialogueTypeId) throws DialogueNotFoundException {
		if(!dialogueRepresentations.containsKey(dialogueTypeId)) {
			try {
				loadDialogue(dialogueTypeId);
				return dialogueRepresentations.get(dialogueTypeId);
			} catch (DialogueNotFoundException e) {
				throw e;
			}
		} else {
			return dialogueRepresentations.get(dialogueTypeId);
		}
	}
	
	/**
	 * Returns the {@link Locale} for this {@link DialogueRepository}.
	 * @return the {@link Locale} for this {@link DialogueRepository}.
	 */
	public Locale getLocale() {
		return locale;
	}
}
