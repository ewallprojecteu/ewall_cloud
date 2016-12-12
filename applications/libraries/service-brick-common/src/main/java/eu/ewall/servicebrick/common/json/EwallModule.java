package eu.ewall.servicebrick.common.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.PackageVersion;

import eu.ewall.servicebrick.common.AggregationPeriod;

/**
 * Jackson module for handling serialization and deserialization 
 * 
 * @author milani
 *
 */
public class EwallModule extends SimpleModule {

	private static final long serialVersionUID = 1L;
	
	public EwallModule() {
		super(PackageVersion.VERSION);
		addSerializer(AggregationPeriod.class, new AggregationPeriodSerializer());
	}
	
}
