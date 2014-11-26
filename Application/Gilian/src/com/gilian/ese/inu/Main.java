package com.gilian.ese.inu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends ActionBarActivity {

	Context context ;
	Button addroute ; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		context = getApplicationContext();
		
		addroute = (Button) findViewById(R.id.addroute);
		addroute.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, Map.class);
				startActivity(intent);
			}
			
		});
	}

	// 가시 수명으로 이어기지 건 액티비티 처리를 위해 호출
	@Override
	protected void onRestart() {
	// 액티비티가 이미 화면에 보이고 있다고 생각하고
	// 여기서 변경된 내용을 읽어 들인다.
	Log.d("activity life cycle","onRestart()");
	super.onRestart();
	}
	 
	// 가시 수명 시작 시 호출됨
	@Override
	protected void onStart() {
	// 이제 액티비티가 화면에 보이므로, 필요한 UI 변경 사항을 적용한다.
	Log.d("activity life cycle","onStart()");
	super.onStart();
	}
	 
	// 활성 수명 시작 시 호출됨
	@Override
	protected void onResume() {
	// 일시 중지된 모든 UI 업데이트나 스레드
	// 혹은 액티비티에 의해 필요하지만
	// 액티비티가 비활성화되면서 일시 중단된 처리를 재개한다.
	Log.d("activity life cycle","onResume()");
	super.onResume();
	}
	 
	// 활성 수명 끝에서 호출됨
	@Override
	protected void onPause() {
	// 액티비티가 활성 상태의 포그라운드 액티비티가 아닐 경우
	// 업데이트될 필요가 없는 UI 얻데이트나 스레드
	// 혹은 CPU를 많이 사용하는 처리를 일시 중단한다.
	Log.d("activity life cycle","onPause()");
	super.onPause();
	}
	 
	// 가시 수명 끝에서 호출됨
	@Override
	protected void onStop() {
	// 남아 있는 UI 업데이트나 스레드 혹은 액티비티가 화면에 보이지 않을 때
	// 필요치 않은 처리를 일시 중단한다. 이 메소드가 호출되고 난 뒤에는
	// 프로세스가 종료될 가능성이 있으므로 바뀐 모든 내용과 상태 변화를 지속시킨다.
	Log.d("activity life cycle","onStop()");
	super.onStop();
	}
	 
	//전체 수명 끝에서 호출
	@Override
	protected void onDestroy() {
	// 스레드를 종료하고 데이터베이스 연결을 닫는 등 모든 리소를 해제한다.
	Log.d("activity life cycle","onDestroy()");
	super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}	
}

// test