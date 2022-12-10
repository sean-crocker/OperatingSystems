/**
 * Interface:		Comparable
 * Purpose:			Used to determine if a type comes before another.
 * Student Name:	Sean Crocker
 * Student Number:	3307768
 * Type Parameter:	@param <T> the type which is a Process.
 */
public interface Comparable<T> {

	public boolean comesBefore(T value);	//true if this < parameter
}
