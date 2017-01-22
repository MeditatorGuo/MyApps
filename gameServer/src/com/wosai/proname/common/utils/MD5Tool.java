package com.wosai.proname.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author zero ice 2014-6-18 ����6:33:29
 * 
 */
public class MD5Tool {
	/**
	 * ����MD5����
	 * 
	 * @param str
	 *            ������ժҪ������
	 * @return ����ժҪ16���ƴ�
	 */
	public static String ToMD5(final String str) {
		if (str == null) {
			return null;
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] buf = messageDigest.digest(str.getBytes());
			String tt = byte2HexString(buf);
			String md5Str1 = tt;
			System.out.println(md5Str1);
			String md5Str2 = tt.substring(8, 24);// buf.toString().md5Strstring(8,
													// 24);
			System.out.println(md5Str2);
			return tt;

		} catch (NoSuchAlgorithmException e) {
			throw new SecurityException(e.getMessage());
		}
	}

	/**
	 * ���ֽ�����ת��Ϊ16�����ַ���
	 * 
	 * @param data
	 *            ����ת�����ֽ�����
	 * @return 16���Ƶ��ַ���
	 */
	public static String byte2HexString(byte[] data) {
		StringBuffer checksumSb = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			String hexStr = Integer.toHexString(0x00ff & data[i]);
			if (hexStr.length() < 2) {
				checksumSb.append("0");
			}
			checksumSb.append(hexStr);
		}
		return checksumSb.toString();
	}

	public static void main(String[] args) throws Exception {
		System.out.println(ToMD5("����"));
	}
}
