package fun.madeby.jumper.screen.game;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import fun.madeby.jumper.assetinfo.AssetDescriptors;
import fun.madeby.jumper.common.GameManager;
import fun.madeby.jumper.config.GameConfig;
import fun.madeby.jumper.entity.Coin;
import fun.madeby.jumper.entity.Obstacle;
import fun.madeby.jumper.entity.Planet;
import fun.madeby.jumper.entity.Monster;
import fun.madeby.util.ViewportUtils;
import fun.madeby.util.debug.DebugCameraController;

public class GameRenderer implements Disposable {
    private static final Logger LOG = new Logger(GameRenderer.class.getName(), Logger.DEBUG);
    private final GameController controller;
    private final GlyphLayout layout = new GlyphLayout();
    private final AssetManager assetManager;
    private final SpriteBatch spriteBatch;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Viewport hudViewport;
    private BitmapFont hudFont;

    private ShapeRenderer shapeRenderer;

    private DebugCameraController debugCameraController;

    public GameRenderer(GameController gameController, AssetManager assetManager,
                        SpriteBatch spriteBatch) {
        controller = gameController;
        this.assetManager = assetManager;
        this.spriteBatch = spriteBatch;
        init();
    }

    private void init() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        shapeRenderer = new ShapeRenderer();
        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);

        hudFont = assetManager.get(AssetDescriptors.HUD_FONT);
    }

    public void render(float delta) {
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyInternalPositionAndZoomToThisCamera(camera);

        //testDebugControllerRendering();

        renderDebug();
        renderHud();
    }

    private void renderHud() {
        hudViewport.apply();
        spriteBatch.setProjectionMatrix(hudViewport.getCamera().combined);

        spriteBatch.begin();
        drawHUD();
        spriteBatch.end();
    }

    private void drawHUD() {
        drawScores();
        
        if (controller.getWaitBetweenGames() >= 0)
            drawWaitTimer();
        
    }

    private void drawWaitTimer() {
        int timeToDisplay = (int) controller.getWaitBetweenGames();
        String timeDisplayString = timeToDisplay == 0 ? "GO!" : "" + timeToDisplay;

        layout.setText(hudFont, timeDisplayString);
        hudFont.draw(spriteBatch, layout,
                (GameConfig.HUD_WIDTH - layout.width) / 2,
                (GameConfig.HUD_HEIGHT + layout.height) / 2);

    }

    private void drawScores() {
        String scoreString = "SCORE: " + GameManager.getInstance().getDisplayedScore();
        String highScoreString = "HIGH-SCORE: " + GameManager.getInstance().getDisplayedHighScore();

        float allHudTextY = hudViewport.getWorldHeight() - GameConfig.HUD_SCORE_PADDING;
        float highScoreX = GameConfig.HUD_SCORE_PADDING;

        layout.setText(hudFont, highScoreString);
        hudFont.draw(spriteBatch, layout, highScoreX, allHudTextY);

        layout.setText(hudFont, scoreString);
        float scoreX = hudViewport.getWorldWidth() - (layout.width + GameConfig.HUD_SCORE_PADDING);

        hudFont.draw(spriteBatch, layout, scoreX, allHudTextY);
    }


    /**
     * Given that viewports always have to be resized:
     * ScreenAdapter method propagated into GameRenderer (bespoke class). Enabling model and view
     * separation of concerns.
     * @param width
     * @param height
     */
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
        ViewportUtils.debugPixelsPerUnit(viewport);
    }


    @Override
    public void dispose() {
        shapeRenderer.dispose();

    }

    /**
     * Test method -> renders 6 WU diameter circle in 30 segments
     * left here as test method for debug controller rendering Before
     * calling drawDebug() so this should be disappearing soon right?
     */
    private void testDebugControllerRendering() {
        viewport.apply();
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.circle(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y, 3,30);
        shapeRenderer.end();
    }

    private void renderDebug() {
        viewport.apply();
        ViewportUtils.drawGrid(viewport, shapeRenderer, GameConfig.CELL_SIZE);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        drawDebug();

        shapeRenderer.end();

    }

    private void drawDebug() {

        shapeRenderer.setColor(Color.RED);
        Planet planet = controller.getPlanet();
        Circle planetBounds = planet.getBoundsThatAreUsedForCollisionDetection();
        shapeRenderer.circle(planetBounds.x, planetBounds.y, planetBounds.radius, 30);

        shapeRenderer.setColor(Color.YELLOW);
        Monster monster = controller.getMonster();
        Rectangle monsterBounds = monster.getBoundsThatAreUsedForCollisionDetection();
        shapeRenderer.rect(
                monsterBounds.x, monsterBounds.y,
                0, 0,
                monsterBounds.width, monsterBounds.height,
                1, 1, GameConfig.START_ANGLE - monster.getCircumferencePositionInDegrees());

        shapeRenderer.setColor(Color.CYAN);
        for(Coin coin : controller.getCoins()) {
            Rectangle coinBounds = coin.getBoundsThatAreUsedForCollisionDetection();
            shapeRenderer.rect(
                    coinBounds.x, coinBounds.y,
                    0, 0,
                    coinBounds.width, coinBounds.height,
                    1, 1,
                    GameConfig.START_ANGLE - coin.getCircumferencePositionInDegrees());

        }

        for(Obstacle obstacle : controller.getObstacles()) {
            Rectangle obstacleBounds = obstacle.getBoundsThatAreUsedForCollisionDetection();
            Rectangle invisibleSensorBounds = obstacle.getJumpSuccessSensor();

            shapeRenderer.setColor(Color.MAGENTA);
            shapeRenderer.rect(
                    obstacleBounds.x, obstacleBounds.y,
                    0, 0,
                    obstacleBounds.width, obstacleBounds.height,
                    1, 1,
                    GameConfig.START_ANGLE - obstacle.getDegreeOfAngle());

            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(
                    invisibleSensorBounds.x, invisibleSensorBounds.y,
                    0, 0,
                    invisibleSensorBounds.width, invisibleSensorBounds.height,
                    1, 1,
                    GameConfig.START_ANGLE - obstacle.getSensorDegreeOfAngle());
        }

    }
}
