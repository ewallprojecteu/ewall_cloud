package eu.ewall.servicebrick.physicalactivity.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import eu.ewall.servicebrick.common.AggregationPeriod;
import eu.ewall.servicebrick.common.validation.ServiceBrickInputValidator;
import eu.ewall.servicebrick.physicalactivity.dao.MovementDao;
import eu.ewall.servicebrick.physicalactivity.model.Movement;
import eu.ewall.servicebrick.physicalactivity.model.MovementHistory;

public class MovementControllerTest {

	private static final DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTimeParser();
	
	private ServiceBrickInputValidator mockValidator;
	private MovementDao mockDao;
	private MovementController controller;
	
	@Before
	public void setUp() {
		mockValidator = mock(ServiceBrickInputValidator.class);
		mockDao = mock(MovementDao.class);
		controller = new MovementController(mockValidator, mockDao);
	}
	
	private DateTime getDateTime(String dateTime) {
		return DateTime.parse(dateTime, dateTimeFormatter);
	}
	
	private void addMovement(List<Movement> movements, String from, String to, int steps, double km, 
			double calories) {
		Movement movement = new Movement(getDateTime(from), getDateTime(to));
		movement.setSteps(steps);
		movement.setKilometers(km);
		movement.setBurnedCalories(calories);
		movements.add(movement);
	}
	
	private void checkResult(MovementHistory expected, MovementHistory actual) {
		Assert.assertEquals(expected.getUsername(), actual.getUsername());
		Assert.assertEquals(expected.getFrom(), actual.getFrom());
		Assert.assertEquals(expected.getTo(), actual.getTo());
		Assert.assertEquals(expected.getAggregation(), actual.getAggregation());
		Assert.assertEquals(expected.getMovements(), actual.getMovements());
	}
	
	@Test
	public void testNoData() {
		// Tests when there is no data in the DB for the given range
		String userId = "user1";
		DateTime from = getDateTime("2015-01-01");
		DateTime to = getDateTime("2015-01-02");
		AggregationPeriod aggregation = new AggregationPeriod("1d");
		List<Movement> movements = new ArrayList<>(0);
		when(mockDao.readMovements(userId, from, to, aggregation,Movement.class)).thenReturn(movements);
		MovementHistory expected = new MovementHistory(
				userId, from, to, aggregation, new ArrayList<Movement>(0));
		MovementHistory actual = controller.getMovement(userId, from, to, aggregation);
		checkResult(expected, actual);
	}
	
	@Test
	public void testStraightAggregation() {
		// Tests when the aggregation length matches the length used to save aggregate data on the DB and no further
		// aggregation is required in the controller
		String userId = "user1";
		DateTime from = getDateTime("2015-01-01");
		DateTime to = getDateTime("2015-01-03");
		AggregationPeriod aggregation = new AggregationPeriod("1d");
		List<Movement> movements = new ArrayList<>(2);
		addMovement(movements, "2015-01-01", "2015-01-02", 100, 0.5, 50);
		addMovement(movements, "2015-01-02", "2015-01-03", 200, 1, 100);
		when(mockDao.readMovements(userId, from, to, aggregation,Movement.class)).thenReturn(movements);
		MovementHistory expected = new MovementHistory(userId, from, to, aggregation, movements);
		MovementHistory actual = controller.getMovement(userId, from, to, aggregation);
		checkResult(expected, actual);
	}
	
	@Test
	public void testExactAggregation() {
		// Tests when the time range is divided by the aggregation period exactly and the DB contains data
		String userId = "user1";
		DateTime from = getDateTime("2015-01-01");
		DateTime to = getDateTime("2015-01-05");
		AggregationPeriod aggregation = new AggregationPeriod("2d");
		AggregationPeriod daoAggregation = new AggregationPeriod("1d");
		List<Movement> daoMovements = new ArrayList<>(4);
		addMovement(daoMovements, "2015-01-01", "2015-01-02", 100, 0.5, 50);
		addMovement(daoMovements, "2015-01-02", "2015-01-03", 200, 1, 100);
		addMovement(daoMovements, "2015-01-03", "2015-01-04", 300, 1.5, 150);
		addMovement(daoMovements, "2015-01-04", "2015-01-05", 400, 2, 200);
		when(mockDao.readMovements(userId, from, to, daoAggregation, Movement.class)).thenReturn(daoMovements);
		List<Movement> expectedMovements = new ArrayList<>(2);
		addMovement(expectedMovements, "2015-01-01", "2015-01-03", 300, 1.5, 150);
		addMovement(expectedMovements, "2015-01-03", "2015-01-05", 700, 3.5, 350);
		MovementHistory expected = new MovementHistory(userId, from, to, aggregation, expectedMovements);
		MovementHistory actual = controller.getMovement(userId, from, to, aggregation);
		checkResult(expected, actual);
	}
	
	@Test
	public void testSparseAggregation() {
		// Tests when data in the DB does not cover the whole interval, with missing data at the beginning, in the
		// middle and at the end of the interval. The response must still include movement blocks aligned to the
		// requested aggregation.
		String userId = "user1";
		DateTime from = getDateTime("2015-01-01");
		DateTime to = getDateTime("2015-01-02");
		AggregationPeriod aggregation = new AggregationPeriod("2h");
		AggregationPeriod daoAggregation = new AggregationPeriod("1h");
		List<Movement> daoMovements = new ArrayList<>(4);
		addMovement(daoMovements, "2015-01-01T05:00", "2015-01-01T06:00", 100, 0.5, 50);
		addMovement(daoMovements, "2015-01-01T06:00", "2015-01-01T07:00", 200, 1, 100);
		addMovement(daoMovements, "2015-01-01T07:00", "2015-01-01T08:00", 300, 1.5, 150);
		addMovement(daoMovements, "2015-01-01T10:00", "2015-01-01T11:00", 400, 2, 200);
		when(mockDao.readMovements(userId, from, to, daoAggregation,Movement.class)).thenReturn(daoMovements);
		List<Movement> expectedMovements = new ArrayList<>(3);
		addMovement(expectedMovements, "2015-01-01T04:00", "2015-01-01T06:00", 100, 0.5, 50);
		addMovement(expectedMovements, "2015-01-01T06:00", "2015-01-01T08:00", 500, 2.5, 250);
		addMovement(expectedMovements, "2015-01-01T10:00", "2015-01-01T12:00", 400, 2, 200);
		MovementHistory expected = new MovementHistory(userId, from, to, aggregation, expectedMovements);
		MovementHistory actual = controller.getMovement(userId, from, to, aggregation);
		checkResult(expected, actual);
	}
	
}
