package eu.ewall.platform.commons.datamodel.profile;

/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * Inspired by Stages of Change from:
 * 
 * @link <a
 *       href="http://psychcentral.com/lib/stages-of-change/">http://psychcentral
 *       .com/lib/stages-of-change/</a>
 * 
 *       Almost 20 years ago, two well-known alcoholism researchers, Carlo C.
 *       DiClemente and J. O. Prochaska, introduced a five-stage model of change
 *       to help professionals understand their clients with addiction problems
 *       and motivate them to change. Their model is based not on abstract
 *       theories but on their personal observations of how people went about
 *       modifying problem behaviors such as smoking, overeating and problem
 *       drinking.
 * 
 *       The six stages of the model are: precontemplation, contemplation,
 *       determination, action, maintenance, termination.
 * 
 *       This implementation is inspired by this work but not completely
 *       compliant.
 * 
 * @author eandgrg
 */
public enum PsychologicalStageOfChangeType {

	/**
	 * Individuals in the precontemplation stage of change are not even thinking
	 * about changing their drinking behavior. They may not see it as a problem,
	 * or they think that others who point out the problem are exaggerating
	 */
	PRECONTEMPLATION,
	/**
	 * Individuals in this stage of change are willing to consider the
	 * possibility that they have a problem, and the possibility offers hope for
	 * change. However, people who are contemplating change are often highly
	 * ambivalent. They are on the fence. Contemplation is not a commitment, not
	 * a decision to change..
	 */
	CONTEMPLATION,
	/** The preparation. */
	PREPARATION,
	/** The action. */
	ACTION,
	/** The maintenance. */
	MAINTENANCE,
	/** StageOfChange unknown. */
	UNKNOWN,
	/** Some other StageOfChange. */
	OTHER

}