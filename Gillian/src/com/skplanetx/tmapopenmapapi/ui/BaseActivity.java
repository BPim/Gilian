package com.skplanetx.tmapopenmapapi.ui;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.skplanetx.tmapopenmapapi.R;


public class BaseActivity extends Activity implements View.OnClickListener {
	
	private RelativeLayout contentView = null;
	private static Context mCtx = null;
	
	private LinearLayout base ;
	private LinearLayout navi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.setContentView(R.layout.base_activity);
		mCtx = this;
		
		contentView  = (RelativeLayout)findViewById(R.id.contentView);
		base = (LinearLayout)findViewById(R.id.base);
	      navi = (LinearLayout)findViewById(R.id.navi);
	      
	      base.setVisibility(View.VISIBLE);
	      navi.setVisibility(View.GONE); 
		
		super.onCreate(savedInstanceState);
		
	}
	   public void goNavigation()
	   {
	      base.setVisibility(View.GONE);
	      navi.setVisibility(View.VISIBLE);
	   }
	   
	   public void cancelNavigation()
	   {
	      navi.setVisibility(View.GONE);
	      base.setVisibility(View.VISIBLE);
	      
	   }
	@Override
	protected void onResume() {
		super.onResume();
	}

	
	
	@Override
	public void setContentView(int res)  {
		
		contentView.removeAllViews();
		
		LayoutInflater inflater;
		inflater = LayoutInflater.from(this);
		
		View item = inflater.inflate(res, null);
		contentView.addView(item, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
				
	}

	
	
	@Override
	public void setContentView(View view) {
		
		contentView.removeAllViews();
		
		contentView.addView(view, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
	}
	
	
	public void addView(View v)
	{
		contentView.removeAllViews();
		contentView.addView(v, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}

	
	
	@Override
	public void onClick(View v) {
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
}
