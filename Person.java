import java.util.Random;

public class Person 
{
	double arriveTime;	
	int buttonPress;
	int arriveFloor;

	Random r = new Random();
	
	public Person(int numFloor)
	{
		arriveTime = getArrival();								/* Person arrives with the exponential distribution */
		buttonPress =(int) (r.nextDouble() * numFloor);			/* Person has randomly press number of floor */
		arriveFloor = (int) (r.nextDouble() * numFloor);			/* Person arrived the floor in the random time*/
		
		while(buttonPress == arriveFloor) 
		{	
			buttonPress =(int) (r.nextDouble() * numFloor);		
			arriveFloor = (int)(r.nextDouble() * numFloor);
		}
	}
	
	public double exponential(int m)
	{
	    return (-m * Math.log(1.0 - Math.random()));
	}

	// generate the next arrival time, with rate 1/5
	public double getArrival()
	{
		double arrival = 0;
		arrival = exponential(5);
	    return arrival;
	}		
}
