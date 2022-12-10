/**
 * Class:			Frame
 * Purpose:			A class used to simulate the frames in the main memory.
 * Student Name:	Sean Crocker
 * Student Number:	3307768
 */
public class Frame {
	private int value;				//The instruction associated with the frame
	private boolean useBit;			//Whether the frame is a use bit

	/**
	 * Constructor with a parameter to initialize all variables
	 * @param value
	 */
	public Frame(int value) {
		this.value = value;
		this.useBit = false;
	}
	
	/**
	 * Method getValue
	 * Purpose:			Used to return the value associated with the frame
	 * Postcondition:	Returns the value of the frame
	 * Return:			@return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Method setValue
	 * Purpose:			Used to set the value associated with a frame.
	 * Postcondition:	The frame's value is set
	 * Parameters:		@param value the value of the frame
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * Method isUseBit
	 * Purpose:			Used to determine if the frame has a use bit. This is used for the clock policy
	 * Postcondition:	Returns true if the frame has a use bit
	 * Return:			@return true if use bit exists
	 */
	public boolean isUseBit() {
		return useBit;
	}

	/**
	 * Method setUseBit
	 * Purpose:			Used to set whether or not the frame has a use bit. This is used for the clock policy
	 * Postcondition:	Sets the value to try if the use bit is used to update the frame
	 * Parameters:		@param useBit the value of the useBit
	 */
	public void setUseBit(boolean useBit) {
		this.useBit = useBit;
	}
}