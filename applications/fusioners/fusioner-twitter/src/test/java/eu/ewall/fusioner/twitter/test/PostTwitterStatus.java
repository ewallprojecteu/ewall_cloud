/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.fusioner.twitter.test;

import java.util.Date;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * The Class PostTwitterStatus.
 * 
 */
public class PostTwitterStatus {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		/*
		 * Can be any text, but every existing status must be unique, so here it
		 * appends current time
		 */
		String statusText = String.format(
				"Status from application created at: %s.", new Date());

		String username = "jdavison";

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<String>(statusText, headers);

		try {
			ResponseEntity<String> response = restTemplate
					.exchange(
							String.format(
									"http://localhost:8080/fusioner-twitter/tweets/%s/update",
									username), HttpMethod.POST, entity,
							new ParameterizedTypeReference<String>() {
							});
			System.out.println(response.toString());

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}