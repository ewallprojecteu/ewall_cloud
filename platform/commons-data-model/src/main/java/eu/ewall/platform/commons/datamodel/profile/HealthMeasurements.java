package eu.ewall.platform.commons.datamodel.profile;

import java.io.Serializable;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * @author eandgrg
 */
public class HealthMeasurements implements Serializable {

  /** The weight in kilograms. */
  private double weight;

  /** The height in centimeters. */
  private double height;

  /**
   * The Constructor.
   */
  public HealthMeasurements() {

  }

  /**
   * Instantiates a new health measurements.
   *
   * @param weight 
   *          the weight in kilograms
   * @param height
   *          the height
   */
  public HealthMeasurements(double weight, double height) {
    super();
    this.weight = weight;
    this.height = height;
  }

  /**
   * Gets the weight in kilograms.
   *
   * @return the weight in kilograms
   */
  public double getWeight() {
    return weight;
  }

  /**
   * Sets the weight in kilograms.
   *
   * @param weight
   *          the new weight in kilograms
   */
  public void setWeight(double weight) {
    this.weight = weight;
  }

  /**
   * Gets the height in centimeters.
   *
   * @return the height in centimeters
   */
  public double getHeight() {
    return height;
  }

  /**
   * Sets the height in centimeters.
   *
   * @param height
   *          the new height in centimeters
   */
  public void setHeight(double height) {
    this.height = height;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {

    StringBuffer buffer = new StringBuffer();
    buffer.append("weight [kg]: ");
    buffer.append(weight);
    buffer.append("\n");
    buffer.append("height [cm]: ");
    buffer.append(height);
    buffer.append("\n");
    return buffer.toString();
  }

}