package org.milaifontanals.vampiresurvivors.view;

import static androidx.core.math.MathUtils.clamp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import org.milaifontanals.vampiresurvivors.GameThread;
import org.milaifontanals.vampiresurvivors.MapGenerator;
import org.milaifontanals.vampiresurvivors.R;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread gameThread;

    // Game state
    Point posPersonatge = new Point(1500, 1500);


    Paint paint = new Paint();
    Paint pBackground = new Paint();
    Paint pLine = new Paint();
    MapGenerator map;

    Path path = new Path();
    JoystickView joystick;
    Bitmap pjSpriteBmp;
    private int SPRITE_WIDTH;
    private int SPRITE_HEIGHT;
    private int SPRITE_FRAMES;
    private int w, h;
    private int W, H;
    private int escala = 5;

    private boolean isMoving;
    private int currentFrame;
    private int cont = 0;


    public GameSurfaceView(Context context) {
        this(context, null);
    }

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.getHolder().addCallback(this);
        paint.setColor(0xFF006A4E);
        pBackground.setColor(0xFFAA0000);

        map = new MapGenerator(getResources(), context);

        pLine.setColor(Color.RED);
        pLine.setStrokeWidth(3);
        pLine.setStyle(Paint.Style.STROKE);


        pLine.setColor(Color.YELLOW);
        pLine.setStrokeWidth(40);
        path.moveTo(500, 500);
        path.cubicTo(500, 500, 1400, 1000, 300, 1000);
        path.lineTo(150, 1200);
        path.moveTo(400, 500);
        path.lineTo(200, 600);
        path.moveTo(300, 550);
        path.lineTo(800, 1200);


        pjSpriteBmp = BitmapFactory.decodeResource(getResources(), R.drawable.amongus_sprites);
        SPRITE_FRAMES = 4;
        SPRITE_WIDTH = pjSpriteBmp.getWidth() / SPRITE_FRAMES;
        SPRITE_HEIGHT = pjSpriteBmp.getHeight();

        W = map.getScenario().getWidth();
        H = map.getScenario().getHeight();

    }

    private Point getScreenCoordinates() {

        Point coordCorner = new Point();
        coordCorner.x = posPersonatge.x - w / 2;
        coordCorner.y = posPersonatge.y - h / 2;

        coordCorner.x = clamp(coordCorner.x, 0, W - w);
        coordCorner.y = clamp(coordCorner.y, 0, H - h);
        return coordCorner;
    }

    private Point getScreenCoordinatesPersonatge() {
        Point coordCorner = new Point();
        Point screenCorner = getScreenCoordinates();

        /*coordCorner.x = w/2;
        coordCorner.y = h/2;*/

        coordCorner.x = posPersonatge.x - screenCorner.x;
        coordCorner.y = posPersonatge.y - screenCorner.y;
        return coordCorner;
    }

    public void setJoystick(JoystickView joystick) {
        this.joystick = joystick;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        w = getWidth();
        h = getHeight();
        gameThread = new GameThread(this);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        gameThread.gameOver();
    }

    public void paint(Canvas canvas) {
        Point screenCorner = getScreenCoordinates();
        Point posPersonatgeScreen = getScreenCoordinatesPersonatge();
        Log.d("TAG", "Pantalla PAINT = " + screenCorner.x + " " + screenCorner.y);

        /*canvas.drawBitmap(map.getScenario(), - screenCorner.x, - screenCorner.y, null);*/
        canvas.drawBitmap(map.getScenario(), new Rect(screenCorner.x, screenCorner.y, screenCorner.x + w, screenCorner.y + h), new Rect(0, 0, w, h), null);

        canvas.drawBitmap(pjSpriteBmp,
                new Rect(SPRITE_WIDTH * (currentFrame - 1), 0, SPRITE_WIDTH * currentFrame, SPRITE_HEIGHT),
                new RectF(posPersonatgeScreen.x - 0.5f * SPRITE_WIDTH * escala,
                        posPersonatgeScreen.y - SPRITE_HEIGHT * 0.5f * escala,
                        posPersonatgeScreen.x + SPRITE_WIDTH * 0.5f * escala,
                        posPersonatgeScreen.y + SPRITE_HEIGHT * 0.5f * escala),
                null);
    }

    public void update() {
        if (joystick != null) {
            Log.d("TAG", "Pantalla UPDATE = " + getScreenCoordinates().x + " " + getScreenCoordinatesPersonatge().y);
            PointF pJoystick = joystick.getDirection();
            if (pJoystick != null) {
                posPersonatge.x += pJoystick.x * 28;
                posPersonatge.y += pJoystick.y * 28;

                posPersonatge.x = clamp(posPersonatge.x, escala*SPRITE_WIDTH/2, W - SPRITE_WIDTH/2*escala);
                posPersonatge.y = clamp(posPersonatge.y, escala*SPRITE_HEIGHT/2, H - SPRITE_HEIGHT/2*escala);

                boolean isMovingNext = Math.abs(pJoystick.x) > 0.001 || Math.abs(pJoystick.y) > 0.001;

                if (isMovingNext && !isMoving) {
                    currentFrame = 1;
                }
                if (isMoving) {
                    cont++;
                    if (cont == 8) {
                        currentFrame++;
                        cont = 0;
                    }
                    if (currentFrame >= SPRITE_FRAMES) {
                        currentFrame = 2;
                    }
                } else {
                    currentFrame = 1;
                }
                isMoving = isMovingNext;
            }
        }
    }
}
