package fun.madeby.jumper.common;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Logger;

import fun.madeby.jumper.CircleJumperGame;
import fun.madeby.jumper.config.GameConfig;

/**
 * Thread safe Singleton with low overhead and reasons for the extra 'result' var
 *
 * https://www.digitalocean.com/community/tutorials/thread-safety-in-java-singleton-classes
 */
public class GameManager {
        private static volatile GameManager gameManagerInstance;
        private static Object mutex = new Object();
        private static final Logger LOG = new Logger(GameManager.class.getName(), Logger.DEBUG);

        private Preferences prefs;
        private int score;
        private int displayedScore;
        private int highScore;
        private int displayedHighScore;

        public void reset() {
            score = 0;
            displayedScore = 0;
        }

        public void incrementScore(int amount){
            score += amount;
            LOG.debug("Score just incremented: " + score + " " + highScore);
            checkAndSetNewHighScore();
        }

    private void checkAndSetNewHighScore() {
            if (score > highScore) {
                highScore = score;
            }
    }

    public void updateDisplayScores(float delta) {
            LOG.debug("score/displayed score = " + score + " " + displayedScore);
            if (score > displayedScore) {
                LOG.debug("Calling smooth current score");
                smoothDisplayedScores(true, score, displayedScore, delta);
            }
            if (highScore > displayedHighScore) {
                LOG.debug("Calling smooth high score");
                smoothDisplayedScores(false, highScore, displayedHighScore, delta);
            }
    }

    /**
     * A clever way of drip feeding (smoothing) displayed scores by a measured minimum amount every frame
     * by testing the smallest amount between a current value and a factor of a delta increased
     * already displayed value. I wrote this so I had to look at it. Though to be fair it could be
     * extended.
     * @param isCurrentScore if true == score if false == highScore
     * @param currentValue the current in game value of the score or highScore
     * @param currentDisplayedValue the current displayed value of the score or highscore
     * @param delta the time elapsed since the last frame
     */
    private void smoothDisplayedScores(boolean isCurrentScore, int currentValue, int currentDisplayedValue , float delta) {
        int updateDisplayValueToThis;
            updateDisplayValueToThis = Math.min(currentValue, currentDisplayedValue + (int) (100 * delta));

            if (isCurrentScore) {
                LOG.debug("Updating displayed score to updateDisplayValueToThis");
                displayedScore = updateDisplayValueToThis;
            } else {
                LOG.debug("Updating high score to updateDisplayValueToThis");
                displayedHighScore = updateDisplayValueToThis;
            }



    }

    public int getDisplayedHighScore() {
        return displayedHighScore;
    }

    public int getDisplayedScore() {
        return this.displayedScore;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getScore() {
        return score;
    }

    // Singleton Thread safe low overhead logic:
        public static GameManager getInstance() {
            GameManager result = gameManagerInstance;
            if (result == null) {
                synchronized (mutex) {
                    result = gameManagerInstance;
                    if (result == null)
                        gameManagerInstance = result = new GameManager();
                }
            }
            return result;
        }

    private GameManager(){
        //lives implemented in GameController
        prefs = Gdx.app.getPreferences(CircleJumperGame.class.getSimpleName());
        highScore = prefs.getInteger(GameConfig.PREF_KEY_HIGH_SCORE, 0);
        displayedHighScore = highScore;
    }

    /**
     * Save highscore to GameConfig file.
     */
    public void updateHighScore(){
        if (score < highScore) {
            return;
        }
        highScore = score;
        prefs.putInteger(GameConfig.PREF_KEY_HIGH_SCORE, highScore);
        prefs.flush();
    }
}
