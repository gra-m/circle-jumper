package fun.madeby.jumper.screen.game;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import fun.madeby.jumper.config.GameConfig;
import fun.madeby.jumper.entity.Planet;
import fun.madeby.jumper.entity.Monster;
import fun.madeby.util.ViewportUtils;
import fun.madeby.util.debug.DebugCameraController;

public class GameRenderer implements Disposable {
    private static final Logger LOG = new Logger(GameRenderer.class.getName(), Logger.DEBUG);
    private final GameController controller;

    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer shapeRenderer;

    private DebugCameraController debugCameraController;

    public GameRenderer(GameController gameController) {
        controller = gameController;
        init();
    }

    private void init() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();
        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);
    }

    public void render(float delta) {
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyInternalPositionAndZoomToThisCamera(camera);

        //testDebugControllerRendering();

        renderDebug();
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
        Monster player = controller.getMonster();
        Rectangle playerBounds = player.getBoundsThatAreUsedForCollisionDetection();
        shapeRenderer.rect(playerBounds.x, playerBounds.y, playerBounds.width, playerBounds.height);

    }
}
