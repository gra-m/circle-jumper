package fun.madeby.jumper.common;

public enum GameState {
    MENU,
    READY,
    PLAYING,
    GAME_OVER;

    public boolean isMenu() {return this == MENU;}
    public boolean isReady() {return this == READY;}
    public boolean isPlaying() {return this == PLAYING;}
    public boolean isGameOver() {return this == GAME_OVER;}

    public boolean isPlayingOrReady() {
        return isReady() ||  isPlaying();
    }
}
