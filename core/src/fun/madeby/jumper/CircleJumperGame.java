package fun.madeby.jumper;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ScreenUtils;

import fun.madeby.jumper.screen.game.GameScreen;

public class CircleJumperGame extends Game {
	private SpriteBatch batch;
	private AssetManager assetManager;


	@Override
	public void create () {

		batch = new SpriteBatch();
		assetManager = new AssetManager();

		// set log levels
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		assetManager.getLogger().setLevel(Logger.DEBUG);

		// set initial screen
		setScreen(new GameScreen(this));
	}

	@Override
	public void dispose () {
		// disposes current screen
		super.dispose();
		batch.dispose();
		assetManager.dispose();
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public SpriteBatch getBatch() {
		return batch;
	}
}
