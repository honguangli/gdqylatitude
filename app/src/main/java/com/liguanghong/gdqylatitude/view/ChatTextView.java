package com.liguanghong.gdqylatitude.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.liguanghong.gdqylatitude.R;


public class ChatTextView extends AppCompatTextView {

    private int mBorderRadius;
    private int mCornerPosition;
    public static final int CORNER_LEFT = 1; //left
    public static final int CORNER_RIGHT = 0; //right
    private static final int BODER_RADIUS_DEFAULT = 5;

    private Paint mBitmapPaint;


    public ChatTextView(Context context) {
        this(context, null);
    }

    public ChatTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBitmapPaint = new Paint();
        mBitmapPaint.setColor(Color.WHITE);
        mBitmapPaint.setAntiAlias(true);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ChatImageView);
        mBorderRadius = a.getDimensionPixelSize(
                R.styleable.ChatImageView_border_radius, (int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                BODER_RADIUS_DEFAULT, getResources()
                                        .getDisplayMetrics()));
        mCornerPosition = a.getInt(R.styleable.ChatImageView_corner_position, CORNER_LEFT);
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        clipit(canvas);
        super.onDraw(canvas);
    }

    private void clipit(Canvas canvas) {

        Path path = new Path();
        //right
        if (mCornerPosition == 0) {
            RectF rectF=new RectF(0, 0, getWidth() - 25, getHeight());
            path.addRoundRect(rectF
                    ,Float.parseFloat(String.valueOf(mBorderRadius)),Float.parseFloat(String.valueOf(mBorderRadius)), Path.Direction.CW);
            path.moveTo(getWidth() - 25, 10+mBorderRadius);
            path.lineTo(getWidth(), 20+mBorderRadius);
            path.lineTo(getWidth() - 25, 30+mBorderRadius);
            path.lineTo(getWidth() - 25, 10+mBorderRadius);
        }
        //left
        if (mCornerPosition == 1) {
            RectF rectF=new RectF(25, 0, getWidth(), getHeight());
            path.addRoundRect(rectF,
                    Float.parseFloat(String.valueOf(mBorderRadius)), Float.parseFloat(String.valueOf(mBorderRadius)), Path.Direction.CW);
            path.moveTo(25, 10+mBorderRadius);
            path.lineTo(0, 20+mBorderRadius);
            path.lineTo(25, 30+mBorderRadius);
            path.lineTo(25, 10+mBorderRadius);
        }
        canvas.drawPath(path, mBitmapPaint);

    }

    public void setmCornerPosition(int mCornerPosition){
        this.mCornerPosition = mCornerPosition;
    }

    public void setmBorderRadius(int mBorderRadius){
        this.mBorderRadius = mBorderRadius;
    }

}
