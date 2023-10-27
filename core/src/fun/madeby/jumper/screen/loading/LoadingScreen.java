package fun.madeby.jumper.screen.loading;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import fun.madeby.jumper.config.GameConfig;
import fun.madeby.jumper.screen.game.GameScreen;
import fun.madeby.util.GdxUtils;
import fun.madeby.jumper.assetinfo.AssetDescriptors;
import fun.madeby.util.game.GameBase;

public class LoadingScreen extends ScreenAdapter {
    private static final float PROGRESS_BAR_WIDTH = GameConfig.HUD_WIDTH / 2f;
    private static final float PROGRESS_BAR_HEIGHT = GameConfig.HUD_HEIGHT / 13f;

    private final GameBase game;
    private final AssetManager assetManager;
    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer shapeRenderer;

    private float currentProgressBetweenZeroAndOne;
    private float minimumWaitDecrementedOnlyAfterLoadingCompleted = 0.75f;
    private BitmapFont hudFont;
    private boolean bothLoadingAndRenderingCompletedSoSafeToChange;

    public LoadingScreen(GameBase gameBase) {
        this.game = gameBase;
        this.assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        viewport = new FitViewport(camera.viewportWidth, camera.viewportHeight, camera);
        shapeRenderer = new ShapeRenderer();
        assetManager.load(AssetDescriptors.HUD_FONT);
    }

    @Override
    public void render(float delta) {
        update(delta);
        GdxUtils.clearScreen();
        viewport.apply();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        draw();

        shapeRenderer.end();

        if(bothLoadingAndRenderingCompletedSoSafeToChange) {
            game.setScreen(new GameScreen(game));
        }

    }

    private void draw() {
        float progressBarX = (GameConfig.HUD_WIDTH - PROGRESS_BAR_WIDTH) / 2f;
        float progressBarY = (GameConfig.HUD_HEIGHT - PROGRESS_BAR_HEIGHT) / 2f;

        shapeRenderer.rect(progressBarX, progressBarY,
                PROGRESS_BAR_WIDTH * currentProgressBetweenZeroAndOne, PROGRESS_BAR_HEIGHT);
    }

    private void update(float delta) {
        currentProgressBetweenZeroAndOne = assetManager.getProgress();

        if(assetManager.update()) {
            minimumWaitDecrementedOnlyAfterLoadingCompleted -= delta;
            if (minimumWaitDecrementedOnlyAfterLoadingCompleted <= 0) {
                bothLoadingAndRenderingCompletedSoSafeToChange = true;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
