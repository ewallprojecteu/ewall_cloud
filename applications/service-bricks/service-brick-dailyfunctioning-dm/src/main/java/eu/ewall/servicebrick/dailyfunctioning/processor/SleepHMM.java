package eu.ewall.servicebrick.dailyfunctioning.processor;

public class SleepHMM {
	/**
	 * Number of states.
	 */
	private int Q;

	/**
	 * Dimensionality of observation vector.
	 */
	private int D;

	/**
	 * Time steps.
	 */
	private long T;

	/**
	 * Initial state probability distribution.
	 */
	private double[] initStateProb;

	/**
	 * Transition probability matrix.
	 */
	private double[][] transMat;

	/**
	 * Observation likelihoods.
	 */
	private double[][] obsLik;

	/**
	 * The best path.
	 */
	private int[] bestPath;

	/**
	 * The GMM.
	 */
	private SleepGMM gmm;

	/**
	 * Initializes a new HMM.
	 * 
	 * @param Q_
	 * @param D_
	 * @param T_
	 * @param initStateProb_
	 * @param transMat_
	 * @param mu
	 * @param sigma
	 */
	public SleepHMM(int Q_, int D_, long T_, double[] initStateProb_, double[][] transMat_, double[][] mu,
			double[][][] sigma) {
		T = T_; // The number of time steps
		D = D_; // The observations dimensionality
		Q = Q_; // The number of states
		gmm = new SleepGMM(Q_, D_, mu, sigma); // GMM
		initStateProb = new double[Q]; // Memory allocation for initial state
		// distribution
		transMat = new double[Q][Q]; // Memory allocation for transition
		// probability matrix
		obsLik = new double[Q][(int) T]; // Memory allocation for observation
		// likelihoods
		bestPath = new int[(int) T]; // Memory allocation for best path
		for (int i = 0; i < Q; i++) // For all states
		{
			initStateProb[i] = initStateProb_[i]; // Copy
			for (int j = 0; j < Q; j++) {
				transMat[i][j] = transMat_[i][j]; // Copy
			}
		}
	}

	/**
	 * @return the obsLik
	 */
	public double[][] getObsLik() {
		return obsLik;
	}

	/**
	 * @param obsLik
	 *            the obsLik to set
	 */
	public void setObsLik(double[][] obsLik) {
		this.obsLik = obsLik;
	}

	/**
	 * @return the bestPath
	 */
	public int[] getBestPath() {
		return bestPath;
	}

	/**
	 * @param bestPath
	 *            the bestPath to set
	 */
	public void setBestPath(int[] bestPath) {
		this.bestPath = bestPath;
	}

	/**
	 * @return the gmm
	 */
	public SleepGMM getGmm() {
		return gmm;
	}

	/**
	 * @param gmm
	 *            the gmm to set
	 */
	public void setGmm(SleepGMM gmm) {
		this.gmm = gmm;
	}

	/**
	 * Calculates observation likelihood.
	 * 
	 * @param t
	 * @param obs
	 */
	public void ObsLikCalc(long t, double[] obs) {
		for (int i = 0; i < Q; i++) // For all states
		{
			// Set the observation likelihood
			obsLik[i][(int) t] = gmm.GaussianProbSampleClassCond(obs, i);
		}
	}

	/**
	 * Calculates observation likelihood matrix.
	 * 
	 * @param obs
	 */
	public void ObsLikMatCalc(double[][] obs) {
		double[] o = new double[D];
		for (long t = 0; t < T; t++) // For all time steps
		{
			for (int i = 0; i < D; i++) // For all observations in a time step
			{
				o[i] = obs[i][(int) t]; // Copy
			}
			for (int i = 0; i < Q; i++) // For all states
			{
				// Set the observation likelihood
				obsLik[i][(int) t] = gmm.GaussianProbSampleClassCond(o, i);
			}
		}
	}

	/**
	 * Calculates the Viterbi path.
	 * 
	 * @param scaled
	 * @return
	 */
	public double ViterbiPath(int scaled) {
		double normFact; // Norm factor
		double max; // Maximum
		double aux; // Auxiliary
		double p; // Probability of the best path
		double logLik; // Log likelihood
		double[][] delta = new double[Q][(int) T]; // Delta
		int[][] psi = new int[Q][(int) T]; // Psi
		double[] scale = new double[(int) T]; // Scale

		normFact = 0.0; // Clear
		for (int j = 0; j < Q; j++) // Initialization (for all states at t = 0)
		{
			delta[j][0] = initStateProb[j] * obsLik[j][0]; // Delta calculation
			psi[j][0] = 0; // Arbitrary value, since there is no predecessor to
			// t=0
			normFact += delta[j][0]; // Norm factor
		}

		if (scaled != 0) // If scaled
		{
			for (int j = 0; j < Q; j++) // For all states
			{
				delta[j][0] /= normFact; // Normalize (sum to 1)
			}
			scale[0] = 1.0 / normFact; // Store
		}
		for (int t = 1; t < T; t++) // Forwards (for all time steps but t = 0)
		{
			normFact = 0.0; // Clear
			for (int j = 0; j < Q; j++) {
				max = delta[0][t - 1] * transMat[0][j]; // Reset
				psi[j][t] = 0; // Reset
				for (int i = 0; i < Q; i++) // For all states
				{
					aux = delta[i][t - 1] * transMat[i][j]; // Auxiliary
					if (aux > max) {
						max = aux; // Store the maximum
						psi[j][t] = i; // Store the index of the maximum

					}
				}
				delta[j][t] = max; // Store the maximum
				delta[j][t] = delta[j][t] * obsLik[j][t]; // Update the delta
				normFact += delta[j][t]; // Norm factor
			}
			if (scaled != 0) // If scaled
			{
				for (int i = 0; i < Q; i++) // For all states
				{
					delta[i][t] /= normFact; // Normalize (sum to 1)
				}
				scale[t] = 1.0 / normFact; // Store
			}
		}
		p = delta[0][(int) (T - 1)]; // Reset
		bestPath[(int) (T - 1)] = 0; // Reset
		for (int i = 0; i < Q; i++) // For all states at last time step
		{
			if (delta[i][(int) (T - 1)] > p) {
				p = delta[i][(int) (T - 1)]; // Maximum
				bestPath[(int) (T - 1)] = i; // Best path at last time step
			}

		}
		for (int t = (int) (T - 2); t >= 0; t--) // Backwards
		{
			bestPath[t] = psi[bestPath[t + 1]][t + 1]; // Calculate the best
			// path
		}
		if (scaled != 0) // If scaled
		{
			logLik = 0.0; // Clear
			for (int t = 0; t < T; t++) {
				logLik += Math.log(scale[t]); // Sum(log(scale))
			}
			logLik = -logLik; // Negate
		} else {
			logLik = Math.log(p); // Probability of the bestPath
		}
		return (logLik); // log Likelihood
	}

}
