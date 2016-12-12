package eu.ewall.servicebrick.dailyfunctioning.processor;

/**
 * Parameters class for GMM.
 * 
 * @author yasvisu
 */
public class SleepGMMParameters {
	/**
	 * Mean
	 */
	double[] mu;

	/**
	 * Covariance matrix.
	 */
	double[][] sigma;

	/**
	 * Inverse covariance matrix.
	 */
	double[][] sigmaInv;
}
