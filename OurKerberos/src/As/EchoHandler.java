package As;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import Helper.Des;
import Helper.Tool;
import Log.AppendContentToFile;
import Socket.Connection;
import Socket.MessageHandler;

class EchoHandler implements MessageHandler {
	AsDisplay frame = new AsDisplay();
	@Override
	public void onReceive(Connection connection, String message) {
		System.out.println("Got a message from a client:");
		System.out.println(message);
		frame.appenedMessage(message+"\n");
		Map<String, String> mapWithoutHead=unpackHead(message);
		String Head = mapWithoutHead.get("HEAD");
		if(Head.equals("000001001")) {
			String decrypted_Message=new Des().Decrypt(mapWithoutHead.get("CONTENT"), "cccccccc");
			Map<String, String> map_a=unpackContentRegister(decrypted_Message);
			File file1 = new File("/Users/chencong/Public/ccc_client.cer");
			InputStream inStream1;
			try {
				inStream1 = new FileInputStream(file1);
				// 创建X509工厂类
				CertificateFactory cf1 = CertificateFactory.getInstance("X.509");
				// 创建证书对象
				X509Certificate oCert1 = (X509Certificate) cf1.generateCertificate(inStream1);
				inStream1.close();
				String USERACCOUNT=map_a.get("USERACCOUNT");
				String PASSWORD=map_a.get("PASSWORD");
				Boolean flag=verifyWhenMd5Sign(USERACCOUNT+" "+PASSWORD, map_a.get("SIGN"), oCert1.getPublicKey());
				if(flag) {
					new ConnectionMysql().saveData(USERACCOUNT, PASSWORD);
				}else {
					frame.appenedMessage("验证不通过"+"\n");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else {
		Map<String, String> map = unpack(mapWithoutHead.get("CONTENT"));
		String TS1 = map.get("TIME");
		String TS2 = new Tool().getTime();
		if (Head.equals("000001000")&&(Integer.parseInt(TS2)-Integer.parseInt(TS1)<=200)) {
			String ID_C = map.get("USERACCOUNT");
			//log 
			AppendContentToFile a = new AppendContentToFile();
			a.method("/Users/apple/desktop/AS.txt", TS1+" "+ID_C);
			if (new ConnectionMysql().userAccountExsits(ID_C).equals(true)) {// 检查用户名在数据库中是否存在
				String K_C_TGS = new Tool().KeyCreate(64);
				String ID_TGS = map.get("TGS_NAME");
				String AD_C = connection.getSocket().getInetAddress().getHostAddress();;
				String Lifetime = "300";
				String Ticket_TGS = new Des().Encrypt(
						K_C_TGS + " " + ID_C + " " + AD_C + " " + ID_TGS + " " + TS2 + " " + Lifetime, "cccccccc");
				String K_C = new ConnectionMysql().getPassword(ID_C);// 获取用户名在数据库中对应的密码
				String returnMessage = new Des().Encrypt(
						"000100000 " + K_C_TGS + " " + ID_TGS + " " + TS2 + " " + Lifetime + " " + Ticket_TGS, K_C);
				System.out.println("Send back the message back to the client.");
				connection.println(returnMessage);
			}else {
				connection.println("");
			}
		}
		}

	}
	public Map<String, String> unpackHead(String message) {// 将client传得消息分离HEAD\CONTENT
		String[] strArr = message.split(" ",2);
		Map<String, String> map = new HashMap<String, String>();
		String[] key = {"HEAD","CONTENT" };
		for (int i = 0; i < strArr.length; i++) {
			frame.appenedMessage(key[i]+" "+strArr[i]+"\n");
			map.put(key[i], strArr[i]);
		}
		return map;
	}
	public Map<String, String> unpackContentRegister(String message) {// 将client传得消息分离HEAD\CONTENT
		String[] strArr = message.split(" ");
		Map<String, String> map = new HashMap<String, String>();
		String[] key = {"USERACCOUNT","PASSWORD","SIGN"};
		for (int i = 0; i < strArr.length; i++) {
			frame.appenedMessage(key[i]+" "+strArr[i]+"\n");
			map.put(key[i], strArr[i]);
		}
		return map;
	}
	public Map<String, String> unpack(String message) {// 将client传得消息分离
		String[] strArr = message.split(" ");
		Map<String, String> map = new HashMap<String, String>();
		String[] key = { "USERACCOUNT", "TGS_NAME", "TIME" };
		for (int i = 0; i < strArr.length; i++) {
			map.put(key[i], strArr[i]);
		}
		return map;
	}
	//对用md5和RSA私钥生成的数字签名进行验证
    public boolean verifyWhenMd5Sign(String content, String sign, PublicKey publicKey) throws Exception {
        byte[] contentBytes = content.getBytes("utf-8");
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(publicKey);
        signature.update(contentBytes);
        byte[] encodeBytes = sign.getBytes("utf-8");
        return signature.verify(Base64.getDecoder().decode(encodeBytes));
    }

}