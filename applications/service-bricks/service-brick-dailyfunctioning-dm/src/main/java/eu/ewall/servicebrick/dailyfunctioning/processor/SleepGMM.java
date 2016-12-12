package eu.ewall.servicebrick.dailyfunctioning.processor;

public class SleepGMM {
	/**
	 * Twice PI.
	 */
	private final double TWICE_PI = 2 * Math.PI;

	/**
	 * Parameters.
	 */
	private SleepGMMParameters[] param;

	/**
	 * Number of states.
	 */
	private int Q;

	/**
	 * Observations dimensionality.
	 */
	private int D;

	/**
	 * Initializes a new GMM.
	 * 
	 * @param Q_
	 * @param D_
	 * @param mu
	 * @param sigma
	 */
	public SleepGMM(int Q_, int D_, double[][] mu, double[][][] sigma) {
		Q = Q_; // The number of states
		D = D_; // The observations dimensionality
		param = new SleepGMMParameters[Q]; // The parameters
		for (int i = 0; i < Q; i++) {
			param[i] = new SleepGMMParameters();
			param[i].mu = new double[D]; // The means
			param[i].sigma = new double[D][]; // The covariance matrices
			param[i].sigmaInv = new double[D][]; // The inverse covariance
			// matrices
			for (int j = 0; j < D; j++) {
				param[i].sigma[j] = new double[D]; // The covariance matrices
				param[i].sigmaInv[j] = new double[D]; // The inverse covariance
				// matrices
			}
		}
		for (int i = 0; i < Q; i++) // For all states
		{
			for (int j = 0; j < D; j++) // For each observation element
			{
				param[i].mu[j] = mu[i][j]; // Copy the means
				for (int k = 0; k < D; k++) {
					// Copy the covariances
					param[i].sigma[j][k] = sigma[i][j][k];
				}
			}
			// Calculate the inverse covariance matrices
			MatInv(param[i].sigma, D, param[i].sigmaInv);
		}
	}

	/**
	 * @return the q
	 */
	public int getQ() {
		return Q;
	}

	/**
	 * @param q
	 *            the q to set
	 */
	public void setQ(int q) {
		Q = q;
	}

	/**
	 * @return the d
	 */
	public int getD() {
		return D;
	}

	/**
	 * @param d
	 *            the d to set
	 */
	public void setD(int d) {
		D = d;
	}

	/**
	 * Matrix minor.
	 * 
	 * @param src
	 * @param dest
	 * @param row
	 * @param col
	 * @param order
	 */
	private void Minor(double[][] src, double[][] dest, int row, int col, int order) {
		int colCount = 0, rowCount = 0; // Indicate which col and row is being
		// copied to dest

		for (int i = 0; i < order; i++) {
			if (i != row) {
				colCount = 0;
				for (int j = 0; j < order; j++) {
					if (j != col) // When j is not the element
					{
						dest[rowCount][colCount] = src[i][j];
						colCount++;
					}
				}
				rowCount++;
			}
		}
	}

	/**
	 * Determinant.
	 * 
	 * @param mat
	 * @param order
	 * @return
	 */
	private double Determinant(double[][] mat, int order) {
		int i;
		double det = 0.0; // The determinant
		double[][] minor; // A minor
		if (order == 1) {
			return mat[0][0]; // Stop the recursion when matrix is a single
			// element
		}
		minor = new double[order - 1][]; // Memory allocation
		for (i = 0; i < order - 1; i++) {
			minor[i] = new double[order - 1]; // Memory allocation
		}
		for (i = 0; i < order; i++) {
			Minor(mat, minor, 0, i, order); // Get minor of element (0,i)
			// Recursion
			det += (i % 2 == 1 ? -1.0 : 1.0) * mat[0][i] * Determinant(minor, order - 1);
			// det += pow(-1.0, i) * mat[0][i] * Determinant( minor, order - 1);
		}
		return det;
	}

	/**
	 * Matrix inversion.
	 * 
	 * @param A
	 * @param order
	 * @param Y
	 */
	private void MatInv(double[][] A, int order, double[][] Y) {
		double det = 1.0 / Determinant(A, order); // Determinant of A
		double[][] minor = new double[order - 1][order - 1];

		for (int j = 0; j < order; j++) {
			for (int i = 0; i < order; i++) {
				// Get the co-factor (matrix) of A(j,i)
				Minor(A, minor, j, i, order);
				Y[i][j] = det * Determinant(minor, order - 1);
				if ((i + j) % 2 == 1) {
					Y[i][j] = -Y[i][j];
				}
			}
		}
	}

	/**
	 * Multivariate Gaussian denominator.
	 * 
	 * @param sigma
	 * @return
	 */
	private double GaussianProbDen(double[][] sigma) {
		double den;
		den = Math.sqrt(Math.pow(TWICE_PI, D) * Math.abs(Determinant(sigma, D))); // Denominator
		return den;
	}

	/**
	 * Squared Mahalanobis distance
	 * 
	 * @param obs
	 * @param mu
	 * @param sigmaInv
	 * @return
	 */
	private double SqMahalDist(double[] obs, double[] mu, double[][] sigmaInv) {
		int i, j;
		double[] aux1 = new double[D]; // Auxiliary
		double[] aux2 = new double[D]; // Auxiliary
		double sqMahalDist = 0.0; // Squared Mahalanobis distance
		for (j = 0; j < D; j++) {
			aux1[j] = obs[j] - mu[j]; // Center
		}
		for (j = 0; j < D; j++) {
			aux2[j] = 0.0; // Clear
			for (i = 0; i < D; i++) {
				aux2[j] += aux1[i] * sigmaInv[i][j]; // Matrix multiply
			}
			sqMahalDist += aux2[j] * aux1[j]; // Inner product
		}

		return sqMahalDist;
	}

	/**
	 * Sample from multivariate Gaussian distribution.
	 * 
	 * @param obs
	 * @param mu
	 * @param sigma
	 * @param sigmaInv
	 * @return
	 */
	private double GaussianProbSample(double[] obs, double[] mu, double[][] sigma, double[][] sigmaInv) {
		double probSample; // Sample from the distribution
		probSample = Math.exp(-0.5 * SqMahalDist(obs, mu, sigmaInv)) / GaussianProbDen(sigma);
		return probSample;
	}

	/**
	 * Class-conditional sample from multivariate Gaussian distribution.
	 * 
	 * @param obs
	 * @param cls
	 * @return
	 */
	public double GaussianProbSampleClassCond(double[] obs, int cls) {
		return GaussianProbSample(obs, param[cls].mu, param[cls].sigma, param[cls].sigmaInv);
	}
}
