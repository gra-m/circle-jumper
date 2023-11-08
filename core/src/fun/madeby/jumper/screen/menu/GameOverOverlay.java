package fun.madeby.jumper.screen.menu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import fun.madeby.jumper.assetinfo.ButtonStyleNames;
import fun.madeby.jumper.assetinfo.RegionNames;
import fun.madeby.jumper.common.GameManager;

public class GameOverOverlay extends Table {
    private final OverlayCallback callback;
    
    private Label scoreLabel;
    private Label highScoreLabel;
    private final Skin skin;
    
    public GameOverOverlay(Skin skin, OverlayCallback callback) {
        super(skin);
        this.skin = skin;
        this.callback = callback;
        init();
    }

    private void init() {
        defaults().pad(20);

        Image gameOverImage = new Image(getSkin(), RegionNames.GAME_OVER);

        Table scoreTable = new Table(skin);
        scoreTable.defaults().pad(10);
        scoreTable.setBackground(RegionNames.PANEL);

        scoreTable.add("SCORE: ").row();
        scoreLabel = new Label("",  skin);
        scoreTable.add(scoreLabel).row();

        scoreTable.add("BEST: ").row();
        highScoreLabel = new Label("", skin);
        scoreTable.add(highScoreLabel);

        scoreTable.center();

        //
        Table buttonTable = new Table();
        buttonTable.pad(10);

        ImageButton homeButton = new ImageButton(skin, ButtonStyleNames.HOME);
        homeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
               callback.home();
            }
        });

        ImageButton restartButton = new ImageButton(skin, ButtonStyleNames.RESTART);
        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                callback.ready();
            }
        });

        buttonTable.add(homeButton).left().expandX();
        buttonTable.add(restartButton).right().expandX();

        add(gameOverImage).row();
        add(scoreTable).row();
        add(buttonTable).grow();

        center();
        setFillParent(true);
        pack();

        updateLabels();
    }

    public void updateLabels() {
        scoreLabel.setText(GameManager.getInstance().getScore());
        highScoreLabel.setText(GameManager.getInstance().getHighScore());
    }
}
