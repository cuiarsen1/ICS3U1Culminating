// Arsen Cui
// ICS3U1-01
// January 18, 2019
// Mr. Radulovic
// ICS Culminating - Mouse Simulator

// Class storing all the methods needed to access checkpoints in the game

public class Checkpoint {
	
	// Variables used to store the values obtained by the checkpoint methods
	public double coordinate_x;
	public double coordinate_y;
	public double size;
	
	public Checkpoint()
	{
		
	}

	// Gets the x coordinate of the checkpoint
	public double getCoordinate_x() {
		return coordinate_x;
	}

	// Sets a value to the x coordinate of the checkpoint
	public void setCoordinate_x(double coordinate_x) {
		this.coordinate_x = coordinate_x;
	}

	// Gets the y coordinate of the checkpoint
	public double getCoordinate_y() {
		return coordinate_y;
	}

	// Sets a value to the y coordinate of the checkpoint
	public void setCoordinate_y(double coordinate_y) {
		this.coordinate_y = coordinate_y;
	}
	
	// Gets the size of the checkpoint
	public double getSize() {
		return size;
	}

	// Sets a value to the size of the checkpoint
	public void setSize(double size) {
		this.size = size;
	}

}
