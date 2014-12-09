package com.skplanetx.tmapopenmapapi.ui;
import com.skplanetx.tmapopenmapapi.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;


public class LoadingActivity extends Activity {	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.loading_activity);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);   
		setTitle("");
		Handler h = new Handler();
		h.postDelayed(new Runnable()
		{
			public void run()
			{
				finish();
			}
		},1500);
	}
}