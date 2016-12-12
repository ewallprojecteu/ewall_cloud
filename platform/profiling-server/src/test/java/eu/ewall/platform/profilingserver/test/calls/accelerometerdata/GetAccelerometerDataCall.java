/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.profilingserver.test.calls.accelerometerdata;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import eu.ewall.platform.commons.datamodel.measure.AccelerometerMeasurement;

/**
 * The Class GetAccelerometerDataCall.
 */
public class GetAccelerometerDataCall {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		//
		// // acceptable media type
		// List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		// acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		//
		// // header
		// HttpHeaders headers = new HttpHeaders();
		// headers.setAccept(acceptableMediaTypes);
		//
		// // body
		// String body = "hello world";
		// HttpEntity<String> entity = new HttpEntity<String>(body, headers);

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<List<AccelerometerMeasurement>> accelerometerMeasurementList = restTemplate
				.exchange(
						"http://localhost:8080/profiling-server/users/testusername/accelerometer/timestamp?from=1000&to=4000",
						HttpMethod.GET,
						null,
						new ParameterizedTypeReference<List<AccelerometerMeasurement>>() {
						});

		List<AccelerometerMeasurement> mlist = new ArrayList<AccelerometerMeasurement>(
				accelerometerMeasurementList.getBody());
		for (AccelerometerMeasurement each : mlist) {
			System.out.println("isa value is:" + each.getIsaValue());
		}

	}
}
