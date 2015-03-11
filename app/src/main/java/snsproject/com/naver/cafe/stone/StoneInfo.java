package snsproject.com.naver.cafe.stone;

/**
 * Created by ws on 2015-03-07.
 */
public class StoneInfo {
    private int x;
    private int y;
    private boolean isBlack;

    public StoneInfo(int x, int y, boolean isBlack) {
        this.x = x;
        this.y = y;
        this.isBlack = isBlack;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    public boolean isBlack() {
        return isBlack;
    }
}
