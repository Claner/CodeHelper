package com.example.clanner.codehelper.utils;

import android.graphics.Paint;

/**
 * Created by Clanner on 2016/6/29.
 */
public class RenderUtil {

    public static float getBaseline(float top, float bottom, Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (top + bottom - fontMetrics.bottom - fontMetrics.top) / 2;
    }

    public static float getStartX(float middle, Paint paint, String text) {
        return middle - paint.measureText(text) * 0.5f;
    }

    public static Paint getPaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        return paint;
    }
}
