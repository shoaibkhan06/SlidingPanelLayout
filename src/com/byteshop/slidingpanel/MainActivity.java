package com.byteshop.slidingpanel;

import com.example.slidingmenu2.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button showSlidingPanel = (Button) findViewById(R.id.show_button);
		
		showSlidingPanel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MainActivity.this, SlidingPanel.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
			}
		});
	}

}