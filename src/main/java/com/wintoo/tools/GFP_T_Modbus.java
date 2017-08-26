package com.wintoo.tools;

public class GFP_T_Modbus {
	public static String IntL8bitToString(int a){
		return (Integer.toHexString(a & 0xff).length() == 2) ? Integer.toHexString(a & 0xff) : "0" + Integer.toHexString(a & 0xff);
	}
	public static String packing(int speed_port, String address, int[] data){
		int portint = speed_port & 0x0f;//只有低4bit有效
		int controlint = (speed_port & 0xe0) | 0x0b;//只有低8bit有效
		String port=IntL8bitToString(portint);//端口号
		String control= IntL8bitToString(controlint);//去speed_port高3bit，然后或上0x0b得到控制字
		
		String addr=address.replace(" ", "");
		int address_int = Integer.parseInt(addr.substring(addr.length()-2,addr.length()), 16);//485地址转成
		int datalength=data.length+3;
		String datalength_l8bit = IntL8bitToString(datalength);
		String datalength_h8bit = IntL8bitToString(datalength>>8);
		
		int[] data_reg=new int[data.length+1];
		data_reg[0]=address_int;//将485地址添加到第一个上
		System.arraycopy(data, 0, data_reg, 1, data.length);//将data数据拷贝到data_reg的第二个数据开始上
		String CRC=Crc16.getcrc16(data_reg);//获取数据的16位crc码
		
		//将data转换为字符串数组
		String dataString="";
		for (int i = 0; i < data_reg.length; ) {
			dataString+=IntL8bitToString(data_reg[i]);
			i++;
		}
		
		
		String data_to_send="10e100000100"+port+control+"0a0a"+ datalength_l8bit + datalength_h8bit +dataString+CRC;
		
		return data_to_send.toUpperCase();
	}
}
