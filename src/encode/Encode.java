package encode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Encode {
	
	public static void main(String[] args) {
		File fileIn = new File("test.txt");
		if(!fileIn.exists()){
			System.out.println("file don't exists! exiting...");
			System.exit(1);
		}
		File fileOut = new File("testout.txt");
		try {
			Zipper.zipFile(fileIn, fileOut);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
