/*******************************************************************************
 * Copyright 2016 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.profilingserver.test.db;

import java.util.ArrayList;
import java.util.List;

import eu.ewall.platform.commons.datamodel.ewallsystem.SensingEnvironment;
import eu.ewall.platform.commons.datamodel.location.IndoorPlace;
import eu.ewall.platform.commons.datamodel.location.IndoorPlaceArea;
import eu.ewall.platform.middleware.datamanager.dao.ewallsystem.SensingEnvironmentDao;

public class UpdateRoomForUser {

	/**
	 * Test updating the IndoorPlace (room) details belonging to the certain
	 * sensing environment (connected to the given user)
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		SensingEnvironmentDao sensEnvironmentDao = new SensingEnvironmentDao();
		SensingEnvironment env = sensEnvironmentDao.getSensingEnvironmentByPrimaryUser("exampleUsername");
		// SensingEnvironment env =
		// sensEnvironmentDao.getSensingEnvironmentByUUID("00000000-0000-0000-0000-000000000000");

		System.out.println("env: " + env.toString());

		IndoorPlace newKitchen = new IndoorPlace("kitchen", IndoorPlaceArea.KITCHEN, 654321L, true, true, false, true);

		// if there is an indoor place that needs to be updated
		if (env.getIndoorPlaceNames().contains("kitchen")) {
			List<IndoorPlace> newIndoorPlacesList = new ArrayList<IndoorPlace>();

			// iterate through all indoor places belonging to the
			// sensing environment
			for (IndoorPlace indoorPlace : env.getIndoorPlaces())
				if (indoorPlace.getName().equalsIgnoreCase("kitchen") == false) {
					// copy all others
					newIndoorPlacesList.add(indoorPlace);
				} else
					// but update the designated
					newIndoorPlacesList.add(newKitchen);
			// save the new list (with updated indoor place) to the
			// sensing environment
			env.setIndoorPlaces(newIndoorPlacesList);

			sensEnvironmentDao.updateSensingEnvironment(env);

		}
	}
}
