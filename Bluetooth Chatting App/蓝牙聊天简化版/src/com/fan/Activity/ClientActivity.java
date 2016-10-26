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

	//显示蓝牙搜索结果
	private ListView searchResult;
	//显示聊天内容
	private TextView chatContent;
	//搜索按钮
	private Button  search;
	//发送按钮
	private Button  clientSend;
	//输入内容的文本框
	private EditText charData;
	//定义一个list，用于存储蓝牙设备的名字和地址的字符串
	private List<String> bluetoothDevices = new ArrayList<String>();
	//定义一个蓝牙适配器
	protected BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
	//定义了一个数据适配器
	private ArrayAdapter<String> data;
	//定义一个变量handler来从线程中更新数据
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			//处理来自线程的消息，获取传来的数据，更新到界面上
			int dataid = msg.what;
			switch(dataid){
			case BluetoothTools.SENDDATA:
				String obj=(String)msg.obj;
				chatContent.append(obj);
				break;
			case BluetoothTools.CONNECTFAIL:
				setTitle("连接失败");
				break;
			case BluetoothTools.CONNECTSUCCESS:
				setTitle("连接成功");
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clientlayout);
		//接下来找到所有的控件，然后赋予初值
		searchResult=(ListView)findViewById(R.id.listview_result);
		chatContent = (TextView)findViewById(R.id.client_result);
		search = (Button)findViewById(R.id.search);
		clientSend = (Button)findViewById(R.id.clientSend);
		charData = (EditText)findViewById(R.id.clientContent);
        //注册广播BluetoothDevice.ACTION_FOUND;  
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(receiver, filter);
        //注册广播BluetoothAdapter.ACTION_DISCOVERY_FINISHED
         filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(receiver, filter);
        //给search按钮设置点击事件
        search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//开始搜索设备，将搜索到的设备添加到列表里
				//假如原来List中有设备，先将其清除
				if(bluetoothDevices.size()>0)
					bluetoothDevices.clear();
				//开始搜索设备，调用蓝牙适配器的方法
				//蓝牙设备的搜索
				setTitle("正在搜索设备...");
				//判断现在是否正在搜索蓝牙设备
				if(blueadapter.isDiscovering()){
				//将之前的搜索工作撤销
					blueadapter.cancelDiscovery();
				}
				//开始新的搜索工作
				blueadapter.startDiscovery();
			

			}
		});
        //定义一个listView数据适配器
        data = 
        		new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,bluetoothDevices);
        //将数据绑定到listview上
        searchResult.setAdapter(data);
        searchResult.setOnItemClickListener(this);
        //设置发送按钮的点击事件,将Edittext中的内容发送到服务端
        clientSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//获取发送内容
				String content = charData.getText().toString();
				//调用客户端线程提供的发送函数
				if(serverConn!=null&&content.length()>0){
					serverConn.write(content.getBytes());
				}
				
			}
		});
	}
	
    /**
     * 定义广播接收者，用于接收蓝牙搜索的广播，然后列举出所有的蓝牙设备
     */
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//用于接收系统发出的蓝牙广播
			String action  = intent.getAction();//获取的是广播的动作
			//蓝牙设备发现的广播
			if(BluetoothDevice.ACTION_FOUND.equals(action)){
				
				BluetoothDevice device =intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			    //使用arraylist记录蓝牙设备的名字和地址
				bluetoothDevices
				.add(device.getName()+"_"+device.getAddress());
				//提醒listView 更新数据
				data.notifyDataSetChanged();
				//蓝牙搜索完成的广播
			}else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
				setTitle("搜索设备");
			}

		}
	};
	private ClientConn serverConn;
	/**
	 * 响应点击某一个列表项的事件的函数
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		//获取列表项中被点击的item里面的内容
		String itemContent=data.getItem(position);
		//将数据中的蓝牙地址解析出来
		String address = itemContent.split("_")[1].trim();
		//如果现在蓝牙还正在搜索，把蓝牙设备关闭搜索
		if(blueadapter.isDiscovering()){
		//将之前的搜索工作撤销
			blueadapter.cancelDiscovery();
		}
		//第二部分  开启线程进行设备的连接和通信
		serverConn = new ClientConn(address,blueadapter,handler);
		serverConn.start();
		
		
		
		
	}

}
