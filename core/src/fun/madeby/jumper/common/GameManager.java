package fun.madeby.jumper.common;


/**
 * Thread safe Singleton with low overhead and reasons for the extra 'result' var
 *
 * https://www.digitalocean.com/community/tutorials/thread-safety-in-java-singleton-classes
 */
public class GameManager {
        private static volatile GameManager gameManagerInstance;
        private static Object mutex = new Object();

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
            checkAndSetNewHighScore();
        }

    private void checkAndSetNewHighScore() {
            if (score > highScore) {
                highScore = score;
            }
    }

    public void updateDisplayScores(float delta) {
            if (score > displayedScore) {
                smoothDisplayedScores(true, score, displayedScore, delta);
            }
            if (highScore > displayedHighScore) {
                smoothDisplayedScores(false, highScore, displayedHighScore, delta);
            }
    }

    /**
     * A clever way of drip feeding (smoothing) displayed scores by a measured minimum amount every frame
     * by testing the smallest amount between a current value and a factor of a delta increased
     * already displayed value. I wrote this so I had to look at it. Though to be fair it could be
     * extended.
     * @param isCurrentScore if true == score if false == highScore
     * @param currentValue the value of the current score or highScore
     * @param displayedValue the current displayed value for the current value
     * @param delta the time elapsed since the last frame
     */
    private void smoothDisplayedScores(boolean isCurrentScore, int currentValue, int displayedValue , float delta) {
            displayedValue = Math.min(currentValue, displayedValue + (int) (100 * delta));

            if (isCurrentScore) {
                displayedScore = displayedValue;
            }
            displayedHighScore = displayedValue;



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

    private GameManager(){}
}
