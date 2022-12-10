import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

/**
 * Class:			Schedule
 * Purpose:			Abstract class that other processors can inherit from to use different replacement algorithms.
 * Student Name:	Sean Crocker
 * Student Number:	3307768
 * Type Parameter:	@param <T> extends Process to its functions
 */
public abstract class CPU <T extends Process> {
	protected int quantum;					//The fixed time quantum used for round robin scheduling
	protected Queue<T> readyQueue;			//The ready queue of processes
	protected Queue<T> blockedQueue;		//The queue of blocked processes
	protected Queue<T> finishedQueue;		//The queue of all finished processes
	protected Queue<T> runningQueue;		//A queue for processes in main memory
	protected int currentTime;				//The current time of the simulation
	protected int processCount;				//The number of processes
	
	/**
	 * Constructor with a parameter to initialize all variables.
	 * Parameters:	@param quantum the time quantum for scheduling
	 */
	public CPU(int quantum) {
		this.quantum = quantum;
		this.processCount = 0;
		this.readyQueue = new ArrayDeque<T>();
		this.blockedQueue = new ArrayDeque<T>();
		this.finishedQueue = new ArrayDeque<T>();
		this.runningQueue = new ArrayDeque<T>();
	}
	
	public abstract String toString();					//Converts the object to a string and returns the results
	
	protected abstract void operateReadyQueue();		//Uses the ready queue and the associated replacement algorithm
	
	/**
	 * Method run
	 * Purpose:			Used to begin using the simulation with the processes received.
	 * Postcondition:	The processes all attempt to run but are all faced with page faults as the instructions in the
	 * 					memory is not yet defined.
	 * Parameters:		@param processes the queue of processes gained from the driver class
	 */
	public void run(Queue<T> processes) {
		this.processCount = processes.size();
		while (!processes.isEmpty()) {
			T process = getNext(processes);
			if (processes.remove(process))
				execute(process);
		}
		start();
	}

	/**
	 * Method sortQueue
	 * Purpose:			Used to sort the queue containing the finished processes by iterating through it and finding the
	 * 					process with the lowest identification number.
	 * Precondition:	The processes must have finished the simulation
	 * Postcondition:	The new queue is used to convert the object to a string for the toString method.
	 * Return:			@return the sorted queue
	 */
	protected Queue<T> sortQueue() {
		Queue<T> newQueue = new ArrayDeque<T>();
		while (!finishedQueue.isEmpty()) {
			T first = null;
			Iterator<T> iter = finishedQueue.iterator();
			while (iter.hasNext()) {
				T current = iter.next();
				if (first == null)
					first = current;
				else if (current.getID() < first.getID())
					first = current;
			}
			finishedQueue.remove(first);
			newQueue.add(first);
		}
		return newQueue;
	}
	
	/**
	 * Method update
	 * Purpose:			Used to update the pointer for the clock policy.
	 * Postcondition:	Updates the pointer and the use bits for the clock policy.
	 * Parameters:		@param process the process with the frames allocated to it
	 * 					@param pointer the pointer for the clock
	 * 					@param instruction the instruction for the paging
	 * Return:			@return the pointer
	 */
	protected int update(T process, int pointer, int instruction) {
		while (true) {
			if (!process.getFrames()[pointer].isUseBit()) {
				process.getFrames()[pointer] = new Frame(instruction);
				return (pointer + 1) % process.getFrames().length;
			}
			process.getFrames()[pointer].setUseBit(false);
			pointer = (pointer + 1) % process.getFrames().length;
		}
	}

	/**
	 * Method hasInstruction
	 * Purpose:			Used to determine whether the frames allocated contain the next paging instruction.
	 * Postcondition:	If the instruction is found within the frames then the function returns true
	 * Parameters:		@param process the process being tested
	 * 					@param instruction the instruction being looked for
	 * Return:			@return true if the instruction is found
	 */
	protected boolean hasInstruction(T process, int instruction) {
		for (int i = 0; i < process.getFrames().length; i++) {
			if (process.getFrames()[i].getValue() == instruction) {
				process.getFrames()[i].setUseBit(true);
				return true;
			}
		}
		return false;
	}

	/**
	 * Method start
	 * Purpose:			Used to begin the rest of the simulation after the processes were originally run by the run function.
	 * 					This function continues until all processes are added to the finished queue.
	 * Precondition:	The run function must call this function.
	 * Postcondition:	The simulation runs until completion.
	 */
	protected void start() {
		while(finishedQueue.size() < processCount) {
			currentTime++;
			operateBlockedQueue();
			if (!runningQueue.isEmpty())
				execute(runningQueue.remove());
			else if (!readyQueue.isEmpty())
				operateReadyQueue();
			else
				continue;
		}
	}
	
	/**
	 * Method isFull
	 * Purpose:			Used to determine whether or not the allocated frames to a process are completely filled with pages.
	 * Postcondition:	If there is a blank frame then the function returns false
	 * Parameters:		@param process the process being tested
	 * Return:			@return true if there is no free space in the memory allocation
	 */
	protected boolean isFull(T process) {
		for (int i = 0; i < process.getFrames().length; i++) {
			if (process.getFrames()[i] == null || process.getFrames()[i].getValue() == 0)
				return false;
		}
		return true;
	}
		
	/**
	 * Method operateBlockedQueue
	 * Purpose:			Used to operate on the queue of blocked processes to determine if any
	 * 					can be moved to the ready queue.
	 * Postcondition:	If a process within the queue has a ready time matching the current time then the
	 * 					process is transported to the ready queue.
	 */
	protected void operateBlockedQueue() {
		Iterator<T> iter = blockedQueue.iterator();
		while (iter.hasNext()) {
			T process = iter.next();
			if (currentTime == process.getReadyTime() && currentTime != 0) {
				blockedQueue.remove(process);
				readyQueue.add(process);
			}
		}
	}

	/**
	 * Method execute
	 * Purpose:			Used to execute a process and determine whether the current page will issue a page fault.
	 * Postcondition:	The function can issue a page fault, interrupt a process, finish a process, or continue
	 * 					running a process. 
	 * Parameters:		@param process the process being executed
	 */
	protected void execute(T process) {
		if (!check(process.getPages().peek().getInstruction(), process.getFrames())) {				//If page is not in memory
			process.getFaultTimes().add(currentTime);												//Add page fault
			process.setExitTime(currentTime);
			process.setReadyTime(currentTime);
			process.setRunningTime(0);
			blockedQueue.add(process);
			operateReadyQueue();
		}
		else {
			process.setRunningTime(process.getRunningTime() + 1);
			process.getPages().remove();
			if (process.getPages().isEmpty()) {
				process.setTurnAroundTime(currentTime + 1);
				finishedQueue.add(process);
			}
			else if (process.getRunningTime() >= quantum && check(process.getPages().peek().getInstruction(), process.getFrames())) {	
				process.setExitTime(currentTime);
				process.setRunningTime(0);
				readyQueue.add(process);
			}
			else
				runningQueue.add(process);
		}
	}

	/**
	 * Method getNext
	 * Purpose:			Used to get the next process to run by using round robin scheduling
	 * Postcondition:	The next process is returned
	 * Parameters:		@param processes the queue of processes
	 * Return:			@return the next process to place in main memory
	 */
	protected T getNext(Queue<T> processes) {
		Iterator<T> iterator = processes.iterator();
		T first = null;
		while (iterator.hasNext()) {
			T current = iterator.next();
			if (first == null) 
				first = current;
			else if (current.comesBefore(first)) 
				first = current;
			else
				continue;	
		}
		return first;
	}

	/**
	 * Method check
	 * Purpose:			Used to check if the page is in the main memory
	 * Postcondition:	Used to determine if the process should be issued a page fault or not
	 * Parameters:		@param instruction the instruction to find
	 *					@param frames the memory frames
	 * Return:			@return true of the instruction is found
	 */
	protected boolean check(int instruction, Frame[] frames) {
		for (int i = 0; i < frames.length; i++) {
			if(frames[i] != null && frames[i].getValue() == instruction)
				return true;
		}
		return false;
	}
}