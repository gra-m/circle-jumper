package fun.madeby.jumper.screen.game;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;

import fun.madeby.jumper.control.GameController;
import fun.madeby.util.GdxUtils;
import fun.madeby.util.game.GameBase;

public class GameScreen extends ScreenAdapter {
    private final GameBase game;
    private final AssetManager assetManager;

    private GameController controller;
    private GameRenderer renderer;

    public GameScreen(GameBase circleJumperGame) {
        this.game = circleJumperGame;
        this.assetManager = this.game.getAssetManager();
    }

    @Override
    public void show() {
        controller = new GameController();
        renderer = new GameRenderer(controller, game.getAssetManager(), game.getBatch());
    }

    @Override
    public void render(float delta) {
        GdxUtils.clearScreen();

        controller.update(delta);
        renderer.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
