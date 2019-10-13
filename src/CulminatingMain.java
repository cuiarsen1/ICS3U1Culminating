// Arsen Cui
// ICS3U1-01
// January 18, 2019
// Mr. Radulovic
// ICS Culminating - Mouse Simulator
/*This program is a game. Essentially, it is an obstacle dodging game. The player is a mouse that 
runs around the screen, collecting cheese. Meanwhile, there will be cats spawning at regular 
intervals. The goal of the game is to avoid the cats and collect as many pieces of cheese as 
possible. The score is based on the amount of cheese collected.*/

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class CulminatingMain extends Application {

	// ArrayLists used to store the objects on the screen
	private ArrayList<Player> players;
	private ArrayList<Obstacle> obstacles;
	private ArrayList<Checkpoint> checkpoints;

	private Group root; // Contains the display on the screen
	
	private Group sceneBackground; // Contains the background image display
	
	private VBox sceneMenu; // Contains the main menu display
	
	// Hboxes and VBoxes containing the instructions menu display
	private VBox sceneInstructions;
	private HBox sceneImages;
	private VBox sceneInstructionsMouse;
	private VBox sceneInstructionsCat;
	private VBox sceneInstructionsCheese;

	private Group sceneObjects; // Display containing the objects on the screen in the game
	
	private VBox sceneEnd; // Display containing the game over scene
	
	// Doubles containing the screen size
	private double scenex;
	private double sceney;

	// Fonts used in the menus
	private Font FONT_MENUTITLE;
	private Font FONT_MENU;
	private Font FONT_INSTRUCTIONS;

	// Objects displayed in the main menu
	private Label menuTitle;
	private Button gameStartButton;
	private Button instructionsButton;

	// Objects displayed in the instructions menu
	private Label instructionsLabel;
	private ImageView mouseView;
	private ImageView catView;
	private ImageView cheeseView;
	private Label mouseLabel;
	private Label catLabel;
	private Label cheeseLabel;

	// ImageViews containing the views for the objects in the game
	private ImageView playerview;
	private ArrayList<ImageView> obstacleviews;
	private ArrayList<ImageView> checkpointviews;

	// Number of obstacles to add every interval
	private int amount;

	// Timer used to animate the game
	private AnimationTimer Timer;

	// Booleans used to track the progression of the game and when it ends to start a new round
	private boolean stopGame;
	private boolean gameStopped;
	
	// Boolean used to check for checkpoint collisions
	private boolean checkpointCollided;
	
	// Label used to track the current score
	private Label currentScoreLabel;

	// Variables used to display the scores
	private int currentScore;
	private int highScore;
	private String highScoreString;

	// Initializes file used to store the high scores
	private File scoreFile;
	private static RandomAccessFile randomFile;

	// Objects displayed in the game over scene
	private Label finalScoreLabel;
	private Label highScoreLabel;
	private Button restartGameButton;

	public CulminatingMain() {
		players = new ArrayList<Player>();
		obstacles = new ArrayList<Obstacle>();
		checkpoints = new ArrayList<Checkpoint>();

		obstacleviews = new ArrayList<ImageView>();
		checkpointviews = new ArrayList<ImageView>();
	}

	// Method used to display the background image
	private void setBackground(String backgroundImage) {
		Image background = new Image(backgroundImage);
		ImageView backgroundview = new ImageView();

		backgroundview.setImage(background);
		backgroundview.setFitWidth(scenex);
		backgroundview.setFitHeight(sceney);
		sceneBackground.getChildren().add(backgroundview);
	}

	// Method used to add a player object to the scene
	public void addPlayer(String playerImage) {
		
		Player p = new Player();
		Random r = new Random();

		// Initializes the size, coordinates, and velocity of the player
		double player_size = 70;
		double p_coordinate_x = r.nextDouble() * (scenex - player_size);
		double p_coordinate_y = r.nextDouble() * (sceney - player_size);
		double p_velocity_x = 0;
		double p_velocity_y = 0;

		// Applies information to the player object and adds it to the player ArrayList
		p.setSize(player_size);
		p.setCoordinate_x(p_coordinate_x);
		p.setCoordinate_y(p_coordinate_y);
		p.setVelocity_x(p_velocity_x);
		p.setVelocity_y(p_velocity_y);

		players.add(p);

		// Initializes an ImageView for the player and displays it
		Image player = new Image(playerImage);
		playerview = new ImageView();
		playerview.setImage(player);
		playerview.setFitWidth(player_size);
		playerview.setFitHeight(player_size);
		playerview.setX(p_coordinate_x);
		playerview.setY(p_coordinate_y);

		// Adds the player to the scene
		sceneObjects.getChildren().add(playerview);

	}

	// Method used to update the player on the scene
	private void updatePlayer() {
		
		//If there is currently no player, don't do anything
		if (players.isEmpty())
			return;

		// Update the player according to its coordinates and velocity every frame
		players.get(0).setCoordinate_x
		(players.get(0).getCoordinate_x() + players.get(0).getVelocity_x());
		
		players.get(0).setCoordinate_y
		(players.get(0).getCoordinate_y() + players.get(0).getVelocity_y());

		playerview.setX(players.get(0).getCoordinate_x());
		playerview.setY(players.get(0).getCoordinate_y());
	}

	// Method used to check for collisions between the player and the sides of the screen
	private void screenCollisionCheck() {

		//If there is currently no player, don't do anything
		if (players.isEmpty())
			return;

		// If the player hits the edge of the screen, prevent it from leaving the screen
		if (players.get(0).getCoordinate_x() < 0)

			players.get(0).setCoordinate_x(0);

		if (players.get(0).getCoordinate_x() + players.get(0).getSize() > scenex)

			players.get(0).setCoordinate_x(scenex - players.get(0).getSize());

		if (players.get(0).getCoordinate_y() < 0)

			players.get(0).setCoordinate_y(0);

		if (players.get(0).getCoordinate_y() + players.get(0).getSize() > sceney)

			players.get(0).setCoordinate_y(sceney - players.get(0).getSize());

	}

	// Method used to add obstacles to the scene
	public void addObstacle(String obstacleImage, int amount) {

		for (int i = 0; i < amount; i += 1) {
			
			Obstacle o = new Obstacle();
			Random r = new Random();

			//Initializes the size, coordinates, and velocity of the obstacle
			
			double size_min = 70;
			double size_max = 150;

			double obstacle_size = size_min + r.nextDouble() * size_max;

			double o_coordinate_x = r.nextDouble() * (scenex - obstacle_size);
			double o_coordinate_y = r.nextDouble() * (sceney - obstacle_size);

			// The minimum horizontal and vertical distance an obstacle can spawn from the player
			double obstacle_distance = 100;

			/*While the obstacle's generated coordinates are too close to the player, 
			continue generating new coordinates until they are far enough*/
			while 
			(players.get(0).getCoordinate_x() < o_coordinate_x + obstacle_size + obstacle_distance
			&& players.get(0).getCoordinate_x() + players.get(0).getSize() 
			> o_coordinate_x - obstacle_distance && players.get(0).getCoordinate_y() <
			o_coordinate_y + obstacle_size + obstacle_distance
			&& players.get(0).getCoordinate_y() + players.get(0).getSize() > 
			o_coordinate_y - obstacle_distance) 
			{
				o_coordinate_x = r.nextDouble() * (scenex - obstacle_size);
				o_coordinate_y = r.nextDouble() * (sceney - obstacle_size);
			}

			double speed_min = -3;
			double speed_max = 6;

			double o_velocity_x = speed_min + r.nextDouble() * speed_max;
			double o_velocity_y = speed_min + r.nextDouble() * speed_max;

			// Applies information to the obstacle object and adds it to the obstacle ArrayList
			o.setSize(obstacle_size);
			o.setCoordinate_x(o_coordinate_x);
			o.setCoordinate_y(o_coordinate_y);
			o.setVelocity_x(o_velocity_x);
			o.setVelocity_y(o_velocity_y);

			obstacles.add(o);

			// Initializes the ImageView to display the obstacle
			Image obstacle = new Image(obstacleImage);
			ImageView obstacleview = new ImageView();
			obstacleview.setImage(obstacle);
			obstacleview.setFitHeight(obstacle_size);
			obstacleview.setFitWidth(obstacle_size);
			obstacleview.setX(o_coordinate_x);
			obstacleview.setY(o_coordinate_y);
			obstacleviews.add(obstacleview);

			// Adds the obstacle to the scene
			sceneObjects.getChildren().add(obstacleview);

		}

	}

	// Method used to update obstacles on the scene
	private void updateObstacles() {
		
		// If there are currently no obstacles, don't do anything
		if (obstacles.isEmpty())
			return;

		// Update the obstacles according to their coordinates and velocity every frame
		for (int i = 0; i < obstacles.size(); i += 1) 
		{
			
			obstacles.get(i).setCoordinate_x
			(obstacles.get(i).getCoordinate_x() + obstacles.get(i).getVelocity_x());
			
			obstacles.get(i).setCoordinate_y
			(obstacles.get(i).getCoordinate_y() + obstacles.get(i).getVelocity_y());

			obstacleviews.get(i).setX(obstacles.get(i).getCoordinate_x());
			obstacleviews.get(i).setY(obstacles.get(i).getCoordinate_y());

		}

	}

	// Method used to check for collisions between the player and the obstacle
	private void obstacleCollisionCheck() {
		
		// If there are currently no players or obstacles, don't do anything 
		if (obstacles.isEmpty() || players.isEmpty())
			return;

		// If the player and an obstacle collide, stop the game
		for (int j = 0; j < obstacles.size(); j += 1) {
			if (players.get(0).getCoordinate_x() < 
			(obstacles.get(j).getCoordinate_x() + obstacles.get(j).getSize()) && 
			players.get(0).getCoordinate_x() + players.get(0).getSize() > 
			obstacles.get(j).getCoordinate_x() && players.get(0).getCoordinate_y() < 
			(obstacles.get(j).getCoordinate_y() + obstacles.get(j).getSize()) && 
			players.get(0).getCoordinate_y() + players.get(0).getSize() > 
			obstacles.get(j).getCoordinate_y()) 
			{
				stopGame = true;
			}
		}

	}

	// Method used to remove obstacles when they go off the screen to free up memory
	private void obstacleRemoval() {
		
		// If there are currently no obstacles, don't do anything
		if (obstacles.isEmpty())
			return;

		// If the obstacle goes off the screen, remove it from the scene and delete it
		for (int i = 0; i < obstacles.size(); i += 1) {
			if (obstacles.get(i).getCoordinate_x() + obstacles.get(i).getSize() < 0
					|| obstacles.get(i).getCoordinate_x() > scenex
					|| obstacles.get(i).getCoordinate_y() + obstacles.get(i).getSize() < 0
					|| obstacles.get(i).getCoordinate_y() > sceney) 
			{
				sceneObjects.getChildren().remove(obstacleviews.get(i));
				obstacleviews.remove(i);
				obstacles.remove(i);

			}
		}
	}

	// Method used to change the velocity of the obstacles at regular intervals
	private void obstacleVelocityChange() {
		Random r = new Random();

		double velocity_min = -3;
		double velocity_max = 6;

		// For every obstacle on the screen, generate a randomized new velocity within a range
		for (int i = 0; i < obstacles.size(); i += 1) {
			double o_velocity_x = velocity_min + r.nextDouble() * velocity_max;
			double o_velocity_y = velocity_min + r.nextDouble() * velocity_max;

			obstacles.get(i).setVelocity_x(o_velocity_x);
			obstacles.get(i).setVelocity_y(o_velocity_y);
		}
	}

	// Method used to add checkpoints to the scene
	public void addCheckpoint(String checkpointImage) {
		Checkpoint c = new Checkpoint();
		Random r = new Random();

		// Initializes the size and coordinates of the checkpoint
		double checkpoint_size = 100;
		double c_coordinate_x = r.nextDouble() * (scenex - checkpoint_size);
		double c_coordinate_y = r.nextDouble() * (sceney - checkpoint_size);

		// Applies information to the player object and adds it to the player ArrayList
		c.setSize(checkpoint_size);
		c.setCoordinate_x(c_coordinate_x);
		c.setCoordinate_y(c_coordinate_y);

		checkpoints.add(c);

		// Initializes an ImageView to display the checkpoint
		Image checkpoint = new Image(checkpointImage);
		ImageView checkpointview = new ImageView();

		checkpointview.setImage(checkpoint);
		checkpointview.setFitWidth(checkpoint_size);
		checkpointview.setFitHeight(checkpoint_size);
		checkpointview.setX(c_coordinate_x);
		checkpointview.setY(c_coordinate_y);

		checkpointviews.add(checkpointview);

		sceneObjects.getChildren().add(checkpointview);
	}

	// Method used to check for collisions between the player and a checkpoint
	private void checkpointCollisionCheck() {
		
		// If there are currently no checkpoints on the screen, don't do anything
		if (checkpoints.isEmpty() || players.isEmpty())
			return;

		/*For every checkpoint, check if the player is colliding with any of them. If there is a 
		collision, remove the checkpoint from the scene and delete it*/
		for (int i = 0; i < checkpoints.size(); i += 1) 
		{
			if (players.get(0).getCoordinate_x() < (checkpoints.get(i).getCoordinate_x() + 
			checkpoints.get(i).getSize()) && players.get(0).getCoordinate_x() +
			players.get(0).getSize() > (checkpoints.get(i).getCoordinate_x()) && 
			players.get(0).getCoordinate_y() < (checkpoints.get(i).getCoordinate_y() + 
			checkpoints.get(i).getSize()) && players.get(0).getCoordinate_y() + 
			players.get(0).getSize() > checkpoints.get(i).getCoordinate_y()) 
			{
				sceneObjects.getChildren().remove(checkpointviews.get(i));
				checkpointviews.remove(i);
				checkpoints.remove(i);

				checkpointCollided = true;
			}
		}

		/*If there has been a collision with a checkpoint, 
		increment the score by 1 and add a new checkpoint*/
		if (checkpointCollided) {
			
			currentScore += 1;
			
			currentScoreLabel.setText("Current Score: " + currentScore);
			
			addCheckpoint("Checkpoint.png");
			
			checkpointCollided = false;
		}

	}

	// Method used to display the main menu of the game
	private void gameMenu() {
		
		sceneMenu.getChildren().addAll(menuTitle, gameStartButton, instructionsButton);
		
		sceneMenu.setSpacing(50);

		sceneMenu.setTranslateY(200);

		menuTitle.setTranslateX(220);
		gameStartButton.setTranslateX(300);
		instructionsButton.setTranslateX(297);

		//Add the background image and the main menu to the scene
		root.getChildren().addAll(sceneBackground, sceneMenu);

	}

	// Method used to start the game
	private void gameStart() {
		
		// Remove the main menu from the scene and add the game objects
		
		root.getChildren().clear();
		root.getChildren().add(sceneBackground);

		addPlayer("Player.png");
		
		addCheckpoint("Checkpoint.png");

		root.getChildren().addAll(sceneObjects, currentScoreLabel);

		// Start the animation timer for the game
		Timer.start();

	}

	// Method used to display the instructions menu
	private void instructionsMenu() {
		
		// Remove the main menu from the scene and add the objects needed for the instructions menu
		
		root.getChildren().clear();
		root.getChildren().add(sceneBackground);

		// Displays images and labels of the objects used in the game for instruction purposes 
		
		double player_size = 70;
		double obstacle_size = 120;
		double checkpoint_size = 100;

		Image instructionsMouse = new Image("Player.png");
		Image instructionsCat = new Image("Obstacle.png");
		Image instructionsCheese = new Image("Checkpoint.png");

		mouseView = new ImageView(instructionsMouse);
		mouseView.setFitHeight(player_size);
		mouseView.setFitWidth(player_size);
		mouseView.setTranslateX(10);

		catView = new ImageView(instructionsCat);
		catView.setFitHeight(obstacle_size);
		catView.setFitWidth(obstacle_size);
		catView.setTranslateX(30);

		cheeseView = new ImageView(instructionsCheese);
		cheeseView.setFitHeight(checkpoint_size);
		cheeseView.setFitWidth(checkpoint_size);
		cheeseView.setTranslateX(10);

		sceneInstructionsMouse.getChildren().addAll(mouseView, mouseLabel);
		sceneInstructionsMouse.setSpacing(10);
		sceneInstructionsMouse.setTranslateY(50);

		sceneInstructionsCat.getChildren().addAll(catView, catLabel);
		sceneInstructionsCat.setSpacing(10);

		sceneInstructionsCheese.getChildren().addAll(cheeseView, cheeseLabel);
		sceneInstructionsCheese.setSpacing(10);
		sceneInstructionsCheese.setTranslateY(20);

		sceneImages.getChildren().addAll
		(sceneInstructionsMouse, sceneInstructionsCat, sceneInstructionsCheese);
		
		sceneImages.setSpacing(150);
		sceneImages.setTranslateX(30);
		sceneImages.setTranslateY(100);
		
		gameStartButton.setTranslateY(50);

		sceneInstructions.getChildren().addAll
		(instructionsLabel, sceneImages, gameStartButton);
		
		sceneInstructions.setSpacing(100);

		// Add the instructions menu to the scene
		root.getChildren().add(sceneInstructions);
	}

	// Method used to end the game and display the end game scene
	private void gameOver() throws IOException {
		
		// If the game has already been stopped, don't do anything
		if (gameStopped)
			return;

		/*Stop the animation timer, clear all the game objects from the scene, and 
		add the end game scene with the scores and a button to restart the game*/
		
		Timer.stop();

		players.clear();
		obstacles.clear();
		checkpoints.clear();
		obstacleviews.clear();
		checkpointviews.clear();
		sceneObjects.getChildren().clear();
		root.getChildren().clear();
		
		root.getChildren().add(sceneBackground);

		finalScoreLabel.setText("Your score is: " + currentScore);

		/*If the high scores file is not empty, compare the current score to the high score and 
		update the high score if the current score is higher. Overwrite the high score in the file 
		with the new high score if the current score is higher. Then display the current score and 
		the high score.
		
		If the high score file is empty, meaning there are no past scores for the game, then 
		display the current score and display the high score as the current score. Overwrite the 
		high score in the file with the new high score if the current score is higher*/
		
		if (randomFile.length() != 0) {
			
			randomFile.seek(0);
			String readScore = randomFile.readLine();
			highScore = Integer.parseInt(readScore);
			
			if (currentScore >= highScore) {
				highScore = currentScore;
				highScoreString = Integer.toString(highScore);
				randomFile.seek(0);
				randomFile.writeBytes(highScoreString);
			}

		} else if (randomFile.length() == 0) {
			highScore = 0;

			if (currentScore >= highScore)
				highScore = currentScore;
			
			highScoreString = Integer.toString(highScore);
			randomFile.writeBytes(highScoreString);
		}

		//Get input from the high score file and display the high score 
		
		randomFile.seek(0);
		String readScore = randomFile.readLine();

		highScoreLabel.setText("Your high score is: " + readScore);
		highScoreLabel.setTranslateX(-35);

		restartGameButton.setTranslateX(15);

		sceneEnd.getChildren().addAll(finalScoreLabel, highScoreLabel, restartGameButton);
		sceneEnd.setTranslateX(300);
		sceneEnd.setTranslateY(270);
		sceneEnd.setSpacing(50);

		root.getChildren().add(sceneEnd);

		/*Lets the program know that the game is over and
		not to display the end game scene every frame*/
		gameStopped = true;

	}

	// Method used to restart the game from the end game scene
	private void gameRestart() {
		
		/*Let the program know the game has restarted and 
		not to display the end game scene anymore*/
		
		stopGame = false;
		gameStopped = false;

		sceneEnd.getChildren().clear();
		root.getChildren().clear();
		
		root.getChildren().add(sceneBackground);

		// Resets the score, adds the game objects to the scene, and starts the animation timer
		
		currentScore = 0;
		
		currentScoreLabel.setText("Current Score: " + currentScore);

		Timer.start();

		addPlayer("Player.png");
		addCheckpoint("Checkpoint.png");

		root.getChildren().addAll(sceneObjects, currentScoreLabel);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		root = new Group();
		sceneBackground = new Group();
		sceneMenu = new VBox();
		sceneInstructions = new VBox();
		sceneImages = new HBox();
		sceneInstructionsMouse = new VBox();
		sceneInstructionsCat = new VBox();
		sceneInstructionsCheese = new VBox();
		sceneObjects = new Group();
		sceneEnd = new VBox();

		// Initializes and sets the scene for the game window 
		
		scenex = 800;
		sceney = 800;
		Scene scene = new Scene(root, scenex, sceney);

		amount = 3; // Amount of obstacles to add to the screen every interval of time

		stopGame = false;
		gameStopped = false;

		checkpointCollided = false;

		scoreFile = new File("scoreFile.txt");
		randomFile = new RandomAccessFile(scoreFile, "rw");

		// Initializes all of the fonts, labels and buttons used in the program
		
		FONT_MENUTITLE = new Font("Arial", 50);
		FONT_MENU = new Font("Arial", 30);
		FONT_INSTRUCTIONS = new Font("Arial", 20);
		
		// Initializes all of the labels and buttons used in the program
		
		menuTitle = new Label("Mouse Simulator");
		menuTitle.setFont(FONT_MENUTITLE);
		menuTitle.setTextFill(Color.YELLOW);
		
		gameStartButton = new Button("Play Game");
		gameStartButton.setFont(FONT_MENU);
		
		instructionsButton = new Button("Instructions");
		instructionsButton.setFont(FONT_MENU);
		
		instructionsLabel = new Label(
		"You are a mouse that is in a house filled with cats. However, you are a very greedy"
		+ "\nmouse, and prioritize eating cheese over escaping from the house. Move around the"
		+ "\nhouse with the arrow keys and dodge the cats, while trying to get as many pieces of"
		+ "\ncheese as possible. How long can you survive?");
		
		instructionsLabel.setFont(FONT_INSTRUCTIONS);
		instructionsLabel.setTextFill(Color.AQUA);
		instructionsLabel.setTranslateX(30);
		instructionsLabel.setTranslateY(100);
		
		mouseLabel = new Label("This is you!");
		mouseLabel.setFont(FONT_INSTRUCTIONS);
		mouseLabel.setTextFill(Color.AQUA);
		
		catLabel = new Label("Keep away from these!");
		catLabel.setFont(FONT_INSTRUCTIONS);
		catLabel.setTextFill(Color.AQUA);
		
		cheeseLabel = new Label("Collect these!");
		cheeseLabel.setFont(FONT_INSTRUCTIONS);
		cheeseLabel.setTextFill(Color.AQUA);
		
		currentScoreLabel = new Label("Current Score: 0");
		currentScoreLabel.setFont(FONT_MENU);
		currentScoreLabel.setTextFill(Color.AQUA);

		finalScoreLabel = new Label();
		finalScoreLabel.setFont(FONT_MENU);
		finalScoreLabel.setTextFill(Color.YELLOW);
		
		highScoreLabel = new Label();
		highScoreLabel.setFont(FONT_MENU);
		highScoreLabel.setTextFill(Color.YELLOW);
		
		restartGameButton = new Button("Play Again");
		restartGameButton.setFont(FONT_MENU);

		// Sets the background for the game
		setBackground("Game_Background.jpg");

		// Displays the main menu of the game upon running the program
		gameMenu();

		// If the arrow keys are pressed, move the player
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {

				// If there is no player on the screen, don't do anything
				if (players.isEmpty())
					return;

				if (event.getCode() == KeyCode.UP)
					players.get(0).setVelocity_y(-4);

				else if (event.getCode() == KeyCode.DOWN)
					players.get(0).setVelocity_y(4);

				else if (event.getCode() == KeyCode.RIGHT)
					players.get(0).setVelocity_x(4);

				else if (event.getCode() == KeyCode.LEFT)
					players.get(0).setVelocity_x(-4);

			}
		});

		// If the arrow keys are released, stop moving the player
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {

				// If there is no player on the screen, don't do anything
				if (players.isEmpty())
					return;

				if (event.getCode() == KeyCode.UP)
					players.get(0).setVelocity_y(0);

				else if (event.getCode() == KeyCode.DOWN)
					players.get(0).setVelocity_y(0);

				else if (event.getCode() == KeyCode.RIGHT)
					players.get(0).setVelocity_x(0);

				else if (event.getCode() == KeyCode.LEFT)
					players.get(0).setVelocity_x(0);

			}
		});

		// Animation timer used to animate the game
		Timer = new AnimationTimer() {

			// Variables used to track the time
			
			long oldTimeAdd = 0;
			long intervalAdd = 5000000000l; 

			long oldTimeVelocity = 0;
			long intervalVelocity = 3000000000l;

			@Override
			public void handle(long time) {

				// Animates all the objects every frame
				try {
					upDateMove();
				} catch (IOException e) {
					e.printStackTrace();
				}

				oldTimeAdd += 1;

				// Add an obstacle to the scene every 5 seconds
				if (time - oldTimeAdd > intervalAdd) {
					upDateAdd();
					oldTimeAdd = time;
				}

				oldTimeVelocity += 1;

				// Change the velocity of every obstacle every 3 seconds
				if (time - oldTimeVelocity > intervalVelocity) {
					upDateVelocity();
					oldTimeVelocity = time;
				}

			}
		};

		// BUtton used to start the game
		gameStartButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				gameStart();
			}

		});

		// Button used to display the instructions menu
		instructionsButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				instructionsMenu();
			}

		});

		// Button used to restart the game from the end game scene
		restartGameButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				gameRestart();
			}

		});

		primaryStage.setTitle("Mouse Simulator");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	// Animates all the objects every frame
	private void upDateMove() throws IOException {
		
		// Animates obstacles and checks for events related to the obstacles
		updateObstacles();
		obstacleCollisionCheck();
		obstacleRemoval();

		// Animates the player and checks for events related to the player
		screenCollisionCheck();
		updatePlayer();

		// Checks for collisions between the player and the checkpoints every frame
		checkpointCollisionCheck();

		// If the player collides with an obstacle, end the game
		if (stopGame == true)
			gameOver();

	}
	
	// If the game is ongoing, add obstacles every 5 seconds
	private void upDateAdd() {
		if (stopGame == false)
			addObstacle("Obstacle.png", amount);
	}

	// Change the velocities of every obstacle every 3 seconds
	private void upDateVelocity() {
		obstacleVelocityChange();
	}

	public static void main(String[] args) throws IOException {
		launch(args);
		randomFile.close();
	}

}