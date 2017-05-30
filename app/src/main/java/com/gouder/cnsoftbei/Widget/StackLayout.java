/*
 * Copyright (c) 2017 Peter Mao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gouder.cnsoftbei.Widget;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

public class StackLayout extends FrameLayout {

    private ArrayAdapter<Object> adapter;

    private View[] viewsBuffer;

    private float mRotateFactor;//控制item旋转范围
    private double mItemAlphaFactor;//控制item透明度变化范围

    private int mLimitTranslateX = 200;//限制移动距离，当超过这个距离的时候，删除该item

    private DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            attachChildViews();
        }
    };


    public StackLayout(Context context) {
        this(context, null);
    }

    public StackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int screenWidth = getScreenWidth(getContext());
        mRotateFactor = 60 * 1.0f / screenWidth;
        //左滑，透明度最少到0.1f
        mItemAlphaFactor = 0.9 * 1.0f / screenWidth / 2;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public void setAdapter(ArrayAdapter<Object> adapter) {
        if (adapter == null) {
            throw new IllegalArgumentException("adapter not null");
        }
        if (this.adapter != null) {
            this.adapter.unregisterDataSetObserver(dataSetObserver);
        }

        this.adapter = adapter;
        this.adapter.registerDataSetObserver(dataSetObserver);
        viewsBuffer = new View[adapter.getCount()];
        attachChildViews();

    }

    private void attachChildViews() {
        removeAllViews();
        for (int position = 0; position < adapter.getCount(); position++) {
            if (position < 2) {
                viewsBuffer[position] = adapter.getView(position, viewsBuffer[position], this);
                viewsBuffer[position].setRotation(0);
                viewsBuffer[position].setAlpha(1);
                addViewInLayout(viewsBuffer[position], 0, viewsBuffer[position].getLayoutParams());
                initEvent(adapter.getView(position, viewsBuffer[position], this));
            }
        }
        requestLayout();
    }

    private void initEvent(final View item) {
        //设置item的重心，主要是旋转的中心
        item.setPivotX(getScreenWidth(getContext()) / 2);
        item.setPivotY(getScreenHeight(getContext()) * 2);
        item.setOnTouchListener(new View.OnTouchListener() {
            float touchX, distanceX;//手指按下时的坐标以及手指在屏幕移动的距离

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchX = event.getRawX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        distanceX = event.getRawX() - touchX;

                        item.setRotation(distanceX * mRotateFactor);
                        //alpha scale 1~0.1
                        //item的透明度为从1到0.1
                        item.setAlpha(1 - (float) Math.abs(mItemAlphaFactor * distanceX));
                        break;
                    case MotionEvent.ACTION_UP:

                        if (Math.abs(distanceX) > mLimitTranslateX) {
                            //移除view
                            removeViewWithAnim(item, distanceX < 0);
                        } else {
                            //复位
                            item.animate()
                                    .alpha(1)
                                    .rotation(0)
                                    .setDuration(400).start();
                        }
                        break;
                }
                return true;
            }
        });
    }

    public void removeViewWithAnim(final View view, boolean isLeft) {
        view.animate()
                .alpha(0)
                .rotation(isLeft ? -90 : 90)
                .setDuration(400).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                adapter.remove(view.getTag());
                adapter.notifyDataSetChanged();
            }
        });

    }

}
