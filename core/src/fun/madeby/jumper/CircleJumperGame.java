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
import fun.madeby.util.game.GameBase;

public class CircleJumperGame extends GameBase {

	/**
	 * Called by Create() in super class (game library in libgdx_utils) so GameBase code is not
	 * repeated with every game created.
	 */
	@Override
	protected void postCreate() {
		setScreen(new GameScreen(this));
	}

}
