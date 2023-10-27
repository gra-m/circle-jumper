package fun.madeby.jumper;


//imports fine
import fun.madeby.jumper.screen.game.GameScreen;
import fun.madeby.jumper.screen.loading.LoadingScreen;
import fun.madeby.util.game.GameBase;

public class CircleJumperGame extends GameBase {

	/**
	 * Called by Create() in super class (game library in libgdx_utils) so GameBase code is not
	 * repeated with every game created.
	 */
	@Override
	protected void postCreate() {
		setScreen(new LoadingScreen(this));
	}

}
