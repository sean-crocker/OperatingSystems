import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Class:			A#
 * Purpose:			Main driver class to read the arguments and files given and run the algorithms.
 * Student Name:	Sean Crocker
 * Student Number:	3307768
 */
public class A3 {

	/**
	 * Method main
	 * Purpose:			Main method that runs when the program begins.
	 * Postcondition:	Runs the method readArgs to read the arguments.
	 * @param args the arguments entered in the command window.
	 */
	public static void main(String[] args) {
		A3 main = new A3();
		main.readArgs(args);
	}
	
	/**
	 * Method readArgs
	 * Purpose:			Used to read the arguments entered to gather the text files and other relevant information.
	 * Postcondition:	Creates the algorithms and their associated data such as processes.
	 * @param args arguments entered by the user defining the data from the text file.
	 */
	private void readArgs(String[] args) {
		if (args.length < 3)
			throw new IllegalArgumentException("Invalid arguements. The number of frames, a time quantum and at least one process must be defined.");
		int processFrames = (Integer.parseInt(args[0])) / (args.length - 2);		//Number of frames allocated to each process
		int quantum = Integer.parseInt(args[1]);									//The time quantum for the round robin scheduling
		Queue<Process> queue = new ArrayDeque<Process>();
		Queue<Process> queue2 = new ArrayDeque<Process>();
		for (int i = 2; i < args.length; i++) {
			queue.add(readFile(args[i], processFrames));
			queue2.add(readFile(args[i], processFrames));
		}
		CPU<Process> cpuLRU = new CPULRU<Process>(quantum);
		CPU<Process> cpuClock = new CPUClock<Process>(quantum);
		cpuLRU.run(queue);
		cpuClock.run(queue2);
		print(cpuLRU, cpuClock);
	}
	
	/**
	 * Method print
	 * Purpose:			Used to output the results to the console
	 * Postcondition:	The results for both replacement algorithms are displayed
	 * @param cpuLRU the CPU using the LRU algorithm
	 * @param cpuClock the CPU using the clock algorithm
	 */
	private void print(CPU<Process> cpuLRU, CPU<Process> cpuClock) {
		System.out.println(cpuLRU.toString());
		System.out.println("-----------------------------------------------------------------------------------\n");
		System.out.println(cpuClock.toString());
	}

	/**
	 * Method readFile
	 * Purpose:			Used to read a file stated in an the argument. The information is used to form pages for a process.
	 * Postcondition:	Pages are added and to a new process
	 * @param fileName the name of the file
	 * @param frames the amount of frames allocated to a process
	 * @return the process created
	 */
	private Process readFile(String fileName, int frames) {
		int id = Integer.parseInt(fileName.replaceAll("\\D", ""));
		Process process = new Process(id, fileName, frames);
		try {
			File file = new File(fileName);
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			
			String line;
			while(!(line = br.readLine()).equalsIgnoreCase("end")) {
				if (line.matches(".*\\d.*")) {
					Page page = new Page(Integer.parseInt(line));
					process.addPage(page);
				}
			}			
			br.close();
		}
		catch (IOException e) {
			System.out.println(e);
		}
		return process;
	}
}