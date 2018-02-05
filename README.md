# Elevator-Simulation

Events are:
	- Person at a arrived floor (Random) wants to go a destination floor (Random)
	- Elevator empties or fills at a floor
	- Elevator gets to its next floor (movement)
	- The event is a person pushes a button on a floor with a direction (UP or DOWN)
		

In order to find the closest elevator (in distance) to the person already going in that direction, or an idle one.  Assign this elevator to stop at that floor. If this is an idle elevator, assign it and have it move in that direction. Create a new elevator event and person event getting to the next floor.


ALGORITHMS:

- Master Controller, which is an array that knows the direction and floor of each elevator or idle. It also has which floor buttons have been pressed (at least on person waiting, up or down)


STATISTICS COLLECT:

	- Average of all trips of going from floor i to j
	- Average wait time of going from floor i to j
	- Standard deviation of the above two
