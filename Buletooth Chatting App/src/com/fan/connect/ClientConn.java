package com.fan.connect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.fan.util.BluetoothTools;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

/**
 * 这个类主要负责客户端连接服务端,获取socket对象，接收服务端的数据,
 * 提供客户端向服务端发送数据的函数
 * 
 * @author Administrator
 *
 */
public class ClientConn extends Thread {
	//定义一个蓝牙设备地址名称
	private String blueDevAddress;
	//定义一个蓝牙适配器成员变量
	protected BluetoothAdapter blueadapter;
	//成员变量
	private InputStream in;
	private OutputStream out;
	private Handler handler;
	//构造器初始化蓝牙设备名称
	public ClientConn(String blueDevAddress,BluetoothAdapter blueadapter,Handler handler){
		this.blueadapter = blueadapter;
		this.blueDevAddress = blueDevAddress;	
		this.handler = handler;
	}
	@Override
	public void run() {
//		///**
//		 * 本程序所使用的UUID
//		 */

		//通过地址名称创建一个蓝牙设备的连接
		BluetoothDevice device = blueadapter.getRemoteDevice(blueDevAddress);
		try {
			BluetoothSocket  socket = device
					.createRfcommSocketToServiceRecord(BluetoothTools.PRIVATE_UUID);
			//socket开始连接服务端
			socket.connect();
			Message msg  = new Message();
			msg.what=BluetoothTools.CONNECTSUCCESS;
			handler.sendMessage(msg);
			//连接成功后，获取读取流和写入流
			//获取读取流
		    in   = socket.getInputStream();
			out =socket.getOutputStream();
			//while循环不断的接收服务端的数据，只要服务端有数据发送，我就接收
			while(true){
				//设置一个缓存来保存接收到的数据
				byte[] buffer = new byte[1024];
				//read为阻塞型函数，有数据来我就读，没数据来我就等着你发数据
				in.read(buffer);
			//handler就是用于将线程中的数据发送到界面上更新
				Message msg1 = new Message();
				msg1.what = BluetoothTools.SENDDATA;
				msg1.obj = new String(buffer);
				handler.sendMessage(msg1);
				
				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Message msg  = new Message();
			msg.what=BluetoothTools.CONNECTFAIL;
			handler.sendMessage(msg);
		}
		super.run();
	}
	/**
	 * 函数作用写入数据
	 */
	public void write(byte[] buffer){
		try {
			//将数据写入
			out.write(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//释放资源
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
