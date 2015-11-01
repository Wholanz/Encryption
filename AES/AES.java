package com.hyit.AES.Java; 

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;




public class AES{  
	
	public static String getPassword(File passfile){  //get the password from the password file
		String password = null;
		byte[] passArray;
        int readCount = 0 , tempCount = 0;
        InputStream input = null;
		byte[] rin = new byte[16];
	    ArrayList<Byte> pass = new ArrayList<Byte>();  
		
		
		if(passfile.isFile() && passfile.exists()){     //read the passfile
			try {
				input = new FileInputStream(passfile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		else {
			System.out.println("Not a valid file, please enter again!");
			return null;
		}
        
        try {
			while((readCount = input.read(rin)) != -1 ){
				while(readCount < 16) {   //each time we read 16 Bytes from the file
					tempCount = input.read(rin,readCount,16-readCount);
					if(tempCount == -1){
						for(int i=readCount;i<16;i++)
							rin[i] = 0;
						break;
					}
					readCount += tempCount;
				}
				for(int i=0; i<readCount; i++) pass.add(rin[i]);  //add the bytes to the list
				
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	passArray = new byte[pass.size()]; //convert the arraylist to the list
    	for(int i =0;i<pass.size();i++)
    		passArray[i] = pass.get(i);
    	pass.clear();
        
        password = new String(passArray); //convert the data to string
        
        try {
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return password;  //return the password string
	}
	
	
	
    public static void main(String[] args) {  
          
        String password = null;
        String filename = null;
       
        int op = 0;
        
        File mfile =null;  //file to be encrypted
		File cfile =null;  //file to be decrypted
		File m2file=null;  //file to get the decrypted data
		File passfile=null;  //file contain the password

//		mfile = new File("D:/m.txt");
//		cfile = new File("D:/c.txt");
//		m2file = new File("D:/m2.txt");
//		passfile = new File("D:/password.txt");

		
		 Scanner in = new Scanner(System.in);
        
        
        
        while( true ){  // a choice interface tell you how to deal with it
        	System.out.println("****************************************************");
        	System.out.println("What do you want to do? Please enter the number of the exact operation");
            System.out.println("1.Encode some file");
            System.out.println("2.Decode some file");
            System.out.println("3.Exit");
            System.out.print("Your choice: ");
            op = in.nextInt();
            in.nextLine();
            passfile = null;
            password = null;
            filename = null;
            mfile = null;
            cfile = null;
	        switch(op){
	        	case 1:
	        		System.out.println("****************************************************");
	        		System.out.println("All files must be put in directory: D:/");
	        		System.out.println("Enter the file name of the password without .txt ");
	        		while(passfile == null||!passfile.exists() || !passfile.isFile() ){ 
	        			filename = in.nextLine(); //get a valid password to encrypt
	        			passfile = new File("D:/"+filename+".txt");
	        			if(!passfile.exists() || !passfile.isFile())
	        				System.out.println("Not a valid file, please enter again:");
	        		}
	        		password = getPassword(passfile); //the password we get
	        		System.out.println("Enter the file name to be encrypted without .txt ");
	        		
	        		mfile = null; 
	        		while(mfile ==null||!mfile.exists()||!mfile.isFile() ){
	        			filename = in.nextLine();  //get a valid mfile
	        			mfile = new File("D:/"+filename+".txt");
	        			if(!mfile.exists()||!mfile.isFile())
	        				System.out.println("Not a valid file,Please enter again:");
	        		}
	        		System.out.println("Enter the file name to output the encrypted data without .txt ");
	        		filename = in.nextLine(); 
	        		cfile = new File("D:/"+filename+".txt");
	        		
	        		Encryption.encrypt(mfile, cfile, password); //encryption method
	        		System.out.println("Encryption done!");
	        		break ;
	        	case 2:
	        		System.out.println("****************************************************");
	        		System.out.println("All files must be put in directory: D:/");
	        		System.out.println("Enter the file name of the password without .txt ");
	        		while( passfile == null || !passfile.exists() || !passfile.isFile() ){
	        			filename = in.nextLine();   //get a valid password to decrypt
	        			passfile = new File("D:/"+filename+".txt");
	        			if(!passfile.exists() || !passfile.isFile())
	        				System.out.println("Not a valid file, please enter again:");
	        		}
	        		password = getPassword(passfile);
	        		System.out.println("Enter the file name to be decrypted without .txt ");
	        		
	        		cfile = null;
	        		while(cfile == null || !cfile.exists()||!cfile.isFile()  ){
	        			filename = in.nextLine(); //get a valid cfile
	        			cfile = new File("D:/"+filename+".txt");
	        			if(!cfile.exists()||!cfile.isFile())
	        				System.out.println("Not a valid file,Please enter again:");
	        		}
	        		System.out.println("Enter the file name to output the decrypted data without .txt ");
	        		filename = in.nextLine();
	        		m2file = new File("D:/"+filename+".txt");
	        		byte[] decryptResult =Decryption.decrypt(m2file, cfile,password);  
	        		System.out.println("Decryption done!");
	                System.out.println("Decryption£º" + new String(decryptResult));  
	        		break ;
	        	case 3:
	        		in.close();  //exit
	        		return;
	        	default:
	        		System.out.println("****************************************************");
	        		System.out.println("Wrong Input! Please enter again:");
	        		break;
        	}
        }
        
         
       
        
        
        
        
        
    }  
  
}  
