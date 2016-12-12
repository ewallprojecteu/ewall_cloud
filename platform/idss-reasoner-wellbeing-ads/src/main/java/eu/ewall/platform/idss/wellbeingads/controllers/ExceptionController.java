/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.idss.wellbeingads.controllers;

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
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler({ IllegalArgumentException.class,
			TypeMismatchException.class })
	public ResponseEntity<String> handleIllegalInputError(Exception e) {
		logger.warn("Illegal argment error in idss wellbeingads reasoner", e);
		return new ResponseEntity<String>(e.getMessage(),
				HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle runtime exception error.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler({ RuntimeException.class })
	public ResponseEntity<String> handleRuntimeExceptionError(Exception e) {
		logger.error("Runtime error in idss wellbeingads reasoner", e);
		return new ResponseEntity<String>(e.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handle exception.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception e) {
		logger.error("Error in idss wellbeingads reasoner", e);
		return new ResponseEntity<String>(e.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
