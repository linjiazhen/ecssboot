package com.wintoo.tools;
public class Crc16
{
	public static String getcrc16(int[] v)
	{	
		long lon = GetModBusCRC(v);
		int h1, l0;
		l0 = (int) lon / 256;
		h1 = (int) lon % 256;
		String s = "";
		if (Integer.toHexString(h1).length() < 2)
		{
			s = "0" + Integer.toHexString(h1);
		} else
		{
			s = Integer.toHexString(h1);
		}
		if (Integer.toHexString(l0).length() < 2)
		{
			s = s + "0" + Integer.toHexString(l0);
		} else
		{
			s = s + Integer.toHexString(l0);
		}
		return s;
	}
	

	
	public static long GetModBusCRC(int[] v)
	{
		long functionReturnValue = 0;
		long i = 0;
		long J = 0;

		long CRC = 0;
		CRC = 0xffffL;
		for (i = 0; i <= (v).length - 1; i++)
		{ // 2.把第一个8位二进制数据（既通讯信息帧的第一个字节）与16位的CRC寄存器的低8位相异或，把结果放于CRC寄存器；
			CRC = (CRC / 256) * 256L + (CRC % 256L) ^ v[(int) i];
			for (J = 0; J <= 7; J++)
			{ // 3.把CRC寄存器的内容右移一位（朝低位）用0填补最高位，并检查最低位；
				// 4.如果最低位为0：重复第3步（再次右移一位）；
				// 如果最低位为1：CRC寄存器与多项式A001（1010 0000 0000 0001）进行异或；
				// 5.重复步骤3和4，直到右移8次，这样整个8位数据全部进行了处理；
				long d0 = 0;
				d0 = CRC & 1L;
				CRC = CRC / 2;
				if (d0 == 1)
					CRC = CRC ^ 0xa001L;
			} // 6.重复步骤2到步骤5，进行通讯信息帧下一字节的处理；
		} // 7.最后得到的CRC寄存器内容即为：CRC码。
		CRC = CRC % 65536;
		functionReturnValue = CRC;
		return functionReturnValue;
	}

}
