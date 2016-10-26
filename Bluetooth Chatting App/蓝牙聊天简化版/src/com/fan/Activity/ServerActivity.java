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
 * ��Ҫ�Ƿ���˵Ľ�����ʾ
 * @author Administrator
 *
 */
public class ServerActivity extends Activity {
	//����textview����������������״̬
	private TextView textview;
	//����button���������ڷ���
	private Button serverSend;
	//����Edittext����,���ڸ��ͻ��˷�������
	private EditText serverContent;
	//����TextView�ı�����������ʾ�ͻ��˷�������˵�����
	private TextView serverShow;
	//����һ������������
	protected BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
	//����һ��handler�����߳���������ݣ�Ȼ����µ�����˽�����
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
				setTitle("���ӳɹ�");
				break;
			case BluetoothTools.CONNECTFAIL:
				setTitle("����ʧ��");
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
		//���÷���˷��Ͱ�ť�ĵ���¼�
		serverSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//��ȡ���ʹ��ڵ�����
				String Content = serverContent.getText().toString();
				//��������
				serverConn.write(Content.getBytes());
			}
		});
		//����������̣߳��ȴ��ͻ��˽�������
		serverConn = new ServerConn(blueadapter, handler);
		serverConn.start();
	}

}
