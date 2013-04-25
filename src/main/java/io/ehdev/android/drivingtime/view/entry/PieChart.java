package io.ehdev.android.drivingtime.view.entry;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class PieChart extends View {

    float percentComplete = .25f;
    private static final float BUFFER = 0.03f;

    public PieChart(Context context) {
        super(context);
    }

    public float getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(float percentComplete) {
        this.percentComplete = percentComplete;
    }

    @Override
    protected void onDraw(Canvas canvas){
        float startAngle = 90 - 360 * percentComplete / 2;
        float endAngle = 90 + 360 * percentComplete / 2;
        RectF rect = new RectF(canvas.getClipBounds());
        float maxSize = rect.width() > rect.height() ? rect.height() : rect.width();
        float boundry = maxSize * BUFFER;

        float centerX = rect.centerX();
        float centerY = rect.centerY();
        RectF renderViewArea = new RectF(boundry, boundry, maxSize * (1 - BUFFER), maxSize * (1 - BUFFER));

        Paint paint = new Paint();
        paint.setAlpha(255);
        paint.setFlags(Paint.DITHER_FLAG | Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG);

        paint.setColor(Color.WHITE);
        canvas.drawCircle(centerX, centerY, maxSize / 2, paint);

        drawCircle(canvas, startAngle, endAngle, renderViewArea, paint);

        drawWhiteLines(canvas, startAngle, maxSize, boundry, centerX, centerY, paint, endAngle);

    }

    private void drawCircle(Canvas canvas, float startAngle, float endAngle, RectF renderViewArea, Paint paint) {
        paint.setColor(Color.parseColor("#3A75C4"));
        canvas.drawArc(renderViewArea, startAngle, 360 * percentComplete, true, paint);

        paint.setColor(Color.parseColor("#616365"));
        canvas.drawArc(renderViewArea, endAngle, 360 * (1 - percentComplete), true, paint);
    }

    private void drawWhiteLines(Canvas canvas, float startAngle, float maxSize, float boundary, float centerX, float centerY, Paint paint, float endAngle) {
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(boundary);
        float startLineX = (float)Math.cos(Math.toRadians(360 - startAngle)) * maxSize / 2;
        float startLineY = (float)Math.sin(Math.toRadians(360 - startAngle)) * maxSize / 2;
        if(startAngle < 180) startLineY = -1 * startLineY;
        if(startAngle > 180) startLineX = -1 * startLineX;
        canvas.drawLine(centerX, centerY, centerX + startLineX, centerY + startLineY, paint);

        float endLineX = (float)Math.cos(Math.toRadians(360 - endAngle)) * maxSize / 2;
        float endLineY = (float)Math.sin(Math.toRadians(360 - endAngle)) * maxSize / 2;
        if(startAngle < 180) endLineY = -1 * endLineY;
        if(startAngle > 180) endLineX = -1 * endLineX;
        canvas.drawLine(centerX, centerY, centerX + endLineX, centerY + endLineY, paint);

        canvas.drawCircle(centerX, centerY, boundary / 2, paint);
    }
}
