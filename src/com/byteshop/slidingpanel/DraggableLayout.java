/*
 * Copyright (c) 2014
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.byteshop.slidingpanel;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.byteshop.slidingpanel.observablescrollview.ObservableScrollView2;
import com.example.slidingmenu2.R;

/**
 * Custom ViewGroup that extends RelativeLayout 
 * for better handling of touch inputs
 *
 */
public class DraggableLayout extends RelativeLayout {
    private final double AUTO_OPEN_SPEED_LIMIT = 1000.0;
    private int mDraggingState = 0;
    private ObservableScrollView2 mDraggableQueenView; // Declare your draggable view / viewgroup here
    private ViewDragHelper mDragHelper;
    private int mDraggingBorder;
    private int mVerticalRange;
    private boolean mIsOpen;
	private DraggableLayoutCallbackListener draggableLayoutCallbackListener;
	public boolean firstSlide = true;
	private final int DRAGGABLE_QUEEN_VIEW_ID = R.id.main_layout; // Declare your draggable view id here


    private class ViewDragHelperCallback extends ViewDragHelper.Callback {
        @Override
        public void onViewDragStateChanged(int state) {
            if (state == mDraggingState) { // no change
                return;
            }
            if ((mDraggingState == ViewDragHelper.STATE_DRAGGING || mDraggingState == ViewDragHelper.STATE_SETTLING) &&
                 state == ViewDragHelper.STATE_IDLE) {
                // the view stopped from moving.
                if (mDraggingBorder == 0) {
                    onStopDraggingToClosed();
                } else if (mDraggingBorder == mVerticalRange) {
                    mIsOpen = true;
                }
            }
            if (state == ViewDragHelper.STATE_DRAGGING) {
                onStartDragging();
            }
            mDraggingState = state;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            mDraggingBorder = top;
            
            if(top == mVerticalRange){
            	changedView.post(new Runnable() {
					public void run() {
						draggableLayoutCallbackListener.onStopVerticalDragDown();	
					}
				});
            	
            }
        }

        public int getViewVerticalDragRange(View child) {
            return mVerticalRange;
        }

        @Override
        public boolean tryCaptureView(View view, int i) {
        	Log.v("sTag", "########## TOUCH CAPTURE VIEW ID = " + view.getId());
            return (view.getId() == DRAGGABLE_QUEEN_VIEW_ID);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            final int topBound = getPaddingTop();
            final int bottomBound = mVerticalRange;
            return Math.min(Math.max(top, topBound), bottomBound);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            final float rangeToCheck = mVerticalRange;
            if (mDraggingBorder == 0) {
                mIsOpen = false;
                return;
            }
            if (mDraggingBorder == rangeToCheck) {
                mIsOpen = true;
                return;
            }
            boolean settleToOpen = false;
            if (yvel > AUTO_OPEN_SPEED_LIMIT) { // speed has priority over position
                settleToOpen = true;
            } else if (yvel < -AUTO_OPEN_SPEED_LIMIT) {
                settleToOpen = false;
            } else if (mDraggingBorder > rangeToCheck / 3) {
                settleToOpen = true;
            } else if (mDraggingBorder < rangeToCheck / 3) {
                settleToOpen = false;
            }

            final int settleDestY = settleToOpen ? mVerticalRange : 0;

            if(mDragHelper.settleCapturedViewAt(0, settleDestY)) {
                ViewCompat.postInvalidateOnAnimation(DraggableLayout.this);
            }
        }
    }

    public DraggableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mIsOpen = false;
    }

    @Override
    protected void onFinishInflate() {
        mDraggableQueenView  = (ObservableScrollView2) findViewById(DRAGGABLE_QUEEN_VIEW_ID);
        mDragHelper = ViewDragHelper.create(this, 1.0f, new com.byteshop.slidingpanel.DraggableLayout.ViewDragHelperCallback());
        mIsOpen = false;
        super.onFinishInflate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mVerticalRange = (int) (h);
        super.onSizeChanged(w, h, oldw, oldh);
    }
    
    @Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		
		if(firstSlide){
			// Apply VDH offsets
	        mDraggableQueenView.offsetLeftAndRight(0);
	        mDraggableQueenView.offsetTopAndBottom(mVerticalRange);
		}
		
		// For sliding the draggable view to half of the screen, the first time
        if(firstSlide && mDragHelper.smoothSlideViewTo(mDraggableQueenView, 0, mVerticalRange/ 3)) {
        	ViewCompat.postInvalidateOnAnimation(DraggableLayout.this);
            firstSlide  = false;
        }
	}

	protected void onStopDraggingToClosed() {
        // To be implemented
    }

    private void onStartDragging() {

    }
    
    public void setOuterLayoutCallbackListener(DraggableLayoutCallbackListener mCallback){
    	this.draggableLayoutCallbackListener = mCallback;
    }

    private boolean isQueenTarget(MotionEvent event) {
        int[] queenLocation = new int[2];
        mDraggableQueenView.getLocationOnScreen(queenLocation);
        int upperLimit = queenLocation[1] + mDraggableQueenView.getMeasuredHeight();
        int lowerLimit = queenLocation[1];
        int y = (int) event.getRawY();
        return (y > lowerLimit && y < upperLimit);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (isQueenTarget(event) && mDragHelper.shouldInterceptTouchEvent(event) && mDraggableQueenView.getScrollY() == 0) {
        	mDraggableQueenView.setScrollingEnabled(false);
            return true;
        } else if(!isQueenTarget(event)){
        	draggableLayoutCallbackListener.onStopVerticalDragDown();
        	return true;
        }else {
        	mDraggableQueenView.setScrollingEnabled(true);
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isQueenTarget(event) || isMoving()) {
            mDragHelper.processTouchEvent(event);
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    @Override
    public void computeScroll() { // needed for automatic settling.
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public boolean isMoving() {
        return (mDraggingState == ViewDragHelper.STATE_DRAGGING ||
                mDraggingState == ViewDragHelper.STATE_SETTLING);
    }

    public boolean isOpen() {
        return mIsOpen;
    }
}

