package io.github.teamfractal.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.actors.ResourceMarketActors;
import io.github.teamfractal.actors.RoboticonPurchaseActors;

public class RoboticonPurchaseScreen implements Screen {
	final RoboticonQuest game;
	final Stage stage;
	final Table table;
	private final RoboticonPurchaseActors actors;

	public RoboticonPurchaseScreen(RoboticonQuest game) {
		this.game = game;
		this.stage = new Stage(new ScreenViewport());
		this.table = new Table();
		table.setFillParent(true);

		actors = new RoboticonPurchaseActors(game, this);
		table.top().center().add(actors);

		stage.addActor(table);
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {

	}

	@Override
	public void resize(int width, int height) {

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

	}
}