package snsproject.com.naver.cafe.stone.board;

/**
 * Created by ws on 2015-03-07.
 */
public class GiboInfo {
    private float mTypeName;
    private char mColor;
    private int mX;
    private int mY;
    private int mCnt;
    private int mId;
    private int mParentId;
    private int mParentCnt;

    public GiboInfo(float typeName, char color, int x, int y, int cnt, int id, int pId) {
        mTypeName = typeName;
        mColor = color;
        mX = x;
        mY = y;
        mCnt = cnt;
        mId = id;
        mParentId = pId;
    }

    public char getColor() {
        return mColor;
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    public int getId() {
        return mId;
    }

    public int getParentId() {
        return mParentId;
    }

    public int getCnt() {
        return mCnt;
    }

    public void setParentCnt(int parentCnt) {
        this.mParentCnt = parentCnt;
    }

    public int getParentCnt() {
        return mParentCnt;
    }
}
