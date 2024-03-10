package org.milaifontanals.vampiresurvivors.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.milaifontanals.vampiresurvivors.R;

public class PaintView extends View {

    public PaintView(Context context) {
        super(context);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        float w = getWidth();
        float h = getHeight();

        /*Bitmap bmpCover = BitmapFactory.decodeResource(getResources(), R.drawable.cover);
        Rect src = new Rect(0, 194, 197, 473);
        RectF dst;

        float screenAspectRatio = w / h;
        float bmpAspectRatio = src.getWidth() / (float) bmpCover.getHeight();

        if (screenAspectRatio > bmpAspectRatio)
            dst = new RectF(0, 0, (bmpAspectRatio * h), h);
        else
            dst = new RectF(0, 0, w, (w / bmpAspectRatio));

        canvas.drawBitmap(bmpCover, src, dst, new Paint());*/

        Paint p = new Paint();
        /*p.setColor(Color.RED);
        p.setStyle(Paint.Style.FILL);
        canvas.drawPaint(p);
*/

        Paint pLine = new Paint();
        pLine.setColor(Color.RED);
        pLine.setStrokeWidth(3);
        pLine.setStyle(Paint.Style.STROKE);


        pLine.setColor(Color.YELLOW);
        pLine.setStrokeWidth(40);
        Path path = new Path();
        path.moveTo(500, 500);
        path.cubicTo(500, 500, 1400, 1000, 300, 1000);
        path.lineTo(150, 1200);
        path.moveTo(400, 500);
        path.lineTo(200, 600);
        path.moveTo(300, 550);
        path.lineTo(800, 1200);
        canvas.drawPath(path, pLine);


    }

    private static void dibuixaEspiral(Canvas canvas, Paint pLine, float w, float h) {
        float[] llistaPunts = new float[200];
        float x = w / 2;
        float y = h / 2;

        int factor = 20;

        for (int i = 0; i < 200; i += 4) {
            int f = i % 10;
            llistaPunts[i] = x;
            llistaPunts[i + 1] = y;
            switch (f) {
                case 4:
                    x -= factor;
                    break;
                case 8:
                    y += factor;
                    break;
                case 2:
                    x += factor;
                    break;
                case 6:
                    y -= factor;
                    break;
            }
            llistaPunts[i + 2] = x;
            llistaPunts[i + 3] = y;
            if (f == 4 || f == 2)
                factor += 20;
        }
        canvas.drawLines(llistaPunts, pLine);
    }
}
