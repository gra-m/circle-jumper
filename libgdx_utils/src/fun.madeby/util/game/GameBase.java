package fun.madeby.util.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;

/**
 * Boilerplate code created by Goran in LibGDX course.
 */
public abstract class GameBase extends Game {
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
        postCreate();
    }

    protected abstract void postCreate();

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
