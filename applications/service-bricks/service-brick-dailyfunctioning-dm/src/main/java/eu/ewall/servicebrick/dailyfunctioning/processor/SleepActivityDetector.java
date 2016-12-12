package eu.ewall.servicebrick.dailyfunctioning.processor;

import java.util.ArrayList;

public class SleepActivityDetector {

	private final int nQ = 3;
	private final int nD = 2;
	private final int nT = 5;

	private double[][] mu;
	private double[][][] sigma;
	private double[] initPr;
	private double[][] transMat;
	private double[][] obsMat;

	/**
	 * Initializes the matrices.
	 */
	private void init(double obs1, double obs2) {
		// Insert configuration logic here
		mu = new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } };
		sigma = new double[][][] { { { 1.0, 0.9 }, { 0.9, 1.0 } }, { { 1.0, 0.9 }, { 0.9, 1.0 } }, { { 1.0, 0.9 }, { 0.9, 1.0 } } };
		initPr = new double[] { 0.333, 0.333, 0.333 };
		transMat = new double[][] { { 0.333, 0.333, 0.333 }, { 0.333, 0.333, 0.333 }, { 0.333, 0.333, 0.333 } };
		obsMat = new double[][] { {obs1, 0, 0, 0, 0}, {obs2, 0, 0, 0, 0} };
	}

	private Integer[] calculateLogLik(double bedPressure, double IMA) {
		init(bedPressure, IMA);

		double logLik;
		double[][] obsMatPtr = new double[nD][nT];

		for (int i = 0; i < nD; i++) {
			for (int j = 0; j < nT; j++) {
				obsMatPtr[i][j] = obsMat[i][j];
			}
		}

		SleepHMM h = new SleepHMM(nQ, nD, nT, initPr, transMat, mu, sigma);

		h.ObsLikMatCalc(obsMatPtr); // Prior to Viterbi!!!

		logLik = h.ViterbiPath(1); // Decode

		ArrayList<Integer> results = new ArrayList<Integer>();

		int[] bestPath = h.getBestPath();
		for (int j = 0; j < nT; j++) {
			results.add(bestPath[j]); // Path
		}
		return results.toArray(new Integer[results.size()]);
	}

	public int getBestPath(double bedPressure, double IMA) {
		Integer[] result = calculateLogLik(bedPressure, IMA);
		if (result == null || result.length == 0) {

		}
		return result[result.length - 1];
	}

}
