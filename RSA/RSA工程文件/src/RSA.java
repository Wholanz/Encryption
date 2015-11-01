import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;




public class RSA {
	
	public static void Encode(File m, File c, BigInteger N, BigInteger e, int scale){
		InputStream input = null; //This method is used for encryption
		OutputStream output =null;
		int bytenum;  //we use scale to define bits number in one block
		if(scale % 8 ==0){  
		bytenum = scale/8;}
		else bytenum = scale/8 + 1;
		byte[] rin = new byte[bytenum];
		byte[] eout = new byte[bytenum];
		int readCount = 0,tempCount = 0;
		BigInteger en;
		try{
			if(!c.exists())  //create the file to output the encrpted data
				c.createNewFile();
		}catch(Exception ex){
			System.out.println("Encode File creation error!");
			return;
		}
		try {
			output = new FileOutputStream(c);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return;
		}
		try{
				if(m.isFile() && m.exists()){
			
					input = new FileInputStream(m); //read the number bytenum bytes into the array
					while((readCount = input.read(rin))!= -1 ){
						while(readCount < bytenum) {
							tempCount = input.read(rin,readCount,bytenum-readCount);
							if(tempCount == -1){
								for(int i=readCount;i<bytenum;i++)
									rin[i] = 0;
								break;
							}
							readCount += tempCount;
							}
						
							en = new BigInteger(rin);  //get the reverse numbers
							
							if(en.compareTo(BigInteger.ZERO) < 0){
							en = en.modPow(e, N);   //encrypt
							en = en.subtract(N);
							}
							else	en = en.modPow(e, N);
						
							eout = en.toByteArray();
							output.write(eout);  //write to the file
							
							
							
			}
					readCount = 0;
					}
		}catch(Exception ex){
			System.out.println("File read error!");
			return;
		}
		try {
			input.close();
			output.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static void Decode(File m, File c, BigInteger N, BigInteger d, int scale){
		InputStream input = null;  //this method is used for decryption
		OutputStream output =null; //all method is the same as the encryption method 
		int bytenum;				
		if(scale % 8 ==0){
		bytenum = scale/8;}
		else bytenum = scale/8 + 1;
		byte[] rin = new byte[bytenum];
		byte[] dout = new byte[bytenum];
		int readCount = 0,tempCount = 0;
		BigInteger de;
		try{
			if(!m.exists())
				m.createNewFile();
		}catch(Exception ex){
			System.out.println("Decode File creation error!");
			return;
		}
		try {
			output = new FileOutputStream(m);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return;
		}
		try{
				if(c.isFile() && c.exists()){
			
					input = new FileInputStream(c);
					while((readCount = input.read(rin)) != -1 ){
						while(readCount < bytenum) {
							tempCount = input.read(rin,readCount,bytenum-readCount);
							if(tempCount == -1){
								for(int i=readCount;i<bytenum;i++)
									rin[i] = 0;
								break;
							}
							readCount += tempCount;
							}
							de = new BigInteger(rin);
							if(de.compareTo(BigInteger.ZERO) < 0){
								de = de.modPow(d, N);
								de = de.subtract(N);
								}
							else	de = de.modPow(d, N);
							dout = de.toByteArray();
							output.write(dout);
							
							
			}
					readCount = 0;
					}
		}catch(Exception ex){
			System.out.println("File read error!");
			return;
		}
		try {
			input.close();
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static BigInteger FindEk(BigInteger LN, int scale){
		BigInteger Ek;  // automatically find a key for encoding
		Random r;
		r = new Random ((int)(Math.random()*100)); //random seed
	
		while(true){
			
			Ek = new BigInteger(scale, r);  //randomly generate a key of some scale
			if(Ek.compareTo(LN) >= 0) continue;
			if(Ek.gcd(LN).compareTo(BigInteger.ONE) != 0) continue;//check if valid
			break;
		}
		return Ek;
	}
	
	public static boolean StrIsDigit(String s){ //check a if a string is a digit
		
		if(s.isEmpty()) return false;
		for(int i = 0 ; i < s.length(); i++){
			if(!Character.isDigit(s.charAt(i)))
				return false;
			
		}
		return true;
	}
	
	public static RSA_key getKey(){ //get a key for encryption and decryption
		BigInteger prime1, prime2; //two prime number
		BigInteger N;		
		BigInteger LN;
		BigInteger e,d;
		int scale = 0 , prime1_scale = 0 , prime2_scale = 0;
		RSA_key result = new RSA_key();	//return the key
		
		
		e = BigInteger.valueOf(0); 
		
		Scanner in = new Scanner(System.in);
		while (scale < 504){  //input the scale >= 504 bits
			System.out.println("********************************************************************************************");
			System.out.print("Please enter the scale(>=504) of the key:");
			scale = in.nextInt();
			scale = scale - scale % 8;
			if(scale<504) 
				System.out.println("Too small scale!");
			}
		in.nextLine();
		
		prime1_scale = (int)(Math.random()*scale/3+1); //two scale of the 2 prime numbers
		prime2_scale = scale - prime1_scale;	//divide the bit scale by 3 to set 2 scales of prime number
		
		//randomly generate the prime number of the scale
		prime1 = new BigInteger(prime1_scale, 100, new Random((int)(Math.random()*100)));
		prime2 = new BigInteger(prime2_scale, 100, new Random((int)(Math.random()*100)));
		
		
		
		N = prime1;
		N = N.multiply(prime2);  //calculate N
		LN = prime1.subtract(BigInteger.ONE);  //calculate LN
		LN = LN.multiply(prime2.subtract(BigInteger.ONE));
		
		String en = null;
		BigInteger recommend = FindEk(LN, scale); //automatically find a key recommended
		
		
		while(true){
			System.out.println("********************************************************************************************");
			System.out.println("Recommended encode key: "+recommend);
			System.out.print("Enter the encode key you like:");
			
			en = in.nextLine();
			System.out.println("********************************************************************************************");
			
			if(!StrIsDigit(en)){ // input the key you like
				System.out.println("Not a decimal number!");
				continue;
			}
			e = new BigInteger(en); //check if the key is valid 
			if(e.compareTo(LN)>=0){
				System.out.println("The number input must be less than LN!");
				continue;
			}
			if(e.gcd(LN).compareTo(BigInteger.ONE) !=0){
				System.out.println("The greatest common divisor of LN and the number must be 1!");
				continue;
			}
			break;
		}
		

		
		d = e.modInverse(LN); //calculate the decryption key
		
		//output the key information
		System.out.println("********************************************************************************************");
		System.out.println("The first prime number is "+prime1);
		System.out.println("The second prime number is "+prime2);
		System.out.println("The public key N is "+N);
		System.out.println("LN is "+LN);
		System.out.println("The public encode key is "+e);
		System.out.println("The private decode key is "+d);
		System.out.println("********************************************************************************************");
		
		//return the key
		result.d = d;
		result.e = e;
		result.N = N;
		result.scale = scale;
		
		
		return result;
		
		
	
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int scale = 0;
		BigInteger N ;
		BigInteger e ,d;
		RSA_key key ; 
		String filename;
		int op = 0;
		
		Scanner in = new Scanner(System.in);
		System.out.println("Now we will get a key first!");
		key = getKey(); 		//get a key
		
		
		N = key.N;
		e = key.e;
		d = key.d;
		scale = key.scale;
		
		
		File mfile = null;
		File cfile = null;
		File m2file = null;
		
		
        while( true ){
        	System.out.println("****************************************************");
        	System.out.println("What do you want to do?Please enter the number of the exact operation");
            System.out.println("1.Encode some file");
            System.out.println("2.Decode some file");
            System.out.println("3.Exit");
            System.out.println("4.Get a new key");
            System.out.print("Your choice:");
            
            op = in.nextInt(); //get a operation number
            in.nextLine();
            filename = null;
            mfile = null;
            cfile = null;
	        switch(op){
	        	case 1:
	        		System.out.println("****************************************************");
	        		System.out.println("All files must be put in directory: D:/");
	        
	        		System.out.println("Enter the file name to be encrypted without .txt ");
	        		
	        		mfile = null;
	        		while(mfile ==null||!mfile.exists()||!mfile.isFile() ){
	        			filename = in.nextLine(); //get a valid mfile
	        			mfile = new File("D:/"+filename+".txt");
	        			if(!mfile.exists()||!mfile.isFile())
	        				System.out.println("Not a valid file,Please enter again:");
	        		}
	        		System.out.println("Enter the file name to output the encrypted data without .txt ");
	        		filename = in.nextLine();
	        		cfile = new File("D:/"+filename+".txt");
	        		
	        		Encode(mfile, cfile, N, e, scale); //encryption
	        		System.out.println("Encryption done!");
	        		break ;
	        	case 2:
	        		System.out.println("****************************************************");
	        		System.out.println("All files must be put in directory: D:/");
	        		
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
	        		Decode(m2file, cfile, N, d, scale); //decryption
	        		System.out.println("Decryption done!");
	              
	        		break ;
	        	case 3:
	        		in.close();
	        		return;
	        	case 4:
	        		key = getKey(); //get a new key
	        		
	        		N = key.N;
	        		e = key.e;
	        		d = key.d;
	        		scale = key.scale;
	        		break;
	        	default:
	        		System.out.println("****************************************************");
	        		System.out.println("Wrong Input! Please enter again:");
	        		break;
        	}
        }
		
		
		
	
		
		
	}

}
