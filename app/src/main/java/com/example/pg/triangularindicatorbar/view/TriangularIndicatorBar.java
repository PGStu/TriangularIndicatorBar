package com.example.pg.triangularindicatorbar.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.pg.triangularindicatorbar.DpUtils;
import com.example.pg.triangularindicatorbar.R;

//三角指示色条栏  记录界面上方指数使用
public class TriangularIndicatorBar extends View {
    public TriangularIndicatorBar(Context context) {
        this(context, null);
    }

    public TriangularIndicatorBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TriangularIndicatorBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.TriangularIndicatorBar);
        init(attributes);
    }
    private Path mPath;
    //色块起点 X
    private int mStartLeft = DpUtils.dip2px(6);
    private int mStartLeftHalf = DpUtils.dip2px(3);
    //三角形左下角 点的X坐标
    private int mTriangleLeftX = 0;
    //三角形右下角 点的X坐标
    private int mTriangleRightX = DpUtils.dip2px(12);
    private int mTriangleY = DpUtils.dip2px(12);
    private int mTextY = DpUtils.dip2px(26);
    private int mTextSize = DpUtils.dip2px(12);
    private int mMoveX = 0;
    private int mWidth = 0;
    private int mTopbgHeight = DpUtils.dip2px(4);
    //渐变色背景画笔
    private Paint mTopbgPaint;
    private RectF mTopbgRect;
    //底部文字画笔
    private Paint mPaintText;
    //三角形画笔
    private Paint mPaintTriangle;
    //渐变色 数组
    private int[] mClolorArray;
    //渐变点数组
    private float[] mGradientPostionArray;
    //除数
    private int mDivisor = 5;
    //文本显示位置 分子
    private int[] mTextMember = new int[]{0, 1, 2, 3, 4, 5};
    //要显示的文字内容
    private String[] mTextArray = new String[]{"0", "20", "40", "60", "80", "100"};
    private String mType = "0";
    //100 或者600 总长度
    private int mTotalInt = 100;
    private static final int DEFAULT_HEIGHT = DpUtils.dip2px(30);
    private void init(TypedArray attributes) {
        //0 为显示为指数时用   1 为心率变异分析时用
        mType = attributes.getString(R.styleable.TriangularIndicatorBar_triangularIndicatorBar_which_type);
        mPath = new Path();
        mTopbgPaint = new Paint();
        mTopbgRect = new RectF();
        mPaintText = new Paint();
        mPaintTriangle = new Paint();
        if ("0".equals(mType)) {
            mClolorArray = new int[]{getResources().getColor(R.color.gradient_0), getResources().getColor(R.color.gradient_50), getResources().getColor(R.color.gradient_75), getResources().getColor(R.color.gradient_100)};
            mGradientPostionArray = new float[]{0, 0.5f, 0.75f, 1};
            mTextArray = new String[]{"0", "20", "40", "60", "80", "100"};
            mDivisor = 5;
            mTextMember = new int[]{0, 1, 2, 3, 4, 5};
            mTotalInt = 100;
        } else {
            mClolorArray = new int[]{getResources().getColor(R.color.gradient_type1_0), getResources().getColor(R.color.gradient_type1_7), getResources().getColor(R.color.gradient_type1_14), getResources().getColor(R.color.gradient_type1_23), getResources().getColor(R.color.gradient_type1_40), getResources().getColor(R.color.gradient_type1_60), getResources().getColor(R.color.gradient_type1_100)};
            mGradientPostionArray = new float[]{0, 0.07f, 0.14f, 0.23f, 0.40f, 0.60f, 1};
            mTextArray = new String[]{"0", "100", "200", "400", "600"};
            mDivisor = 6;
            mTextMember = new int[]{0, 1, 2, 4, 6};
            mTotalInt = 600;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if(heightMode == MeasureSpec.UNSPECIFIED
                || heightMode == MeasureSpec.AT_MOST){
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(DEFAULT_HEIGHT, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取View的宽高
        LinearGradient backGradient = new LinearGradient(0, 0, mWidth, 0, mClolorArray, mGradientPostionArray, Shader.TileMode.CLAMP);
        mTopbgPaint.setShader(backGradient);
        mTopbgRect.left = mStartLeft;
        mTopbgRect.right = mWidth - mStartLeft;
        mTopbgRect.top = 0;
        mTopbgRect.bottom = mTopbgHeight;
        canvas.drawRoundRect(mTopbgRect, 5, 5, mTopbgPaint);
        //清除之前的三角形 防止重复
        mPath.reset();
        mPath.moveTo(mStartLeft + mMoveX, 5);// 起点
        mPath.lineTo(mTriangleLeftX + mMoveX, mTriangleY);
        mPath.lineTo(mTriangleRightX + mMoveX, mTriangleY);
        mPath.close(); //
        //绘制path路径
        canvas.drawPath(mPath, mPaintTriangle);
        mPaintText.setTextSize(mTextSize);
        mPaintText.setColor(getResources().getColor(R.color.text_color));
        drawTheText(canvas, mPaintText);
    }

    private void drawTheText(Canvas canvas, Paint paint) {
        for (int i = 0; i < mTextArray.length; i++) {
            if (i == mTextArray.length - 1) {
                canvas.drawText(mTextArray[i], mWidth - mTextSize * 2, mTextY, paint);
            } else {
                canvas.drawText(mTextArray[i], mStartLeftHalf + (mWidth / mDivisor) * mTextMember[i], mTextY, paint);
            }
        }
    }

    /**
     * 设置所处评分 0 - 100 / 0-600
     *
     * @param score
     */
    public void setScore(String score) {
        try {
            int intScore = Integer.parseInt(score);
            setScore(intScore);
        } catch (Exception e) {

        }
    }

    /**
     * 设置所处评分 0 - 100 / 0-600
     *
     * @param score
     */
    private int mScore = 0;
    public void setScore(int score) {
        mScore = score;
        //数据处理 防止画三角形时超出
        if (score < 0) mScore = 0;
        if (score > mTotalInt) mScore = mTotalInt;
        post(new Runnable() {
            @Override
            public void run() {
                ValueAnimator animator = ValueAnimator.ofInt(mMoveX, (((mWidth - mStartLeft * 2) * mScore / mTotalInt)));
                animator.setDuration(500);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mMoveX = Integer.parseInt(animation.getAnimatedValue().toString());
                        invalidate();
                    }
                });
                animator.start();
            }
        });
    }
}
