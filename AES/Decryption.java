package com.hyit.AES.Java;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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


import java.io.File;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;



public class Decryption {  
    public static byte[] decrypt(File m, File c, String password) {  
    	InputStream input = null;
    	OutputStream output = null;
//    	byte[] dout = null;
    	ArrayList<Byte> list = new ArrayList<Byte>();
    	byte[] rin = new byte[16];
    	byte[] content = null;
    	int readCount = 0, tempCount = 0;
    	
    	
    	try{
			if(!m.exists())  //if mfile not exist, create one
				m.createNewFile();
		}catch(Exception ex){
			System.out.println("Encode File creation error!");
			return null;
		}
    	try {
			output = new FileOutputStream(m); //output to the mfile
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return null;
		}
    	
    	if(c.isFile() && c.exists()){
			try {
				input = new FileInputStream(c); //get the encrypted data from the cfile
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
    	
    	try {
			while((readCount = input.read(rin)) != -1 ){
				while(readCount < 16) {  //every time we read 16 bytes
					tempCount = input.read(rin,readCount,16-readCount);
					if(tempCount == -1){
						for(int i=readCount;i<16;i++)
							rin[i] = 0;
						break;
					}
					readCount += tempCount;
				}
				for(int i=0; i<readCount; i++) list.add(rin[i]);
				
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	content = new byte[list.size()];  //convert the arraylist to an array
    	for(int i =0;i<list.size();i++)
    		content[i] = list.get(i);
    	list.clear();
    	
        try {   //decode
            KeyGenerator kgen = KeyGenerator.getInstance("AES");     //a AES keygenerator
            kgen.init(128, new SecureRandom(password.getBytes()));    //128 bits random key
            SecretKey secretKey = kgen.generateKey();     	//generate a key
            byte[] enCodeFormat = secretKey.getEncoded();     
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");                 
            Cipher cipher = Cipher.getInstance("AES");// cipher creator    
            cipher.init(Cipher.DECRYPT_MODE, key);// initialize to decrypt mode 
            byte[] result = cipher.doFinal(content); //decryption method
            output.write(result);
            return result; // ¼ÓÃÜ     
        } catch (NoSuchAlgorithmException e) {     
            e.printStackTrace();     
        } catch (NoSuchPaddingException e) {     
            e.printStackTrace();     
        } catch (InvalidKeyException e) {     
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
        return null;     
    }    
}  