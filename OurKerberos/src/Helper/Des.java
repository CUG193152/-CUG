package Helper;


public class Des {
	public String XOR(String m,String c){//异或运算
		String temp="";
		if(m.length()==c.length()){
			for(int i=0;i<m.length();i++){
				temp+=m.charAt(i)^c.charAt(i);
			}
			//System.out.println("异或"+temp);
		}
		else{
			System.out.println("进行异或的两个字符串长度不等，不能进行异或操作");
		}
		return temp;
	}
	
	public String Initial_Permutation(String binary){//IP置换
		int []IP_Table = {//初始置换表IP
				58,50,42,34,26,18,10,2,
				60,52,44,36,28,20,12,4,
				62,54,46,38,30,22,14,6,
				64,56,48,40,32,24,16,8,
				57,49,41,33,25,17,9,1,
				59,51,43,35,27,19,11,3,
				61,53,45,37,29,21,13,5,
				63,55,47,39,31,23,15,7
				};
		String temp_binary="";
		for(int i=0;i<IP_Table.length;i++){//置换过程
			temp_binary += binary.charAt(IP_Table[i]-1);
		}
		//System.out.println("初始置换IP后： "+temp_binary);
		return temp_binary;
	}

	public String Initial_Permutation_1(String binary){//IP逆置换  
		int []IP_1_Table = {//逆初始置换表IP^-1
				40,8,48,16,56,24,64,32,39,7,47,15,55,23,63,31,
				38,6,46,14,54,22,62,30,37,5,45,13,53,21,61,29,
				36,4,44,12,52,20,60,28,35,3,43,11,51,19,59,27,
				34,2,42,10,50,18,58,26,33,1,41, 9,49,17,57,25
				};
		String temp_binary="";
		for(int i=0;i<IP_1_Table.length;i++){//置换过程
			temp_binary += binary.charAt(IP_1_Table[i]-1);
		}
		//System.out.println("逆置换IP： "+temp_binary);
		return temp_binary;
	}
	
	public String Expand_Substitution(String binary){//扩充/置换
		 //扩充置换表E  
		int []E_Table = {31, 0, 1, 2, 3, 4,  
		                  3, 4, 5, 6, 7, 8,  
		                  7, 8, 9, 10,11,12,  
		                  11,12,13,14,15,16,  
		                  15,16,17,18,19,20,  
		                  19,20,21,22,23,24,  
		                  23,24,25,26,27,28,  
		                  27,28,29,30,31,0};
		String expand_binary="";
		for(int i=0;i<48;i++){
			expand_binary+=binary.charAt(E_Table[i]);
		}
		//System.out.println("扩展置换"+expand_binary.length()+ "  "+ "  "+expand_binary);
		return expand_binary;
	}
	
	public String PC_1(String cipher_code){//置换选择1  
		int []PC_1_Table={57,49,41,33,25,17,9,
				1,58,50,42,34,26,18,
				10,2,59,51,43,35,27,
				19,11,3,60,52,44,36,
				63,55,47,39,31,23,15,
				7,62,54,46,38,30,22,
				14,6,61,53,45,37,29,
				21,13,5,28,20,12,4};
		String C0="";
		String D0="";
		for(int i=0;i<28;i++){
			C0+=cipher_code.charAt(PC_1_Table[i]-1);
		}
		for(int i=28;i<56;i++){
			D0+=cipher_code.charAt(PC_1_Table[i]-1);
		}
		//System.out.println("置换选择1"+C0.length()+" "+C0);
		//System.out.println("置换选泽1"+D0.length()+" "+D0);
		return C0+D0;
	}
	
	public String PC_2(String cipher_code){//置换/紧缩 
		int []PC_2_Table = {14,17,11,24,1,5,3,28,
				15,6,21,10,23,19,12,4,
				26,8,16,7,27,20,13,2,
				41,52,31,37,47,55,30,40,
				51,45,33,48,44,49,39,56,
				34,53,46,42,50,36,29,32};
		String temp="";
		for(int i=0;i<48;i++){
			temp+=cipher_code.charAt(PC_2_Table[i]-1);
		}
		//System.out.println("置换紧缩后"+temp.length()+ "  "+temp);
		return temp;
	}
	
	public String Left_Shift(String code,int x){//循环左移x位
		String First_char="";
		String temp="";
		for(int i=0;i<x;i++){//将前x位先进行保存
			First_char += code.charAt(i);
		}
		for(int i=x;i<28;i++){//左移
			temp+=code.charAt(i);
		}
		temp+=First_char;//将前x位补在后面
		//System.out.println("左移"+x+"位：   "+"长度"+temp.length()+"      "+temp);
		return temp;
	}
	
	public String Sbox(String m){//代换/选择 
		 int [][]S =//S1  
	            {{14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7,  
	              0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8,  
	              4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0,  
	              15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13},  
	                //S2  
	              {15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10,  
	              3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5,  
	              0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15,  
	              13,8,10,1,3,15,4,2,11,6,7,12,0,5,14,9},  
	              //S3  
	              {10,0,9,14,6,3,15,5,1,13,12,7,11,4,2,8,  
	              13,7,0,9,3,4,6,10,2,8,5,14,12,11,15,1,  
	              13,6,4,9,8,15,3,0,11,1,2,12,5,10,14,7,  
	              1,10,13,0,6,9,8,7,4,15,14,3,11,5,2,12},  
	              //S4  
	              {7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15,  
	              13,8,11,5,6,15,0,3,4,7,2,12,1,10,14,9,  
	              10,6,9,0,12,11,7,13,15,1,3,14,5,2,8,4,  
	              3,15,0,6,10,1,13,8,9,4,5,11,12,7,2,14},  
	              //S5  
	              {2,12,4,1,7,10,11,6,8,5,3,15,13,0,14,9,  
	              14,11,2,12,4,7,13,1,5,0,15,10,3,9,8,6,  
	              4,2,1,11,10,13,7,8,15,9,12,5,6,3,0,14,  
	              11,8,12,7,1,14,2,13,6,15,0,9,10,4,5,3},  
	              //S6  
	              {12,1,10,15,9,2,6,8,0,13,3,4,14,7,5,11,  
	              10,15,4,2,7,12,9,5,6,1,13,14,0,11,3,8,  
	              9,14,15,5,2,8,12,3,7,0,4,10,1,13,11,6,  
	              4,3,2,12,9,5,15,10,11,14,1,7,6,0,8,13},  
	              //S7  
	              {4,11,2,14,15,0,8,13,3,12,9,7,5,10,6,1,  
	              13,0,11,7,4,9,1,10,14,3,5,12,2,15,8,6,  
	              1,4,11,13,12,3,7,14,10,15,6,8,0,5,9,2,  
	              6,11,13,8,1,4,10,7,9,5,0,15,14,2,3,12},  
	              //S8  
	              {13,2,8,4,6,15,11,1,10,9,3,14,5,0,12,7,  
	              1,15,13,8,10,3,7,4,12,5,6,11,0,14,9,2,  
	              7,11,4,1,9,12,14,2,0,6,10,13,15,3,5,8,  
	              2,1,14,7,4,10,8,13,15,12,9,0,3,5,6,11}
	              }; 
		 String temp="";
		 String new_m="";
		 if(m.length()==48){//检验m的长度是否为48
			 for(int i=0;i<8;i++){//对八个盒子进行分别处理
				 String Six_temp="";//对m进行切分，每六位一块
				 Six_temp=m.substring(i*6,(i+1)*6);
				 String row="";
				 row+=Six_temp.charAt(0);
				 row+=Six_temp.charAt(5);
				 String column="";
				 for(int k=1;k<5;k++)
					 column+=Six_temp.charAt(k);
				 int Row=0;//行
				 int Column=0;//列
				 for(char c: row.toCharArray()){//将二进制字符串序列转换成对应数值
					 Row = Row * 2 + (c == '1' ? 1 : 0);}
				 for(char c: column.toCharArray()){
					 Column = Column * 2 + (c == '1' ? 1 : 0);}
				 
				 //确定行和列后在对应的S盒中找出相应的数值，并将其转换成二进制字符串
				 int location=S[i][Row*16+Column];//转换后在S表中的数值，需要将其转换为四位二进制字符串
				 temp=Integer.toBinaryString(location);//转换为2进制
				 if(temp.length()!=4){//扩展为四位
					 String temp1="";
					 for(int length=0;length<4-temp.length();length++)
						 temp1+="0";
					 temp=temp1+temp;
				 }
				 new_m+=temp;
				// System.out.println(temp+"  "+location+" "+Row+"  "+Column  +"   "+Six_temp);
			 }
		 }
		 else{
			 System.out.println("代换选择输入长度不为48");
		 }
		 //System.out.println("S合："+new_m);
		 return new_m;
	}
	
	public String P(String binary){//P置换
		int []P_Table = {//P置换表
				16,7,20,21,29,12,28,17,
				1,15,23,26,5,18,31,10,
				2,8,24,14,32,27,3,9,
				19,13,30,6,22,11,4,25,
				};
		String temp_binary="";
		for(int i=0;i<P_Table.length;i++){//置换过程
			temp_binary += binary.charAt(P_Table[i]-1);
		}
		//System.out.println("P置换后： "+temp_binary);
		return temp_binary;
	}
	
	public String F(String R,String K){//轮函数R  32  K  48
		String temp="";
		temp = Expand_Substitution(R);//扩充/置换  temp->48
		temp = XOR(temp,K);//异或运算    temp->48
		temp = Sbox(temp);//代换/选择   temp->32
		temp = P(temp);//P置换  temp 32
		return temp;
	}
	
	public String DES(String binary,String cipher_code){//DES加密过程
		binary = Initial_Permutation(binary);//IP置换
		String KEY = PC_1(cipher_code);//对密钥进行置换选择1，该过程只进行一次
		String L0=binary.substring(0, 32);
		String R0=binary.substring(32,64);
		for(int i=0;i<16;i++){
			String C=KEY.substring(0 ,28);//将K进行拆解
			String D=KEY.substring(28,56);
			String C0 = Left_Shift(C,Move_Times(i+1));//将C和D进行移位
			String D0 = Left_Shift(D,Move_Times(i+1));
			String temp =F(R0,PC_2(C0+D0));//F轮函数
			temp = XOR(temp,L0);//异或
			L0=R0;
			R0=temp;
		}
		return Initial_Permutation_1(L0+R0);
	}

	public String Decrypt_DES(String binary,String cipher_code){//DES解密
		binary = Initial_Permutation(binary);//IP置换
		String KEY = PC_1(cipher_code);//对密钥进行置换选择1，该过程只进行一次
		String L0=binary.substring(0, 32);
		String R0=binary.substring(32,64);
		for(int i=15;i>=0;i--){
			String C=KEY.substring(0 ,28);//将K进行拆解
			String D=KEY.substring(28,56);
			String C0 = Left_Shift(C,Move_Times(i+1));//将C和D进行移位
			String D0 = Left_Shift(D,Move_Times(i+1));
			//System.out.println("第"+i+" C  "+C0+" D  "+D0);
			String temp =F(L0,PC_2(C0+D0));//F轮函数
			temp = XOR(temp,R0);//异或
			R0=L0;
			L0=temp;
			//System.out.println("第"+(16-i)+"解密"+L0+R0);
		}
		
		return Initial_Permutation_1(L0+R0);
	}
	
	public String Encrypt(String message,String cipher_code) {//文件加密
		cipher_code=StrToBinstr(cipher_code);
		String temp="";
		String temp1="";
		int n=message.length();
		//System.out.println(n);
		int m=n/8;
		if((n%8)!=0){
			m=m+1;
			for(int i=0;i<m;i++){
				if(i!=m-1){
					temp1=message.substring(0, 8);
					temp1=StrToBinstr(temp1);
					temp=temp+DES(temp1,cipher_code);
					//System.out.println(temp);
					message=message.substring(8);
					//System.out.println(message);
				}
				
				else{
					//System.out.println(message.length()+"  "+(8-n%8));
		            for(int k=0;k<(8-n%8);k++){
		            	
		            	message=message+" ";
		            	//result+="0"+temp;
		            }
		            temp1=message;
		            temp1=StrToBinstr(temp1);
					temp=temp+DES(temp1,cipher_code);
				}
			}
			//System.out.println(temp);
			//System.out.println(temp.length());
		}
		else{
			//System.out.println(m);
			for(int i=0;i<m;i++){
				temp1=message.substring(0, 8);
				temp1=StrToBinstr(temp1);
				temp=temp+DES(temp1,cipher_code);
				//System.out.println(temp);
				message=message.substring(8);
				//System.out.println(message);
			}
			//System.out.println(temp);
			//System.out.println(temp.length());
		}
		return temp;
      // String Encrypt_result=DES(temp,cipher_code);


    }
	
    public String Decrypt(String message,String cipher_code) {//文件解密
		cipher_code=StrToBinstr(cipher_code);
		String temp="";
		String temp1="";
		int n=message.length();
		//System.out.println("n="+n);
		int m=n/64;
		//System.out.println("m="+m);
		n=n%64;
		if(n==0){
			for(int i=0;i<m;i++){
				temp1=message.substring(0, 64);
				temp1=Decrypt_DES(temp1,cipher_code);
				message=message.substring(64);
				temp=temp+temp1;
				//System.out.println(temp);
			}
		}
		else{
            System.out.println(temp+"数据不对啊");
        }
		
		return BinstrToStr(temp);
    }
    
	public int Move_Times(int n){//对左移次数的规定 
		int temp=0;
		int []MOVE_TIMES = {1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};
		for(int i=0;i<n;i++){
			temp += MOVE_TIMES[i];
		}
		return temp;
	}
	
	public String StrToBinstr(String str) {//字符串转二进制
        char[] strChar=str.toCharArray();
        String result="";
        String temp="";
       
        for(int i=0;i<strChar.length;i++){
        	temp=Integer.toBinaryString(strChar[i]);
        	//System.out.println(temp+" "+temp.length());
            int num=temp.length();
            String other="";
            //System.out.println(result.length());
            for(int k=0;k<8-num;k++){
            	
            	other+="0";
            	//result+="0"+temp;
            }
            result +=other+temp;
        }
        //System.out.println(result+"       "+result.length());
        return result;
    }

	public String BinstrToStr(String binStr) { //将二进制字符串转换成字符串
		String result="";
		for(int i=0;i*8<binStr.length();i++){
			String temp=binStr.substring(i*8, (i+1)*8);
		    char[] t=temp.toCharArray();
		    int[] r=new int[t.length];   
		    for(int j=0;j<t.length;j++) {
		        r[j]=t[j]-48;
		    }
		    int sum=0;   
		    for(int j=0; j<r.length;j++){
		        sum +=r[r.length-1-j]<<j;
		    }
		    result+=(char)sum;
		}
	    return result;
	}
	

}


