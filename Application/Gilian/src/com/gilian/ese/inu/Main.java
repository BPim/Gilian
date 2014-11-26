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

	// ���� �������� �̾���� �� ��Ƽ��Ƽ ó���� ���� ȣ��
	@Override
	protected void onRestart() {
	// ��Ƽ��Ƽ�� �̹� ȭ�鿡 ���̰� �ִٰ� �����ϰ�
	// ���⼭ ����� ������ �о� ���δ�.
	Log.d("activity life cycle","onRestart()");
	super.onRestart();
	}
	 
	// ���� ���� ���� �� ȣ���
	@Override
	protected void onStart() {
	// ���� ��Ƽ��Ƽ�� ȭ�鿡 ���̹Ƿ�, �ʿ��� UI ���� ������ �����Ѵ�.
	Log.d("activity life cycle","onStart()");
	super.onStart();
	}
	 
	// Ȱ�� ���� ���� �� ȣ���
	@Override
	protected void onResume() {
	// �Ͻ� ������ ��� UI ������Ʈ�� ������
	// Ȥ�� ��Ƽ��Ƽ�� ���� �ʿ�������
	// ��Ƽ��Ƽ�� ��Ȱ��ȭ�Ǹ鼭 �Ͻ� �ߴܵ� ó���� �簳�Ѵ�.
	Log.d("activity life cycle","onResume()");
	super.onResume();
	}
	 
	// Ȱ�� ���� ������ ȣ���
	@Override
	protected void onPause() {
	// ��Ƽ��Ƽ�� Ȱ�� ������ ���׶��� ��Ƽ��Ƽ�� �ƴ� ���
	// ������Ʈ�� �ʿ䰡 ���� UI ����Ʈ�� ������
	// Ȥ�� CPU�� ���� ����ϴ� ó���� �Ͻ� �ߴ��Ѵ�.
	Log.d("activity life cycle","onPause()");
	super.onPause();
	}
	 
	// ���� ���� ������ ȣ���
	@Override
	protected void onStop() {
	// ���� �ִ� UI ������Ʈ�� ������ Ȥ�� ��Ƽ��Ƽ�� ȭ�鿡 ������ ���� ��
	// �ʿ�ġ ���� ó���� �Ͻ� �ߴ��Ѵ�. �� �޼ҵ尡 ȣ��ǰ� �� �ڿ���
	// ���μ����� ����� ���ɼ��� �����Ƿ� �ٲ� ��� ����� ���� ��ȭ�� ���ӽ�Ų��.
	Log.d("activity life cycle","onStop()");
	super.onStop();
	}
	 
	//��ü ���� ������ ȣ��
	@Override
	protected void onDestroy() {
	// �����带 �����ϰ� �����ͺ��̽� ������ �ݴ� �� ��� ���Ҹ� �����Ѵ�.
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