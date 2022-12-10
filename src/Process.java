import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Class:			Process
 * Purpose:			A class to act as a process that is used in the processor with the assistance of a round robin
 * 					scheduling algorithm. Implements the interface comparable to compare two processes. The process
 * 					makes use of paging.
 * Student Name:	Sean Crocker
 * Student Number:	3307768
 */
public class Process implements Comparable<Process> {
	private int ID;							//The identification of the process
	private final int maxPages = 50;		//The maximum amount of pages a process can have
	private final int swapTime = 6;			//The time it takes to swap in a page
	private String name;					//The name of the process
	private int turnAroundTime;				//The turn around time of the process
	private Frame[] frames;					//Frames of Memory Allocated to this process
	private Queue<Page> pages;				//The pages the process
	private ArrayList<Integer> faultTimes;	//A collection of the times a page fault has occurred
	private int exitTime;					//The time the process was interrupted
	private int readyTime;					//The time the process will be ready
	private int runningTime;				//The amount of the time the process spends running without an interrupt
		
	/**
	 * Constructor with parameters to initialize all variables. The ready, exit, and turn around
	 * time are all set to zero while the remaining values are set with the parameters and the collections
	 * created
	 * Parameters:	@param ID the ID of the process
	 * 				@param name the name of the process
	 * 				@param frames the amount of the frames
	 */
	public Process(int id, String name, int frames) {
		this.ID = id;
		this.name = name;
		this.turnAroundTime = 0;
		this.frames = new Frame[frames];
		this.pages = new ArrayDeque<Page>();
		this.faultTimes = new ArrayList<Integer>();
		this.exitTime = 0;
		this.readyTime = 0;
	}

	/**
	 * Method getID
	 * Purpose:			Used to return the ID of the process.
	 * Postcondition:	Returns the ID.
	 * Return:			@return the ID
	 */
	public int getID() {
		return ID;
	}

	/**
	 * Method setID
	 * Purpose:			Used to set the ID of the process.
	 * Postcondition:	The ID of the process is set with the value given.
	 * Parameters:		@param iD the value used to set the ID
	 */
	public void setID(int iD) {
		ID = iD;
	}

	/**
	 * Method getTurnAroundTime
	 * Purpose:			Used to return the turn around time of the process.
	 * Postcondition:	Returns the turn around time.
	 * Return:			@return the turn around time
	 */
	public int getTurnAroundTime() {
		return turnAroundTime;
	}

	/**
	 * Method setTurnAroundTime
	 * Purpose:			Used to set the turn around time the process.
	 * Postcondition:	The turn around time of the process is set with the value given.
	 * Parameters:		@param trTime the value used to set the turn around time time
	 */
	public void setTurnAroundTime(int trTime) {
		this.turnAroundTime = trTime;
	}
	
	/**
	 * Method getPages
	 * Purpose:			Used to retrieve the pages owned by the process.
	 * Precondition:	Page objects must be added to a queue in order to use it.
	 * Postcondition:	The queue containing pages are returned
	 * Return:			@return the queue of pages
	 */
	public Queue<Page> getPages() {
		return pages;
	}

	/**
	 * Method setPages
	 * Purpose:			Used to set the pages for the process
	 * Postcondition:	The process has a queue of pages
	 * Parameters:		@param pages the queue of pages
	 */
	public void setPages(Queue<Page> pages) {
		this.pages = pages;
	}

	/**
	 * Method getName
	 * Purpose:			Used to return the name of the process
	 * Postcondition:	The name of the process is returned
	 * Return:			@return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method getFaultTimes
	 * Purpose:			Used to return the list of page faults
	 * Postcondition:	The list of times page faults occur are returned.
	 * Return:			@return list of page faults
	 */
	public ArrayList<Integer> getFaultTimes() {
		return faultTimes;
	}

	/**
	 * Method getReadyTime
	 * Purpose:			Used to return the time the process will next be ready
	 * Postcondition:	The time is returned and validated to determine whether the process can run
	 * Return:			@return ready time
	 */
	public int getReadyTime() {
		return readyTime;
	}

	/**
	 * Method setReadyTime
	 * Purpose:			Used to set the time the process will next be ready
	 * Postcondition:	The time has the time it takes to swap in a process added onto it and the time is set.
	 * Parameters:		@param readyTime the readyTime
	 */
	public void setReadyTime(int readyTime) {
		this.readyTime = readyTime + swapTime;
	}

	/**
	 * Method getFrames
	 * Purpose:			Used to return the array of frames that the process is allocated in the memory.
	 * Postcondition:	The collection of allocated memory frames are returned 
	 * Return:			@return frames
	 */
	public Frame[] getFrames() {
		return frames;
	}

	/**
	 * Method setFrames
	 * Purpose:			Used to set the array of frames that the process is allocated in the main memory.
	 * Postcondition:	The collection of frames are set	
	 * Parameters:		@param frames the frames
	 */
	public void setFrames(Frame[] frames) {
		this.frames = frames;
	}
	
	/**
	 * Method setExitTime
	 * Purpose:			Used to set the time the process exits the main memory.
	 * Postcondition:	The time is used for the round robin scheduling
	 * Parameters:		@param exit the exit time
	 */
	public void setExitTime(int exit) {
		this.exitTime = exit;
	}

	/**
	 * Method getRunningTime
	 * Purpose:			Used to return amount of time the process has been running for.
	 * Postcondition:	The value is used to determine if the process should be interrupted by the time quantum
	 * Return:			@return the running time
	 */
	public int getRunningTime() {
		return runningTime;
	}

	/**
	 * Method setRunningTime
	 * Purpose:			Used to set the amount of time the process has been running for.
	 * Postcondition:	The running time of the process is set
	 * Parameters:		@param runningTime the running time
	 */
	public void setRunningTime(int runningTime) {
		this.runningTime = runningTime;
	}

	/**
	 * Method toString
	 * Purpose:			Used to convert the process to a string and return the result, detailing
	 * 					the ID, turn around time, and waiting time.
	 * Postcondition:	Returns the process converted to a string.
	 * Return:			@return the process converted to a string
	 */
	public String toString() {
		String results = "{";
		for (int i = 0; i < faultTimes.size() - 1; i++)
			results += faultTimes.get(i)+ ", ";
		results += faultTimes.get(faultTimes.size() - 1)+"}";
		return ID+"\t"+name+"\t\t"+turnAroundTime+"\t\t\t"+faultTimes.size()+"\t\t"+results;
	}

	/**
	 * Method addPage
	 * Purpose:			Used to add a page into the collection of pages used by the process.
	 * Postcondition:	The collection has another page added to it
	 * Parameter:		@param page the page to add
	 */
	public void addPage(Page page) {
		if (pages.size() < maxPages) {
			pages.add(page);
		}
	}

	/**
	 * Method comesBefore
	 * Purpose:			Used to determine if another process should be executed before this one.
	 * Precondition:	The process being compared must exist.
	 * Postcondition:	If the process comes before this, it returns false. Else, it returns true.
	 * 					If true, this process will compared with others. If true for all processes,
	 * 					this process will be fetched from the queue by the dispatch and executed.
	 * Parameters:		@param value the process being compared
	 * Return:			@return true if this comes before the process being compared.
	 */
	@Override
	public boolean comesBefore(Process value) {
		if (exitTime != 0 && value.readyTime <= exitTime)
			return false;
		else {
			if (readyTime < value.readyTime)			//If this process was ready before the other
				return true;
			else if (readyTime > value.readyTime)		//If the other process was ready before this
				return false;
			else {										//If they were ready at the same time, compare their IDs
				if (ID < value.ID)
					return true;
				else
					return false;
			}
		}
	}
}