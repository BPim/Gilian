package com.skplanetx.tmapopenmapapi.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.skplanetx.tmapopenmapapi.LogManager;
import com.skplanetx.tmapopenmapapi.R;



public class SelectActivity extends Activity {
	
	private ArrayList<String> route = new ArrayList<String>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
		setContentView(R.layout.select_activity);

		for(String tmp : route)
			LogManager.printLog("tmp "+tmp);
		
		
	    // TODO Auto-generated method stub
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode ==1){
			 route = data.getStringArrayListExtra("RoutePoint");
		}
	}
}
