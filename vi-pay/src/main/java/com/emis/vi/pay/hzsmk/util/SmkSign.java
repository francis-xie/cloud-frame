package com.emis.vi.pay.hzsmk.util;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.emis.vi.pay.hzsmk.util.CertificateCoder;
import com.emis.vi.pay.hzsmk.util.ConstantUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

public class SmkSign {

	private static PrivateKey getPvkformPfx(String strPfx, String strPassword) {
		try {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			FileInputStream fis = new FileInputStream(strPfx);
			// If the keystore password is empty(""), then we have to set
			// to null, otherwise it won't work!!!
			char[] nPassword = null;
			if ((strPassword == null) || strPassword.trim().equals("")) {
				nPassword = null;
			} else {
				nPassword = strPassword.toCharArray();
			}
			ks.load(fis, nPassword);
			fis.close();
			// Now we loop all the aliases, we need the alias to get keys.
			// It seems that this value is the "Friendly name" field in the
			// detals tab <-- Certificate window <-- view <-- Certificate
			// Button <-- Content tab <-- Internet Options <-- Tools menu
			// In MS IE 6.
			@SuppressWarnings("rawtypes")
			Enumeration enumas = ks.aliases();
			String keyAlias = null;
			if (enumas.hasMoreElements())// we are readin just one certificate.
			{
				keyAlias = (String) enumas.nextElement();
				// System.out.println("alias=[" + keyAlias + "]");
			}
			// Now once we know the alias, we could get the keys.
			PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, nPassword);
			return prikey;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	private static Certificate getCertformPfx(String strPfx, String strPassword) {
		FileInputStream fis = null;
		try {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			fis = new FileInputStream(strPfx);
			// If the keystore password is empty(""), then we have to set
			// to null, otherwise it won't work!!!
			char[] nPassword = null;
			if ((strPassword == null) || strPassword.trim().equals("")) {
				nPassword = null;
			} else {
				nPassword = strPassword.toCharArray();
			}
			ks.load(fis, nPassword);
			// Now we loop all the aliases, we need the alias to get keys.
			// It seems that this value is the "Friendly name" field in the
			// detals tab <-- Certificate window <-- view <-- Certificate
			// Button <-- Content tab <-- Internet Options <-- Tools menu
			// In MS IE 6.
			@SuppressWarnings("rawtypes")
			Enumeration enumas = ks.aliases();
			String keyAlias = null;
			if (enumas.hasMoreElements())// we are readin just one certificate.
			{
				keyAlias = (String) enumas.nextElement();
			}
			// Now once we know the alias, we could get the keys.
			Certificate cert = ks.getCertificate(keyAlias);
			return cert;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public static String sign(String sign, String pfx, String pwd) throws Exception {
		// 获取证书
		X509Certificate x509 = (X509Certificate) getCertformPfx(pfx, pwd);
		// 构建签名,由证书指定签名算法
		Signature sa = Signature.getInstance(x509.getSigAlgName());
		// 获取私匙
		PrivateKey privateKey = getPvkformPfx(pfx, pwd);
		sa.initSign(privateKey);
		sa.update(sign.getBytes("GBK"));
		return Base64.encodeBase64String(sa.sign());
		
	}
	
	public static boolean verifySignRSA(Map<String, Object> map) {
		String signStr = (String) map.get("signAture");
		System.out.println("signAture:[" + signStr + "]");
		map.remove("signAture");
		String resultSignData = ConstantUtil.coverMap2String(map);
        System.out.println("对参数按照key=value的格式，并按照参数名ASCII字典序排序生成字符串：[ " + resultSignData + " ]");
        
		Map<String, String> pmap = new HashMap<String, String>();
		pmap.put("certificate", "MIIDzDCCAzWgAwIBAgIQXu0meCrOtJsu+IBTh3wxdzANBgkqhkiG9w0BAQUFADAqMQswCQYDVQQGEwJDTjEbMBkGA1UEChMSQ0ZDQSBPcGVyYXRpb24gQ0EyMB4XDTE1MDUwNjA1MjMxOVoXDTE3MDUwNjA1MjMxOVowfTELMAkGA1UEBhMCQ04xGzAZBgNVBAoTEkNGQ0EgT3BlcmF0aW9uIENBMjESMBAGA1UECxMJc2hhcmUgc3VuMRQwEgYDVQQLEwtFbnRlcnByaXNlczEnMCUGA1UEAxQeMDQxQDc2OTgyOTA0Mi00QGphc29uQDAwMDAwMDAxMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCyfoctleTno7T+zOC3r18yJgmgqrQATN4AH9VCB+o/el5lLT6jgUUY3NWeqc+kZNy+uFrAWZ2KbOgajBwUgh6oCCSvYIo6yS0sFboGrE82p30uG5F0k17cRplLf4RvxoqRAkiJZkxi+VSF/v/5fcC3IfMnSRSIHO77V1A6Szo5kQIDAQABo4IBnjCCAZowHwYDVR0jBBgwFoAU8I3ts0G7++8IHlUCwzE37zwUTs0wHQYDVR0OBBYEFFZ9mDbO+7Ayn/V+pkJZezDmZ54pMAsGA1UdDwQEAwIE8DAMBgNVHRMEBTADAQEAMDsGA1UdJQQ0MDIGCCsGAQUFBwMBBggrBgEFBQcDAgYIKwYBBQUHAwMGCCsGAQUFBwMEBggrBgEFBQcDCDCB/wYDVR0fBIH3MIH0MFegVaBTpFEwTzELMAkGA1UEBhMCQ04xGzAZBgNVBAoTEkNGQ0EgT3BlcmF0aW9uIENBMjEMMAoGA1UECxMDQ1JMMRUwEwYDVQQDEwxjcmwxMDRfMTMxMzMwgZiggZWggZKGgY9sZGFwOi8vY2VydDg2My5jZmNhLmNvbS5jbjozODkvQ049Y3JsMTA0XzEzMTMzLE9VPUNSTCxPPUNGQ0EgT3BlcmF0aW9uIENBMixDPUNOP2NlcnRpZmljYXRlUmV2b2NhdGlvbkxpc3Q/YmFzZT9vYmplY3RjbGFzcz1jUkxEaXN0cmlidXRpb25Qb2ludDANBgkqhkiG9w0BAQUFAAOBgQCkiOQrTwY/IhMTXnoFInCHLc5xctDyW+gdsh09BHnHVntdLS49N74NOBgjRzBkqX3kQo+W9bWcJWoBiFd25zuugSpaJalcazZ/UfyGm9AvEYOMYzCbzsgqU2ieT4hAbd75T6wUOSXHx7AH1A24IRAOvZdT5XBOZJ6jGQedd80wfw==");
		
		X509Certificate x509Certificate = null;
		try {
			// Base64解码
			byte[] byteCert = Base64.decodeBase64(pmap.get("certificate").getBytes());
			// 转换成二进制流
			ByteArrayInputStream bain = new ByteArrayInputStream(byteCert);
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			// 生产证书
			x509Certificate = (X509Certificate) cf.generateCertificate(bain);

			return CertificateCoder.verify(resultSignData, signStr, x509Certificate);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean verifySignMD5(Map<String, Object> map, String key) {
		String signStr = (String) map.get("signAture");
		System.out.println("signAture:[" + signStr + "]");
		map.remove("signAture");
		String resultSignData = ConstantUtil.coverMap2String(map);
        System.out.println("对参数按照key=value的格式，并按照参数名ASCII字典序排序生成字符串：[ " + resultSignData + " ]");
        resultSignData = resultSignData + "&key=" + key;
        String verifySign = MD5Encoder(resultSignData, "UTF-8").toUpperCase();
        if (StringUtils.equals(verifySign, signStr)) {
        	return true;
        }
        return false;
		
	}
	
	public final static String MD5Encoder(String s, String charset) {
        try {
            byte[] btInput = s.getBytes(charset);
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < md.length; i++) {
                int val = ((int) md[i]) & 0xff;
                if (val < 16){
                	sb.append("0");
                }
                sb.append(Integer.toHexString(val));
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
