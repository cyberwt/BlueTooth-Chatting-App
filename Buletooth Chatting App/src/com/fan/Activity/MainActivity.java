package com.fan.Activity;

import android.support.v7.app.ActionBarActivity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //�����û���������
    	Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    	startActivityForResult(enable, 1);
        //�ҵ��򿪷���˵İ�ť
        Button openServer = (Button)findViewById(R.id.openserver);
        //���õ���¼�
        openServer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//�������ת��Server����
				Intent intent = new Intent(MainActivity.this,ServerActivity.class);
				startActivity(intent);
			}
		});
        //�ҵ��򿪿ͻ��˵İ�ť
        Button openClient = (Button)findViewById(R.id.openclient);
        //���õ���¼�
        openClient.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//�������ת��Client����
				Intent intent = new Intent(MainActivity.this,ClientActivity.class);
				startActivity(intent);
			}
		});
        
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
