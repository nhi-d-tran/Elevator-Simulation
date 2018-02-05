import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Random;

public class Building 
{
	static final int LOBBY = 0;	
	Random r = new Random();				/* Generating random */
	Elevator[] elevator;					/* Elevator array	*/
	Floor[] floor;							/* Floor array		*/
	double arrival;							/* time that person arrives */						
	int totalPeople;						/* total people get into the building */
	double[][] matrixFloorAvg;				/* Matrix contains the service that elevator moves from floor i to floor j*/
	int[][] peopleInFloor;					/* Matrix contains the numbers of people arrive in each floor */
	double[][] waitTime;					/* Matrix for people waiting in queue to get into elevator to go from floor i to floor j*/
	long[] peopleWaiting;					/* Array contains the number of people waiting on the floor */
	double[][] prettyPrintAvgTrip;			/* For printing in final OUTPUT in outFile4*/
	double[][] prettyPrintAvgWait;			/* For printing in final OUTPUT in outFile4*/
	
	
	/*----------------------------------------- BUILDING CONSTRUCTOR -------------------------------------------------------*/
	public Building(int thefloor, int elevators)
	{	
		arrival = 0;
		waitTime = new double[thefloor][thefloor];
		elevator = new Elevator[elevators];
		floor = new Floor[thefloor];
		peopleInFloor = new int[thefloor][thefloor];
		peopleWaiting = new long[thefloor];
		prettyPrintAvgTrip = new double[thefloor][thefloor];
		prettyPrintAvgWait = new double[thefloor][thefloor];
		
		if (elevators > thefloor || elevators < 1 || thefloor < 2 )
		{
				throw new IllegalArgumentException("Error. Make sure you input the right values");
		}
	
		/* Using matrix to calculate the AVERAGE from floor to floor
		*
		* For example, from floor 1 -> 2: amount of time
		*			   from floor 1 -> 4: different amount of time
		*			   and so on
		*
		* In order to store the values from floor i to j, using matrix to store 
		* 
		* Initialize matrix value with all 0
		*/

		matrixFloorAvg = new double[thefloor][thefloor];		// declare the size of the matrix
		for(int m = 0; m < thefloor; m++)
		{
			for(int n = 0; n < thefloor; n++)
			{
				matrixFloorAvg[m][n] = 0.0;
				peopleInFloor[m][n] = 0;
				waitTime[m][n] = 0;
				prettyPrintAvgTrip[m][n] = 0;
				prettyPrintAvgWait[m][n] = 0;
			}
		}
		
		/*
		 * Initialize the floor and elevators array
		 * */
		for(int i = LOBBY; i < thefloor; i++)
		{
			floor[i] = new Floor();
			peopleWaiting[i] = 0;
		}
		
		for(int k = 0; k < elevators; k++)
		{
			elevator[k] = new Elevator();
		}
	}

	/*------------------------------- Exponential Distribution -----------------------------------*/
	public double exponential(int m)
	{
	    return (-m * Math.log(1.0 - r.nextDouble()));
	}

	/*--------------------- generate the next arrival time, with rate 1/5 --------------------------*/
	public double getArrival()
	{
		double arrival = 0;
	    arrival = exponential(5);
	    return arrival;
	}
	
	/*--------------------- Calculating the time travel from floor i to floor j -----------------------*/
	/*
	 * Method: to calculate the time travel
	 * 	time get off elevator - time get in elevator + service time in elevator
	 * 
	 * */
	public double calculating(double timein, double timeoff, double service)
	{
	  	return  service - timeoff - timein;
	}
	
	/*----------------- STATISTICS Collect: Calculating the average of trip and wait time from floor i to j ------------------*/
	/*
	 * Method: 
	 * The matrix1 contains the service run time from floor i to floor j 
	 * 
	 * The matrix2 contains the numbers that people want to floor i to floor j
	 * 
	 * Formula:
	 * mean = total service run time from floor i to j / total people want to go from floor i to j 
	 *
	 * */
	 public double[] calculateAverage(double[][] matrix1, int[][] matrix2, BufferedWriter outFile) throws IOException
	 {
		 double mean = 0;
		 DecimalFormat f = new DecimalFormat("###0.000000");
		 double[] array = new double[floor.length];
		 for(int i = 0; i < floor.length; i++)
		 {
	  		for(int j = 0; j < floor.length; j++)
	  		{
	  			if(i == j)
		  		{
		  			continue;
		  		}
		  		else
		 		{
 					mean = (matrix1[i][j] / matrix2[i][j]);					/* average time travel from floor i to j over total people get into same floor i to j	*/	
 					array[i] = mean;
 					prettyPrintAvgTrip[i][j] = array[i];
 					outFile.write("TRIP from floor " + i + " to floor " + j + " has average " +  f.format(mean) + "\n");			
		  		}
		 	}
	  		outFile.write("\n");			
		  }
		 return array;
	 }
	 
	 
	 /*
		 * Method: 
		 * The waitTime contains the time waiting for getting in elevator from floor i to floor j 
		 * 
		 * The peopleWaiting contains the numbers that people that are waiting on specific floor 
		 * 
		 * Formula:
		 * mean = total wait time / total people wait to be serve
		 *
		 * */
	public double[] calculateWaitTime(double[][] waitTime, long[] peopleWaiting, BufferedWriter outFile) throws IOException
	{
		 double mean = 0.0;
		 DecimalFormat f = new DecimalFormat("###0.000000");
		 double[] array = new double[floor.length];
		 for(int i = 0; i < floor.length; i++)
		 {
	  		for(int j = 0; j < floor.length; j++)
	  		{
	  			if(i == j)
		  		{
		  			continue;
		  		}
		  		else
		 		{
		  			mean = (waitTime[i][j] / peopleWaiting[i]);					/* average time travel from floor i to j over total people get into same floor i to j	*/	
					array[i] = mean;
					prettyPrintAvgWait[i][j] = array[i];
					outFile.write("WAIT TIME from floor " + i + " to floor " + j + " has average " +  f.format((mean)) + "\n");			
		  		}
		 	}
	  		outFile.write("\n");			
		  }
		 return array;
	}
	
	/*--------------------------------- For print in DEBUGGED FILE-------------------------------------------*/
	public void debuggedFile(Elevator elevator, int elevatorIndex, BufferedWriter outFile) throws IOException
	{
		if(elevator.currentFloor == 0)
		{
			outFile.write("Elevator " + (elevatorIndex + 1) + " is now at " + "LOBBY " +"\n");						/* Print result to debug outFile2*/
		}
		else
		{
			outFile.write("Elevator " +  (elevatorIndex + 1) + " is now at " + elevator.currentFloor + " and going UP" + "\n");		/* Print result to debug outFile2*/
		}	
	}

	/*-----------------------------Calculating the Standard Deviation -------------------------------*/
	 /*	Formula:
	  * Standard Deviation = sqrt(variance)
	  * 
	  * */
	public double[] standardDeviation(double[] variance, BufferedWriter outFile) throws IOException
	{
		DecimalFormat f = new DecimalFormat("###0.000000");
		double[] result = new double[floor.length];
		
		for(int i = 0; i < floor.length; i++)
		{
			result[i] = Math.sqrt(variance[i]);
			outFile.write("Standard Deviation from " + i + " to each floors " + f.format(result[i]) + " \n");
		}
		outFile.write("\n");
		return result;
	}
	
	/*----------------------------------Calculating the Variance i to floor j----------------------------------------------*/
	/*
	 * Formula: 
	 * 
	 * ( Sum(x_i - mean)^2 ) / N
	 * 
	 * in this case, x_i is from floor i to floor j and N is the number of floors
	 * */
	public double[] variancetrip(double[][] Matrix, double[] mean, int numPeople)  
	{
	  	double[] variance = new double[floor.length];
	  	
	  	 for(int i = 0; i < floor.length; i++)
		 {
	  		for(int j = 0; j < floor.length; j++)
	  		{
	  			if(i == j)
		  		{
		  			continue;
		  		}
		  		else
		 		{
	 				variance[i] += Math.pow(Matrix[i][j] - mean[i], 2);				/* Variance time travel from floor i to j*/	
		  		}		
		 	}
		 }
	  	 
	  	 for(int i = 0; i < floor.length; i++)
	  	 {
	  		 variance[i] = variance[i]/(numPeople);
	  	 }
	  	 return variance;
	}
	
	
	public double[] variancewait(double[][] Matrix, double[] mean)  
	{
	  	double[] variance = new double[floor.length];
	  	
	  	 for(int i = 0; i < floor.length; i++)
		 {
	 		variance[i] += Math.pow(mean[i], 2);				/* Variance time travel from floor i to j*/
		 }
	  	 
	  	 for(int j = 0; j < variance.length; j++)
	  	 {
	  		 variance[j] = variance[j]/(floor.length);
	  	 }
	  	 return variance;
	}
	
	
	/*--------------------------------------------------- FINAL PRINT - Pretty Printing --------------------------------------------------------------*/

	public void printing(double[] sd1, double[] sd2, BufferedWriter outFile) throws IOException
	{
		 DecimalFormat f = new DecimalFormat("###0.000000");
		for(int i = 0; i < floor.length; i++)
		{
			outFile.write("Floor " + i);
			outFile.write("\n");
			
			
			for(int k = 0; k < floor.length; k++)
			{
				outFile.write("\t \t \t \t Floor " + k);
			} 
			outFile.write("\n");
			outFile.write("Average Trip:    ");
			
			for(int j = 0; j < floor.length; j++) 
			{
				outFile.write(f.format(prettyPrintAvgTrip[i][j]) + " \t \t \t \t");
			}
			
			outFile.write("\n");
			outFile.write("Average Wait:    ");
			
			for(int j = 0; j < floor.length; j++) 
			{
				outFile.write(f.format(prettyPrintAvgWait[i][j]) + " \t \t \t \t");
			}
			
			outFile.write("\n");
			outFile.write("Standard Deviation Trip to each floors is ");		
			outFile.write(f.format(sd1[i]) + " \t \t");			
			outFile.write("\n");
			outFile.write("Standard Deviation Wait to each floors is ");		
			outFile.write(f.format(sd2[i]) + " \t \t");		
			outFile.write("\n");
			outFile.write("       		---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------   ");
			outFile.write("\n");
		}	
	}
	
	
	/*--------------------------------- Add person into the elevator with the appropriate DIRECTION -------------------------------------------*/
	public void addPersonToElevator(Person person, Elevator[] elevator, int second, BufferedWriter outFile) throws IOException
	{
		double service = 0;	
		double temp = 0;
		double value = 0;/*Run time between floor, 0.2 for moving from floor i to floor j*/
		peopleInFloor[person.arriveFloor][person.buttonPress]++;								/*Keep track numbers of people get into each floor and arrived floor*/
		
		
		/* CASE: Elevator is going UP */
		if(person.arriveFloor < person.buttonPress)	 		
		{
			floor[person.arriveFloor].waitToServeUP(person);									/* Add person in queue for waiting in the floor to go UP*/		
			peopleWaiting[person.arriveFloor] += floor[person.arriveFloor].numWaitingForUP;
			for(int i = 0; i < elevator.length; i++)
			{ 
				/* Print result to debug outFile3*/
				debuggedFile(elevator[i], i, outFile);
				
				if(elevator[i].currentFloor < person.arriveFloor && (elevator[i].UP == true || elevator[i].DOWN == false))
				{	
					elevator[i].stopFloorUp(person);															/* Pick that person up */
					elevator[i].currentFloor = person.arriveFloor;	
				
					if(floor[person.arriveFloor].waitForElevatorUP.isEmpty())
					{
						waitTime[person.arriveFloor][person.buttonPress] +=  (second - elevator[i].timeGetInUP - person.arriveTime);				/* Keep track the wait time of person in specific floors*/
						temp = second - elevator[i].timeGetInUP - person.arriveTime;				/*The amount of time that other person arrives on the queue*/
					}
					else
					{
//						temp = second - elevator[i].timeGetInUP - person.arriveTime;	
						waitTime[person.arriveFloor][person.buttonPress] += (second + service - temp);
					}
							/* Keep track the wait time of person in specific floors*/
	
					outFile.write("Elevator " + (i+1) + " stops at floor " + elevator[i].currentFloor + " to pick up person going UP\n"); 	/* Pick that person up */
					elevator[i].decideFloorUp(person, second);															/* go to his/her desire floor	*/
					outFile.write("The person chose to go to floor " + person.buttonPress + " \n");				/* Print result to debug outFile2*/
					floor[person.arriveFloor].waitForElevatorUP.remove();
				
					/* Moving elevator until arrive the floor that person desired*/
					while(elevator[i].currentFloor != person.buttonPress && elevator[i].currentFloor < floor.length)
					{
						service += 0.2;
						elevator[i].currentFloor++;
					}
					
					outFile.write("Person get off at " + elevator[i].currentFloor + "\n ");			/* Print result to debug outFile2*/
					elevator[i].getOffElevatorUP(person, second);									/* Remove people from the set of button that has been pressed and keep track the time they get off*/
					value = person.arriveTime - elevator[i].timeGetInUP - elevator[i].timeGetOffUP+ service;					/*	Calculating the time he/she moves from arrived floor to desired floor*/	
					matrixFloorAvg[person.arriveFloor][person.buttonPress] += (value);				/* Add the time that person arrived to desired floor to Matrix */ 
					return;
				}
			}
			
			/*The other case: Choose closest random elevator available and the person will be waiting */
			int fl = (int) (r.nextDouble()*elevator.length);
			elevator[fl].stopFloorUp(person);			// pick that person up
			elevator[fl].currentFloor = person.arriveFloor;
			service += 0.2;

			if(floor[person.arriveFloor].waitForElevatorUP.isEmpty())
			{
				waitTime[person.arriveFloor][person.buttonPress] +=  (second - elevator[fl].timeGetInUP - person.arriveTime);						/* Keep track the wait time of person in specific floors*/
//				temp = second - elevator[fl].timeGetInUP - person.arriveTime;				/*The amount of time that other person arrives on the queue*/
			}
			else
			{
				temp = second - elevator[fl].timeGetInUP - person.arriveTime;	
				waitTime[person.arriveFloor][person.buttonPress] += (second + service - temp);
			}
			outFile.write("Elevator " + fl + " stops at floor " + elevator[fl].currentFloor + " to pick up person going UP\n");
			elevator[fl].decideFloorUp(person, second);											/* go to his/her desire floor*/
			floor[person.arriveFloor].waitForElevatorUP.remove();						/* Remove from the queue for waiting for elevator*/
			
			/* Moving elevator until arrive the floor that person desired*/
			while(elevator[fl].currentFloor != person.buttonPress && elevator[fl].currentFloor < floor.length)
			{
				service += 0.2;
				elevator[fl].currentFloor++;
			}
			outFile.write("Person get off at " + elevator[fl].currentFloor + "\n ");	/* Print result to debug outFile2*/
			
			elevator[fl].getOffElevatorUP(person, second);								/* Remove people from the set of button that has been pressed and keep track the time they get off*/
			value = person.arriveTime -elevator[fl].timeGetOffUP - elevator[fl].timeGetInUP + service; //calculating(elevator[fl].timeGetInUP, elevator[fl].timeGetOffUP, service);				/*	Calculating the time he/she moves from arrived floor to desired floor*/	
			matrixFloorAvg[person.arriveFloor][person.buttonPress] += (value);				/* Add the time that person arrived to desired floor to Matrix */
		}
		else 	/* CASE: Elevator is going DOWN */
		{
			
			floor[person.arriveFloor].waitToServeDOWN(person);							/* Add person in queue for waiting in the floor to go DOWN*/
			peopleWaiting[person.arriveFloor]+= floor[person.arriveFloor].numWaitingForDOWN;
			for(int i = 0; i < elevator.length; i++)
			{
				/* Print result to debug outFile3*/
				debuggedFile(elevator[i], i, outFile);
				
				if(elevator[i].currentFloor > person.arriveFloor && (elevator[i].DOWN == true || elevator[i].UP == false))
				{
					elevator[i].stopFloorUp(person);													/* Pick that person up */
					elevator[i].currentFloor = person.arriveFloor;										/* Update the floor */
					
					if(floor[person.arriveFloor].waitForElevatorDOWN.isEmpty())
					{
						waitTime[person.arriveFloor][person.buttonPress] +=  (second - elevator[i].timeGetInUP - person.arriveTime);		/* Keep track the wait time of person in specific floors*/
//						temp = second - elevator[i].timeGetInUP - person.arriveTime;				/*The amount of time that other person arrives on the queue*/
					}
					else
					{
						temp = second - elevator[i].timeGetInUP - person.arriveTime;
						waitTime[person.arriveFloor][person.buttonPress] += (second + service- temp);
					}
				
					outFile.write("Elevator " + (i+1) + " stops at " + elevator[i].currentFloor + "\n"); /* Print result to debug outFile2*/
					floor[person.arriveFloor].waitForElevatorDOWN.remove();
					
					/* Moving elevator until arrive the floor that person desired*/
					while(elevator[i].currentFloor != person.buttonPress && elevator[i].currentFloor < floor.length)
					{
						service += 0.2;
						elevator[i].currentFloor--;	
					}	
					
					outFile.write("Person get off at " + elevator[i].currentFloor + "\n "); 			/* Print result to debug outFile2*/
					elevator[i].getOffElevatorDOWN(person, second);										/* Remove people from the set of button that has been pressed and keep track the time they get off*/
					value = person.arriveTime -elevator[i].timeGetOffDOWN  - elevator[i].timeGetInDOWN + service;							/* Calculating the time he/she moves from arrived floor to desired floor */				
					matrixFloorAvg[person.arriveFloor][person.buttonPress] +=(value);						/* Add the time that person arrived to desired floor to Matrix */
					return;
				}
			}
			
			/*	The other cases: Choose random closest elevator available for the person	*/
			int f = (int) r.nextDouble()*elevator.length;
			elevator[f].stopFloorDown(person);															/* Pick that person up */
			elevator[f].currentFloor = person.arriveFloor;												/* Update the floor */
			service += 0.2;
	
			if(floor[person.arriveFloor].waitForElevatorDOWN.isEmpty())
			{
				waitTime[person.arriveFloor][person.buttonPress] +=  (second - elevator[f].timeGetInUP - person.arriveTime);			/* Keep track the wait time of person in specific floors*/
					/*The amount of time that other person arrives on the queue*/
			}
			else
			{
				temp = second - elevator[f].timeGetInUP - person.arriveTime;	
				waitTime[person.arriveFloor][person.buttonPress] += (second + service- temp);
			}
			
			outFile.write("Elevator " + f + " stops at floor " + elevator[f].currentFloor + " to pick up person going DOWN\n"); /* Print result to debug outFile2*/
			elevator[f].decideFloorDown(person, second);														/* go to his/her desire floor*/
			floor[person.arriveFloor].waitForElevatorDOWN.remove();										/* Remove from the queue for waiting for elevator*/
			
			/* Moving elevator until arrive the floor that person desired*/
			while(elevator[f].currentFloor != person.buttonPress && elevator[f].currentFloor < floor.length)
			{
				service += 0.2;
				elevator[f].currentFloor--;
			}
			outFile.write("Person get off at " + elevator[f].currentFloor + "\n ");					/* Print result to debug outFile2*/
			elevator[f].getOffElevatorUP(person, second);												/* Remove people from the set of button that has been pressed and keep track the time they get off*/
			/* Calculating the time he/she moves from arrived floor to desired floor */	
			value = person.arriveTime - elevator[f].timeGetOffDOWN - elevator[f].timeGetInDOWN + service;			
			/* Add the time that person arrived to desired floor to Matrix*/
			matrixFloorAvg[person.arriveFloor][person.buttonPress] += (value);
		}
	}
	

	/*---------------------------------Check the elevator direction---------------------------------*/
	public void checkElevatorDirection()
	{
		for(int i = 0; i < elevator.length; i++)
		{
			/* if the buttons set for going UP and DOWN are empty, elevator is IDLE */
			if(elevator[i].buttonsDown.isEmpty() && elevator[i].buttonsUp.isEmpty())
			{
				elevator[i].UP = false;
				elevator[i].DOWN = false;
			}
			/* the elevator will go DOWN */
			else if(!elevator[i].buttonsDown.isEmpty())
			{
				elevator[i].UP = false;
				elevator[i].DOWN = true;
				
			}
			/*the elevator will go UP*/
			else if(!elevator[i].buttonsUp.isEmpty())
			{
				elevator[i].UP = true;
				elevator[i].DOWN = false;		
			}
			else		/*check the closest distance to see if it will be going up or down to pick up person*/
			{
				if(((floor.length - 1) - elevator[i].currentFloor) < floor.length/2)
				{
					elevator[i].UP = true;
					elevator[i].DOWN = false;	
				}
				else
				{
					elevator[i].UP = false;
					elevator[i].DOWN = true;
				}
			}
		}
	}

	
	/*======================================================= MAIN FUNCTION ======================================================= */
	
	public static void main(String[] args) throws IOException
	{	
		try
		{
				Scanner scanner = new Scanner(System.in);
				BufferedWriter outFile1 = new BufferedWriter(new FileWriter(args[0]));		/* output results for TRIP TIME*/
				BufferedWriter outFile2 = new BufferedWriter(new FileWriter(args[1]));		/* output results for WAIT TIME*/
				BufferedWriter outFile3 = new BufferedWriter(new FileWriter(args[2]));		/* debugged file for people get in and off from elevators */
				BufferedWriter outFile4 = new BufferedWriter(new FileWriter(args[3]));		/* Pretty Print*/
				DecimalFormat f = new DecimalFormat("###0.000000");
			
			
		      System.out.print("Specify # of elevators: ");       					    /* get number of elevators input */
		      int numElevators = scanner.nextInt();
		      System.out.print("Specify # of floors per building: "); 					/* get number of floors input    */
		      int numFloors = scanner.nextInt();
		      Building building = new Building(numFloors, numElevators);				
		    
		     
		      /*Run in 10000 times*/
		      for(int second = 1; second < 10000; second++)
		      {
		    
		        if((building.getArrival()) <= second)
		        {
		          Person person = new Person(numFloors);		    	 
		          outFile3.write("Person arrives at " + f.format(person.arriveTime) + " from floor " + person.arriveFloor + " and wants to get into floor " + person.buttonPress + " \n");
		          building.checkElevatorDirection();
		          building.addPersonToElevator(person, building.elevator, second, outFile3);    	 	
		          building.totalPeople++;			
		          outFile3.write("\n");
		        }
		      }
		      outFile1.write("Total people get into building: " + building.totalPeople + " \n");
		      outFile1.write("\n");
		      outFile1.write("The building has " + numFloors + " floors and " + numElevators + " elevators \n");
		      outFile1.write("\n");
		      
		      outFile1.write("------------------------------AVERAGE TRIPS FROM FLOOR TO FLOOR --------------------------------\n");
		      double[] averagetrip = building.calculateAverage(building.matrixFloorAvg, building.peopleInFloor, outFile1);
		      double[] variancetrip = building.variancetrip(building.matrixFloorAvg, averagetrip,  building.totalPeople); 
		      outFile1.write("------------------------------STANDARD DEVIATION FOR TRIPS------------------------------ \n");
		      double[] sd1 = building.standardDeviation(variancetrip, outFile1);
		      outFile2.write("------------------------------AVERAGE WAIT TIME FROM FLOOR TO FLOOR --------------------------------\n");
		      double[] averagewait =  building.calculateWaitTime(building.waitTime, building.peopleWaiting, outFile2);
		      double[] variancewait = building.variancewait(building.waitTime, averagewait); 
		      outFile2.write("------------------------------STANDARD DEVIATION FOR WAIT TIME------------------------------ \n");
		      double[] sd2 = building.standardDeviation(variancewait, outFile2);
		      
		      building.printing(sd1, sd2, outFile4);
		      outFile1.close();
		      outFile2.close();
		      outFile3.close();
		      outFile4.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
