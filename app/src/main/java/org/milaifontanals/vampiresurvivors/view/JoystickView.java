package org.milaifontanals.vampiresurvivors.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class JoystickView extends View implements ViewTreeObserver.OnGlobalLayoutListener {

    private Paint p, pJ, pB;
    private Point centre, pJoystick;
    private int h, w, mida;
    private float radiJoystick = 50;
    private JoystickMoveListener listener;

    public JoystickView(Context context) {
        this(context, null);
    }

    public void addListener(JoystickMoveListener listener) {
        this.listener = listener;
    }

    public interface JoystickMoveListener {
        void onMove(PointF move);
    }

    public JoystickView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(20);
        p.setColor(0x55222222);
        /*pB = new Paint();
        pB.setColor(0xFF006A4E);*/
        pJ = new Paint();
        pJ.setStyle(Paint.Style.FILL);
        pJ.setColor(0x55222222);


        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        /*canvas.drawPaint(pB);*/
        canvas.drawCircle(mida / 2, mida / 2, mida / 2 - p.getStrokeWidth(), p);
        if (pJoystick != null) canvas.drawCircle(pJoystick.x, pJoystick.y, radiJoystick, pJ);
    }

    @Override
    public void onGlobalLayout() {
        h = getHeight();
        w = getWidth();
        mida = Math.min(w, h);
        centre = new Point(mida / 2, mida / 2);
        radiJoystick = mida / 6;
        pJoystick = new Point(w / 2, h / 2);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if(event.getActionMasked() == MotionEvent.ACTION_UP) {
            pJoystick = new Point(w / 2, h / 2);
            invalidate();
            return true;
        }

        Point touchPoint = new Point((int) event.getX(), (int) event.getY());
        Point centre = new Point(mida / 2, mida / 2);
        double distance = Utils.getDistance(centre, touchPoint);
        double maxDistance = mida / 2 - radiJoystick;
        if (distance < maxDistance) {
            pJoystick = touchPoint;
        } else {
            Point vector = new Point(touchPoint.x - centre.x, touchPoint.y - centre.y);
            vector = Utils.scale(vector, maxDistance / distance);
            pJoystick = Utils.suma(centre, vector);
        }
        invalidate();
        if(listener!=null) listener.onMove(getDirection());
        return true;
    }

    public PointF getDirection() {
        double Distancia  = Utils.getDistance(centre, pJoystick);
        double maxDistance = mida / 2 - radiJoystick;

        PointF vector = new PointF(pJoystick.x - centre.x, pJoystick.y - centre.y);

        float escala = (float) (1 / maxDistance);
        return new PointF(vector.x * escala, vector.y * escala);
    }
}
