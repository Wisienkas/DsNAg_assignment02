package encode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.InputSource;

public class Zipper {
	
	public static void zipFile(File in, File out) throws FileNotFoundException{
		ArrayList<Node> chars = new ArrayList<>();
		HashMap<Integer, Counter> charsMap = new HashMap<>();
		FileInputStream fis = new FileInputStream(in);
		int input;
		try {
			// Reads occounrences
			while((input = fis.read()) != -1){
				if(charsMap.containsKey(input)){
					charsMap.get(input).count++;
				}else{
					charsMap.put(input, new Counter());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Node = makeTree(charsMap);
		
	}
	
}
