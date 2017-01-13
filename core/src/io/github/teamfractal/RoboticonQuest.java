package io.github.teamfractal;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.teamfractal.screens.MainMenuScreen;
import io.github.teamfractal.entity.Player;
import io.github.teamfractal.screens.GameScreen;
import io.github.teamfractal.util.PlotManager;

/**
 * This is the main game boot up class.
 * It will set up all the necessary classes.
 */
public class RoboticonQuest extends Game {
	private PlotManager plotManager;
	SpriteBatch batch;
	public Skin skin;
	public MainMenuScreen mainMenuScreen;
	public GameScreen gameScreen;
	private int phase;
	private int currentPlayer;
	public ArrayList<Player> playerList = new ArrayList<Player>();
	
	public RoboticonQuest(){
		this.currentPlayer = 0;
		this.phase = 1;

		Player player1 = new Player();
		Player player2 = new Player();
		this.playerList.add(player1);
		this.playerList.add(player2);

		this.plotManager = new PlotManager(this);
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setupSkin();
		
		

		// Setup other screens.
		mainMenuScreen = new MainMenuScreen(this);
		gameScreen = new GameScreen(this);

		setScreen(mainMenuScreen);
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
	
	public void nextPhase(){
		if(this.phase!= 5){
			this.phase += 1;
		}
		else{
			this.phase = 1;
			this.nextPlayer();
		}
	}

	public String getphaseEven(){

		int phase = getPhase();

		switch(phase){
			case 1:
				return "Buy Land Plot";

			case 2:
				return "Purchase Roboticons";

			case 3:
				return "Install Roboticons";

			case 4:
				return "Resource generation";

			case 5:
				return "Resource auction";

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
		if (playerList.size() -1 == this.currentPlayer){
			this.currentPlayer = 0; 
		}
		else{
			this.currentPlayer += 1;
		}
	}

	public PlotManager getPlotManager() {
		return plotManager;
	}
}
