package fun.madeby.jumper.assetinfo;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class AssetDescriptors {

    public static final AssetDescriptor<BitmapFont> HUD_FONT =
            new AssetDescriptor<BitmapFont>(AssetPaths.HUD_FONT_PATH, BitmapFont.class);

    private AssetDescriptors(){}
}
