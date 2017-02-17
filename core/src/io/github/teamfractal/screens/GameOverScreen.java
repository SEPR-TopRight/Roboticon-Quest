package io.github.teamfractal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.actors.AdjustableActor;
import io.github.teamfractal.actors.GameOverScreenActors;
import io.github.teamfractal.actors.HomeMainMenu;

public class GameOverScreen implements Screen {
	final RoboticonQuest game;
	final Stage stage;
	final Table table;
	private final GameOverScreenActors gameOverScreenActors;

	public GameOverScreen(final RoboticonQuest game) {
		this.game = game;
		this.stage = new Stage(new ScreenViewport());
		this.table = new Table();
		table.setFillParent(true);

		gameOverScreenActors = new GameOverScreenActors(game);
		table.center().center().add(gameOverScreenActors);
		stage.addActor(gameOverScreenActors.getBackgroundImage());
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

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		gameOverScreenActors.resizeScreen((float)width, (float) height);
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
