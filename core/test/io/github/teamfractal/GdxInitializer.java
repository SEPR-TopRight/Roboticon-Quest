package io.github.teamfractal;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import static org.mockito.Mockito.mock;

public class GdxInitializer {
	/**
	 * Fake application listener class
	 */
	@SuppressWarnings("EmptyClass")
	private static class TestApplication extends ApplicationAdapter {

	}

	/**
	 * init the environment, once.
	 */
	static {
		//init GDX environment to have the methods available
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		Gdx.gl = mock(GL20.class);
		Gdx.gl20 = mock(GL20.class);


		cfg.title = "Test";
		cfg.width = 2;
		cfg.height = 2;
		LwjglApplicationConfiguration.disableAudio = true;

		new LwjglApplication(new RoboticonQuest(), cfg);
	}



}
