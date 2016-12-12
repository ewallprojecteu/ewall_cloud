/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.profilingserver.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * The Class ExceptionController.
 * 
 * @author EMIRMOS
 */
@ControllerAdvice
public class ExceptionController {

	/** The log. */
	private static Logger logger = LoggerFactory
			.getLogger(ExceptionController.class);

	/**
	 * Handle illegal input error.
	 *
	 * @param exception
	 *            the exception
	 * @return the response entity
	 */
	@ExceptionHandler({ IllegalArgumentException.class,
			TypeMismatchException.class })
	public ResponseEntity<String> handleIllegalInputError(Exception exception) {
		logger.warn(exception.getMessage());
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle exception.
	 *
	 * @param exception
	 *            the exception
	 * @return the response entity
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception exception) {
		logger.error(exception.getMessage());
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
