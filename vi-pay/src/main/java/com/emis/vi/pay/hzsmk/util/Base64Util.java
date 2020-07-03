package com.emis.vi.pay.hzsmk.util;

import org.apache.commons.codec.binary.Base64;


public class Base64Util {
	
	public static byte[] encode(byte[] str) {
		byte[] result = null;

		if (str != null) {
			result = Base64.encodeBase64(str);
		}
		return result;
	}

	
	public static byte[] decode(byte[] str) {
		byte[] result = null;
		if (str != null) {
			result = Base64.decodeBase64(str);

		}
		return result;
	}

	
	public static String encodeString(String str) {
		return Base64.encodeBase64String(str.getBytes());
	}

	
	public static String decodeString(String str) {
		return new String(decode(str.getBytes()));
	}
	
}
