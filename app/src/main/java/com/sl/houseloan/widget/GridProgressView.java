package com.sl.houseloan.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Toast;

/**
 * GridProgressView
 *
 * @author csl
 * @class GridProgressView
 * @date 2019-06-14
 */
public class GridProgressView extends View {
    private static final int ROWS = 12;
    private static final int COLUMNS = 30;
    private int mLineWidth;
    private int rowNum=ROWS;
    private  int columnNum=COLUMNS;
    private Paint mPaint;
    private int startColor = Color.parseColor("#FB7EA9");
    private int endColor = Color.parseColor("#309F4A");
    private int doneNum=0;

    public GridProgressView(Context context) {
        this(context, null);
    }

    public GridProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLineWidth = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()) + 0.5f);
        mPaint = new Paint();
        mPaint.setColor(startColor);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 150;
        int height = 50;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(width, widthSize);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(height, heightSize);
        }

        setMeasuredDimension(width, height);
    }

    private int gray=Color.parseColor("#EAEAEA");
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        float itemHeight = ((height - (rowNum - 1) * mLineWidth) * 1.0f / rowNum);
        float itemWidth = ((width - (columnNum - 1) * mLineWidth) * 1.0f / columnNum);

        for (int j = 0; j < columnNum; j++) {
            for (int i = 0; i < rowNum; i++) {
                int index=i + j * 12;
                if (index<doneNum){
                    mPaint.setColor(gray);
                }else {
                    mPaint.setColor(evaluate(index * 1.0f / (rowNum * columnNum), startColor, endColor));
                }

                float left = j * itemWidth + j * mLineWidth;
                float top = i * itemHeight + i * mLineWidth;
                float right = left + itemWidth;
                float bottom = top + itemHeight;
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    private float pointX;
    private float pointY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pointX=event.getX();
                pointY=event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float x = event.getX();
                float y = event.getY();

                ViewConfiguration configuration=ViewConfiguration.get(getContext());
                int touchSlop = configuration.getScaledTouchSlop();

                if (Math.sqrt((x-pointX)*(x-pointX)+(y-pointY)*(y-pointY))<touchSlop){
                    getCellPosition(x,y);
                }

                break;
            default:
                break;
        }
        return true;
    }

    private void getCellPosition(float x, float y) {
        int width = getWidth();
        int height = getHeight();

        float itemHeight = ((height - (rowNum - 1) * mLineWidth) * 1.0f / rowNum);
        float itemWidth = ((width - (columnNum - 1) * mLineWidth) * 1.0f / columnNum);


        int row=-1;
        int col=-1;
        for (int j = 0; j < columnNum; j++) {
            float left = j * itemWidth + j * mLineWidth;
            float right = left + itemWidth;
            if (x>=left && x<=right){
                col=j;
                break;
            }
        }

        for (int i = 0; i < rowNum; i++) {
            float top = i * itemHeight + i * mLineWidth;
            float bottom = top + itemHeight;
            if (y>=top && y<=bottom){
                row=i;
            }
        }

        if (col>0 && row>0){
            Toast.makeText(getContext(),"col:"+col+",row"+row,Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 计算渐变颜色值 ARGB
     *
     * @param fraction   变化比率 0~1
     * @param startValue 初始色值
     * @param endValue   结束色值
     * @return 渐变颜色值
     */
    private int evaluate(float fraction, Integer startValue, Integer endValue) {
        int startInt = startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;
        int endInt = endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;
        return ((startA + (int) (fraction * (endA - startA))) << 24) | ((startR + (int) (fraction * (endR - startR))) << 16) | ((startG + (int) (fraction * (endG - startG))) << 8) | ((startB + (int) (fraction * (endB - startB))));
    }


    public void setRowColumnsAndDone(int rows,int columns,int done){
        rowNum=rows;
        columnNum=columns;
        doneNum=done;
        invalidate();
    }
}
