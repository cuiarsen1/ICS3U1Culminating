// Arsen Cui
// ICS3U1-01
// January 18, 2019
// Mr. Radulovic
// ICS Culminating - Mouse Simulator

// Class storing all the methods needed to access the player in the game

public class Player {
	
	// Variables used to store the values obtained by the player methods
	public double coordinate_x;
	public double coordinate_y;
	public double size;
	public double velocity_x;
	public double velocity_y;
	
	public Player() 
	{
		
	}
	
	// Gets the x coordinate of the player
	public double getCoordinate_x() {
		return coordinate_x;
	}

	// Sets a value to the x coordinate of the player
	public void setCoordinate_x(double coordinate_x) {
		this.coordinate_x = coordinate_x;
	}

	// Gets the y coordinate of the player
	public double getCoordinate_y() {
		return coordinate_y;
	}

	// Sets a value to the y coordinate of the player
	public void setCoordinate_y(double coordinate_y) {
		this.coordinate_y = coordinate_y;
	}
	
	// Gets the size of the player
	public double getSize() {
		return size;
	}
	
	// Sets a value to the size of the player
	public void setSize(double size) {
		this.size = size;
	}
	
	// Gets the horizontal velocity of the player
	public double getVelocity_x() {
		return velocity_x;
	}

	// Sets a value to the horizontal velocity of the player
	public void setVelocity_x(double velocity_x) {
		this.velocity_x = velocity_x;
	}

	// Gets the vertical velocity of the player
	public double getVelocity_y() {
		return velocity_y;
	}

	// Sets a value to the vertical velocity of the player
	public void setVelocity_y(double velocity_y) {
		this.velocity_y = velocity_y;
	}

}
