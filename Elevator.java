import java.util.*;

public class Elevator 
{
	boolean UP;							/* Elevator in the direction UP	*/
	boolean DOWN;						/* Elevator in the direction DOWN */	
	int currentFloor;					/* The floor that elevator is in */
	double service;						/* Service time that person in the elevator */
	HashSet<Person> buttonsUp;			/* Sorted Set the person with the button he/she pressed go UP */
	HashSet<Person> buttonsDown;		/* Sorted Set the person with the button he/she pressed go DOWN */
	double timeGetInUP;					/* Time to get in elevator, that goes UP */
	double timeGetInDOWN;				/* Time to get in elevator, that goes DOWN */
	double timeGetOffUP;				/* Time to get off elevator, that goes UP */
	double timeGetOffDOWN;				/* Time to get off elevator, that goes DOWN */
	
	public Elevator()
	{
		UP = false;
		DOWN = false;
		currentFloor = 0;
		service = 0;
		buttonsUp = new HashSet<>();
		buttonsDown = new HashSet<>();
		timeGetOffUP = 0;
		timeGetOffDOWN = 0;
		timeGetInUP = 0;					/*Time to get in elevator, that goes UP */
		timeGetInDOWN = 0;	
	}
	
	// Add person into the set for going UP
	public void decideFloorUp(Person person, int second)
	{	
		timeGetInUP = (0.1);
		buttonsUp.add(person);
	}
	
	// Add person into the set for going DOWN
	public void decideFloorDown(Person person, int second)
	{	
		timeGetInDOWN = (0.1);
		buttonsDown.add(person);
	}
	
	// The elevator stops at floor and pick person (who wants to go UP) to the set 
	public void stopFloorUp(Person person)
	{
		buttonsUp.add(person);
	}
	
	// The elevator stops at floor and pick person (who wants to go UP) to the set 
	public void stopFloorDown(Person person)
	{
		buttonsDown.add(person);
	}
	
	public void getOffElevatorUP(Person person, int second)
	{
		timeGetOffUP = (0.1);
		buttonsUp.remove(person.buttonPress);
	}
	
	public void getOffElevatorDOWN(Person person, int second)
	{
		timeGetOffDOWN = (0.1);
		buttonsDown.remove(person.buttonPress);
	}
	
	
}
