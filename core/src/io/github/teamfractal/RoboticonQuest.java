package io.github.teamfractal;
// Executable version can be found here: https://sepr-topright.github.io/SEPR/documentation/assessment3/game.zip
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import io.github.teamfractal.animation.AnimationAddResources;
import io.github.teamfractal.animation.AnimationPhaseTimeout;
import io.github.teamfractal.animation.AnimationShowPlayer;
import io.github.teamfractal.animation.IAnimation;
import io.github.teamfractal.animation.IAnimationFinish;
import io.github.teamfractal.screens.*;
import io.github.teamfractal.entity.Market;
import io.github.teamfractal.entity.Player;
import io.github.teamfractal.util.SoundEffects;
import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.util.PlotManager;

/**
 * This is the main game boot up class.
 * It will set up all the necessary classes.
 */
public class RoboticonQuest extends Game {
	static RoboticonQuest _instance;
	
	/**
	 * Currently unused
	 * @return The instance of this class
	 */
	public static RoboticonQuest getInstance() {
		return _instance;
	}


	private PlotManager plotManager;
	SpriteBatch batch;
	public Skin skin;
	public MainMenuScreen mainMenuScreen;
	public GameScreen gameScreen;
	private int phase;
	private int currentPlayer;
	public ArrayList<Player> playerList;
	public Market market;
	private int landBoughtThisTurn;
	private Music gameMusic;

	/**
	 * Returns the index at which a given player is stored in the playerList
	 * @param player
	 * @return the index at which a given player is stored in the playerList
	 */
	public int getPlayerIndex (Player player) {
		return playerList.indexOf(player);
	}

	public TiledMap tmx;
	
	/**
	 * Constructor
	 */
	public RoboticonQuest(){
		_instance = this;
		reset();
	}
	
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setupSkin();
		
	
		gameScreen = new GameScreen(this);

		// Setup other screens.
		mainMenuScreen = new MainMenuScreen(this);

		setScreen(mainMenuScreen);
		
		// Added by Christian Beddows to implement the background music
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("music/roundone.wav"));
		gameMusic.play();
		gameMusic.setLooping(true);
		gameMusic.setVolume((float)0.4);
	}

	/**
	 * 
	 * @return The batch object used when rendering various aspects of the GUI
	 */
	public Batch getBatch() {
		return batch;
	}

	/**
	 * Setup the default skin for GUI components.
	 */
	private void setupSkin() {
		skin = new Skin(
			Gdx.files.internal("skin/skin.json"),
			new TextureAtlas(Gdx.files.internal("skin/skin.atlas"))
		);
	}

	/**
	 * Clean up and destroy all UI components when the application is closed
	 */
	@Override
	public void dispose () {
		mainMenuScreen.dispose();
		gameScreen.dispose();
		skin.dispose();
		batch.dispose();
	}
	
	/**
	 * The phase number of the phase that the game is currently in (i.e. the roboticon placing phase)
	 * @return The phase number of the phase that the game is currently in (i.e. the roboticon placing phase)
	 */
	public int getPhase(){
		return this.phase;
	}

	/**
	 * Rest the state of the game. Used when player's wish to start a new game.
	 */
	public void reset() {
		this.currentPlayer = 0;
		this.phase = 0;

		Player player1 = new Player(this);
		Player player2 = new Player(this);
		this.playerList = new ArrayList<Player>();
		this.playerList.add(player1);
		this.playerList.add(player2);
		this.currentPlayer = 0;
		this.market = new Market();
		plotManager = new PlotManager();
	}

	/**
	 * Move to the next phase in the game
	 */
	public void nextPhase () {
		int newPhaseState = phase + 1;
		phase = newPhaseState;
		// phase = newPhaseState = 4;

		System.out.println("RoboticonQuest::nextPhase -> newPhaseState: " + newPhaseState);
		switch (newPhaseState) {
			// Phase 2: Purchase Roboticon
			case 2:
				
				RoboticonMarketScreen roboticonMarket = new RoboticonMarketScreen(this);
				roboticonMarket.addAnimation(new AnimationPhaseTimeout(getPlayer(), this, newPhaseState, 30));
				setScreen(roboticonMarket);
				break;

			// Phase 3: Roboticon Customisation
			case 3:
				AnimationPhaseTimeout timeoutAnimation = new AnimationPhaseTimeout(getPlayer(), this, newPhaseState, 30);
				gameScreen.addAnimation(timeoutAnimation);
				timeoutAnimation.setAnimationFinish(new IAnimationFinish() {
					@Override
					public void OnAnimationFinish() {
						gameScreen.getActors().hideInstallRoboticon();
					}
				});
				setScreen(gameScreen);
				break;

			// Phase 4: Purchase Resource
			case 4:
				gameScreen.hideNextStageButton(); // Added by Josh Neil
				generateResources();
				break;

			// Modified by Josh Neil
			case 5:
				// If the current player is not the last player
				// then we want the next player to have their turn.
				// However if the current player is the last player then
				// we want to go to the shared market phase (case 7)
				
				if(currentPlayer < playerList.size()-1){ 
					nextPlayer();
				}
				else{
					nextPlayer();
					setScreen(new ResourceMarketScreen(this));
					break;
				}
				
			
			// Added by Josh Neil - ensures that we go back to phase 1 if not all plots have been acquired or the last
				// player has not yet had their turn
				// and the game over screen otherwise
			case 6:
				if(plotManager.allOwned() && currentPlayer == playerList.size() -1){
					setScreen(new GameOverScreen(this));
					break;
				}
				else{
					phase = newPhaseState =1;
					// Deliberately falls through to the next case
				}
				
			
			// Phase 1: Enable of purchase LandPlot
			case 1:
				gameScreen.showNextStageButton(); // Added by Josh Neil - next stage button hidden during resource generation
				setScreen(gameScreen);
				landBoughtThisTurn = 0;
				gameScreen.addAnimation(new AnimationShowPlayer(getPlayerInt() + 1));
				break;
			
			
		}

		if (gameScreen != null)
			gameScreen.getActors().textUpdate();
	}

	/**
	 * Phase 4: generate resources.
	 */
	private void generateResources() {
		// Switch back to purchase to game screen.
		setScreen(gameScreen);

		// Generate resources.
		Player p = getPlayer();
		
		// Modified by Josh Neil - now accepts the values returned by Player.generateResources()
		// and produces an animation that displays this information on screen (see Player.generateResources
		// for a more in depth explanation)
		HashMap<ResourceType,Integer> generatedResources = p.generateResources();
		int energy = generatedResources.get(ResourceType.ENERGY);
		int food = generatedResources.get(ResourceType.FOOD);
		int ore = generatedResources.get(ResourceType.ORE);
		IAnimation animation = new AnimationAddResources(p, energy, food, ore);
		animation.setAnimationFinish(new IAnimationFinish() {
			@Override
			public void OnAnimationFinish() {
					nextPhase();
			}
		});
		gameScreen.addAnimation(animation);
	}

	/**
	 * Event callback on player bought a {@link io.github.teamfractal.entity.LandPlot}
	 */
	public void landPurchasedThisTurn() {
		landBoughtThisTurn ++;
	}

	/**
	 * Returns true if the current player is allowed to buy land and false otherwise
	 * @return true if the current player is allowed to buy land and false otherwise
	 */
	public boolean canPurchaseLandThisTurn () {
		return landBoughtThisTurn < 1;
	}

	/**
	 * Returns a string that describes the current phase of the game
	 * <p>
	 * Used to instruct users as to what they should be doing
	 * </p>
	 * @return Returns a string that describes the current phase of the game
	 */
	public String getPhaseString () {
		int phase = getPhase();

		switch(phase){
			case 1:
				return "Buy Land Plot";

			case 2:
				return "Purchase Roboticons";

			case 3:
				return "Install Roboticons";

			case 4:
				return "Resource Generation";

			case 5:
				return "Resource Auction";

			default:
				return "Unknown phase";
		}

	}

	/**
	 * Returns the current player (the one who's currently having their turn)
	 * @return The current player (the one who's currently having their turn)
	 */
	public Player getPlayer(){
		return this.playerList.get(this.currentPlayer);
	}
	
	/**
	 * Returns the playerIndex of the current player (the position at which they are stored in the playerList)
	 * @return The playerIndex of the current player (the position at which they are stored in the playerList)
	 */
	public int getPlayerInt(){
		return this.currentPlayer;
	}
	
	/**
	 * Used to allow the next player to have their turn
	 */
	public void nextPlayer(){
		if (this.currentPlayer == playerList.size() - 1){
			this.currentPlayer = 0; 
		}
		else{
			this.currentPlayer ++;
		}
	}

	/**
	 * @return The instance of PlotManager in use by the game
	 */
	public PlotManager getPlotManager() {
		return plotManager;
	}
}
