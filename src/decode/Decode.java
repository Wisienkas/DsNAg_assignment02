package decode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import encode.Zipper;

public class Decode {
	
	public static void main(String[] args) {
		File fileIn = new File("testout.txt");
		if(!fileIn.exists()){
			System.out.println("file don't exists! exiting...");
			System.exit(1);
		}
		File fileOut = new File("testdecoded.txt");
		try {
			Zipper.unzipFile(fileIn, fileOut);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
