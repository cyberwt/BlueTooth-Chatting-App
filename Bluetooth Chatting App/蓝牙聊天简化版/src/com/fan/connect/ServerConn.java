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
 * �������������÷���˵ȴ��ͻ��˽������ӣ�
 * ��ȡsocket,����ͨ��
 * @author Administrator
 *
 */
public class ServerConn extends Thread {
	//��������˵������豸
	private BluetoothAdapter bluetoothadapter;
	//����һ��handler��������ΪҪ���߳��л�ȡ�ͻ��˵����ݣ������µ�������
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
			//��������������Ķ˿�
			BluetoothServerSocket serverSocket=bluetoothadapter
			.listenUsingRfcommWithServiceRecord("Server", BluetoothTools.PRIVATE_UUID);
			//�ͻ��������ӣ��ҷ���˽��գ�����˺Ϳͻ��˵Ķ˿����ӵ�һ�𣬾Ϳ���ͨ����
			BluetoothSocket socket=serverSocket.accept();
			Message msg = new Message();
			msg.what = BluetoothTools.CONNECTSUCCESS;
			handler.sendMessage(msg);
			in = socket.getInputStream();
			out = socket.getOutputStream();
			while(true){
				//�ͻ����������ҾͶ���û�������Ҿ͵���������
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
	 * д����������˿���ͨ�����������ͻ���д������
	 * @param buffer ��������
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
