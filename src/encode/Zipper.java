package encode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class Zipper {
	
	public static void unzipFile(File in, File out) throws IOException {
		System.out.println("Unzipping requested file: " + in.getName());
		System.out.println("output file:" + out.getName());
		long time = System.currentTimeMillis();
		
		int[] charsmap = new int[ByteQueue.headerSize];
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(in), "UTF-8"));
		// reads in header
		int total = getCharMap(br, charsmap);
		br.close();
		// makes tree
		INode root = makeTree(charsmap);
		getCharMap(root);

		// decoding to new file 
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out), "UTF-8"));
		br = new BufferedReader(new InputStreamReader(new FileInputStream(in), "UTF-8"));
		br.skip(ByteQueue.headerSize * 4 + 4);
		ByteQueue bq = new ByteQueue(root);
		bq.decodeToFile(br, bw, total);
		System.out.println("Wrote new file... took: " + (System.currentTimeMillis() - time) + " ms!");
	}
	
	private static int getCharMap(BufferedReader br, int[] charsmap) throws IOException {
		int input;
		for (int i = 0; i < ByteQueue.headerSize; i++) {
			int[] numbers = new int[4];
			for (int j = 0; j < 4; j++) {
				input = br.read();
				if(input == -1){
					throw new IOException("End of file, not a zipped file!");
				}
				if(input != 0){
					input++;
					input--;
				}
				numbers[j] = input;
			}
			int Multiplier = 256 * 256 * 256;
			int total = 0;
			for (int j = 0; j < numbers.length; j++) {
				total += numbers[j] * Multiplier;
				Multiplier /= 256;
			}
			charsmap[i] = total;
		}
		
		// to get total number
		int[] numbers = new int[4];
		for (int j = 0; j < 4; j++) {
			input = br.read();
			if(input == -1){
				throw new IOException("End of file, not a zipped file!");
			}
			numbers[j] = input;
		}
		int Multiplier = 256 * 256 * 256;
		int total = 0;
		for (int j = 0; j < numbers.length; j++) {
			System.out.println(numbers[j]);
			total += numbers[j] * Multiplier;
			Multiplier /= 256;
		}
		System.out.println("total length: " + total);
		return total;
	}

	public static void zipFile(File in, File out) throws IOException {
		System.out.println("Zipping requested file: " + in.getName());
		System.out.println("output file:" + out.getName());
		long time = System.currentTimeMillis();
		
		int[] charsmap = new int[ByteQueue.headerSize];
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(in), "UTF-8"));
		
		int input;
		try {
			// Reads occounrences
			while ((input = br.read()) != -1) {
				charsmap[input]++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Making tree of roots");
		INode root = makeTree(charsmap); // works correct now
		System.out.println("Constructing char to code map!");
		Map<Integer, boolean[]> map = getCharMap(root); // works correct now
		System.out.println("Writing outputfile");
		writeFile(map, out, in, charsmap);
		System.out.println("Wrote new file... took: " + (System.currentTimeMillis() - time) + " ms!");
	}

	private static void writeFile(Map<Integer, boolean[]> map, File out, File in, int[] charsmap) throws IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out), "UTF-8"));
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(in), "UTF-8"));
		ByteQueue bq = new ByteQueue(map);
		bq.writeHeaderToFile(bw, charsmap);
		bq.writeToFile(br, bw);
		bw.flush();
		bw.close();
		br.close();
	}
	
	private static String printBoolArray(ArrayList<Boolean> A) {
		String s = "";
		for (int i = 0; i < A.size(); i++) {
			s += A.get(i) ? "1" : "0";
		}
		return s;
	}

	private static HashMap<Integer, boolean[]> getCharMap(INode root) {
		HashMap<Integer, boolean[]> result = new HashMap<Integer, boolean[]>();
		traversalRun(root.getLeftChild(), result, new ArrayList<Boolean>(), false);
		traversalRun(root.getRightChild(), result, new ArrayList<Boolean>(), true);
		
		return result;
	}

	private static void traversalRun(INode x,
			HashMap<Integer, boolean[]> result, ArrayList<Boolean> arrayList, boolean right) {
		if(x == null){
			//Security
			return;
		}
		// Adding boolean depending on if left or right child
		arrayList.add(right ? true : false);
		if(x.getChar() == null){
			traversalRun(x.getLeftChild(), result, arrayList, false);
			arrayList.remove(arrayList.size() - 1); // removing last added boolean from recursive call
			traversalRun(x.getRightChild(), result, arrayList, true);
			arrayList.remove(arrayList.size() - 1); // removing last added boolean from recursive call
		}else{
			addToMap(result, x.getChar(), arrayList);
			System.out.println("char: " + x.getChar() + "\tcode: " + printBoolArray(arrayList));
		}
	}



	private static void addToMap(HashMap<Integer, boolean[]> result,
			Integer c, ArrayList<Boolean> list) {
		boolean[] b_arr = new boolean[list.size()];
		for (int i = 0; i < b_arr.length; i++) {
			if(list.get(i)){
				b_arr[i] = true;
			}
		}
		result.put(c, b_arr);
	}

	private static INode makeTree(int[] charsMap) {
		ArrayList<INode> nodeMinHeap = new ArrayList<INode>();
		insertValues(charsMap, nodeMinHeap);
		Heapify(nodeMinHeap);
		while(nodeMinHeap.size() > 1){
			makeMoreTree(nodeMinHeap);
		}
		System.out.println("Assembled optimal tree!");
		return nodeMinHeap.get(0);
	}

	private static void Heapify(ArrayList<INode> nodeMinHeap) {
		for(int i = (nodeMinHeap.size() / 2) + 1; i >= 0; i--){
			int index = i;
			int indexCheck = index;
			while(true){
				index = isChildSwap(nodeMinHeap, index);
				if(indexCheck == index){
					break;
				}
				indexCheck = index;	
			}
		}
	}

	private static int isChildSwap(ArrayList<INode> nodeMinHeap, int i) {
		int i_parent = nodeMinHeap.get(i).getSum();
		int i_leftChild = nodeMinHeap.size() > (i * 2) + 1 ? nodeMinHeap.get((i * 2) + 1).getSum() : Integer.MAX_VALUE;
		int i_rightChild = nodeMinHeap.size() > (i * 2) + 2 ? nodeMinHeap.get((i * 2) + 2).getSum() : Integer.MAX_VALUE;
		
		if(i_parent <= i_leftChild){
			if(i_parent <= i_rightChild){
				// no swap
				return i;
			}else{
				// swap right child and parent
				swap(nodeMinHeap, i, (i * 2) + 2);
				return (i * 2) + 2;
			}
		}else if(i_parent <= i_rightChild){
			// do nothing, is correct
			return i;
		}else{
			if(i_rightChild < i_leftChild){
				// swap right child and parent
				swap(nodeMinHeap, i, (i * 2) + 2);
				return (i * 2) + 2;
			}else{
				// swap left child and parent
				swap(nodeMinHeap, i, (i * 2) + 1);
				return (i * 2) + 1;
			}
		}
	}

	private static void swap(ArrayList<INode> nodeMinHeap, int i_parent,
			int i_child) {
		INode temp = nodeMinHeap.get(i_parent);
		nodeMinHeap.set(i_parent, nodeMinHeap.get(i_child));
		nodeMinHeap.set(i_child, temp);
	}

	private static void makeMoreTree(ArrayList<INode> nodeMinHeap) {
		INode candidate1 = extractMin(nodeMinHeap);
		INode candidate2 = extractMin(nodeMinHeap);
		INode result = new PointerNode(candidate1, candidate2);
		insertIntoHeap(nodeMinHeap, result);
	}
	
	private static void insertIntoHeap(ArrayList<INode> nodeMinHeap,
			INode result) {
		int index = nodeMinHeap.size();
		nodeMinHeap.add(result);
		if(nodeMinHeap.size() == 1){
			return;
		}
		while(nodeMinHeap.get((index / 2) + 1).getSum() > nodeMinHeap.get(index).getSum() && index != 0){
			swap(nodeMinHeap, (index / 2) + 1, index);
			index /= 2;
		}
	}

	private static INode extractMin(ArrayList<INode> nodeMinHeap){
		if(nodeMinHeap.size() < 1){
			return null;
		}
		INode result = nodeMinHeap.get(0);
		if(nodeMinHeap.size() == 1){
			nodeMinHeap.remove(0);
			return result;
		}else{
			nodeMinHeap.set(0, nodeMinHeap.remove(nodeMinHeap.size() - 1));
		}
		int i = 0;
		int j = 0;
		do{
			i = j;
			j = isChildSwap(nodeMinHeap, i);
		}while(j != i);
		return result;
	}

	private static void insertValues(int[] charsMap,
			ArrayList<INode> nodeMinHeap) {
		for (int i = 0; i < charsMap.length; i++) {
			if(charsMap[i] != 0){
				nodeMinHeap.add(new CharNode(i, charsMap[i]));
			}
		}
	}

}
