/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.fusioner.twitter.test;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import eu.ewall.fusioner.twitter.model.Tweet;

/**
 * The Class GetTweetsForUsernameAndStatus.
 * 
 */
public class GetTweetsForUsernameAndStatus {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy.");

		String username = "jdavison";

		String dateStrFrom = "03.03.2015.";
		String dateStrTo = "25.05.2015.";

		RestTemplate restTemplate = new RestTemplate();

		try {
			long from = SDF.parse(dateStrFrom).getTime();
			long to = SDF.parse(dateStrTo).getTime();

			ResponseEntity<List<Tweet>> tweetsResponse = restTemplate
					.exchange(
							String.format(
									"http://localhost:8080/fusioner-twitter/tweets/%s/timestamp?from=%s&to=%s",
									username, from, to), HttpMethod.GET, null,
							new ParameterizedTypeReference<List<Tweet>>() {
							});
			List<Tweet> tweets = tweetsResponse.getBody();

			for (Tweet tweet : tweets) {
				System.out.println(tweet.toString());
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}