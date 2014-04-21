package encode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.print.attribute.IntegerSyntax;

import decode.BoolArrEvolved;

public class ByteQueue {
	
	private static final int chunkSize = 32;
	Map<Integer, boolean[]> map;
	private boolean[] queue;
	private int index;
	private INode root;
	public static int headerSize = 256;

	public ByteQueue(Map<Integer, boolean[]> map){
		this.map = map;
		this.queue = new boolean[ByteQueue.chunkSize * 2]; // allow for bufferzone
		this.index = 0;
	}
	
	public ByteQueue(INode root) {
		this.root = root;
		this.queue = new boolean[ByteQueue.chunkSize * 2]; // allow for bufferzone
	}
	
	public void decodeToFile(BufferedReader br, BufferedWriter bw) throws IOException{
		int input;
		boolean[] new_bits;
		while ((input = br.read()) != -1) {
			new_bits = byteToBooleans(input);
			addToQueue(new_bits);
			while(index > chunkSize){
				writeNextChar(bw);
			}
		}
		br.close();
		bw.flush();
		bw.close();
	}

	private void writeNextChar(BufferedWriter bw) throws IOException {
		int count = 0;
		INode x;
		if(queue[count]){
			x = root.getRightChild();
		}else{
			x = root.getLeftChild();
		}
		count++;
		while(x.getChar() == null){
			x = queue[count++] ? x.getRightChild() : x.getLeftChild();
		}
		bw.write(x.getChar());
		bw.flush();
		moveBits(count);
	}

	public void writeToFile(BufferedReader fis, BufferedWriter fos) throws IOException{
		int input;
		boolean[] new_bits;
		while ((input = fis.read()) != -1) {
			new_bits = map.get(input);
			addToQueue(new_bits); // adding to queue
			new_bits = null; // unsetting
			if(index > chunkSize){
				writeChunk(fos);
			}
		}
		for (int i = index; i < ByteQueue.chunkSize; i++) {
			this.queue[i] = false;
		}
		writeChunk(fos); // adding last few bytes
	}

	private void writeChunk(BufferedWriter fos) throws IOException {
		int[] bytes = new int[chunkSize / 8];
		for (int i = 0; i < bytes.length; i++) {
			BitObject bo = new BitObject();
			for (int j = 7; j >= 0; j--) {
				bo.add(queue[(i * 8) + j]);
			}
			bytes[i] = bo.getNumber();
		}
		for (int i = 0; i < bytes.length; i++) {
			fos.write(bytes[i]);
		}
		moveBits();
	}

	private void moveBits() {
		boolean[] new_queue = new boolean[ByteQueue.chunkSize * 2];
		for (int i = ByteQueue.chunkSize; i < index; i++) {
			new_queue[i - ByteQueue.chunkSize] = this.queue[i];
		}
		this.queue = new_queue;
		index -= ByteQueue.chunkSize;
	}
	
	private void moveBits(int from) {
		boolean[] new_queue = new boolean[ByteQueue.chunkSize * 2];
		for (int i = from; i < index; i++) {
			new_queue[i - from] = this.queue[i];
		}
		this.queue = new_queue;
		index -= from;
	}

	private void addToQueue(boolean[] new_bits) {
		for (int i = 0; i < new_bits.length; i++) {
			queue[index] = new_bits[i];
			this.index++;
		}
	}

	public void writeHeaderToFile(BufferedWriter fos, int[] charsmap) throws IOException {
		System.out.println("Writing header");
		int total = 0;
		for (int i = 0; i < ByteQueue.headerSize; i++) {
			int[] numbers = new int[4]; // 4 bytes to 1 int
			integerToBytes(charsmap[i], numbers);
			total += charsmap[i];
			for (int j = 0; j < numbers.length; j++) {
				fos.write(numbers[j]);
			}
		}
		fos.write(total);
	}

	private void integerToBytes(int number, int[] array) {
		int firstReduce = 256 * 256 * 256;
		for (int i = 0; i < array.length; i++) {
			array[i] = number / firstReduce;
			number -= array[i] * firstReduce;
			firstReduce /= 256;
		}
	}
	
	private boolean[] byteToBooleans(int input) {
		boolean[] result = new boolean[8];
		BitObject bo = new BitObject(input);
		for (int i = 0; i < result.length; i++) {
			result[i] = bo.decrease();
		}
		return result;
	}
}
