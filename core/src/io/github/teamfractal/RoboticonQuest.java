package io.github.teamfractal;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.teamfractal.animation.AnimationPhaseTimeout;
import io.github.teamfractal.animation.AnimationShowPlayer;
import io.github.teamfractal.animation.IAnimationFinish;
import io.github.teamfractal.screens.*;
import io.github.teamfractal.entity.Market;
import io.github.teamfractal.entity.Player;
import io.github.teamfractal.util.GameMusic;
import io.github.teamfractal.util.PlotManager;

/**
 * This is the main game boot up class.
 * It will set up all the necessary classes.
 */
public class RoboticonQuest extends Game {
	static RoboticonQuest _instance;
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
	private GameMusic gameMusic;

	public int getPlayerIndex (Player player) {
		return playerList.indexOf(player);
	}

	public TiledMap tmx;
	
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

		startMusic();
	}

	public Batch getBatch() {
		return batch;
	}

	/**
	 * Initialises and starts the music playing
	 * @author cb1423
	 */
	private void startMusic(){
		gameMusic = new GameMusic();
		gameMusic.play();
		gameMusic.setLooping(true);
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
	 * Clean up
	 */
	@Override
	public void dispose () {
		mainMenuScreen.dispose();
		gameScreen.dispose();
		skin.dispose();
		batch.dispose();
	}
	
	public int getPhase(){
		return this.phase;
	}

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

	public void nextPhase () {
		int newPhaseState = phase + 1;
		phase = newPhaseState;
		// phase = newPhaseState = 4;

		System.out.println("RoboticonQuest::nextPhase -> newPhaseState: " + newPhaseState);
		switch (newPhaseState) {
			// Phase 2: Purchase Roboticon
			case 2:
				
				// Modified by Josh Neil - now passes market to roboticonMarket via constructor
				RoboticonMarketScreen roboticonMarket = new RoboticonMarketScreen(this,market);
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
				gameScreen.getActors().updateRoboticonSelection();
				setScreen(gameScreen);
				break;

			// Phase 4: Purchase Resource
			case 4:
				generateResources();
				break;

			// Modified by Josh Neil
			case 5:
				// If the current player is not the last player
				// then we want the next player to have their turn.
				// However if the current player is the last player then
				// we want to go to the shared market phase (case 7)
				System.out.println(currentPlayer);
				System.out.println(playerList.size());
				if(currentPlayer < playerList.size()-1){ 
					nextPlayer();
				}
				else{
					nextPlayer();
					setScreen(new ResourceMarketScreen(this));
					break;
				}
				
			
			// Added by Josh Neil - ensures that we go back to phase 1
			case 6:
				phase = newPhaseState =1;
				// Deliberately falls through to the next case
			
			// Modified by Josh Neil so that we go to the game over screen once all plots have been acquired
			// Phase 1: Enable of purchase LandPlot
			case 1:
				if(plotManager.allOwned()){ 
					setScreen(new GameOverScreen(this));
				}
				else{
					setScreen(gameScreen);
					landBoughtThisTurn = 0;
					gameScreen.addAnimation(new AnimationShowPlayer(getPlayerInt() + 1));
				}
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
		p.generateResources();
	}

	/**
	 * Event callback on player bought a {@link io.github.teamfractal.entity.LandPlot}
	 */
	public void landPurchasedThisTurn() {
		landBoughtThisTurn ++;
	}

	public boolean canPurchaseLandThisTurn () {
		return landBoughtThisTurn < 1;
	}

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

	public Player getPlayer(){
		return this.playerList.get(this.currentPlayer);
	}
	
	public int getPlayerInt(){
		return this.currentPlayer;
	}
	public void nextPlayer(){
		if (this.currentPlayer == playerList.size() - 1){
			this.currentPlayer = 0; 
		}
		else{
			this.currentPlayer ++;
		}
	}

	public PlotManager getPlotManager() {
		return plotManager;
	}
}
