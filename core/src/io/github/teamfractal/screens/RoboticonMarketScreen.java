package io.github.teamfractal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.actors.RoboticonMarketActors;
import io.github.teamfractal.entity.Market;

public class RoboticonMarketScreen extends AbstractAnimationScreen implements Screen {

	final RoboticonQuest game;
	final Stage stage;
	final Table table;
	private RoboticonMarketActors actors;
	
	/// Modified by Josh Neil so that it takes the market so that it can pass it to the RoboticonMarketActors constructor
	public RoboticonMarketScreen(final RoboticonQuest game,Market market) {
		this.game = game;
		this.stage = new Stage(new ScreenViewport());
		this.table = new Table();
		table.setFillParent(true);
		
		actors = new RoboticonMarketActors(game, this, market);

		table.top().left().add(actors);
		
		stage.addActor(table);
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		actors.drawBackground();

		stage.act(delta);
		stage.draw();

		renderAnimation(delta);
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		game.getBatch().setProjectionMatrix(stage.getCamera().combined);
		actors.widgetUpdate();
		actors.resizeScreen(width, height);
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
	public Stage getStage(){
		return this.stage;
	}

	@Override
	protected RoboticonQuest getGame() {
		return game;
	}

	@Override
	public Size getScreenSize() {
		Size s = new Size();
		s.Width = stage.getViewport().getScreenWidth();
		s.Height = stage.getViewport().getScreenHeight();
		return s;
	}
}
