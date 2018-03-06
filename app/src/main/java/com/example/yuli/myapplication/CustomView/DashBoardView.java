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

import com.example.yuli.myapplication.Activity.DashViewActivity;
import com.example.yuli.myapplication.R;

public class DashBoardView extends View{

    private float centerX;
    private float centerY;
    private float radius;
    private float screenWidth;
    private float screenHeight;
    private float bitmapWidth;
    private float bitmapHeight;
    private final float lineLen_LONG = 100.0f;
    private final float lineLen_SHORT = 50.0f;
    private float startX;
    private float startY;
    private float stopX;
    private float stopY;
    private float textStartX;
    private float textStartY;
    private float dashLineStartX;
    private float dashLineStartY;
    private float dashLineStopX;
    private float dashLineStopY;
    private Bitmap bitmap;
    private Quadrant firstQua;
    private Quadrant secondQua;
    private Quadrant thirdQua;
    private Quadrant fourthQua;
    private Paint paint;
    private final int bitmapRotateRound = -45;
    private int rotateRound = 0;

    public DashBoardView(Context context) {
        super(context);
    }

    public DashBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DashBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setPaint(Paint.Style style, boolean isAntiAlias, float strokeWidth, int color, int textSize) {
        paint.setStyle(style);
        paint.setAntiAlias(isAntiAlias);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(color);
        paint.setTextSize(textSize);
    }

    public void setRotateRound(int rotateRound){
        this.rotateRound = rotateRound;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //获取屏幕宽高
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint = new Paint();

        //画外圆
        drawCircle(canvas);

        //画中心图标
        drawIcon(canvas);

        //画刻度线
        drawLine(canvas);

        //画焦点线
        drawFocusLine(canvas);

        //画四个象限的放大按钮
        drawButtons(canvas);
    }

    private void drawCircle(Canvas canvas){
        //计算出圆心
        centerX = screenWidth/2;
        centerY = screenHeight/2;
        //计算出半径
        radius = (screenWidth<=screenHeight?screenWidth:screenHeight)/2;
        setPaint(Paint.Style.STROKE,true,10,Color.GRAY,0);
        canvas.drawCircle(centerX,centerY,radius,paint);
    }

    private void drawIcon(Canvas canvas){
        setPaint(Paint.Style.STROKE,true,10,Color.BLACK,0);

        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.dashboard);
        bitmapWidth = 200.f;
        bitmapHeight = 200.0f;

        float left = centerX-bitmapWidth/2;
        float top = centerY-bitmapHeight/2;
        float right = centerX+bitmapWidth/2;
        float bottom = centerY+bitmapHeight/2;

        canvas.rotate(bitmapRotateRound+rotateRound,centerX,centerY);
        RectF rectF = new RectF(left,top,right,bottom);
        canvas.drawBitmap(bitmap,null,rectF,paint);
        canvas.rotate(-(bitmapRotateRound+rotateRound),centerX,centerY);
    }

    private void drawLine(Canvas canvas){
        startX = centerX;
        startY = centerY-radius;
        stopX = centerX;
        float lineLen = 0;
        int flag = -1;
        setPaint(Paint.Style.STROKE,true,5,Color.BLACK,40);
        for (int degree=0;degree<=360;){
            //画刻度线
            if (degree%15==0){
                if (degree%30==0)flag = 0;//长线
                else flag = 1;//短线
            }

            switch (flag){
                case 0:
                    lineLen = lineLen_LONG;
                    stopY = startY+lineLen;
                    canvas.drawLine(startX,startY,stopX,stopY,paint);

                    //描刻度值
                    drawText(canvas,degree);
                    break;
                case 1:
                    lineLen = lineLen_SHORT;
                    stopY = startY+lineLen;
                    canvas.drawLine(startX,startY,stopX,stopY,paint);
                    break;
                default:
                    break;
            }
            //如果degree已经为360°，就不需要再旋转了，不然中轴线会指向15°，元素会倾斜
            if (degree!=360){
                canvas.rotate(15,centerX,centerY);
            }
            degree += 15;
        }
    }

    private void drawText(Canvas canvas,int degree){
        if (degree == 360) degree = 0;
        String degreeText = ""+degree+"°";
        textStartX = centerX-paint.measureText(degreeText)/2;
        textStartY = stopY+50;
        canvas.drawText(degreeText,textStartX,textStartY,paint);

        if (degree%90==0){
            drawDashLine(canvas);
        }
    }

    private void drawDashLine(Canvas canvas){
        dashLineStartX = centerX;
        dashLineStartY = textStartY+50;
        dashLineStopX = centerX;
        dashLineStopY = centerY-bitmapHeight/2-10;

        paint.setPathEffect(new DashPathEffect(new float[]{4,4},0));
        Path path = new Path();
        path.moveTo(dashLineStartX,dashLineStartY);
        path.lineTo(dashLineStopX,dashLineStopY);
        canvas.drawPath(path,paint);
        paint.setPathEffect(null);
    }

    private void drawFocusLine(Canvas canvas){
        setPaint(Paint.Style.STROKE,true,10,Color.RED,0);

        canvas.rotate(rotateRound,centerX,centerY);
        canvas.drawLine(startX,startY,dashLineStopX,dashLineStopY,paint);
        drawText(canvas,rotateRound);
        canvas.rotate(-rotateRound,centerX,centerY);
    }

    private void drawButtons(Canvas canvas){

        firstQua = new Quadrant((int) (centerX+radius/10)
                ,(int) (centerY-100-radius/10*3)
                ,(int)(centerX+radius/10*3)
                ,(int)(centerY-100-radius/10));
        drawQuadrantButton(canvas, firstQua);

        secondQua = new Quadrant((int) (centerX-radius/10*3),
                (int) (centerY-100-radius/10*3),
                (int)(centerX-radius/10),
                (int)(centerY-100-radius/10));
        drawQuadrantButton(canvas,secondQua);

        thirdQua = new Quadrant((int) (centerX-radius/10*3)
                ,(int) (centerY+100+radius/10)
                ,(int)(centerX-radius/10)
                ,(int)(centerY+100+radius/10*3));
        drawQuadrantButton(canvas,thirdQua);

        fourthQua = new Quadrant((int) (centerX+radius/10)
                ,(int) (centerY+100+radius/10)
                ,(int)(centerX+radius/10*3)
                ,(int)(centerY+100+radius/10*3));
        drawQuadrantButton(canvas,fourthQua);
    }

    private void drawQuadrantButton(Canvas canvas,Quadrant quadrant){
        setPaint(Paint.Style.STROKE,true,3,Color.alpha(0),40);
        paint.setTextAlign(Paint.Align.CENTER);

        Rect rect = new Rect(quadrant.getLeft(),quadrant.getTop(),quadrant.getRight(),quadrant.getBottom());
        canvas.drawRect(rect,paint);

        setPaint(Paint.Style.STROKE,true,3,Color.RED,40);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.magnifier);
        canvas.drawBitmap(bitmap,null,rect,paint);

//        本来样式是四个“放大字样”的按钮，更替为图标
//        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
//        float fontTop = fontMetrics.top;
//        float fontBottom = fontMetrics.bottom;
//        int baselineY = (int)(rect.centerY()-fontTop/2-fontBottom/2);
//        paint.setColor(Color.RED);
//        canvas.drawText("放大", rect.centerX(),baselineY,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x,y;

        x = (int) event.getX();
        y = (int) event.getY();
        int whichQua = posInWhichQua(x,y);
        ((DashViewActivity)getContext()).handler.sendEmptyMessage(whichQua);

        return super.onTouchEvent(event);
    }

    public int posInWhichQua(int x,int y){
        int flag = -1;
        if (x>firstQua.getLeft() && x<firstQua.getRight()
                && y>firstQua.getTop() && y<firstQua.getBottom()){
            flag = 1;
        }else if (x>secondQua.getLeft() && x<secondQua.getRight()
                && y>secondQua.getTop() && y<secondQua.getBottom()){
            flag = 4;
        }else if (x>thirdQua.getLeft() && x<thirdQua.getRight()
                && y>thirdQua.getTop() && y<thirdQua.getBottom()){
            flag = 3;
        }else if (x>fourthQua.getLeft() && x<fourthQua.getRight()
                && y>fourthQua.getTop() && y<fourthQua.getBottom()){
            flag = 2;
        }else ;
        return flag;
    }
}