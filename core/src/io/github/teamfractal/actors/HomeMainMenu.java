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

/**
 * Creates all of the UI widgets that are displayed on the main menu screen
 *
 */
public class HomeMainMenu extends Table {
	private RoboticonQuest game;
	private TextButton btnNewGame;
	private TextButton btnExit;
	private Texture backgroundImage;
	private SpriteBatch batch;

	private static Texture titleTexture = new Texture(Gdx.files.internal("roboticon_images/Roboticon_Quest_Title"));

	/**
	 * Initialise the Home Menu.
	 * @param game    The game object.
	 */
	public HomeMainMenu(RoboticonQuest game) {
		this.game = game;

		//Added by Christian Beddows
		batch = (SpriteBatch) game.getBatch();
		backgroundImage = new Texture(Gdx.files.internal("background/corridor.png"));

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
	 * by Christian Beddows
	 */
	public void drawBackground() {
		batch.begin();
		batch.draw(backgroundImage, 0, 0);
		batch.end();
	}

	/**
	 * Bind button events.
	 */
	private void bindEvents() {
		btnNewGame.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				game.setScreen(game.gameScreen);
				game.gameScreen.newGame();
			}
		});

		btnExit.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
	}
}
