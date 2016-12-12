package eu.ewall.platform.lr.sleepmonitor.service;

import eu.ewall.platform.idss.service.model.common.LRSleepDayParameter;
import eu.ewall.platform.lr.sleepmonitor.service.params.BedOffTimeParameter;
import eu.ewall.platform.lr.sleepmonitor.service.params.BedOnTimeParameter;
import eu.ewall.platform.lr.sleepmonitor.service.params.FrequencyWakingUpParameter;
import eu.ewall.platform.lr.sleepmonitor.service.params.SleepEfficiencyParameter;
import eu.ewall.platform.lr.sleepmonitor.service.params.TotalSleepTimeParameter;

/**
 * A sleep parameter can derive a numeric value from a list of night sleep
 * periods. The available parameters can be obtained with {@link
 * #getParameters() getParameters()}.
 * 
 * @author Dennis Hofs (RRD)
 */
public abstract class SleepParameter {
	private static Object lock = new Object();
	private static SleepParameter[] parameters = null;
	
	private String name;
	
	/**
	 * Constructs a new instance.
	 * 
	 * @param name the parameter name. This should be one of the PARAM_*
	 * constants defined in {@link LRSleepDayParameter LRSleepDayParameter}.
	 */
	protected SleepParameter(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the parameter name. This should be one of the PARAM_* constants
	 * defined in {@link LRSleepDayParameter LRSleepDayParameter}.
	 * 
	 * @return the parameter name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Calculates the value for this parameter from the specified night sleep
	 * data.
	 *
	 * @param sleepData the night sleep data
	 * @return the parameter value
	 */
	public abstract int calculate(NightSleepData sleepData);
	
	/**
	 * Returns the parameter with the specified name. This should be one of the
	 * PARAM_* constants defined in {@link LRSleepDayParameter
	 * LRSleepDayParameter}.
	 * 
	 * @param name the parameter name
	 * @return the parameter
	 */
	public static SleepParameter getParameterByName(String name) {
		SleepParameter[] params = getParameters();
		for (SleepParameter param : params) {
			if (param.getName().equals(name))
				return param;
		}
		return null;
	}
	
	/**
	 * Returns all known sleep parameters.
	 * 
	 * @return the parameters
	 */
	public static SleepParameter[] getParameters() {
		synchronized (lock) {
			if (parameters != null)
				return parameters;
			parameters = new SleepParameter[] {
					new BedOnTimeParameter(),
					new BedOffTimeParameter(),
					new TotalSleepTimeParameter(),
					new FrequencyWakingUpParameter(),
					new SleepEfficiencyParameter()
			};
			return parameters;
		}
	}
}
