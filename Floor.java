import java.util.LinkedList;
import java.util.Queue;

public class Floor 
{
	Queue<Person> waitForElevatorUP;		/* People waiting for the Queue to go UP */
	Queue<Person> waitForElevatorDOWN;		/* People waiting for the Queue to go DOWN */
	int numFloors;							/* The floor inside building*/		
	int numPeopleOnFloor;					/* The number of people waiting on the specific floor */				
	int numWaitingForUP;					/* Counter for numbers waiting for going UP*/
	int numWaitingForDOWN;					/* Counter for numbers waiting for going UP*/

	public Floor(int numFloor, int peopleOnFloor)
	{	
		numFloors = numFloor;				
		numWaitingForUP = 0;
		numWaitingForDOWN = 0;
	    numPeopleOnFloor = peopleOnFloor;
	    waitForElevatorUP = new LinkedList<>();
	    waitForElevatorDOWN = new LinkedList<>();;
	}
	
	public Floor()
	{
		waitForElevatorUP = new LinkedList<>();
	    waitForElevatorDOWN = new LinkedList<>();
	}
	
	// Add person into the waiting queue for going UP
	public void waitToServeUP(Person person)
	{
		waitForElevatorUP.add(person);
		numWaitingForUP++;
	}
	
	// Add person into the waiting queue for going DOWN
	public void waitToServeDOWN(Person person)
	{
		waitForElevatorDOWN.add(person);
		numWaitingForDOWN++;
	}
}
