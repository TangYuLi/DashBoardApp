package com.example.yuli.myapplication.CustomView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.yuli.myapplication.R;

public class QuaDashBoardView extends View {

    private float screenWidth;
    private float screenHeight;
    private float outRectWidth;
    private float outRectHeight;
    private int whichQua;
    private float outRectLeft;
    private float outRectTop;
    private float outRectRight;
    private float outRectBottom;
    private float radius;
    private float centerX;
    private float centerY;
    private int startAngle;
    private Bitmap bitmap;
    private int bitmapRotateRound;
    private float bitmapWidth;
    private float bitmapHeight;
    private float quaLeft;
    private float quaRight;
    private float quaTop;
    private float quaBottom;
    private float graduationStartX;
    private float graduationStartY;
    private float graduationStopX;
    private float graduationStopY;
    private float textStartX;
    private float textStartY;
    private float dashLineStartX;
    private float dashLineStartY;
    private float dashLineStopX;
    private float dashLineStopY;
    private final int lineLen_XL = 120;
    private final int lineLen_L = 90;
    private final int lineLen_M = 60;
    private final int lineLen_S = 30;
    private int round;
    private int rotateRound;
    private boolean isFirstTimeIn = true;
    private Paint paint;

    public QuaDashBoardView(Context context, int whichQua) {
        super(context);
        this.whichQua = whichQua;
    }

    public QuaDashBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public QuaDashBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setPaint(Paint.Style style, boolean isAntiAlias, float strokeWidth, int color, int textSize) {
        paint.setStyle(style);
        paint.setAntiAlias(isAntiAlias);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(color);
        paint.setTextSize(textSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //动态获取屏幕宽高，并且初始化相关数据以便后续使用
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
        outRectWidth = screenWidth <= screenHeight ? screenWidth - 100 : screenHeight - 100;
        outRectHeight = outRectWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint = new Paint();
        initData();
        //画外圆
        drawOutRect(canvas);
        //画象限扇形
        drawWhichQua(canvas);
    }

    private void initData() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.dashboard);
        bitmapWidth = 100.0f;
        bitmapHeight = 100.0f;
        radius = outRectWidth - bitmapWidth / 2;
    }

    /**
     * 描透明的长方形可以更好地定点其余元素的坐标
     * @param canvas
     */
    private void drawOutRect(Canvas canvas) {
        setPaint(Paint.Style.STROKE, true, 5, Color.alpha(0), 40);

        int left = (int) (screenWidth / 2 - outRectWidth / 2);
        int top = (int) (screenHeight / 2 - outRectHeight / 2);
        int right = (int) (screenWidth / 2 + outRectWidth / 2);
        int bottom = (int) (screenHeight / 2 + outRectHeight / 2);
        Rect rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, paint);

        //初始化数据操作
        outRectLeft = rect.left;
        outRectTop = rect.top;
        outRectRight = rect.right;
        outRectBottom = rect.bottom;
    }

    /**
     * 不同象限中的数据初始计算方式不同
     * @param canvas
     */
    private void drawWhichQua(Canvas canvas) {

        //根据象限来初始化相关数据
        RectF rectF;
        switch (whichQua) {
            case 1:
                centerX = outRectLeft + bitmapWidth / 2;
                centerY = outRectBottom - bitmapHeight / 2;
                startAngle = -90;
                graduationStartX = centerX;
                graduationStartY = outRectTop;
                bitmapRotateRound = -45;
                setPaint(Paint.Style.FILL, true, radius, Color.GREEN, 40);
                break;
            case 2:
                centerX = outRectLeft + bitmapWidth / 2;
                centerY = outRectTop + bitmapHeight / 2;
                startAngle = 0;
                graduationStartX = centerX + radius;
                graduationStartY = centerY;
                bitmapRotateRound = 45;
                setPaint(Paint.Style.FILL, true, radius, Color.YELLOW, 40);
                break;
            case 3:
                centerX = outRectRight - bitmapWidth / 2;
                centerY = outRectTop + bitmapHeight / 2;
                startAngle = 90;
                graduationStartX = centerX;
                graduationStartY = outRectBottom;
                bitmapRotateRound = 135;
                setPaint(Paint.Style.FILL, true, radius, Color.BLUE, 40);
                break;
            case 4:
                centerX = outRectRight - bitmapWidth / 2;
                centerY = outRectBottom - bitmapHeight / 2;
                startAngle = 180;
                graduationStartX = centerX - radius;
                graduationStartY = centerY;
                bitmapRotateRound = -135;
                setPaint(Paint.Style.FILL, true, radius, getResources().getColor(R.color.orange), 40);
                break;
            default:
                break;
        }
        //绘制扇形时需要指定的矩形的属性
        quaLeft = centerX - radius;
        quaRight = centerX + radius;
        quaTop = centerY - radius;
        quaBottom = centerY + radius;

        rectF = new RectF(quaLeft, quaTop, quaRight, quaBottom);
        drawArcWithColor(canvas, rectF, startAngle);
        drawQua(canvas,startAngle);
    }

    private void drawArcWithColor(Canvas canvas, RectF rectF, int startAngle) {
        //填充扇形内部
        canvas.drawArc(rectF, startAngle, 90, true, paint);

        //描扇形外廓
        setPaint(Paint.Style.STROKE, true, 3, Color.BLACK, 40);
        RectF outLineRectF = new RectF(quaLeft, quaTop, quaRight, quaBottom);
        canvas.drawArc(outLineRectF, startAngle, 90, false, paint);
    }

    /**
     * 可以动态旋转的中心图标
     * @param canvas
     */
    private void drawIcon(Canvas canvas) {

        setPaint(Paint.Style.STROKE, true, 3, Color.BLACK, 40);
        canvas.rotate(bitmapRotateRound+rotateRound,centerX,centerY);
        RectF insideRectF = new RectF(centerX-bitmapWidth/2, centerY-bitmapHeight/2,
                centerX+bitmapWidth/2, centerY+bitmapHeight/2);
        canvas.drawBitmap(bitmap, null, insideRectF, paint);
        canvas.rotate(-(bitmapRotateRound+rotateRound),centerX,centerY);
    }

    private void drawQua(Canvas canvas, int startAngle) {

        drawFocusLine(canvas);
        drawIcon(canvas);

        setPaint(Paint.Style.STROKE, true, 3, Color.BLACK, 40);

        int flag = -1;
        for (int degree = startAngle+90; degree <= startAngle + 180; degree++) {

            if (degree % 10 == 0) {
                if (degree % 30 == 0) {
                    if (degree % 90 == 0) flag = 3;
                    else flag = 2;
                } else flag = 1;
            } else flag = 0;

            int lineLen = getLineLen(flag);
            if (lineLen != 0) {
                int _degree;
                if (degree % 360 == 0) {
                    _degree = 0;
                } else {
                    _degree = degree;
                }
                String degreeText = "" + _degree + "°";
                //先初始化象限数据
                initWhichQuaData(lineLen,degreeText);
                //再进行绘制
                drawGraduation(canvas, paint, flag, degreeText);
            }
            canvas.rotate(1, centerX, centerY);
        }
        canvas.rotate(-90);
    }

    /**
     * 可以动态旋转的高亮线
     * @param canvas
     */
    public void drawFocusLine(Canvas canvas) {

        if (isFirstTimeIn){
            rotateRound = 0;
            isFirstTimeIn = false;
            return;
        }

        setPaint(Paint.Style.STROKE,true,10,Color.RED,40);
        canvas.rotate(rotateRound, centerX, centerY);
        Path path = new Path();
        path.moveTo(graduationStartX, graduationStartY);
        path.lineTo(dashLineStopX, dashLineStopY);
        canvas.drawPath(path, paint);
        canvas.rotate(-rotateRound, centerX, centerY);
    }

    /**
     * 获取不同角度对应的刻度线长度
     * @param flag
     * @return
     */
    private int getLineLen(int flag){
        int lineLen = 0;
        switch (flag) {
            case 0:
                lineLen = lineLen_S;
                break;
            case 1:
                lineLen = lineLen_M;
                break;
            case 2:
                lineLen = lineLen_L;
                break;
            case 3:
                lineLen = lineLen_XL;
                break;
            default:
                lineLen = 0;
                break;
        }
        return lineLen;
    }

    private void initWhichQuaData(int lineLen,String degreeText){
        switch (whichQua){
            case 1:
                initFirstQuaData(lineLen,degreeText);
                break;
            case 2:
                initSecondQuaData(lineLen,degreeText);
                break;
            case 3:
                initThirdQuaData(lineLen,degreeText);
                break;
            case 4:
                initFourthQuaData(lineLen,degreeText);
                break;
            default:
                break;
        }
    }

    private void initFirstQuaData(int lineLen,String degreeText){
        graduationStopX = graduationStartX;
        graduationStopY = graduationStartY + lineLen;
        textStartX = graduationStopX - paint.measureText(degreeText) / 2;
        textStartY = graduationStopY + 50;

        dashLineStartX = graduationStopX;
        dashLineStartY = textStartY + 50;
        dashLineStopX = graduationStopX;
        dashLineStopY = centerY - bitmapHeight / 2 - 10;
    }

    private void initSecondQuaData(int lineLen,String degreeText){
        graduationStopX = graduationStartX - lineLen;
        graduationStopY = graduationStartY;
        textStartX = graduationStopX - paint.measureText(degreeText) - 10;
        textStartY = graduationStopY;

        dashLineStartX = textStartX - 10;
        dashLineStartY = graduationStopY;
        dashLineStopX = centerX + bitmapWidth / 2 + 10;
        dashLineStopY = graduationStopY;
    }

    private void initThirdQuaData(int lineLen,String degreeText){
        graduationStopX = graduationStartX;
        graduationStopY = graduationStartY - lineLen;
        textStartX = graduationStopX - paint.measureText(degreeText) / 2;
        textStartY = graduationStopY - 20;

        dashLineStartX = graduationStopX;
        dashLineStartY = textStartY - 50;
        dashLineStopX = graduationStopX;
        dashLineStopY = centerY + bitmapHeight / 2 + 10;
    }

    private void initFourthQuaData(int lineLen,String degreeText){
        graduationStopX = graduationStartX + lineLen;
        graduationStopY = graduationStartY;
        textStartX = graduationStopX + 10;
        textStartY = graduationStopY;

        dashLineStartX = textStartX + paint.measureText(degreeText) + 10;
        dashLineStartY = graduationStopY;
        dashLineStopX = centerX - bitmapWidth / 2 - 10;
        dashLineStopY = graduationStopY;
    }

    public void drawGraduation(Canvas canvas, Paint paint, int flag, String degreeText) {
        //画刻度线
        canvas.drawLine(graduationStartX, graduationStartY, graduationStopX, graduationStopY, paint);

        if (flag == 3 || flag == 2) {

            //描刻度值
            canvas.drawText(degreeText, textStartX, textStartY, paint);

            //如果为0°，90°，180°，270°，还要描虚线
            if (flag == 3) {
                Paint mPaint = new Paint();
                mPaint.setAntiAlias(true);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(5);
                mPaint.setPathEffect(new DashPathEffect(new float[]{4, 4}, 0));

                Path path = new Path();
                path.moveTo(dashLineStartX, dashLineStartY);
                path.lineTo(dashLineStopX, dashLineStopY);
                canvas.drawPath(path, mPaint);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = 0, y = 0;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            x = event.getX();
            y = event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            x = event.getX();
            y = event.getY();
        } else ;
        if (isPointInQua(x, y)) {
            round = getRound(new float[]{x, y});
            Toast.makeText(getContext(), "" + round + "°", Toast.LENGTH_LONG).show();
            postInvalidate();
        }
        return super.onTouchEvent(event);
    }

    public boolean isPointInQua(float x, float y) {
        switch (whichQua) {
            case 1:
                if (x >= centerX && y <= centerY) return true;
            case 2:
                if (x >= centerX && y >= centerY) return true;
            case 3:
                if (x <= centerX && y >= centerY) return true;
            case 4:
                if (x <= centerX && y <= centerY) return true;
        }
        return false;
    }

    public int getRound(float[] pts) {
        float x = Math.abs(pts[0] - centerX);
        float y = Math.abs(pts[1] - centerY);
        float z = (float) Math.sqrt(x * x + y * y);
        float round = (float) (Math.asin(y / z) / Math.PI * 180);

        switch (whichQua) {
            case 1:
                rotateRound = (int) (90 - round);
                round = 90 - round;
                break;
            case 2:
                rotateRound = (int) round;
                round = 90 + round;
                break;
            case 3:
                rotateRound = (int) (90 - round);
                round = 270 - round;
                break;
            case 4:
                rotateRound = (int) round;
                round = 270 + round;
                break;
        }
        return (int) round;
    }

}