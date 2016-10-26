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
        //提醒用户开启蓝牙
    	Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    	startActivityForResult(enable, 1);
        //找到打开服务端的按钮
        Button openServer = (Button)findViewById(R.id.openserver);
        //设置点击事件
        openServer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//点击后跳转到Server界面
				Intent intent = new Intent(MainActivity.this,ServerActivity.class);
				startActivity(intent);
			}
		});
        //找到打开客户端的按钮
        Button openClient = (Button)findViewById(R.id.openclient);
        //设置点击事件
        openClient.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//点击后跳转到Client界面
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
