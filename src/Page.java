/**
 * Class:			Page
 * Purpose:			Used to simulate one of many pages that belong to a process. The pages are used for virtual memory
 * Student Name:	Sean Crocker
 * Student Number:	3307768
 */
public class Page {
	private int instruction;			//Instruction of page
	
	/**
	 * Constructor with parameters to initialize all variables
	 * Parameter:			@param instruction the instruction stored in the page
	 */
	public Page(int instruction) {
		this.instruction = instruction;
	}

	/**
	 * Method getInstruction
	 * Purpose:			Used to return the instruction of the page
	 * Postcondition:	Returns the instruction of the page
	 * Return:			@return the page's instruction
	 */
	public int getInstruction() {
		return instruction;
	}

	/**
	 * Method setInstruction
	 * Purpose:			Used to set the value of the instruction
	 * Postcondition:	The value of the instruction for the page is set
	 * Parameters:		@param instruction the instruction
	 */
	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}
}