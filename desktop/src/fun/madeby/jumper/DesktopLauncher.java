package fun.madeby.jumper;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import fun.madeby.jumper.config.GameConfig;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setWindowedMode((int) GameConfig.WIDTH, (int) GameConfig.HEIGHT);

		config.setForegroundFPS(60);
		config.setTitle("circle-jumper");

		new Lwjgl3Application(new CircleJumperGame(), config);
	}
}
