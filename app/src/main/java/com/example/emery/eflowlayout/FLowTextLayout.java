package com.example.emery.eflowlayout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by emery on 2017/8/5.
 */

public class FLowTextLayout extends ViewGroup {
    List<List<View>> linesView = new ArrayList<>();
    List<Integer> linesHeight = new ArrayList<>();
    private Context mContext;

    public FLowTextLayout(Context context) {
        this(context, null);
        this.mContext = context;
    }

    public FLowTextLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
    }

    public FLowTextLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    /**
     * 获取 xml定义的margin属性
     *
     * @param p
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet p) {
        return new MarginLayoutParams(getContext(), p);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //获取宽高的建议值
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //测量宽高值
        int measureWidth = 0;
        int measureHeight = 0;

        //当前行的高度，宽度
        int currentRowWidth = 0;
        int currentRowHeight = 0;

        //如果宽高是精确值，不需要测量
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            measureWidth = widthSize;
            measureHeight = heightSize;
        } else {
            //宽,高之一不是精确值都需要测量
            int childWidth = 0;
            int childHeight = 0;

            int childCount = getChildCount();

            //第一行的views
            List<View> rowViews = new ArrayList<>();

            for (int i = 0; i < childCount; i++) {

                View childView = getChildAt(i);

                /*measureChildWithMargins(childView,widthMeasureSpec,childWidth,
                heightMeasureSpec,childHeight);
                childWidth=childView.getMeasuredWidth();
                childHeight = childView.getMeasuredHeight();*/


                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) childView.getLayoutParams();
                childWidth = childView.getMeasuredWidth() + marginLayoutParams.leftMargin +
                        marginLayoutParams.rightMargin;
                childHeight = childView.getMeasuredHeight() + marginLayoutParams.topMargin +
                        marginLayoutParams.bottomMargin;


                if (currentRowWidth + childWidth > widthSize) {
                    //换行
                    measureWidth = Math.max(measureWidth, currentRowWidth);
                    measureHeight += currentRowHeight;

                    //将当前行的行高，views保存
                    linesHeight.add(currentRowHeight);

                    //当前行放入集合
                    linesView.add(rowViews);

                    //为新的一行赋值
                    currentRowWidth = childWidth;
                    currentRowHeight = childHeight;

                    rowViews = new ArrayList<>();
                    rowViews.add(childView);

                } else {
                    currentRowWidth += childWidth;
                    currentRowHeight = Math.max(childHeight, currentRowHeight);
                    rowViews.add(childView);
                }
                //如果正好是最后一行需要换行
                if (i == childCount - 1) {

                    //换行
                    measureWidth = Math.max(measureWidth, currentRowWidth);
                    measureHeight += currentRowHeight;

                    //将当前行的行高，views保存
                    linesHeight.add(currentRowHeight);

                    //当前行放入集合
                    linesView.add(rowViews);

                }

            }
        }

        //测量完成后设置宽高
        setMeasuredDimension(measureWidth, measureHeight);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int currentLeft = 0;
        int currentTop = 0;
        for (int i = 0; i < linesView.size(); i++) {
            List<View> views = linesView.get(i);
            int left, top, right, bottom;
            //每一行的views
            for (int j = 0; j < views.size(); j++) {
                View childView = views.get(j);
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) childView
                        .getLayoutParams();
                left = currentLeft + marginLayoutParams.leftMargin;
                top = currentTop + marginLayoutParams.topMargin;
                right = left + childView.getMeasuredWidth();
                bottom = top + childView.getMeasuredHeight();
                childView.layout(left, top, right, bottom);
                //第一个摆放完成
                currentLeft += childView.getMeasuredWidth() + marginLayoutParams.rightMargin +
                        marginLayoutParams.leftMargin;
            }
            //换行
            currentLeft = 0;
            currentTop += linesHeight.get(i);
        }
        linesView.clear();

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int i);
    }

    public void setTextList(List<String> textList) {
        Random random=new Random();
        for (String s : textList) {
            TextView textView = new TextView(mContext);
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(layoutParams);
            textView.setText(s);
            textView.setBackgroundColor(Color.argb(random.nextInt(255),random.nextInt(255),random.nextInt(255),random.nextInt(255)));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
            addView(textView);
        }
    }

    public void setOnItemClickListener(final OnItemClickListener listener) {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            final int index = i;
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, index);
                }
            });
        }
    }
}
