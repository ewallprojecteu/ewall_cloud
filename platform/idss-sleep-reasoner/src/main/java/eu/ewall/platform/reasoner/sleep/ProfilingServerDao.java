package eu.ewall.platform.reasoner.sleep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

import eu.ewall.platform.commons.datamodel.measure.AccelerometerMeasurement;
import eu.ewall.platform.commons.datamodel.measure.CarbonMonoxideMeasurement;
import eu.ewall.platform.commons.datamodel.measure.BloodPressureMeasurement;
import eu.ewall.platform.commons.datamodel.measure.DoorStatus;
import eu.ewall.platform.commons.datamodel.measure.HeartRateMeasurement;
import eu.ewall.platform.commons.datamodel.measure.HumidityMeasurement;
import eu.ewall.platform.commons.datamodel.measure.IlluminanceMeasurement;
import eu.ewall.platform.commons.datamodel.measure.LiquefiedPetroleumGasMeasurement;
import eu.ewall.platform.commons.datamodel.measure.MattressPressureSensing;
import eu.ewall.platform.commons.datamodel.measure.MovementMeasurement;
import eu.ewall.platform.commons.datamodel.measure.NaturalGasMeasurement;
import eu.ewall.platform.commons.datamodel.measure.OxygenSaturationMeasurement;
import eu.ewall.platform.commons.datamodel.measure.TemperatureMeasurement;
import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.UserRole;
import eu.ewall.platform.commons.datamodel.sensing.VisualSensing;
import eu.ewall.platform.reasoner.sleep.CWAUser;


/**
 * DAO that reads user data from the profiling server, such as sensor readings. The data is used by service bricks to
 * infer higher level information, such as user activity.
 */
public class ProfilingServerDao {
	private static final Logger log = LoggerFactory.getLogger(ProfilingServerDao.class);
	
	private RestOperations ewallClient;
	private String profilingServerUrl;
	
	private static final long ONE_DAY = 1000*60*60*24;
	
	public ProfilingServerDao(RestOperations ewallClient, String profilingServerUrl) {
		this.ewallClient = ewallClient;
		this.profilingServerUrl = profilingServerUrl;
	}
	
	/**
	 * Gets all accelerometer data for the interval.
	 * 
	 * @param username user identification string
	 * @param from type Date variable identifying the beginning of the desired interval
	 * @param to type Date variable identifying the end of the desired interval
	 * @return array with the accumulated IMA values for the interval
	 */	
	public AccelerometerMeasurement[] getAccelerometerData(String username, Date from, Date to, int maxDaysPerRequest) {
		ArrayList<AccelerometerMeasurement> accelerometerMeasurementsList = new ArrayList<AccelerometerMeasurement>();
		Date newTo = new Date(to.getTime());
		while(true){
			//If more then x days, we ask for x days
			if((newTo.getTime()-from.getTime())>(ONE_DAY*maxDaysPerRequest)){
				newTo.setTime(from.getTime()+(ONE_DAY*maxDaysPerRequest));
			} else {
				//else we ask for the latest x-from days
				newTo.setTime(to.getTime());
			}
						
			String url = profilingServerUrl+"/users/"+username+"/accelerometer/timestamp?from="+from.getTime()+"&to="+newTo.getTime();
			log.debug("Invoking endpoint: " + url);
			AccelerometerMeasurement[] accelerometerMeasurements = ewallClient.getForObject(url, new AccelerometerMeasurement[0].getClass());
			accelerometerMeasurementsList.addAll(Arrays.asList(accelerometerMeasurements));
			if(newTo.compareTo(to)==0){
				break;
			}
			from.setTime(newTo.getTime());
			newTo.setTime(to.getTime());
		}
		
		log.debug("Ordering elements...");
		long now = System.currentTimeMillis();
		Collections.sort(accelerometerMeasurementsList, new Comparator<AccelerometerMeasurement>() {
			  public int compare(AccelerometerMeasurement m1, AccelerometerMeasurement m2) {
				  return Long.compare(m1.getTimestamp(), m2.getTimestamp());
			  };
		});
		log.debug("Ordering took " + (System.currentTimeMillis() - now) + " ms.");
		return accelerometerMeasurementsList.toArray(new AccelerometerMeasurement[accelerometerMeasurementsList.size()]);
	}
	
	/**
	 * Get all primary users
	 * @return all primary users
	 */
	public User[] getPrimaryUsers(){
		String url = profilingServerUrl+"/users/";
		log.debug("Invoking endpoint: " + url);
		User[] users = ewallClient.getForObject(url, new User[0].getClass());
		ArrayList<User> primaryUsers = new ArrayList<User>();
		for(User user : users){
			if(user.getUserRole().equals(UserRole.PRIMARY_USER)){
				primaryUsers.add(user);
			}
		}
		User[] results = new User[primaryUsers.size()];	
		return primaryUsers.toArray(results);
	}

	/**
	 * Get all primary users associated to a given caregiver
	 * @return all primary users associated to caregiver
	 */
	public List<User> getPrimaryUsers(String caregiverUsername){
		String url = profilingServerUrl+"/users/"+caregiverUsername+"/primaryusers";
		log.info("Invoking endpoint: " + url);
		User[] results = ewallClient.getForObject(url, new User[0].getClass());
		return Arrays.asList(results);
	}

	/**
	 * Get all primary users associated to a given caregiver
	 * @return all primary users associated to caregiver
	 */
	public List<CWAUser> getPrimaryUsersExtendedProfile(String caregiverUsername){
		String url = profilingServerUrl+"/users/"+caregiverUsername+"/primaryusers";
		log.info("Invoking endpoint: " + url);
		CWAUser[] results = ewallClient.getForObject(url, new CWAUser[0].getClass());
		return Arrays.asList(results);
	}

	/**
	 * Returns the profile of the given user.
	 * 
	 * @return the profile of the given user
	 */
	public User getUser(String username) {
		String url = profilingServerUrl + "/users/{username}";
		log.debug("Invoking endpoint: " + url);
		try {
			return ewallClient.getForObject(url, User.class, username);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				return null;
			} else {
				throw e;
			}
		}
	}
	
	/**
	 * Returns the extended profile of the given user.
	 * 
	 * @return the extended profile of the given user
	 */
	public CWAUser getCWAUser(String username) {
		String url = profilingServerUrl + "/users/{username}";
		log.debug("Invoking endpoint: " + url);
		try {
			return ewallClient.getForObject(url, CWAUser.class, username);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				return null;
			} else {
				throw e;
			}
		}
	}
	
	public MovementMeasurement[] getPirMovementData(String username, Date from, Date to, int maxDaysPerRequest) {		
		ArrayList<MovementMeasurement> movementMeasurementsList = new ArrayList<MovementMeasurement>();
		Date newTo = new Date(to.getTime());
		while(true){
			//If more then x days, we ask for x days
			if((newTo.getTime()-from.getTime())>(ONE_DAY*maxDaysPerRequest)){
				newTo.setTime(from.getTime()+(ONE_DAY*maxDaysPerRequest));
			} else {
				//else we ask for the latest x-from days
				newTo.setTime(to.getTime());
			}
						
			String url = profilingServerUrl+"/users/"+username+"/movement/timestamp?from="+from.getTime()+"&to="+newTo.getTime();
			log.debug("Invoking endpoint: " + url);
			MovementMeasurement[] movementMeasurements = ewallClient.getForObject(url, new MovementMeasurement[0].getClass());			
			movementMeasurementsList.addAll(Arrays.asList(movementMeasurements));
			if(newTo.compareTo(to)==0){
				break;
			}
			from.setTime(newTo.getTime());
			newTo.setTime(to.getTime());
		}

		log.debug("Ordering elements...");
		long now = System.currentTimeMillis();
		Collections.sort(movementMeasurementsList, new Comparator<MovementMeasurement>() {
			  public int compare(MovementMeasurement m1, MovementMeasurement m2) {
				  return Long.compare(m1.getTimestamp(), m2.getTimestamp());
			  };
		});
		log.debug("Ordering took " + (System.currentTimeMillis() - now) + " ms.");
		return movementMeasurementsList.toArray(new MovementMeasurement[movementMeasurementsList.size()]);

	}

	public HumidityMeasurement[] getHumidityData(String username, Date from, Date to, int maxDaysPerRequest) {
		ArrayList<HumidityMeasurement> humidityMeasurementsList = new ArrayList<HumidityMeasurement>();
		Date newTo = new Date(to.getTime());
		while(true){
			//If more then x days, we ask for x days
			if((newTo.getTime()-from.getTime())>(ONE_DAY*maxDaysPerRequest)){
				newTo.setTime(from.getTime()+(ONE_DAY*maxDaysPerRequest));
			} else {
				//else we ask for the latest x-from days
				newTo.setTime(to.getTime());
			}
						
			String url = profilingServerUrl+"/users/"+username+"/humidity/timestamp?from="+from.getTime()+"&to="+newTo.getTime();
			log.debug("Invoking endpoint: " + url);
			HumidityMeasurement[] humidityMeasurements = ewallClient.getForObject(url, new HumidityMeasurement[0].getClass());
			humidityMeasurementsList.addAll(Arrays.asList(humidityMeasurements));
			if(newTo.compareTo(to)==0){
				break;
			}
			from.setTime(newTo.getTime());
			newTo.setTime(to.getTime());
		}

		log.debug("Ordering elements...");
		long now = System.currentTimeMillis();
		Collections.sort(humidityMeasurementsList, new Comparator<HumidityMeasurement>() {
			  public int compare(HumidityMeasurement m1, HumidityMeasurement m2) {
				  return Long.compare(m1.getTimestamp(), m2.getTimestamp());
			  };
		});
		log.debug("Ordering took " + (System.currentTimeMillis() - now) + " ms.");
		return humidityMeasurementsList.toArray(new HumidityMeasurement[humidityMeasurementsList.size()]);
	}

	public IlluminanceMeasurement[] getIlluminanceData(String username, Date from, Date to, int maxDaysPerRequest) {
		ArrayList<IlluminanceMeasurement> illuminanceMeasurementsList = new ArrayList<IlluminanceMeasurement>();
		Date newTo = new Date(to.getTime());
		while(true){
			//If more then x days, we ask for x days
			if((newTo.getTime()-from.getTime())>(ONE_DAY*maxDaysPerRequest)){
				newTo.setTime(from.getTime()+(ONE_DAY*maxDaysPerRequest));
			} else {
				//else we ask for the latest x-from days
				newTo.setTime(to.getTime());
			}
						
			String url = profilingServerUrl+"/users/"+username+"/illuminance/timestamp?from="+from.getTime()+"&to="+newTo.getTime();
			log.debug("Invoking endpoint: " + url);
			IlluminanceMeasurement[] illuminanceMeasurements = ewallClient.getForObject(url, new IlluminanceMeasurement[0].getClass());
			illuminanceMeasurementsList.addAll(Arrays.asList(illuminanceMeasurements));
			if(newTo.compareTo(to)==0){
				break;
			}
			from.setTime(newTo.getTime());
			newTo.setTime(to.getTime());
		}
		
		log.debug("Ordering elements...");
		long now = System.currentTimeMillis();
		Collections.sort(illuminanceMeasurementsList, new Comparator<IlluminanceMeasurement>() {
			  public int compare(IlluminanceMeasurement m1, IlluminanceMeasurement m2) {
				  return Long.compare(m1.getTimestamp(), m2.getTimestamp());
			  };
		});
		log.debug("Ordering took " + (System.currentTimeMillis() - now) + " ms.");
		return illuminanceMeasurementsList.toArray(new IlluminanceMeasurement[illuminanceMeasurementsList.size()]);
	}

	public TemperatureMeasurement[] getTemperatureData(String username, Date from, Date to, int maxDaysPerRequest) {
		ArrayList<TemperatureMeasurement> temperatureMeasurementsList = new ArrayList<TemperatureMeasurement>();
		Date newTo = new Date(to.getTime());
		while(true){
			//If more then x days, we ask for x days
			if((newTo.getTime()-from.getTime())>(ONE_DAY*maxDaysPerRequest)){
				newTo.setTime(from.getTime()+(ONE_DAY*maxDaysPerRequest));
			} else {
				//else we ask for the latest x-from days
				newTo.setTime(to.getTime());
			}
						
			String url = profilingServerUrl+"/users/"+username+"/temperature/timestamp?from="+from.getTime()+"&to="+newTo.getTime();
			log.debug("Invoking endpoint: " + url);
			TemperatureMeasurement[] temperatureMeasurements = ewallClient.getForObject(url, new TemperatureMeasurement[0].getClass());
			temperatureMeasurementsList.addAll(Arrays.asList(temperatureMeasurements));
			if(newTo.compareTo(to)==0){
				break;
			}
			from.setTime(newTo.getTime());
			newTo.setTime(to.getTime());
		}
	
		log.debug("Ordering elements...");
		long now = System.currentTimeMillis();
		Collections.sort(temperatureMeasurementsList, new Comparator<TemperatureMeasurement>() {
			  public int compare(TemperatureMeasurement m1, TemperatureMeasurement m2) {
				  return Long.compare(m1.getTimestamp(), m2.getTimestamp());
			  };
		});
		log.debug("Ordering took " + (System.currentTimeMillis() - now) + " ms.");
		return temperatureMeasurementsList.toArray(new TemperatureMeasurement[temperatureMeasurementsList.size()]);
	}

	public MattressPressureSensing[] getMattressPressureData(String username, Date from, Date to, int maxDaysPerRequest) {
		ArrayList<MattressPressureSensing> bedPressureMeasurementsList = new ArrayList<MattressPressureSensing>();
		Date newTo = new Date(to.getTime());
		while(true){
			//If more then x days, we ask for x days
			if((newTo.getTime()-from.getTime())>(ONE_DAY*maxDaysPerRequest)){
				newTo.setTime(from.getTime()+(ONE_DAY*maxDaysPerRequest));
			} else {
				//else we ask for the latest x-from days
				newTo.setTime(to.getTime());
			}
						
			String url = profilingServerUrl+"/users/"+username+"/environment/mattress_pressure?from="+from.getTime()+"&to="+newTo.getTime();
			log.debug("Invoking endpoint: " + url);
			MattressPressureSensing[] mattressPressureMeasurements = ewallClient.getForObject(url, new MattressPressureSensing[0].getClass());
			bedPressureMeasurementsList.addAll(Arrays.asList(mattressPressureMeasurements));
			if(newTo.compareTo(to)==0){
				break;
			}
			from.setTime(newTo.getTime());
			newTo.setTime(to.getTime());
		}

		log.debug("Ordering elements...");
		long now = System.currentTimeMillis();
		Collections.sort(bedPressureMeasurementsList, new Comparator<MattressPressureSensing>() {
			  public int compare(MattressPressureSensing m1, MattressPressureSensing m2) {
				  return Long.compare(m1.getTimestamp(), m2.getTimestamp());
			  };
		});
		log.debug("Ordering took " + (System.currentTimeMillis() - now) + " ms.");
		return bedPressureMeasurementsList.toArray(new MattressPressureSensing[bedPressureMeasurementsList.size()]);
	}

	public DoorStatus[] getDoorData(String username, Date from, Date to, int maxDaysPerRequest) {
		ArrayList<DoorStatus> doorStatusMeasurementsList = new ArrayList<DoorStatus>();
		Date newTo = new Date(to.getTime());
		while(true){
			//If more then x days, we ask for x days
			if((newTo.getTime()-from.getTime())>(ONE_DAY*maxDaysPerRequest)){
				newTo.setTime(from.getTime()+(ONE_DAY*maxDaysPerRequest));
			} else {
				//else we ask for the latest x-from days
				newTo.setTime(to.getTime());
			}
						
			String url = profilingServerUrl+"/users/"+username+"/environment/doors?from="+from.getTime()+"&to="+newTo.getTime();
			log.debug("Invoking endpoint: " + url);
			DoorStatus[] doorStatusMeasurements = ewallClient.getForObject(url, new DoorStatus[0].getClass());
			doorStatusMeasurementsList.addAll(Arrays.asList(doorStatusMeasurements));
			if(newTo.compareTo(to)==0){
				break;
			}
			from.setTime(newTo.getTime());
			newTo.setTime(to.getTime());
		}

		log.debug("Ordering elements...");
		long now = System.currentTimeMillis();
		Collections.sort(doorStatusMeasurementsList, new Comparator<DoorStatus>() {
			  public int compare(DoorStatus m1, DoorStatus m2) {
				  return Long.compare(m1.getTimestamp(), m2.getTimestamp());
			  };
		});
		log.debug("Ordering took " + (System.currentTimeMillis() - now) + " ms.");
		return doorStatusMeasurementsList.toArray(new DoorStatus[doorStatusMeasurementsList.size()]);
	}
	
	public VisualSensing[] getMoodData(String username, Date from, Date to, int maxDaysPerRequest) {
		ArrayList<VisualSensing> visualSensingMeasurementsList = new ArrayList<VisualSensing>();
		Date newTo = new Date(to.getTime());
		while(true){
			//If more then x days, we ask for x days
			if((newTo.getTime()-from.getTime())>(ONE_DAY*maxDaysPerRequest)){
				newTo.setTime(from.getTime()+(ONE_DAY*maxDaysPerRequest));
			} else {
				//else we ask for the latest x-from days
				newTo.setTime(to.getTime());
			}
						
			String url = profilingServerUrl+"/users/"+username+"/visual?from="+from.getTime()+"&to="+newTo.getTime();
			log.debug("Invoking endpoint: " + url);
			VisualSensing[] visualSensingMeasurements = ewallClient.getForObject(url, new VisualSensing[0].getClass());
			visualSensingMeasurementsList.addAll(Arrays.asList(visualSensingMeasurements));
			if(newTo.compareTo(to)==0){
				break;
			}
			from.setTime(newTo.getTime());
			newTo.setTime(to.getTime());
		}

		
		log.debug("Ordering elements...");
		long now = System.currentTimeMillis();
		Collections.sort(visualSensingMeasurementsList, new Comparator<VisualSensing>() {
			  public int compare(VisualSensing m1, VisualSensing m2) {
				  return Long.compare(m1.getTimestamp(), m2.getTimestamp());
			  };
		});
		log.debug("Ordering took " + (System.currentTimeMillis() - now) + " ms.");
		return visualSensingMeasurementsList.toArray(new VisualSensing[visualSensingMeasurementsList.size()]);
	}

	public LiquefiedPetroleumGasMeasurement[] getGasLpgData(String username, Date from, Date to, int maxDaysPerRequest) {
		ArrayList<LiquefiedPetroleumGasMeasurement> gasLpgMeasurementsList = new ArrayList<LiquefiedPetroleumGasMeasurement>();
		Date newTo = new Date(to.getTime());
		while(true){
			//If more then x days, we ask for x days
			if((newTo.getTime()-from.getTime())>(ONE_DAY*maxDaysPerRequest)){
				newTo.setTime(from.getTime()+(ONE_DAY*maxDaysPerRequest));
			} else {
				//else we ask for the latest x-from days
				newTo.setTime(to.getTime());
			}
			String url = profilingServerUrl+"/users/"+username+"/environment/gases/lpg?from="+from.getTime()+"&to="+newTo.getTime();
			log.debug("Invoking endpoint: " + url);
			LiquefiedPetroleumGasMeasurement[] gasLpgMeasurements = ewallClient.getForObject(url, new LiquefiedPetroleumGasMeasurement[0].getClass());
			gasLpgMeasurementsList.addAll(Arrays.asList(gasLpgMeasurements));
			if(newTo.compareTo(to)==0){
				break;
			}
			from.setTime(newTo.getTime());
			newTo.setTime(to.getTime());
		}
	
		log.debug("Ordering elements...");
		long now = System.currentTimeMillis();
		Collections.sort(gasLpgMeasurementsList, new Comparator<LiquefiedPetroleumGasMeasurement>() {
			  public int compare(LiquefiedPetroleumGasMeasurement m1, LiquefiedPetroleumGasMeasurement m2) {
				  return Long.compare(m1.getTimestamp(), m2.getTimestamp());
			  };
		});
		log.debug("Ordering took " + (System.currentTimeMillis() - now) + " ms.");
		return gasLpgMeasurementsList.toArray(new LiquefiedPetroleumGasMeasurement[gasLpgMeasurementsList.size()]);
	}

	public NaturalGasMeasurement[] getGasNgData(String username, Date from, Date to, int maxDaysPerRequest) {
		ArrayList<NaturalGasMeasurement> gasNgMeasurementsList = new ArrayList<NaturalGasMeasurement>();
		Date newTo = new Date(to.getTime());
		while(true){
			//If more then x days, we ask for x days
			if((newTo.getTime()-from.getTime())>(ONE_DAY*maxDaysPerRequest)){
				newTo.setTime(from.getTime()+(ONE_DAY*maxDaysPerRequest));
			} else {
				//else we ask for the latest x-from days
				newTo.setTime(to.getTime());
			}
			String url = profilingServerUrl+"/users/"+username+"/environment/gases/ng?from="+from.getTime()+"&to="+newTo.getTime();
			log.debug("Invoking endpoint: " + url);
			NaturalGasMeasurement[] gasNgMeasurements = ewallClient.getForObject(url, new NaturalGasMeasurement[0].getClass());
			gasNgMeasurementsList.addAll(Arrays.asList(gasNgMeasurements));
			if(newTo.compareTo(to)==0){
				break;
			}
			from.setTime(newTo.getTime());
			newTo.setTime(to.getTime());
		}
	
		log.debug("Ordering elements...");
		long now = System.currentTimeMillis();
		Collections.sort(gasNgMeasurementsList, new Comparator<NaturalGasMeasurement>() {
			  public int compare(NaturalGasMeasurement m1, NaturalGasMeasurement m2) {
				  return Long.compare(m1.getTimestamp(), m2.getTimestamp());
			  };
		});
		log.debug("Ordering took " + (System.currentTimeMillis() - now) + " ms.");
		return gasNgMeasurementsList.toArray(new NaturalGasMeasurement[gasNgMeasurementsList.size()]);
	}

	public CarbonMonoxideMeasurement[] getGasCoData(String username, Date from, Date to, int maxDaysPerRequest) {
		ArrayList<CarbonMonoxideMeasurement> gasCoMeasurementsList = new ArrayList<CarbonMonoxideMeasurement>();
		Date newTo = new Date(to.getTime());
		while(true){
			//If more then x days, we ask for x days
			if((newTo.getTime()-from.getTime())>(ONE_DAY*maxDaysPerRequest)){
				newTo.setTime(from.getTime()+(ONE_DAY*maxDaysPerRequest));
			} else {
				//else we ask for the latest x-from days
				newTo.setTime(to.getTime());
			}
			String url = profilingServerUrl+"/users/"+username+"/environment/gases/co?from="+from.getTime()+"&to="+newTo.getTime();
			log.debug("Invoking endpoint: " + url);
			CarbonMonoxideMeasurement[] gasCoMeasurements = ewallClient.getForObject(url, new CarbonMonoxideMeasurement[0].getClass());
			gasCoMeasurementsList.addAll(Arrays.asList(gasCoMeasurements));
			if(newTo.compareTo(to)==0){
				break;
			}
			from.setTime(newTo.getTime());
			newTo.setTime(to.getTime());
		}
	
		log.debug("Ordering elements...");
		long now = System.currentTimeMillis();
		Collections.sort(gasCoMeasurementsList, new Comparator<CarbonMonoxideMeasurement>() {
			  public int compare(CarbonMonoxideMeasurement m1, CarbonMonoxideMeasurement m2) {
				  return Long.compare(m1.getTimestamp(), m2.getTimestamp());
			  };
		});
		log.debug("Ordering took " + (System.currentTimeMillis() - now) + " ms.");
		return gasCoMeasurementsList.toArray(new CarbonMonoxideMeasurement[gasCoMeasurementsList.size()]);
	}

	public BloodPressureMeasurement[] getBloodPressure(String username, DateTime from, DateTime to, Integer latestEvents) {
		String url = profilingServerUrl + "/users/{username}/vitals/bloodpressure?";
		log.debug("Requesting blood pressure for " + username + " from " + from + " to " + to + " latestevents " + latestEvents);
		if(latestEvents != null && from != null && to != null) {
			url = url+"from={from}&to={to}&latestevents={latestEvents}";
			return readMeasurements(url, BloodPressureMeasurement[].class, username, from, to, latestEvents);
		} else 
			if(latestEvents != null && (from == null || to == null)) {
				url = url+"latestevents={latestEvents}";
				return readMeasurements(url, BloodPressureMeasurement[].class, username, latestEvents);				
			} else 
				if(latestEvents == null && (from != null && to != null)) {
					url = url+"from={from}&to={to}";
					return readMeasurements(url, BloodPressureMeasurement[].class, username, from, to);				
				} else 
					return null;
	}
	
	public HeartRateMeasurement[] getHeartRate(String username, DateTime from, DateTime to, Integer latestEvents) {
		String url = profilingServerUrl + "/users/{username}/vitals/heartrate?";
		log.debug("Requesting heart rate for " + username + " from " + from + " to " + to + " latestevents " + latestEvents);
		if(latestEvents != null && from != null && to != null) {
			url = url+"from={from}&to={to}&latestevents={latestEvents}";
			return readMeasurements(url, HeartRateMeasurement[].class, username, from, to, latestEvents);
		} else 
			if(latestEvents != null && (from == null || to == null)) {
				url = url+"latestevents={latestEvents}";
				return readMeasurements(url, HeartRateMeasurement[].class, username, latestEvents);				
			} else 
				if(latestEvents == null && (from != null && to != null)) {
					url = url+"from={from}&to={to}";
					return readMeasurements(url, HeartRateMeasurement[].class, username, from, to);				
				} else 
					return null;
	}
	
	public OxygenSaturationMeasurement[] getOxygenSaturation(String username, DateTime from, DateTime to, Integer latestEvents) {
		String url = profilingServerUrl + "/users/{username}/vitals/oxygensaturation?";
		log.debug("Requesting oxygen saturation for " + username + " from " + from + " to " + to + " latestevents " + latestEvents);
		if(latestEvents != null && from != null && to != null) {
			url = url+"from={from}&to={to}&latestevents={latestEvents}";
			return readMeasurements(url, OxygenSaturationMeasurement[].class, username, from, to, latestEvents);
		} else 
			if(latestEvents != null && (from == null || to == null)) {
				url = url+"latestevents={latestEvents}";
				return readMeasurements(url, OxygenSaturationMeasurement[].class, username, latestEvents);				
			} else 
				if(latestEvents == null && (from != null && to != null)) {
					url = url+"from={from}&to={to}";
					return readMeasurements(url, OxygenSaturationMeasurement[].class, username, from, to);				
				} else 
					return null;
	}
	
	private <T> T readMeasurements(String url, Class<T> clazz, String username, DateTime from, DateTime to) {
		// The profiling server considers time intervals as strictly-between, so adapt the from and to parameters
		// to get the from-inclusive, to-exclusive semantics used by service bricks.
		return ewallClient.getForObject(url, clazz, username, from.getMillis() - 1,	to.getMillis());
	}

	private <T> T readMeasurements(String url, Class<T> clazz, String username, Integer latestEvents) {
		// The profiling server considers time intervals as strictly-between, so adapt the from and to parameters
		// to get the from-inclusive, to-exclusive semantics used by service bricks.
		return ewallClient.getForObject(url, clazz, username, latestEvents);
	}
	
	private <T> T readMeasurements(String url, Class<T> clazz, String username, DateTime from, DateTime to, Integer latestEvents) {
		// The profiling server considers time intervals as strictly-between, so adapt the from and to parameters
		// to get the from-inclusive, to-exclusive semantics used by service bricks.
		return ewallClient.getForObject(url, clazz, username, from.getMillis() - 1,	to.getMillis(), latestEvents);
	}
	
}
