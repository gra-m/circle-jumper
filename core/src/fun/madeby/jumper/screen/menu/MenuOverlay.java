package fun.madeby.jumper.screen.menu;


import com.badlogic.gdx.Gdx;
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

public class MenuOverlay extends Table {
    private final OverlayCallback callback;
    private Label highScoreLabel;

    public MenuOverlay(Skin skin, OverlayCallback callback){
        super(skin);
        this.callback = callback;
        init();
    }

    private void init() {
        defaults().pad(20);

        Table logoTable = new Table();
        logoTable.top();
        Image logoImage = new Image(getSkin(), RegionNames.LOGO);
        logoTable.add(logoImage);

        Table buttonTable = new Table();

        ImageButton playButton = new ImageButton(getSkin(), ButtonStyleNames.PLAY);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                callback.ready();
            }
        });
        ImageButton quitButton = new ImageButton(getSkin(), ButtonStyleNames.QUIT);
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();;
            }
        });


        Table scoreTable = new Table();
        scoreTable.add("BEST: ").row();
        highScoreLabel = new Label("", getSkin());
        updateLabel();
        scoreTable.add(highScoreLabel);

        buttonTable.add(playButton).left().expandX();
        buttonTable.add(scoreTable).bottom().expandX();
        buttonTable.add(quitButton).right().expandX();

        add(logoTable).top().grow().row();
        add(buttonTable).grow().center().row();
        center();
        setFillParent(true);
        pack();
    }

    public void updateLabel() {
        highScoreLabel.setText("" + GameManager.getInstance().getHighScore());
    }
}
