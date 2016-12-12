package eu.ewall.platform.lr.habits.math;

import eu.ewall.platform.lr.habits.math.DoubleVector;

public interface DistanceMeasurer {

  public double measureDistance(double[] set1, double[] set2);

  public double measureDistance(DoubleVector vec1, DoubleVector vec2);

}
