package encode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Zipper {

	public static void zipFile(File in, File out) throws FileNotFoundException {
		HashMap<Integer, Counter> charsMap = new HashMap<>();
		FileInputStream fis = new FileInputStream(in);
		int input;
		try {
			// Reads occounrences
			while ((input = fis.read()) != -1) {
				if (charsMap.containsKey(input)) {
					charsMap.get(input).count++;
				} else {
					charsMap.put(input, new Counter());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		INode root = makeTree(charsMap);
		Map<Character, Integer> map = getCharMap(root);
	}

	private static HashMap<Character, Integer> getCharMap(INode root) {
		HashMap<Character, Integer> result = new HashMap<Character, Integer>();
		traversalRun(root, result, new ArrayList<Boolean>());
		
		
		return null;
	}

	private static void traversalRun(INode x,
			HashMap<Character, Integer> result, ArrayList<Boolean> arrayList) {
		if(x.getChar() != null){
			//TODO 
		}else{
			result.put(x.getChar(), getBoolArray(arrayList));
		}
	}

	private static Integer getBoolArray(ArrayList<Boolean> arrayList) {
		// TODO Auto-generated method stub
		return null;
	}

	private static INode makeTree(HashMap<Integer, Counter> charsMap) {
		ArrayList<INode> nodeMinHeap = new ArrayList<INode>();
		insertValues(charsMap, nodeMinHeap);
		Heapify(nodeMinHeap);
		while(nodeMinHeap.size() > 1){
			makeMoreTree(nodeMinHeap);
		}
		return nodeMinHeap.get(0);
	}

	private static void Heapify(ArrayList<INode> nodeMinHeap) {
		for(int i = nodeMinHeap.size() / 2; i >= 0; i--){
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
		int i_leftChild = nodeMinHeap.get(i * 2).getSum();
		int i_rightChild = nodeMinHeap.get((i * 2) + 1).getSum();
		
		if(i_parent <= i_leftChild){
			if(i_parent <= i_rightChild){
				// no swap
				return i;
			}else{
				// swap right child and parent
				swap(nodeMinHeap, i_parent, i_rightChild);
				return i_rightChild;
			}
		}else if(i_parent <= i_rightChild){
			// do nothing, is correct
			return i;
		}else{
			if(i_rightChild < i_leftChild){
				swap(nodeMinHeap, i_parent, i_rightChild);
				return i_rightChild;
			}else{
				swap(nodeMinHeap, i_parent, i_leftChild);
				return i_leftChild;
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
		while(nodeMinHeap.get(index / 2).getSum() > nodeMinHeap.get(index).getSum() && index != 0){
			swap(nodeMinHeap, index / 2, index);
			index /= 2;
		}
	}

	private static INode extractMin(ArrayList<INode> nodeMinHeap){
		if(nodeMinHeap.size() < 1){
			return null;
		}
		INode result = nodeMinHeap.get(0);
		nodeMinHeap.set(0, nodeMinHeap.remove(nodeMinHeap.size() - 1));
		return result;
	}

	private static void insertValues(HashMap<Integer, Counter> charsMap,
			ArrayList<INode> nodeMinHeap) {
		for (Integer i : charsMap.keySet()) {
			int in = i;
			nodeMinHeap.add(new CharNode((char)(in), charsMap.get(i).count));
		}
	}

}
