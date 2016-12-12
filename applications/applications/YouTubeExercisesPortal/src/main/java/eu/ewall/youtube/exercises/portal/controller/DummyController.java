/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.youtube.exercises.portal.controller;

import org.springframework.web.bind.annotation.*;

/**
 * The Class DummyController.
 */
@RestController
@RequestMapping("/dummy")
public class DummyController {

	/**
	 * Preparing for authorizing.
	 *
	 * @return the string
	 */
	@RequestMapping(value = "/prepare", method = RequestMethod.GET)
	public String preparingForAuthorizing() {
		return "Opening authorizing page...";
	}

	/**
	 * Callback dummy call.
	 *
	 * @return the string
	 */
	@RequestMapping(value = "/callback", method = RequestMethod.GET)
	public String callbackDummyCall() {
		return "Redirecting to application...";
	}
}