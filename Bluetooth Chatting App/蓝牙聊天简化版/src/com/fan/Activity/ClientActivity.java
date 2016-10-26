package com.fan.Activity;



import java.util.ArrayList;
import java.util.List;

import com.fan.connect.ClientConn;
import com.fan.util.BluetoothTools;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ClientActivity extends Activity implements OnItemClickListener{

	//��ʾ�����������
	private ListView searchResult;
	//��ʾ��������
	private TextView chatContent;
	//������ť
	private Button  search;
	//���Ͱ�ť
	private Button  clientSend;
	//�������ݵ��ı���
	private EditText charData;
	//����һ��list�����ڴ洢�����豸�����ֺ͵�ַ���ַ���
	private List<String> bluetoothDevices = new ArrayList<String>();
	//����һ������������
	protected BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
	//������һ������������
	private ArrayAdapter<String> data;
	//����һ������handler�����߳��и�������
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			//���������̵߳���Ϣ����ȡ���������ݣ����µ�������
			int dataid = msg.what;
			switch(dataid){
			case BluetoothTools.SENDDATA:
				String obj=(String)msg.obj;
				chatContent.append(obj);
				break;
			case BluetoothTools.CONNECTFAIL:
				setTitle("����ʧ��");
				break;
			case BluetoothTools.CONNECTSUCCESS:
				setTitle("���ӳɹ�");
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clientlayout);
		//�������ҵ����еĿؼ���Ȼ�����ֵ
		searchResult=(ListView)findViewById(R.id.listview_result);
		chatContent = (TextView)findViewById(R.id.client_result);
		search = (Button)findViewById(R.id.search);
		clientSend = (Button)findViewById(R.id.clientSend);
		charData = (EditText)findViewById(R.id.clientContent);
        //ע��㲥BluetoothDevice.ACTION_FOUND;  
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(receiver, filter);
        //ע��㲥BluetoothAdapter.ACTION_DISCOVERY_FINISHED
         filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(receiver, filter);
        //��search��ť���õ���¼�
        search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//��ʼ�����豸�������������豸��ӵ��б���
				//����ԭ��List�����豸���Ƚ������
				if(bluetoothDevices.size()>0)
					bluetoothDevices.clear();
				//��ʼ�����豸�����������������ķ���
				//�����豸������
				setTitle("���������豸...");
				//�ж������Ƿ��������������豸
				if(blueadapter.isDiscovering()){
				//��֮ǰ��������������
					blueadapter.cancelDiscovery();
				}
				//��ʼ�µ���������
				blueadapter.startDiscovery();
			

			}
		});
        //����һ��listView����������
        data = 
        		new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,bluetoothDevices);
        //�����ݰ󶨵�listview��
        searchResult.setAdapter(data);
        searchResult.setOnItemClickListener(this);
        //���÷��Ͱ�ť�ĵ���¼�,��Edittext�е����ݷ��͵������
        clientSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//��ȡ��������
				String content = charData.getText().toString();
				//���ÿͻ����߳��ṩ�ķ��ͺ���
				if(serverConn!=null&&content.length()>0){
					serverConn.write(content.getBytes());
				}
				
			}
		});
	}
	
    /**
     * ����㲥�����ߣ����ڽ������������Ĺ㲥��Ȼ���оٳ����е������豸
     */
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//���ڽ���ϵͳ�����������㲥
			String action  = intent.getAction();//��ȡ���ǹ㲥�Ķ���
			//�����豸���ֵĹ㲥
			if(BluetoothDevice.ACTION_FOUND.equals(action)){
				
				BluetoothDevice device =intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			    //ʹ��arraylist��¼�����豸�����ֺ͵�ַ
				bluetoothDevices
				.add(device.getName()+"_"+device.getAddress());
				//����listView ��������
				data.notifyDataSetChanged();
				//����������ɵĹ㲥
			}else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
				setTitle("�����豸");
			}

		}
	};
	private ClientConn serverConn;
	/**
	 * ��Ӧ���ĳһ���б�����¼��ĺ���
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		//��ȡ�б����б������item���������
		String itemContent=data.getItem(position);
		//�������е�������ַ��������
		String address = itemContent.split("_")[1].trim();
		//������������������������������豸�ر�����
		if(blueadapter.isDiscovering()){
		//��֮ǰ��������������
			blueadapter.cancelDiscovery();
		}
		//�ڶ�����  �����߳̽����豸�����Ӻ�ͨ��
		serverConn = new ClientConn(address,blueadapter,handler);
		serverConn.start();
		
		
		
		
	}

}
