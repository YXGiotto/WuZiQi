package com.example.administrator.wuziqi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/10.
 */

public class FivePanel extends View {
     private int mPanelWidth;
     private float mLineHeight;
     private int MAX_LINE=10;
     private int MAX_COUNT_IN_LINE=5;

    private Paint  paint=new Paint();
    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;

    private float ratioPieceOfLinHeight=3*1.0f/4;

    private boolean mIsWhite=true;
    private ArrayList<Point> mWhiteArray=new ArrayList<>();
    private ArrayList<Point> mBlackArray=new ArrayList<>();

    private boolean mIsGameOver;
    private boolean mIsWhiteWinner;

    public FivePanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(0x44ff0000);
        init();
    }

    private void init() {
        paint.setColor(0x88000000);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        mWhitePiece= BitmapFactory.decodeResource(getResources(),R.drawable.stone_w2);
        mBlackPiece= BitmapFactory.decodeResource(getResources(),R.drawable.stone_b1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize= MeasureSpec.getSize(widthMeasureSpec);
        int  widthMode=MeasureSpec.getMode(widthMeasureSpec);

        int heightSize= MeasureSpec.getSize(heightMeasureSpec);
        int  heightMode=MeasureSpec.getMode(heightMeasureSpec);

        int width=Math.min(widthSize,heightSize);

        if(widthMode==MeasureSpec.UNSPECIFIED) {
            width=heightSize;
        }else if(heightMode==MeasureSpec.UNSPECIFIED){
            width=widthSize;
        }
        setMeasuredDimension(width,width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidth=w;
        mLineHeight=mPanelWidth*1.0f/MAX_LINE;
        int pieceWidth= (int) (mLineHeight*ratioPieceOfLinHeight);
        mWhitePiece=Bitmap.createScaledBitmap(mWhitePiece,pieceWidth,pieceWidth,false);
        mBlackPiece=Bitmap.createScaledBitmap(mBlackPiece,pieceWidth,pieceWidth,false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mIsGameOver) return false;
        int action=event.getAction();
        if(action==MotionEvent.ACTION_UP){
            int x= (int) event.getX();
            int y= (int) event.getY();
            Point p=getValidPoint(x,y);
            if(mWhiteArray.contains(p)||mBlackArray.contains(p)){
                return false;
            }
            if(mIsWhite){
                mWhiteArray.add(p);
            }else{
                mBlackArray.add(p);
            }
            invalidate();
            mIsWhite=!mIsWhite;
            return true;
        }
        return true;
    }

    private Point getValidPoint(int x, int y) {
        return new Point((int) (x/mLineHeight),(int)(y/mLineHeight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawPiece(canvas);
        checkGameOver();

    }

    private void checkGameOver() {
       boolean whiteWin= checkFiveInLine(mWhiteArray);
        boolean blackWin= checkFiveInLine(mBlackArray);

        if(whiteWin||blackWin){
            mIsGameOver=true;
            mIsWhiteWinner=whiteWin;
            String text=mIsWhiteWinner?"白棋胜利":"黑棋胜利";
            Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkFiveInLine(List<Point> points) {
        for (Point p:points){
            int x=p.x;
            int y=p.y;
            boolean win=checkHorizontal(x,y,points);
            if(win)return true;
            win=checkVertial(x,y,points);
            if(win)return true;
            win=checkLeftDiagona(x,y,points);
            if(win)return true;
            win=checkRightDiagona(x,y,points);
            if(win)return true;

        }
        return false;
    }

    private boolean checkHorizontal(int x, int y, List<Point> points) {
        int count =1;
        for(int i=1;1<MAX_COUNT_IN_LINE;i++){
            if(points.contains(new Point(x-i,y))){
                count++;
            }else{
                break;
            }
        }
        if(count==MAX_COUNT_IN_LINE) return true;
        for(int i=1;1<MAX_COUNT_IN_LINE;i++){
            if(points.contains(new Point(x+i,y))){
                count++;
            }else{
                break;
            }
        }
        if(count==MAX_COUNT_IN_LINE) return true;
        return false;
    }
    private boolean checkVertial(int x, int y, List<Point> points) {
        int count =1;
        for(int i=1;1<MAX_COUNT_IN_LINE;i++){
            if(points.contains(new Point(x,y-i))){
                count++;
            }else{
                break;
            }
        }
        if(count==MAX_COUNT_IN_LINE) return true;
        for(int i=1;1<MAX_COUNT_IN_LINE;i++){
            if(points.contains(new Point(x,y+i))){
                count++;
            }else{
                break;
            }
        }
        if(count==MAX_COUNT_IN_LINE) return true;
        return false;
    }
    private boolean checkLeftDiagona(int x, int y, List<Point> points) {
        int count =1;
        for(int i=1;1<MAX_COUNT_IN_LINE;i++){
            if(points.contains(new Point(x-i,y+i))){
                count++;
            }else{
                break;
            }
        }
        if(count==MAX_COUNT_IN_LINE) return true;
        for(int i=1;1<MAX_COUNT_IN_LINE;i++){
            if(points.contains(new Point(x+i,y-i))){
                count++;
            }else{
                break;
            }
        }
        if(count==MAX_COUNT_IN_LINE) return true;
        return false;
    }
    private boolean checkRightDiagona(int x, int y, List<Point> points) {
        int count =1;
        for(int i=1;1<MAX_COUNT_IN_LINE;i++){
            if(points.contains(new Point(x-i,y-i))){
                count++;
            }else{
                break;
            }
        }
        if(count==MAX_COUNT_IN_LINE) return true;
        for(int i=1;1<MAX_COUNT_IN_LINE;i++){
            if(points.contains(new Point(x+i,y+i))){
                count++;
            }else{
                break;
            }
        }
        if(count==MAX_COUNT_IN_LINE) return true;
        return false;
    }

    private void drawPiece(Canvas canvas) {
        for(int i=0,n=mWhiteArray.size();i<n;i++){
            Point whitePoint=mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePiece,
                    (whitePoint.x+ (1-ratioPieceOfLinHeight) /2 ) * mLineHeight,
                    (whitePoint.y+ (1-ratioPieceOfLinHeight) /2 ) * mLineHeight,null);
        }
        for(int i=0,n=mBlackArray.size();i<n;i++){
            Point blackPoint=mBlackArray.get(i);
            canvas.drawBitmap(mBlackPiece,
                    (blackPoint.x +  (1 - ratioPieceOfLinHeight) /2 ) * mLineHeight,
                    (blackPoint.y +  (1 - ratioPieceOfLinHeight) /2 ) * mLineHeight,null);
        }
    }

    private void drawBoard(Canvas canvas) {
        int  w=mPanelWidth;
        float lineheight=mLineHeight;
        for(int i=0;i<MAX_LINE;i++){
            int startX= (int) (lineheight/2);
            int endX= (int) (w-lineheight/2);
            int  y= (int) ((0.5+i)*lineheight);
            canvas.drawLine(startX,y,endX,y,paint);
            canvas.drawLine(y,startX,y,endX,paint);
        }
    }
     private static final String INSTANCE="instance";
    private static final String INSTANCE_GAME_OVER="instance_game_over";
    private static final String INSTANCE_WHITE_ARRAY="instance_white_array";
    private static final String INSTANCE_BLACK_ARRAY="instance_black_array";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle=new Bundle();
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER,mIsGameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY,mWhiteArray);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY,mBlackArray);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof  Bundle){
            Bundle bundle= (Bundle) state;
            mIsGameOver=bundle.getBoolean(INSTANCE_GAME_OVER);
            mWhiteArray=bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray=bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
        }
        super.onRestoreInstanceState(state);
    }
    public void restart(){
        mWhiteArray.clear();
        mBlackArray.clear();
        mIsGameOver=false;
        mIsWhiteWinner=false;
        invalidate();
    }
}
