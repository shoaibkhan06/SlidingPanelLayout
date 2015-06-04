package com.byteshop.slidingpanel;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.byteshop.slidingpanel.observablescrollview.ObservableScrollView2;
import com.example.slidingmenu2.R;

public class SlidingPanel extends Activity implements DraggableLayoutCallbackListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sliding_panel);

		final ObservableScrollView2 scrollView = (ObservableScrollView2) findViewById(R.id.main_layout);
		scrollView.setScrollingEnabled(false);
		
		final DraggableLayout draggableLayout = (DraggableLayout) findViewById(R.id.outerlayout);
		draggableLayout.setOuterLayoutCallbackListener(this);
	}

	@Override
	public void onStopVerticalDragDown() {
		this.finish();
	}	
}
