package com.hyit.AES.Java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;



import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;


public class Encryption {  
    public static void encrypt (File m, File c, String password) {  
    	InputStream  input =null;
    	OutputStream output =null;
    	byte[] byteContent  = null;
    	byte[] eout =null;
    	int readCount = 0,tempCount = 0;
    	byte[] rin = new byte[16];
    	ArrayList<Byte> list = new ArrayList<Byte>();
    	
    	try{
			if(!c.exists()) //if cfile not exist, create one to output the data
				c.createNewFile();
		}catch(Exception ex){
			System.out.println("Encode File creation error!");
			return ;
		}
    	try {
			output = new FileOutputStream(c); //output to cfile
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return ;
		}
    	
    	if(m.isFile() && m.exists()){   //read from mfile to get the data
			try {
				input = new FileInputStream(m);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
    	
    	try {
			while((readCount = input.read(rin)) != -1 ){
				while(readCount < 16) {    //every time read 16 bytes
					tempCount = input.read(rin,readCount,16-readCount);
					if(tempCount == -1){
						for(int i=readCount;i<16;i++)
							rin[i] = 0;
						break;
					}
					readCount += tempCount;
				}
				for(int i=0; i<readCount; i++) list.add(rin[i]); //add the data to the arraylist
				
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	byteContent = new byte[list.size()]; //convert the arraylist to an array
    	for(int i =0;i<list.size();i++)
    		byteContent[i] = list.get(i);
    	list.clear();
    	
    	try {                //encrypt and some information read from API 
                KeyGenerator kgen = KeyGenerator.getInstance("AES"); //KeyGenerator提供（对称）密钥生成器的功能。使用getInstance 类方法构造密钥生成器。  
           kgen.init(128, new SecureRandom(password.getBytes()));//使用用户提供的随机源初始化此密钥生成器，使其具有确定的密钥大小。  
           SecretKey secretKey = kgen.generateKey();     
                byte[] enCodeFormat = secretKey.getEncoded();     
                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");//使用SecretKeySpec类来根据一个字节数组构造一个 SecretKey,，而无须通过一个（基于 provider 的）SecretKeyFactory.  
                Cipher cipher = Cipher.getInstance("AES");// 创建密码器   //为创建 Cipher 对象，应用程序调用 Cipher 的 getInstance 方法并将所请求转换 的名称传递给它。还可以指定提供者的名称（可选）。   
                      
                cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化     
           byte[] result = cipher.doFinal(byteContent); //按单部分操作加密或解密数据，或者结束一个多部分操作。数据将被加密或解密（具体取决于此 Cipher 的初始化方式）。     
           eout =  result;
           output.write(eout);
           // 加密     
        } catch (NoSuchAlgorithmException e) {     
                e.printStackTrace();     
        } catch (NoSuchPaddingException e) {     
                e.printStackTrace();     
        } catch (InvalidKeyException e) {     
                e.printStackTrace();     
        } catch (UnsupportedEncodingException e) {     
                e.printStackTrace();     
        } catch (IllegalBlockSizeException e) {     
                e.printStackTrace();     
        } catch (BadPaddingException e) {     
                e.printStackTrace();     
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     
    	try {
			input.close();
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return ;     
    }    
}  
