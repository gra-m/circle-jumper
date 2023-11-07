package fun.madeby.jumper.assetinfo;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AssetDescriptors {

    public static final AssetDescriptor<BitmapFont> HUD_FONT =
            new AssetDescriptor<BitmapFont>(AssetPaths.HUD_FONT_PATH, BitmapFont.class);
    public static final AssetDescriptor<TextureAtlas> GAME_PLAY_ATLAS =
        new AssetDescriptor<>(AssetPaths.GAME_PLAY, TextureAtlas.class);

    private AssetDescriptors(){}
}
