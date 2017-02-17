package io.github.teamfractal.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.util.GameAudio;
import io.github.teamfractal.util.GameMusic;


public class HomeMainMenu extends Table {
	private RoboticonQuest game;
	private TextButton btnNewGame;
	private TextButton btnExit;
	private Image backgroundImage;
	private SpriteBatch batch;
	private float scaleFactorX;
	private float scaleFactorY;
	private GameAudio gameAudio;

	private static Texture titleTexture = new Texture(Gdx.files.internal("roboticon_images/Roboticon_Quest_Title"));

	/**
	 * Initialise the Home Menu.
	 * @param game    The game object.
	 */
	public HomeMainMenu(RoboticonQuest game) {
		this.game = game;

		//Added by Christian Beddows
		batch = (SpriteBatch) game.getBatch();
		backgroundImage = new Image(new Texture(Gdx.files.internal("background/corridor.jpg")));
		gameAudio = new GameAudio();

		// Create UI Components
		final Image imgTitle = new Image();
		imgTitle.setDrawable(new TextureRegionDrawable(new TextureRegion(titleTexture)));
		
		btnNewGame = new TextButton("New game!", game.skin);
		btnExit = new TextButton("Exit", game.skin);

		// Adjust properties.
		btnNewGame.pad(10);
		btnExit.pad(10);

		// Bind events.
		bindEvents();

		// Add UI Components to table.
		add(imgTitle);
		row();
		add(btnNewGame).pad(5);
		row();
		add(btnExit).pad(5);
	}

	/**
	 * Method to draw the background to the resource market
	 * @author cb1423
	 */
	public void drawBackground() {
		batch.begin();
		backgroundImage.draw(batch, 1);
		batch.end();
	}

	/**
	 * Scales the background to fit the screen
	 * @author cb1423
	 * @param width
	 * @param height
	 */
	public void resizeScreen(float width, float height) {
		System.out.println("screen: " + String.valueOf(width) + ", " + String.valueOf(height));
		System.out.println("background: " + String.valueOf(backgroundImage.getWidth()) + ", " + String.valueOf(backgroundImage.getHeight()));
		scaleFactorX = width/backgroundImage.getWidth();
		scaleFactorY = height/backgroundImage.getHeight();
		System.out.println("scale: " + String.valueOf(scaleFactorX) + ", " + String.valueOf(scaleFactorY));
		System.out.println("scaling: " + String.valueOf(scaleFactorX*backgroundImage.getWidth()) + ", " + String.valueOf(scaleFactorY*backgroundImage.getHeight()));
		backgroundImage.setScale(scaleFactorX,scaleFactorY);
	}

	/**
	 * Bind button events.
	 */
	private void bindEvents() {
		btnNewGame.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				gameAudio.click();
				game.setScreen(game.gameScreen);
				game.gameScreen.newGame();
			}
		});

		btnExit.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				gameAudio.click();
				Gdx.app.exit();
			}
		});
	}
}
