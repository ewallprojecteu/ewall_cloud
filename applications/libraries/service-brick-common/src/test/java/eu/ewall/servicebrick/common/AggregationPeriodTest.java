package eu.ewall.servicebrick.common;

import org.junit.Assert;
import org.junit.Test;

import eu.ewall.servicebrick.common.AggregationPeriod;
import eu.ewall.servicebrick.common.AggregationUnit;

public class AggregationPeriodTest {


	@Test
	public void testNull() {
		try {
			new AggregationPeriod(null);
			Assert.fail("An IllegalArgumentException was expected");
		} catch (IllegalArgumentException e) {}
	}
	
	@Test
	public void testEmpty() {
		try {
			new AggregationPeriod("");
			Assert.fail("An IllegalArgumentException was expected");
		} catch (IllegalArgumentException e) {}
	}
	
	@Test
	public void testMissingValue() {
		try {
			new AggregationPeriod("mo");
			Assert.fail("An IllegalArgumentException was expected");
		} catch (IllegalArgumentException e) {}
	}
	
	@Test
	public void testMissingUnit() {
		try {
			new AggregationPeriod("1");
			Assert.fail("An IllegalArgumentException was expected");
		} catch (IllegalArgumentException e) {}
	}
	
	@Test
	public void testHours() {
		AggregationPeriod splitWidth = new AggregationPeriod("1h");
		Assert.assertEquals(1, splitWidth.getLength());
		Assert.assertEquals(AggregationUnit.HOUR, splitWidth.getUnit());
	}
	
	@Test
	public void testDays() {
		AggregationPeriod splitWidth = new AggregationPeriod("2d");
		Assert.assertEquals(2, splitWidth.getLength());
		Assert.assertEquals(AggregationUnit.DAY, splitWidth.getUnit());
	}
	
	@Test
	public void testMonths() {
		AggregationPeriod splitWidth = new AggregationPeriod("10mo");
		Assert.assertEquals(10, splitWidth.getLength());
		Assert.assertEquals(AggregationUnit.MONTH, splitWidth.getUnit());
	}
	
}
