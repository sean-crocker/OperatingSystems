import java.util.Iterator;

/**
 * Class:			CPULRU
 * Purpose:			A CPU class that implements a Least Recently Used (LRU) page replacement policy. 
 * Student Name:	Sean Crocker
 * Student Number:	3307768
 * Type Parameter:	@param <T> extends Process to use process functions.
 */
public class CPULRU<T extends Process> extends CPU<T> {
	
	/**
	 * Constructor that calls the super class with the help of a parameter to initialize all variables.
	 * Parameters:			@param quantum the time quantum
	 */
	public CPULRU(int quantum) {
		super(quantum);
	}
	
	/**
	 * Method toString
	 * Purpose:			Used to convert the object into a string and return the result.
	 * Precondition:	The CPU must finish running and successfully add processes to the finished queue
	 * Postcondition:	The object is converted into a string and the result is returned.
	 * Return:			The object converted into a string
	 */
	@Override
	public String toString() {
		String results = "LRU - Fixed:\n";
		results += "PID\tProcess Name\t\tTurnaround Time\t\t# Faults\tFault Times\n";
		finishedQueue = sortQueue();
		Iterator<T> iter = finishedQueue.iterator();
		while (iter.hasNext()) 
			results += iter.next().toString() + "\n";
		return results;
	}
	
	/**
	 * Method operateReadyQueue
	 * Purpose:			Use the ready queue with the assistance of the LRU page replacement algorithm
	 * Postcondition:	If there was no space in the memory allocation, the LRU policy would run. The
	 * 					execute function will be called to execute the process.
	 */
	@Override
	protected void operateReadyQueue() {
		T process = getNext(readyQueue);
		if (readyQueue.remove(process)) {
			int instruction = process.getPages().peek().getInstruction();
			if (isFull(process) && !hasInstruction(process, instruction)) {
				Frame[] newArray = new Frame[process.getFrames().length];
				for (int i = 0; i < newArray.length - 1; i++) {
					newArray[i] = process.getFrames()[i + 1];
				}
				newArray[newArray.length - 1] = new Frame(instruction);
				process.setFrames(newArray);
			}
			else {
				for (int i = 0; i < process.getFrames().length; i++) {
					if (process.getFrames()[i] == null) {
						process.getFrames()[i] = new Frame(instruction);
						break;
					}
					else if (process.getFrames()[i].getValue() == instruction)
						break;
					else
						continue;
				}
			}
			execute(process);
		}
	}
}