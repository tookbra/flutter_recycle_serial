package com.washer.sdk.flutter.recycle.serial;

import java.nio.ByteBuffer;
import java.util.Locale;

/**
 * Byte转换工具
 * @author okboom
 * @date 2021/1/23
 * @description
 */
public class ByteUtils {

	/**
	 * 十六进制字符串转byte[]
	 * 
	 * @param hex
	 *            十六进制字符串
	 * @return byte[]
	 */
	public static byte[] hexStr2Byte(String hex) {
		if (hex == null) {
			return new byte[] {};
		}

		// 奇数位补0
		if (hex.length() % 2 != 0) {
			hex = "0" + hex;
		}

		int length = hex.length();
		ByteBuffer buffer = ByteBuffer.allocate(length / 2);
		for (int i = 0; i < length; i++) {
			String hexStr = hex.charAt(i) + "";
			i++;
			hexStr += hex.charAt(i);
			byte b = (byte) Integer.parseInt(hexStr, 16);
			buffer.put(b);
		}
		return buffer.array();
	}

	/**
	 * byte[]转十六进制字符串
	 * 
	 * @param array
	 *            byte[]
	 * @return 十六进制字符串
	 */
	public static String byteArrayToHexString(byte[] array) {
		if (array == null) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			buffer.append(byteToHex(array[i]));
		}
		return buffer.toString();
	}

	public static String bytes2HexStr(byte[] src) {
		StringBuilder builder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return "";
		}
		char[] buffer = new char[2];
		for (int i = 0; i < src.length; i++) {
			buffer[0] = forDigit((src[i] >>> 4) & 0x0F, 16);
			buffer[1] = forDigit(src[i] & 0x0F, 16);
			builder.append(buffer);
		}
		return builder.toString();
	}

	private static char forDigit(int digit, int radix) {
		if ((digit >= radix) || (digit < 0)) {
			return '\0';
		}
		if ((radix < Character.MIN_RADIX) || (radix > Character.MAX_RADIX)) {
			return '\0';
		}
		if (digit < 10) {
			return (char) ('0' + digit);
		}
		return (char) ('A' - 10 + digit);
	}

	/**
	 * byte转十六进制字符
	 * 
	 * @param b
	 *            byte
	 * @return 十六进制字符
	 */
	public static String byteToHex(byte b) {
		String hex = Integer.toHexString(b & 0xFF);
		if (hex.length() == 1) {
			hex = '0' + hex;
		}
		return hex.toUpperCase(Locale.getDefault());
	}

	public static int isOdd(int num) {
		return num & 0x1;
	}

	public static byte HexToByte(String inHex) {
		return (byte) Integer.parseInt(inHex, 16);
	}

	public static byte[] hexToByteArr(String hex) {
		int hexLen = hex.length();
		byte[] result;
		if (isOdd(hexLen) == 1) {
			hexLen++;
			result = new byte[hexLen / 2];
			hex = "0" + hex;
		} else {
			result = new byte[hexLen / 2];
		}
		int j = 0;
		for (int i = 0; i < hexLen; i += 2) {
			result[j] = HexToByte(hex.substring(i, i + 2));
			j++;
		}
		return result;
	}
}
