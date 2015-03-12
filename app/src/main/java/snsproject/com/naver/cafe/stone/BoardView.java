package snsproject.com.naver.cafe.stone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class BoardView extends View {

    private final DatabaseManager dbMgr;

    private final int MIN_LINE = 1;
    private final int MAX_LINE = 20;
    private final int SPACING;

    private int currentId = 0;

    private ArrayList<StoneInfo> stones = new ArrayList<>();

    private List<GiboInfo> giboInfos = null;

    public BoardView(Context context, AttributeSet attr) {
        super(context, attr);

        DisplayMetrics display = this.getResources().getDisplayMetrics();

        final int width = display.widthPixels;

        SPACING = width / MAX_LINE;

        dbMgr = new DatabaseManager(context);
        getGiboInfos(currentId);
    }

    public BoardView(Context context) {
        this(context, null);
    }

    private void getGiboInfos(int parentId) {
        List<GiboInfo> giboInfos = dbMgr.getGiboInfo(parentId);
        if (giboInfos != null && giboInfos.size() > 0) {

            int sum = 0;
            for (GiboInfo giboInfo : giboInfos) {
                sum += giboInfo.getCnt();
            }

            for (GiboInfo giboInfo : giboInfos) {
                giboInfo.setParentCnt(sum);
            }

            this.giboInfos = giboInfos;

            this.invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        Paint pnt = new Paint();

        // background
        pnt.setColor(Color.parseColor("#cc6600"));
        int newSize = (MAX_LINE * SPACING);
        final Rect r = new Rect(0, 0, newSize, newSize);
        canvas.drawRect(r, pnt);

        // draw lines
        pnt.setColor(Color.parseColor("#000000"));
        pnt.setStrokeWidth(3);

        for (int i = MIN_LINE; i < MAX_LINE; i++) {
            float newPos = i * SPACING;
            float minPos = MIN_LINE * SPACING;
            float maxPos = (MAX_LINE - 1) * SPACING;

            canvas.drawLine(minPos, newPos, maxPos, newPos, pnt);
            canvas.drawLine(newPos, minPos, newPos, maxPos, pnt);
        }

        // draw dots
        final int[] dots = {4, 10, 16};
        int size = dots.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                canvas.drawCircle(dots[i] * SPACING, dots[j] * SPACING,
                        10, pnt);
            }
        }

        if (this.giboInfos != null) {
            for (GiboInfo giboInfo : this.giboInfos) {
                pnt.setColor(Color.parseColor("#ff0000"));

                float x = giboInfo.getX() * SPACING;
                float y = giboInfo.getY() * SPACING;
                float radius = SPACING / 2;
                canvas.drawCircle(x, y, SPACING / 2, pnt);

                pnt.setColor(Color.parseColor("#ffffff"));
                pnt.setTextSize(30.0f);
                int percent = (int) (giboInfo.getCnt() * 100 / giboInfo.getParentCnt());
                canvas.drawText(Integer.toString(percent), x - radius, y, pnt);
            }
        } else {
            getGiboInfos(currentId);
        }

        {
            //Log.d("test", "curIndex : " + curIndex);

            if (curIndex != -1) {
                for (int i = 0; i <= curIndex; i++) {
                    StoneInfo stone = stones.get(i);

                    if (stone.isBlack()) {
                        pnt.setColor(Color.parseColor("#000000"));
                    } else {
                        pnt.setColor(Color.parseColor("#ffffff"));
                    }

                    //Log.d("test", "draw stone");

                    canvas.drawCircle(stone.getX() * SPACING, stone.getY() * SPACING, SPACING / 2, pnt);
                }
            }
        }
    }

    private enum AddType {
        ADD_LAST, EXIST, CLEAR_EXIST, INVALID;
    }

    private void addStone(int xIndex, int yIndex, boolean isBlack, int parentId, int id) {
        AddType type = AddType.INVALID;

        //Log.d("test", "xIndex : " + xIndex + ", yIndex : " + yIndex + ", parentId : " + parentId + ", id : " + id);

        int size = stones.size();
        int index = curIndex + 1;
        if (index < size) {
            StoneInfo stoneInfo = stones.get(index);
            if (stoneInfo != null) {

                if (xIndex == stoneInfo.getX() && yIndex == stoneInfo.getY() && isBlack == stoneInfo.isBlack() && parentId == stoneInfo.getParentId()) {
                    curIndex = index;

                    currentId = stoneInfo.getId();

                    type = AddType.EXIST;
                    //Log.d("test", "exist");
                } else {
                    //Log.d("test", "before : " + stones.size());

                    for (int i = size - 1; index <= i; i--) {
                        stones.remove(i);
                    }

                    //Log.d("test", "after : " + stones.size());

                    stones.add(new StoneInfo(xIndex, yIndex, isBlack, parentId, id));

                    curIndex = stones.size() - 1;

                    currentId = id;

                    type = AddType.CLEAR_EXIST;
                    //Log.d("test", "CLEAR_EXIST");
                }
            }
        } else {
            stones.add(new StoneInfo(xIndex, yIndex, isBlack, parentId, id));

            curIndex = stones.size() - 1;

            currentId = id;

            type = AddType.ADD_LAST;

            //Log.d("test", "add last");
        }


        if (type == AddType.ADD_LAST || type == AddType.EXIST || type == AddType.CLEAR_EXIST) {
            //
            this.giboInfos = null;
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        int xIndex = Math.round(x / SPACING);
        int yIndex = Math.round(y / SPACING);

        if ((xIndex >= MIN_LINE && xIndex < MAX_LINE) && (yIndex >= MIN_LINE && yIndex < MAX_LINE)) {

            if (this.giboInfos != null) {
                for (GiboInfo giboInfo : this.giboInfos) {
                    if (giboInfo.getX() == xIndex && giboInfo.getY() == yIndex) {
                        addStone(xIndex, yIndex, (giboInfo.getColor() == 'B'), giboInfo.getParentId(), giboInfo.getId());
                        break;
                    }
                }
            }
        }

        return super.onTouchEvent(event);
    }

    public void goFirst() {

    }

    public void goPrevBranch() {

    }

    private int curIndex = -1;

    public void goPrev() {
        int size = stones.size();

        if (curIndex >= 0 && curIndex < size) {
            int index = curIndex--;

            getGiboInfos(stones.get(index).getParentId());

            invalidate();
        }
//        if (prevStone == null)
//            return;
//
//        StoneInfo prevStone = null;
//
//        for (StoneInfo stoneInfo : stones) {
//
//        }
    }

    public void goNext() {

    }

    public void goNextBranch() {

    }

    public void goEnd() {

    }


}
