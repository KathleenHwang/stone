package snsproject.com.naver.cafe.stone;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;



public class BoardView extends View {

    public BoardView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        Paint pnt = new Paint();

        final int MIN_LINE = 1;
        final int MAX_LINE = 20;
        final int SPACING = 30;

        // 배경 그리기
        pnt.setColor(Color.parseColor("#cc6600"));
        int newSize = (MAX_LINE * SPACING);
        final Rect r = new Rect(0, 0, newSize, newSize);
        canvas.drawRect(r, pnt);

        // 보드 라인 그리기
        pnt.setColor(Color.parseColor("#000000"));

        for (int i = MIN_LINE; i < MAX_LINE; i++) {
            float newPos = i * SPACING;
            float minPos = MIN_LINE * SPACING;
            float maxPos = (MAX_LINE - 1) * SPACING;

            canvas.drawLine(minPos, newPos, maxPos, newPos, pnt);
            canvas.drawLine(newPos, minPos, newPos, maxPos, pnt);
        }

        // 화점 찍기
        final int[][] dots = { { 4, 4 }, { 4, 10 }, { 4, 16 }, { 10, 4 },
                { 10, 10 }, { 10, 16 }, { 16, 4 }, { 16, 10 }, { 16, 16 } };

        for (int i = 0; i < dots.length; i++) {
            canvas.drawCircle(dots[i][0] * SPACING, dots[i][1] * SPACING,
                    5, pnt);

        }
    }
}
