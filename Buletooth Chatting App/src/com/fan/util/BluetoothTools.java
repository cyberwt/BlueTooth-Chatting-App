package com.fan.util;

import java.util.UUID;

public class BluetoothTools {
		//本程序使用的UUID
public static final UUID PRIVATE_UUID = UUID.fromString("0f3561b9-bda5-4672-84ff-ab1f98e349b6");

//发送数据的消息
public static final int SENDDATA = 0;

//连接失败的消息
public static final int CONNECTFAIL = 1;
//连接成功的消息
public static final int CONNECTSUCCESS = 2;

}
