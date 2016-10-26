package com.fan.connect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.fan.util.BluetoothTools;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

/**
 * 这个类的作用是让服务端等待客户端进行连接，
 * 获取socket,进行通信
 * @author Administrator
 *
 */
public class ServerConn extends Thread {
	//开启服务端的蓝牙设备
	private BluetoothAdapter bluetoothadapter;
	//定义一个handler变量，因为要从线程中获取客户端的数据，来更新到界面上
	private Handler handler ;
	private InputStream in;
	private OutputStream out;
	public ServerConn(BluetoothAdapter bluetoothadapter,Handler handler){
		this.bluetoothadapter = bluetoothadapter;
		this.handler = handler;
	}
	@Override
	public void run() {
		
		try {
			//开启服务端蓝牙的端口
			BluetoothServerSocket serverSocket=bluetoothadapter
			.listenUsingRfcommWithServiceRecord("Server", BluetoothTools.PRIVATE_UUID);
			//客户端来连接，我服务端接收，服务端和客户端的端口连接到一起，就可以通信了
			BluetoothSocket socket=serverSocket.accept();
			Message msg = new Message();
			msg.what = BluetoothTools.CONNECTSUCCESS;
			handler.sendMessage(msg);
			in = socket.getInputStream();
			out = socket.getOutputStream();
			while(true){
				//客户端有数据我就读，没有数据我就等着你来发
				byte[] buffer = new byte[1024];
				int count = in.read(buffer);
				Message msg1 = new Message();
				msg1.what = BluetoothTools.SENDDATA;
				msg1.obj = new String(buffer,0,count);
				handler.sendMessage(msg1);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Message msg = new Message();
			msg.what = BluetoothTools.CONNECTFAIL;
			handler.sendMessage(msg);
		}
		
		super.run();
	}
	/**
	 * 写方法，服务端可以通过这个方法向客户端写入数据
	 * @param buffer 数据内容
	 */
	public void write(byte[] buffer){
		try {
			out.write(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

}
