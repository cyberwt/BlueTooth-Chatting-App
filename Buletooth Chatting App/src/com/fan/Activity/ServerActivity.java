package com.fan.Activity;

import com.fan.connect.ServerConn;
import com.fan.util.BluetoothTools;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * 主要是服务端的界面显示
 * @author Administrator
 *
 */
public class ServerActivity extends Activity {
	//定义textview变量，来设置连接状态
	private TextView textview;
	//定义button变量，用于发送
	private Button serverSend;
	//定义Edittext变量,用于给客户端发送内容
	private EditText serverContent;
	//定义TextView的变量，用于显示客户端发给服务端的数据
	private TextView serverShow;
	//定义一个蓝牙适配器
	protected BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
	//定义一个handler，从线程里接收数据，然后更新到服务端界面上
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			int serverId = msg.what;
			switch(serverId){
			case BluetoothTools.SENDDATA:
				String content = (String)msg.obj;
				serverShow.append(content);
				break;
			case BluetoothTools.CONNECTSUCCESS:
				setTitle("连接成功");
				break;
			case BluetoothTools.CONNECTFAIL:
				setTitle("连接失败");
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	private ServerConn serverConn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serverlayout);
		textview = (TextView)findViewById(R.id.conState);
		serverSend = (Button)findViewById(R.id.serverSend);
		serverContent = (EditText)findViewById(R.id.serverContent);
		serverShow = (TextView)findViewById(R.id.serverShow);
		//设置服务端发送按钮的点击事件
		serverSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//获取发送窗口的内容
				String Content = serverContent.getText().toString();
				//发送数据
				serverConn.write(Content.getBytes());
			}
		});
		//开启服务端线程，等待客户端进行连接
		serverConn = new ServerConn(blueadapter, handler);
		serverConn.start();
	}

}
