/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.byteshop.slidingpanel.observablescrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * ScrollView that its scroll position can be observed.
 */
public class ObservableScrollView2 extends ScrollView {

	private boolean isScrollingEnabled = true;

	public ObservableScrollView2(Context context) {
		super(context);
	}

	public ObservableScrollView2(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ObservableScrollView2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setScrollingEnabled(boolean isScrollingEnabled){
		this.isScrollingEnabled  = isScrollingEnabled;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(isScrollingEnabled){
			return super.onInterceptTouchEvent(ev);
		}else{
			return false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(isScrollingEnabled){
			return super.onTouchEvent(ev);
		}else{
			return false;
		}
	}

}