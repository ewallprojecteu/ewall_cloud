package eu.ewall.platform.idss.utils;

import java.util.Iterator;

/**
 * This iterator can return the current element rather than the next element as
 * a normal iterator does. At construction it is positioned before the first
 * element. You can move to the first element with {@link #moveNext()
 * moveNext()}. Then you can access the current element as many times as needed
 * with {@link #getCurrent() getCurrent()}, and you can move to the next
 * element with {@link #moveNext() moveNext()}.
 * 
 * @author Dennis Hofs (RRD)
 *
 * @param <T> the type of elements in the iterator
 */
public class CurrentIterator<T> {
	private Iterator<T> iterator;
	private T current = null;

	/**
	 * Constructs a new instance. The iterator will be positioned before the
	 * first element.
	 * 
	 * @param it the underlying iterator
	 */
	public CurrentIterator(Iterator<T> it) {
		this.iterator = it;
	}
	
	/**
	 * Returns the current element. If the iterator is positioned before the
	 * first element or after the last element, this method returns null. An
	 * element itself may also be null. Use {@link #moveNext() moveNext()} to
	 * know the position.
	 * 
	 * @return the current element (can be null)
	 */
	public T getCurrent() {
		return current;
	}

	/**
	 * Moves to the next element. If there is no more element, this method
	 * returns false.
	 * 
	 * @return true if the iterator is at the next element, false if there is
	 * no more element
	 */
	public boolean moveNext() {
		if (!iterator.hasNext()) {
			current = null;
			return false;
		} else {
			current = iterator.next();
			return true;
		}
	}
}
