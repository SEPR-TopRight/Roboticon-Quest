package io.github.teamfractal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.actors.HomeMainMenu;

/**
 * The main menu screen that is displayed when the game is first opened and can
 * be used to start a new game
 */
public class MainMenuScreen implements Screen {
	final RoboticonQuest game;
	final Stage stage;
	final Table table;
	private final HomeMainMenu homeMainMenu;

	/**
	 * Constructor
	 * @param game The RoboticonQuest object that will be used to actually play the game once started
	 */
	public MainMenuScreen(final RoboticonQuest game) {
		this.game = game;
		this.stage = new Stage(new ScreenViewport());
		this.table = new Table();
		table.setFillParent(true);

		homeMainMenu = new HomeMainMenu(game);
		table.center().center().add(homeMainMenu);

		stage.addActor(homeMainMenu.getBackgroundImage());
		stage.addActor(table);

		/*
		AdjustableActor actor1 = new AdjustableActor(game.skin, 0, 0, 100, "Ore: 10 Gold","Action");
		actor1.setActionEvent(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("Action clicked!");
			}
		});
		actor1.setPosition(40, 40);
		stage.addActor(actor1);
		*/
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//homeMainMenu.drawBackground();

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		homeMainMenu.resizeScreen((float) width, (float) height);
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
