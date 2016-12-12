package eu.ewall.servicebrick.dailyfunctioning.processor;

import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DailyFunctioningDetector {
	private static final Logger log = LoggerFactory.getLogger(DailyFunctioningDetector.class);
	
	@Autowired
	private RoomsDao roomsDao;
	
	private boolean roomEvidence[];
	private double roomWeights[];
	private boolean transitions[][] = {
			{true, true, true, true, true},
			{true, true, true, true, true},
			{true, true, true, true, true},
			{true, true, true, true, true},
			{true, true, true, true, true}};
	private double tempAvg[], tempStd[], tempSq[], humidityAvg[], humidityStd[], humiditySq[];
	private boolean tempLargeVariation[], humidityLargeVariation[];
	private double[] pirAvg;
	private double activityWeights[];
	private double dw;
	private int frontalDoorState;
	private int countForDoor;
	private int reasonedRoom, reasonedActivity;
	private String userName;

	// 8+1 activities:
	private String[] activityNames = {"cooking", "sleeping", "showering", "entertaining", "sanitary_visits", "houseworks", "getting_out", "visits", "undefined"};
	// 4+1 rooms (or locations)
	private String[] roomNames = {"bedroom", "livingroom", "kitchen", "bathroom", "undefined"};
	private int notValidCounter;
	
	//@Autowired
	//private OutputItemSimulator outputItemSimulator;
	
	/**
	 * This method detects the "daily functioning activity", the "in bed/out of bed" status,
	 * the "location" (room) related to the user in a specific moment in time, based on the situation 
	 * described by the sensed data  
	 * @param sensorsSnapshot the latest events detected by the sensors in the home. Every event is 
	 * related to a room.
	 * @return A data structure containing information on "daily functioning activity", "in bed/out of bed" 
	 * status, "location" for the user at a specific timestamp 
	 * 
	 */
	
	public DailyFunctioningDetector(String userName) {
		final int ROOM_NUMBER = roomNames.length;
		final int ACTIVITY_NUMBER = activityNames.length;
		
		this.userName = userName;
		pirAvg = new double [ROOM_NUMBER];
		roomEvidence = new boolean [ROOM_NUMBER];
		roomWeights = new double [ROOM_NUMBER];
		tempAvg = new double [ROOM_NUMBER];
		tempStd =  new double [ROOM_NUMBER];
		tempSq=  new double [ROOM_NUMBER];
		humidityAvg= new double [ROOM_NUMBER]; 
		humidityStd= new double [ROOM_NUMBER];
		humiditySq= new double [ROOM_NUMBER];
		tempLargeVariation = new boolean [ROOM_NUMBER];
		humidityLargeVariation = new boolean [ROOM_NUMBER];
		activityWeights= new double [ACTIVITY_NUMBER];
		
		dw = 0.3;
		frontalDoorState = 0;
		for (int i = 0; i < ROOM_NUMBER; i++) {
			tempStd[i] = -1;
			humidityStd[i] = -1;
			tempAvg[i] = 0;
			humidityAvg[i] = 0;
			tempSq [i]=0;
			humiditySq[i]=0;
			roomEvidence[i] = false;
			roomWeights[i]= 0;
			pirAvg[i] = 0;
			humidityLargeVariation[i]=false;
			tempLargeVariation[i]=false;
		}
		for (int i = 0; i < ACTIVITY_NUMBER; i++)
			activityWeights[i]=0;
		reasonedRoom = ROOM_NUMBER - 1;
		notValidCounter = 0;
		reasonedActivity = ACTIVITY_NUMBER - 1;
		log.info("    Daily Functioning Detector initialised for user " + this.userName);
	}

	
	public DailyFunctioningDetector() {
		final int ROOM_NUMBER = roomNames.length;
		final int ACTIVITY_NUMBER = activityNames.length;
		
		pirAvg = new double [ROOM_NUMBER];
		roomEvidence = new boolean [ROOM_NUMBER];
		roomWeights = new double [ROOM_NUMBER];
		tempAvg = new double [ROOM_NUMBER];
		tempStd =  new double [ROOM_NUMBER];
		tempSq=  new double [ROOM_NUMBER];
		humidityAvg= new double [ROOM_NUMBER]; 
		humidityStd= new double [ROOM_NUMBER];
		humiditySq= new double [ROOM_NUMBER];
		tempLargeVariation = new boolean [ROOM_NUMBER];
		humidityLargeVariation = new boolean [ROOM_NUMBER];
		activityWeights= new double [ACTIVITY_NUMBER];
		
		dw = 0.3;
		frontalDoorState = 0;
		for (int i = 0; i < ROOM_NUMBER; i++) {
			tempStd[i] = -1;
			humidityStd[i] = -1;
			tempAvg[i] = 0;
			humidityAvg[i] = 0;
			tempSq [i]=0;
			humiditySq[i]=0;
			roomEvidence[i] = false;
			roomWeights[i]= 0;
			pirAvg[i] = 0;
			humidityLargeVariation[i]=false;
			tempLargeVariation[i]=false;
		}
		for (int i = 0; i < ACTIVITY_NUMBER; i++)
			activityWeights[i]=0;
		reasonedRoom = 0;
		notValidCounter = 0;
		reasonedActivity = 8;
		log.info("*** Daily Functioning Detector initialised ***");
	}


	public String getUserName() {
		return userName;
	}
	
	private RoomSensing getRoomSensingByName(String name, List<RoomSensing> rooms) {
		for(RoomSensing room : rooms) {
			if(room.getRoomName().equalsIgnoreCase(name)) {
				return room;
			}
		}
		return null;
	}
	
	// List<RoomSensing> rooms has the measurements from all sensors in the actual home rooms, not the additional "outside" one
	public void reasonTempHumidity(List<RoomSensing> rooms) {
		for (int i=0; i<rooms.size(); i++) {
			RoomSensing room = getRoomSensingByName(roomNames[i], rooms);
			if(room == null) {
				log.error("Missing RoomSensing item for room named " + roomNames[i]);
				continue;
			}
			double temp = room.getTemp();
			double humidity = room.getHumidity();
			tempLargeVariation[i] = false;
			humidityLargeVariation[i] = false;
			if (tempStd[i] == -1) {
				// Initialisation
				tempAvg[i] = temp;
				tempSq[i] = tempAvg[i]*tempAvg[i];
				tempStd[i] = .5;
			}
			else if (Math.abs(tempAvg[i]-temp) < 3*tempStd[i]) {
				// Acceptable variation from model
				tempAvg[i] = 0.99*tempAvg[i] + 0.01*temp;
				tempSq[i] = 0.99*tempSq[i] + 0.01*temp*temp;
				tempStd[i] = Math.sqrt(tempSq[i] - tempAvg[i]*tempAvg[i]);
				if (tempStd[i]<0.25)
					tempStd[i] = 0.25;
			}
			else {
				// Unexpected variation from model -> caused by some activity
				tempLargeVariation[i] = true;
			}
			if (humidityStd[i] == -1) {
				// Initialisation
				humidityAvg[i] = humidity;
				humiditySq[i] = humidityAvg[i]*humidityAvg[i];
				humidityStd[i] = 2.5;
			}
			else if (Math.abs(humidityAvg[i]-humidity) < 3*humidityStd[i]) {
				// Acceptable variation from model
				humidityAvg[i] = 0.99*humidityAvg[i] + 0.01*humidity;
				humiditySq[i] = 0.99*humiditySq[i] + 0.01*humidity*humidity;
				humidityStd[i] = Math.sqrt(humiditySq[i] - humidityAvg[i]*humidityAvg[i]);
				if (humidityStd[i]<1.25)
					humidityStd[i] = 1.25;
			}
			else {
				// Unexpected variation from model -> caused by some activity
				humidityLargeVariation[i] = true;
			}
		}		
	}
	
	public void recentPIR(List<RoomSensing> rooms, double alpha) {
		// Update PIR averages
		for (int i=0; i<rooms.size(); i++) {
			RoomSensing room = getRoomSensingByName(roomNames[i], rooms);
			if(room == null) {
				log.error("Missing RoomSensing item for room named " + roomNames[i]);
				continue;
			}
			if (room.isPresence())
				pirAvg[i] = alpha*pirAvg[i] + (1-alpha);
			else
				pirAvg[i] = alpha*pirAvg[i];
		}
	}

	public void reasonRoom(List<RoomSensing> rooms) {
		boolean anyPIR = false;
		
		// roomEvidence from PIR
		for (int i=0; i<rooms.size(); i++) {
			roomEvidence[i] = rooms.get(i).isPresence() | rooms.get(i).isBedPressure() | rooms.get(i).getFaceNo()>0;
			RoomSensing room = getRoomSensingByName(roomNames[i], rooms);
			if(room == null) {
				log.error("Missing RoomSensing item for room named " + roomNames[i]);
				return;
			}
			if (room.isPresence())
				anyPIR = true;
		}
		// Frontal door state:
		// 0 -> user inside
		// 1 -> user inside, door open
		// 2 -> door closed, probably user outside
		// 3 -> door opened again, probably user returning inside
		if (frontalDoorState == 0 && rooms.get(1).isDoorOpen())
			frontalDoorState = 1;
		if (frontalDoorState == 1 && rooms.get(1).isDoorOpen() == false) {
			frontalDoorState = 2;
			countForDoor = 0;
		}
		if (frontalDoorState == 2 && rooms.get(1).isDoorOpen())
			frontalDoorState = 3;
		if (frontalDoorState == 3 && rooms.get(1).isDoorOpen() == false)
			frontalDoorState = 0;
		// If 3 consecutive PIRs while frontal door at state 2, then assume false alarm and correct to state 0
		if (frontalDoorState == 2) {
			if (anyPIR == false) {
				roomEvidence[rooms.size()] = true;
				countForDoor--;
			}
			else {
				roomEvidence[rooms.size()] = false;
				countForDoor++;
			}
			if (countForDoor == 3) {
				countForDoor = 0;
				frontalDoorState = 0;
			}
		}
		else
			roomEvidence[rooms.size()] = false;
		// Adjust room presence roomWeights based on roomEvidence
		boolean anyroomEvidence = false;
		for (int i=0; i<=rooms.size(); i++) {
			// Is there roomEvidence for i-th room?
			if (roomEvidence[i] == false)
				continue;
			anyroomEvidence = true;
			// Is i-th room a valid transition?
			boolean valid = transitions[reasonedRoom][i];
			if (valid) {
				roomWeights[i] += dw;
				notValidCounter = 0;
			}
			else if (notValidCounter>4)
				roomWeights[i] += dw;
			else {
				roomWeights[i] += dw/10;
				notValidCounter++;
			}
		}
		// No roomEvidence
		if (anyroomEvidence == false) {
			for (int i=0; i<=rooms.size(); i++) {
				// Is i-th room a valid transition?
				boolean valid = transitions[reasonedRoom][i];
				if ((valid ) && i == reasonedRoom)
					roomWeights[i] += dw/6;
				if (valid && i != reasonedRoom)
					roomWeights[i] += dw/9;
			}
		}
		// Find reasoned room by maximum weight and normalise roomWeights
		double maxWeight = 0, maxWeight2 = 0, sumWeight = 0;
		for (int i=0; i<=rooms.size(); i++) {
			sumWeight += roomWeights[i];
			if (roomWeights[i] > maxWeight) {
				maxWeight2 = maxWeight;
				maxWeight = roomWeights[i];
				if (maxWeight > 1.2 * maxWeight2)
					reasonedRoom = i;
				else
					reasonedRoom = rooms.size();
			}
		}
		for (int i=0; i<=rooms.size(); i++)
			roomWeights[i] /= sumWeight;
	}

	public void reasonActivities(List<RoomSensing> rooms) {
		// 8 activities + unspecified:
		// Cooking: Kitchen presence, high humidity, high temperature
		try{ 
			if (reasonedRoom == 2 && tempLargeVariation[2] && humidityLargeVariation[2])
				activityWeights[0] += dw;
			// Visited: Living room presence, multiple faces
			else if (reasonedRoom == 1 && getRoomSensingByName(roomNames[1], rooms).getFaceNo() > 1)
				activityWeights[7] += dw;
			// Sleeping: Bedroom presence, bed pressure, bedroom TV not on
			else if (false && reasonedRoom == 0 && getRoomSensingByName(roomNames[0], rooms).isBedPressure() && getRoomSensingByName(roomNames[0], rooms).getTvPower() <= 5)
				activityWeights[1] += dw;
			// Sleeping: Living room presence, sofa pressure, living room TV not on
			else if (false && reasonedRoom == 1 && getRoomSensingByName(roomNames[1], rooms).isBedPressure() && getRoomSensingByName(roomNames[1], rooms).getTvPower() <= 5)
				activityWeights[1] += dw;
			// Showering: Bathroom presence, high humidity, high temperature
			else if (reasonedRoom == 3 && tempLargeVariation[3] && humidityLargeVariation[3])
				activityWeights[2] += dw;
			// Entertaining: Living room presence, sofa pressure, up to one face visible, living room TV on
			else if (false && reasonedRoom == 1 && getRoomSensingByName(roomNames[1], rooms).isBedPressure() && getRoomSensingByName(roomNames[1], rooms).getFaceNo() < 2 && getRoomSensingByName(roomNames[1], rooms).getTvPower() > 5)
				activityWeights[3] += dw;
			// Entertaining: Bedroom presence, bed pressure, bedroom TV on
			else if (false && reasonedRoom == 0 && getRoomSensingByName(roomNames[0], rooms).isBedPressure() && getRoomSensingByName(roomNames[0], rooms).getTvPower() > 5)
				activityWeights[3] += dw;
			// Sanitary: Bathroom presence, normal humidity, normal temperature, PIR recently mostly inactive
			else if (reasonedRoom == 3 && !tempLargeVariation[3] && !humidityLargeVariation[3] && pirAvg[3] < 0.5)
				activityWeights[4] += dw;
			// Housework: Presence in home, PIR recently mostly active, not any other activity
			else if ((reasonedRoom == 0 && pirAvg[0] > 0.8) || (reasonedRoom == 1 && pirAvg[1] > 0.8) || (reasonedRoom == 2 && pirAvg[2] > 0.8) || (reasonedRoom == 3 && pirAvg[3] > 0.8))
				activityWeights[5] += dw;
			// Outdoors: Presence outdoors
			else if (false && reasonedRoom == 4)
				activityWeights[6] += dw;
			// Unspecified: Add 1/3 of the weight
			else
				activityWeights[8] += dw/3;
			// Certain activities are tied to a room. If user is not present there, zero the weights
			if (reasonedRoom != 5) {
				if (reasonedRoom > 1) {
					// Neither in bedroom, nor in living room -> not sleeping, neither entertaining
					activityWeights[1] = 0;
					activityWeights[3] = 0;
				}
				if (reasonedRoom != 1) {
					// Not in living room -> not visited
					activityWeights[7] = 0;
				}
				if (reasonedRoom != 2) {
					// Not in kitchen -> not cooking
					activityWeights[0] = 0;
				}
				if (reasonedRoom != 3) {
					// Not in bathroom -> neither shower, not sanitary visits
					activityWeights[2] = 0;
					activityWeights[4] = 0;
				}
				if (reasonedRoom != 4) {
					// Not out of home -> not outdoors
					activityWeights[6] = 0;
				}
			}
			double maxWeight = 0, sumWeight = 0;
			for (int i=0; i<activityWeights.length; i++) {
				sumWeight += activityWeights[i];
				if (activityWeights[i] > maxWeight) {
					maxWeight = activityWeights[i];
					reasonedActivity = i;
				}
			}
			for (int i=0; i<activityWeights.length; i++)
				activityWeights[i] /= sumWeight;
		} catch (Exception e) {
			log.error("Exception while detecting activities", e);
		}
		
	}

	public AlgoOutputItem prepareOutput(DateTime t) {
		AlgoOutputItem output = new AlgoOutputItem();
		output.setActivity(activityNames[reasonedActivity]);
		output.setInBed(reasonedActivity == 1);
		output.setLocation(roomNames[reasonedRoom]);
		output.setTimestamp(t);
		return output;
	}

	public void writeOutput(DateTime t) {
		Rooms rooms1 = new Rooms();
		rooms1.setTimestamp(t);
		rooms1.setUsername(userName);
		rooms1.setTempStd(tempStd);
		rooms1.setHumidityStd(humidityStd);
		rooms1.setTempAvg(tempAvg);
		rooms1.setHumidityAvg(humidityAvg);
		rooms1.setTempSq(tempSq);
		rooms1.setHumiditySq(humiditySq);
		rooms1.setRoomEvidence(roomEvidence);
		rooms1.setRoomWeights(roomWeights);
		rooms1.setPirAvg(pirAvg);
		rooms1.setHumidityLargeVariation(humidityLargeVariation);
		rooms1.setTempLargeVariation(tempLargeVariation);
		rooms1.setResonedActivity(reasonedActivity);
		rooms1.setResonedRoom(reasonedRoom);
		rooms1.setFrontalDoorState(frontalDoorState);
		roomsDao.insertEvent(rooms1);
	}

	public void report() {
		log.info("    " + activityNames[reasonedActivity] + " in " + roomNames[reasonedRoom]);
	}
}
