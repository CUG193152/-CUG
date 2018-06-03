package com.cc.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Cipher;

import Helper.Des;
import Socket.SocketClient;
import sun.misc.BASE64Encoder;

public class CertManager {
	String message;
	public CertManager(String message) {
		this.message=message;
		showCertInfo();
	}

	public void showCertInfo() {
		try {
			// 读取证书文件
			File file = new File("/Users/chencong/Public/ccc_client.cer");
			InputStream inStream = new FileInputStream(file);
			// 创建X509工厂类
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			// 创建证书对象
			X509Certificate oCert = (X509Certificate) cf.generateCertificate(inStream);
			inStream.close();
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
			String info = null;
			// 获得证书版本
			info = String.valueOf(oCert.getVersion());
			System.out.println("证书版本:" + info+"		");
			// 获得证书序列号
			info = oCert.getSerialNumber().toString(16);
			System.out.println("证书序列号:" + info+	"");
			// 获得证书有效期
			Date beforedate = oCert.getNotBefore();
			info = dateformat.format(beforedate);
			System.out.println("证书生效日期:" + info+"		");
			Date afterdate = oCert.getNotAfter();
			info = dateformat.format(afterdate);
			System.out.println("证书失效日期:" + info+"		");
			// 获得证书主体信息
			info = oCert.getSubjectDN().getName();
			System.out.print("证书拥有者:" + info+"		");
			System.out.print("证书HASH值:" + info+"		");//1541214148
			PublicKey publicKey = oCert.getPublicKey();
			BASE64Encoder base64Encoder = new BASE64Encoder();
			String publicKeyString = base64Encoder.encode(publicKey.getEncoded());
			System.out.println("-----------------公钥--------------------");
			System.out.println(publicKeyString);
			System.out.println("-----------------公钥--------------------");
//			System.out.println("公钥:" + oCert.getPublicKey());
			//加上数字签名
			PrivateKey privateKeyVerify = getPrivateKey("/Users/chencong/Public/ccc_client.jks", "111222zz","ccc_client", "111222zz");
			String sign=getMd5Sign(message,privateKeyVerify);
			message=message+" "+sign;
			String encrypted_Message=new Des().Encrypt(message, "cccccccc");
			System.out.println("encrypted_Message : " + encrypted_Message);
			System.out.println("000001001 "+"message:"+message);
			SocketClient clientToAs = new SocketClient(InetAddress.getLocalHost(), 5556);
			clientToAs.println("000001001 "+encrypted_Message);

			
//			byte[] encryptedBytes = encrypt(message.getBytes(), publicKey);
//			System.out.println("加密后：" + new String(encryptedBytes));
//			SocketClient clientToAs = new SocketClient(InetAddress.getLocalHost(), 5556);
//			clientToAs.println("000001001 "+new String(encryptedBytes));
//			PrivateKey privateKey = getPrivateKey("/Users/chencong/Public/ccc_server.jks", "111222zz","ccc_server", "111222zz");
//			// 私钥解密
//			byte[] decryptedBytes = decrypt(encryptedBytes, privateKey);
//			System.out.println("解密后：" + new String(decryptedBytes));
//			File file1 = new File("/Users/chencong/Public/ccc_client.cer");
//			InputStream inStream1 = new FileInputStream(file1);
//			// 创建X509工厂类
//			CertificateFactory cf1 = CertificateFactory.getInstance("X.509");
//			// 创建证书对象
//			X509Certificate oCert1 = (X509Certificate) cf1.generateCertificate(inStream1);
//			inStream.close();
//			System.out.println(verifyWhenMd5Sign(message, getMd5Sign(message,privateKeyVerify), oCert1.getPublicKey()));
		} catch (Exception e) {
			System.out.println("解析证书出错！");
			e.printStackTrace();
		}
	}// end showCertInfo
		// 公钥加密
	//用md5生成内容摘要，再用RSA的私钥加密，进而生成数字签名
    static String getMd5Sign(String content , PrivateKey privateKey) throws Exception {
        byte[] contentBytes = content.getBytes("utf-8");
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(privateKey);
        signature.update(contentBytes);
        byte[] signs = signature.sign();
        return Base64.getEncoder().encodeToString(signs);
    }
    //对用md5和RSA私钥生成的数字签名进行验证
    static boolean verifyWhenMd5Sign(String content, String sign, PublicKey publicKey) throws Exception {
        byte[] contentBytes = content.getBytes("utf-8");
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(publicKey);
        signature.update(contentBytes);
        byte[] encodeBytes = sign.getBytes("utf-8");
        return signature.verify(Base64.getDecoder().decode(encodeBytes));
    }
	public static byte[] encrypt(byte[] content, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");// java默认"RSA"="RSA/ECB/PKCS1Padding"
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(content);
	}

	// 私钥解密
	public static byte[] decrypt(byte[] content, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(content);
	}

	public static PrivateKey getPrivateKey(String keyStoreFile, String storeFilePass, String keyAlias,
			String keyAliasPass) {
		KeyStore ks;
		PrivateKey prikey = null;
		try {
			ks = KeyStore.getInstance("JKS");
			FileInputStream fin;
			try {
				fin = new FileInputStream(keyStoreFile);
				try {
					try {
						ks.load(fin, storeFilePass.toCharArray());
						// 先打开文件
						prikey = (PrivateKey) ks.getKey(keyAlias, keyAliasPass.toCharArray());
						// 通过别名和密码得到私钥
					} catch (UnrecoverableKeyException e) {
						e.printStackTrace();
					} catch (CertificateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return prikey;
	}
}