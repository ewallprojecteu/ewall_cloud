package eu.ewall.platform.commons.datamodel.core.ieeesumo;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * A ConstantQuantity is a PhysicalQuantity which has a constant value, e.g. 3
 * meters and 5 hours. The magnitude (see MagnitudeFn) of every ConstantQuantity
 * is a RealNumber. ConstantQuantities are distinguished from
 * FunctionQuantities, which map ConstantQuantities to other ConstantQuantities.
 * All ConstantQuantites are expressed with the BinaryFunction MeasureFn, which
 * takes a Number and a UnitOfMeasure as arguments. For example, 3 Meters can be
 * expressed as (MeasureFn 3 Meter). ConstantQuantities form a partial order
 * (see PartialOrderingRelation) with the lessThan relation, since lessThan is a
 * RelationExtendedToQuantities and lessThan is defined over the RealNumbers.
 * The lessThan relation is not a total order (see TotalOrderingRelation) over
 * the class ConstantQuantity since elements of some subclasses of
 * ConstantQuantity (such as length quantities) are incomparable to elements of
 * other subclasses of ConstantQuantity (such as mass quantities).
 * 
 * @author eandgrg
 */
public abstract class ConstantQuantity extends PhysicalQuantity {

	/**
	 * The Constructor.
	 */
	public ConstantQuantity() {

	}

}