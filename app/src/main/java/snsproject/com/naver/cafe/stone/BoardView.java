package snsproject.com.naver.cafe.stone;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class BoardView extends View {

    private DatabaseManager dbMgr;

    private final int MIN_LINE = 1;
    private final int MAX_LINE = 20;
    private final int SPACING;

    public BoardView(Activity activity) {
        super(activity);

        DisplayMetrics display = this.getResources().getDisplayMetrics();

        final int width = display.widthPixels;

        SPACING = width / MAX_LINE;

        dbMgr = new DatabaseManager(activity);
        getGiboInfos(currentId);
    }

    private int currentId;

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
                canvas.drawText(Integer.toString(percent), x-radius, y, pnt);
            }
        } else {
            getGiboInfos(currentId);
        }

        {
            for (StoneInfo stone : stones) {
                if (stone.isBlack()) {
                    pnt.setColor(Color.parseColor("#000000"));
                } else {
                    pnt.setColor(Color.parseColor("#ffffff"));
                }
                canvas.drawCircle(stone.getX() * SPACING, stone.getY() * SPACING, SPACING / 2, pnt);
            }


        }
    }


    private ArrayList<StoneInfo> stones = new ArrayList<>();
    private List<GiboInfo> giboInfos;

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
                        stones.add(new StoneInfo(xIndex, yIndex, (giboInfo.getColor() == 'B')));
                        this.giboInfos = null;
                        invalidate();

                        currentId = giboInfo.getId();

                        break;
                    }
                }
            }
        }

        return super.onTouchEvent(event);
    }

    public void setGiboInfos(List<GiboInfo> giboInfos) {
        this.giboInfos = giboInfos;
    }
}
