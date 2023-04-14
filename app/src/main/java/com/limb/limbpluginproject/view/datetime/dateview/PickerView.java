package com.limb.limbpluginproject.view.datetime.dateview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.limb.limbpluginproject.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class PickerView extends View {
    /**
     * 新增字段 控制是否首尾相接循环显示 默认为循环显示
     */
    private boolean loop = true;


    /**
     * text之间间距和minTextSize之比
     */
    public static float MARGIN_ALPHA = 1.8f;
    /**
     * 自动回滚到中间的速度
     */
    public static final float SPEED = 10;

    private List<String> mDataList;
    /**
     * 选中的位置，这个位置是mDataList的中心位置，一直不变
     */
    private int mCurrentSelected;
    private Paint mPaint, nPaint, linePaint;

    private float mMaxTextSize = 80;
    private float mMinTextSize = 40;

    private float mCenterHeight = 0;

    private float mMaxTextAlpha = 255;
    private float mMinTextAlpha = 50;

    private int mColorText = 0x8e8e92;

    private int mViewHeight;
    private int mViewWidth;

    private float mLastDownY;
    /**
     * 滑动的距离
     */
    private float mMoveLen = 0;
    private boolean isInit = false;
    private onSelectListener mSelectListener;
    private Timer timer;
    private MyTimerTask mTask;

    public String label = "";

    @SuppressLint("HandlerLeak")
    Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (Math.abs(mMoveLen) < SPEED) {
                mMoveLen = 0;
                if (mTask != null) {
                    mTask.cancel();
                    mTask = null;
                    performSelect();
                }
            } else
                // 这里mMoveLen / Math.abs(mMoveLen)是为了保有mMoveLen的正负号，以实现上滚或下滚
                mMoveLen = mMoveLen - mMoveLen / Math.abs(mMoveLen) * SPEED;
            invalidate();
        }

    };
    private boolean canScroll = true;

    public PickerView(Context context) {
        super(context);
        init();
    }

    public PickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PickerView);
        loop = typedArray.getBoolean(R.styleable.PickerView_isLoop, loop);
        init();
    }

    public void setOnSelectListener(onSelectListener listener) {
        mSelectListener = listener;
    }

    private void performSelect() {
        if (mSelectListener != null)
            mSelectListener.onSelect(mDataList.get(mCurrentSelected));
    }

    /**
     * 返回当前选中数据
     * @return
     */
    public String getSelectData(){
        if (mDataList == null){
            return "";
        }
        return mDataList.get(mCurrentSelected);
    }

    public void setData(List<String> datas) {
        mDataList = datas;
        mCurrentSelected = datas.size() / 4;
        invalidate();
    }

    /**
     * 选择选中的item的index
     *
     * @param selected
     */
    public void setSelected(int selected) {
        mCurrentSelected = selected;
        if (loop) {
            int distance = mDataList.size() / 2 - mCurrentSelected;
            if (distance < 0)
                for (int i = 0; i < -distance; i++) {
                    moveHeadToTail();
                    mCurrentSelected--;
                }
            else if (distance > 0)
                for (int i = 0; i < distance; i++) {
                    moveTailToHead();
                    mCurrentSelected++;
                }
        }
        invalidate();
    }

    /**
     * 选择选中的内容
     *
     * @param mSelectItem
     */
    public void setSelected(String mSelectItem) {
        for (int i = 0; i < mDataList.size(); i++)
            if (mDataList.get(i).equals(mSelectItem)) {
                setSelected(i);
                break;
            }
    }


    /**
     * 设置标签
     * @param label
     */
    public void setLabel(String label){
        this.label = label;
    }

    private void moveHeadToTail() {
        if (loop) {
            String head = mDataList.get(0);
            mDataList.remove(0);
            mDataList.add(head);
        }
    }

    private void moveTailToHead() {
        if (loop) {
            String tail = mDataList.get(mDataList.size() - 1);
            mDataList.remove(mDataList.size() - 1);
            mDataList.add(0, tail);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        // 按照View的高度计算字体大小
        if (!mIsCustomTextSize) {// 不自定义字体大小时走默认字体大小
            mMaxTextSize = mViewHeight / 10f;
            mMinTextSize = mMaxTextSize / 2f;
        }
        isInit = true;
        invalidate();
    }

    private void init() {
        timer = new Timer();
        mDataList = new ArrayList<String>();
        //第一个paint
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Style.FILL);
        Typeface font = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        mPaint.setTypeface(font);
        mPaint.setTextAlign(Align.CENTER);
        mPaint.setColor(getResources().getColor(R.color.color_2e2e30));

        //第二个paint
        nPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nPaint.setStyle(Style.FILL);
        nPaint.setTextAlign(Align.CENTER);
        nPaint.setColor(mColorText);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStrokeWidth((float) 1);
        linePaint.setColor(getResources().getColor(R.color.color_eaeaee));

        mMaxTextSize = getResources().getDimensionPixelSize(R.dimen.dimen_18);
        mMinTextSize = getResources().getDimensionPixelSize(R.dimen.dimen_12);
        mCenterHeight = getResources().getDimensionPixelSize(R.dimen.dimen_53);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 根据index绘制view
        if (isInit)
            drawData(canvas);
    }

    private void drawData(Canvas canvas) {
        // 先绘制选中的text再往上往下绘制其余的text
        // 先绘制选中的text再往上往下绘制其余的text
        float scale = parabola(mViewHeight / 4.0f, mMoveLen);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        // text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
        float x = (float) (mViewWidth / 2.0);
        float y = (float) (mViewHeight / 2.0 + mMoveLen);
        FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));

        mCurrentSelected = regetCurrentSelected();
        if (mCurrentSelected >= mDataList.size()) {
            return;
        }
        canvas.drawText(mDataList.get(mCurrentSelected) + label, x, baseline, mPaint);

        float center = mViewHeight / 2.0f;
        float offset = mCenterHeight / 2.8f;
        canvas.drawLine(0, center - offset, mViewWidth, center - offset, linePaint);
        // 绘制下边直线
        canvas.drawLine(0, center + offset, mViewWidth, center + offset, linePaint);
        // 绘制上方data
        for (int i = 1; (mCurrentSelected - i) >= 0; i++) {
            drawOtherText(canvas, i, -1);
        }
        // 绘制下方data
        for (int i = 1; (mCurrentSelected + i) < mDataList.size(); i++) {
            drawOtherText(canvas, i, 1);
        }

    }

    /**
     * DW 重新获取mCurrentSelected值，防止大于mDataList.size时溢出
     *
     * @return
     */
    private int regetCurrentSelected() {
        if (mDataList.size() > 0 && mCurrentSelected > mDataList.size() - 1) {
            String first = mDataList.get(0);
            if (!TextUtils.isEmpty(first) && TextUtils.isDigitsOnly(first)) {
                for (String item : mDataList) {
                    if (Integer.parseInt(item) == mCurrentSelected) {
                        return mDataList.indexOf(item) + 1;
                    }
                }
                return 0;
            }
        } else
            return mCurrentSelected;
        return 0;
    }

    /**
     * @param canvas
     * @param position 距离mCurrentSelected的差值
     * @param type     1表示向下绘制，-1表示向上绘制
     */
    private void drawOtherText(Canvas canvas, int position, int type) {
        float d = MARGIN_ALPHA * mMinTextSize * position + type
                * mMoveLen;
        float scale = 1.0f;
        // 用于不需要字体大小变化时
        if (mUnSelectTextScale) scale *= parabola(mViewHeight / 4.0f, d);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        nPaint.setTextSize(size);
        // 用于不需要字体透明度变化时
        if (mUnSelectTextNeedAlpha)
            nPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        float y = (float) (mViewHeight / 2.0 + type * d) + type * mCenterHeight / 3;
        FontMetricsInt fmi = nPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));

        canvas.drawText(mDataList.get(mCurrentSelected + type * position) + label,
                (float) (mViewWidth / 2.0), baseline, nPaint);
    }

    /**
     * 抛物线z
     *
     * @param zero 零点坐标
     * @param x    偏移量
     * @return scale
     */
    private float parabola(float zero, float x) {
        float f = (float) (1 - Math.pow(x / zero, 2));
        return f < 0 ? 0 : f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                doDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveLen += (event.getY() - mLastDownY);

                if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2) {
                    if (!loop && mCurrentSelected == 0) {
                        mLastDownY = event.getY();
                        invalidate();
                        return true;
                    }
                    if (!loop) mCurrentSelected--;
                    // 往下滑超过离开距离
                    moveTailToHead();
                    mMoveLen = mMoveLen - MARGIN_ALPHA * mMinTextSize;
                } else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2) {
                    if (mCurrentSelected == mDataList.size() - 1) {
                        mLastDownY = event.getY();
                        invalidate();
                        return true;
                    }
                    if (!loop) mCurrentSelected++;
                    // 往上滑超过离开距离
                    moveHeadToTail();
                    mMoveLen = mMoveLen + MARGIN_ALPHA * mMinTextSize;
                }

                mLastDownY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                doUp(event);
                break;
        }
        return true;
    }

    private void doDown(MotionEvent event) {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mLastDownY = event.getY();
    }

    private void doUp(MotionEvent event) {
        // 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
        if (Math.abs(mMoveLen) < 0.0001) {
            mMoveLen = 0;
            return;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mTask = new MyTimerTask(updateHandler);
        timer.schedule(mTask, 0, 10);
    }

    class MyTimerTask extends TimerTask {
        Handler handler;

        public MyTimerTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }

    }

    public interface onSelectListener {
        void onSelect(String text);
    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (canScroll)
            return super.dispatchTouchEvent(event);
        else
            return false;
    }

    /**
     * 新增字段 控制内容是否首尾相连
     * by liuli
     *
     * @param isLoop
     */
    public void setIsLoop(boolean isLoop) {
        loop = isLoop;
        invalidate();
    }

    public void setSelectTextColor(int color) {
        mPaint.setColor(color);
        invalidate();
    }

    public void setUnSelectTextColor(int color) {
        nPaint.setColor(color);
        invalidate();
    }

    // 未选中字体是否缩放
    private boolean mUnSelectTextScale = true;
    // 未选中字体是否改变透明度
    private boolean mUnSelectTextNeedAlpha = true;
    // 是否自定义字间距
    private boolean mIsCustomTextMargin = false;
    // 是否自定义字体大小
    private boolean mIsCustomTextSize = true;
    private float mCustomTextMargin = 0f;

    public void setIsTextScale(boolean isScale) {
        mUnSelectTextScale = isScale;
        invalidate();
    }

    public void setTextSize(int size) {
        mIsCustomTextSize = true;
        mMaxTextSize = size;
        mMinTextSize = size;
        invalidate();
    }

    public void setIsUnSelectTextNeedAlpha(boolean alpha) {
        mUnSelectTextNeedAlpha = alpha;
        invalidate();
    }

    public void setCustomTextMargin(float margin) {
        mIsCustomTextMargin = true;
        mCustomTextMargin = margin;
        MARGIN_ALPHA = mCustomTextMargin / mMinTextSize;
        invalidate();
    }
}