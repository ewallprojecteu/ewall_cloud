package eu.ewall.platform.commons.datamodel.core.ieeesumo;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * A PhysicalQuantity is a measure of some quantifiable aspect of the modeled
 * world, such as 'the earth's diameter' (a constant length) and 'the stress in
 * a loaded deformable solid' (a measure of stress, which is a function of three
 * spatial coordinates). All PhysicalQuantities are either ConstantQuantities or
 * FunctionQuantities. Instances of ConstantQuantityMeasure are dependent on a
 * UnitOfMeasure, while instances of FunctionQuantity are Functions that map
 * instances of ConstantQuantityMeasure to other instances of ConstantQuantityMeasure (e.g.,
 * TimeDependentQuantities are FunctionQuantities). Although the name and
 * definition of PhysicalQuantity is borrowed from physics, PhysicalQuantities
 * need not be material. Aside from the dimensions of length, time, velocity,
 * etc., nonphysical dimensions such as currency are also possible. Accordingly,
 * amounts of money would be instances of PhysicalQuantity. PhysicalQuantities
 * are distinguished from Numbers by the fact that the former are associated
 * with a dimension of measurement.
 * 
 * @author eandgrg
 */
public abstract class PhysicalQuantity extends Quantity {

	/**
	 * The Constructor.
	 */
	public PhysicalQuantity() {

	}

}