package com.emis.vi.pay.hzsmk.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.apache.commons.codec.binary.Base64;

/**
 * @author:jianghm
 * @date:2016年6月21日 上午10:33:13
 * @description:
 */
public class CertificateCoder {

	public static final String CERT_TYPE = "X.509";

	/**
	 * 
	 * @param certificatePath
	 * @return Certificate 证书
	 * @throws Exception
	 */
	private static Certificate getCertificate(String certificatePath) throws Exception {
		CertificateFactory factory = CertificateFactory.getInstance(CERT_TYPE);
		FileInputStream in = new FileInputStream(certificatePath);
		Certificate certificate = factory.generateCertificate(in);
		in.close();
		return certificate;

	}

	/**
	 * 签名
	 * 
	 * @param sign
	 * @param strPfx
	 * @param strPassword
	 * @return
	 * @throws Exception
	 */
	public static String sign(String sign, String strPfx, String strPassword) throws Exception {
		// 获取证书
		X509Certificate x509 = (X509Certificate) getCertformPfx(strPfx, strPassword);
		// 构建签名,由证书指定签名算法
		Signature sa = Signature.getInstance(x509.getSigAlgName());
		// 获取私匙
		PrivateKey privateKey = getPvkformPfx(strPfx, strPassword);
		sa.initSign(privateKey);
		sa.update(sign.getBytes("GBK"));
		return Base64.encodeBase64String(sa.sign());
	}

	/**
	 * 验证签名
	 * 
	 * @param data
	 * @param sign
	 * @param cerPath
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(String data, String sign, String cerPath) throws Exception {
		X509Certificate x509 = (X509Certificate) getCertificate(cerPath);
		Signature sa = Signature.getInstance(x509.getSigAlgName());
		sa.initVerify(x509);
		sa.update(data.getBytes("GBK"));
		return sa.verify(Base64.decodeBase64(sign.getBytes("GBK")));
	}

	/**
	 * 验证签名
	 * 
	 * @param data
	 * @param sign
	 * @param x509
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(String data, String sign, X509Certificate x509) throws Exception {
		Signature sa = Signature.getInstance(x509.getSigAlgName());
		sa.initVerify(x509);
		sa.update(data.getBytes("GBK"));
		return sa.verify(Base64.decodeBase64(sign.getBytes("GBK")));
	}

	// 转换成十六进制字符串
	public static String Byte2String(byte[] b) {
		String hs = "";
		String stmp = "";

		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			// if (n<b.length-1) hs=hs+":";
		}
		return hs.toUpperCase();
	}

	public static byte[] StringToByte(int number) {
		int temp = number;
		byte[] b = new byte[4];
		for (int i = b.length - 1; i > -1; i--) {
			b[i] = new Integer(temp & 0xff).byteValue();// 将最高位保存在最低位
			temp = temp >> 8; // 向右移8位
		}
		return b;
	}

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

	public static void main(String[] args) throws Exception {
		// 签名测试
		// System.out.println(CertificateCoder.sign("签名串", "pfx证书路径", "pfx证书密码"));
		// System.out.println(CertificateCoder.sign("version=1.0.0&transCode=WHP0003&reqSeq=20160811144710&merCode=000016&chainNo=004&merCustId=9558801001179072005&serialNo=0000000011&orderNo=0000000011&shortCardNo=6226698929&dateTime=20160811
		// 14:47:30&amount=100&goods=test", "C:\\Users\\dell\\Desktop\\市民卡代扣\\市民卡代扣开发包\\市民卡代扣开发包\\test.pfx", "hzsmk"));
		// System.out.println(CertificateCoder.sign("version=1.0.0&transCode=WHP0001&reqSeq=20160812110110&merCode=000016&chainNo=004&merCustId=197823978932920&name=张小月&certType=0&certNo=330681198801181619&phone=13929748096&cardType=D&cardNo=6222531310857853&validDate=&cvv2=",
		// "C:\\Users\\dell\\Desktop\\市民卡代扣\\市民卡代扣开发包\\市民卡代扣开发包\\test.pfx", "hzsmk"));
		System.out.println(CertificateCoder.sign(
				"version=1.0.0&transCode=WHP0001&reqSeq=141344141243121212&merCode=000016&chainNo=004&merCustId=1234&name=张小月&certType=0&certNo=330681198801181619&phone=13929748096&cardType=D&cardNo=6222531310857853&validDate=&cvv2=",
				"D://zxx//实名卡//smk_agent.pfx", "smk123456"));
	}
}
