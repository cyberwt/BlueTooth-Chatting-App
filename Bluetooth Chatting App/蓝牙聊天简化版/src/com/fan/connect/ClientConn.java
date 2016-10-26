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
 * �������Ҫ����ͻ������ӷ����,��ȡsocket���󣬽��շ���˵�����,
 * �ṩ�ͻ��������˷������ݵĺ���
 * 
 * @author Administrator
 *
 */
public class ClientConn extends Thread {
	//����һ�������豸��ַ����
	private String blueDevAddress;
	//����һ��������������Ա����
	protected BluetoothAdapter blueadapter;
	//��Ա����
	private InputStream in;
	private OutputStream out;
	private Handler handler;
	//��������ʼ�������豸����
	public ClientConn(String blueDevAddress,BluetoothAdapter blueadapter,Handler handler){
		this.blueadapter = blueadapter;
		this.blueDevAddress = blueDevAddress;	
		this.handler = handler;
	}
	@Override
	public void run() {
//		///**
//		 * ��������ʹ�õ�UUID
//		 */

		//ͨ����ַ���ƴ���һ�������豸������
		BluetoothDevice device = blueadapter.getRemoteDevice(blueDevAddress);
		try {
			BluetoothSocket  socket = device
					.createRfcommSocketToServiceRecord(BluetoothTools.PRIVATE_UUID);
			//socket��ʼ���ӷ����
			socket.connect();
			Message msg  = new Message();
			msg.what=BluetoothTools.CONNECTSUCCESS;
			handler.sendMessage(msg);
			//���ӳɹ��󣬻�ȡ��ȡ����д����
			//��ȡ��ȡ��
		    in   = socket.getInputStream();
			out =socket.getOutputStream();
			//whileѭ�����ϵĽ��շ���˵����ݣ�ֻҪ����������ݷ��ͣ��Ҿͽ���
			while(true){
				//����һ��������������յ�������
				byte[] buffer = new byte[1024];
				//readΪ�����ͺ��������������ҾͶ���û�������Ҿ͵����㷢����
				in.read(buffer);
			//handler�������ڽ��߳��е����ݷ��͵������ϸ���
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
	 * ��������д������
	 */
	public void write(byte[] buffer){
		try {
			//������д��
			out.write(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//�ͷ���Դ
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
