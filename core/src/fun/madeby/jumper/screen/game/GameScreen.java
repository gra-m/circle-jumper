package fun.madeby.jumper.screen.game;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;

import fun.madeby.jumper.CircleJumperGame;

public class GameScreen extends ScreenAdapter {
    private final CircleJumperGame game;
    private final AssetManager assetManager;

    public GameScreen(CircleJumperGame circleJumperGame) {
        this.game = circleJumperGame;
        this.assetManager = this.game.getAssetManager();
    }
}
