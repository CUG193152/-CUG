package com.cc.test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import sun.misc.BASE64Encoder;

public class TestRsa {

	public static void main(String[] args) throws InterruptedException, CertificateException {
		// TODO Auto-generated method stub

		StringBuffer cmd = new StringBuffer();

		cmd.append("keytool -printcert -rfc -file ccc_client.cer");
		try {
			Process process = Runtime.getRuntime()
					.exec("keytool -printcert -rfc -file /Users/chencong/Public/ccc_server.cer");
			// 取得命令结果的输出流
			InputStream fis = process.getInputStream();

			 //用一个读输出流类去读
			 InputStreamReader isr=new InputStreamReader(fis);
			 //用缓冲器读行
			 BufferedReader br=new BufferedReader(isr);
			 String line=null;
			 String content="";
			 //直到读完为止
			 while((line=br.readLine())!=null)
			 {
//			 System.out.println(line);
			 content+=line+"\n";
			 }
			 System.out.println(content);
			 getInformation(getInputStreamFromString(content));
			 process.destroy();
			 fis.close();
			 isr.close();
			 br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static InputStream getInputStreamFromString(String str){  
		  
        InputStream in=new ByteArrayInputStream(str.getBytes());
		return in;  
	}
	public static void getInformation(InputStream fis) throws CertificateException, IOException {
		// 创建X509工厂类
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
	
		// 创建证书对象
		X509Certificate oCert = (X509Certificate) cf.generateCertificate(fis);
		fis.close();
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
		String info = null;
		// 获得证书版本
		info = String.valueOf(oCert.getVersion());
		System.out.println("证书版本:" + info);
		// 获得证书序列号
		info = oCert.getSerialNumber().toString(16);
		System.out.println("证书序列号:" + info);
		// 获得证书有效期
		Date beforedate = oCert.getNotBefore();
		info = dateformat.format(beforedate);
		System.out.println("证书生效日期:" + info);
		Date afterdate = oCert.getNotAfter();
		info = dateformat.format(afterdate);
		System.out.println("证书失效日期:" + info);
		// 获得证书主体信息
		info = oCert.getSubjectDN().getName();
		System.out.println("证书拥有者:" + info);
		// 获得证书颁发者信息
		info = oCert.getIssuerDN().getName();
		System.out.println("证书颁发者:" + info);
		// 获得证书签名算法名称
		info = oCert.getSigAlgName();
		System.out.println("证书签名算法:" + info);
		PublicKey publicKey = oCert.getPublicKey();
		info = oCert.getSerialNumber().toString();
		System.out.println("证书HASH值:" + info);
		BASE64Encoder base64Encoder = new BASE64Encoder();
		String publicKeyString = base64Encoder.encode(publicKey.getEncoded());
		System.out.println("-----------------公钥--------------------");
		System.out.println(publicKeyString);
		System.out.println("-----------------公钥--------------------");
		System.out.println("公钥:" + oCert.getPublicKey());
	}


}  