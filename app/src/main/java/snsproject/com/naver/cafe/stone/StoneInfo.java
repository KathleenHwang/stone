package snsproject.com.naver.cafe.stone;

/**
 * Created by ws on 2015-03-07.
 */
public class StoneInfo {
    private int x;
    private int y;
    private boolean isBlack;
    private int parentId;
    private int id;

    public StoneInfo(int x, int y, boolean isBlack, int parentId, int id) {
        this.x = x;
        this.y = y;
        this.isBlack = isBlack;
        this.parentId = parentId;
        this.id = id;
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

    public int getParentId() {
        return parentId;
    }

    public int getId() {
        return id;
    }
}
