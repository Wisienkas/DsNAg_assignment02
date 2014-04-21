package encode;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class ByteQueue {
	
	private static final int chunkSize = 32;
	Map<Integer, boolean[]> map;
	private boolean[] queue;
	private int index;

	public ByteQueue(Map<Integer, boolean[]> map){
		this.map = map;
		this.queue = new boolean[ByteQueue.chunkSize * 2]; // allow for bufferzone
		this.index = 0;
	}
	
	public void writeToFile(FileInputStream fis, FileOutputStream fos) throws IOException{
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

	private void writeChunk(FileOutputStream fos) throws IOException {
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

	private void addToQueue(boolean[] new_bits) {
		for (int i = 0; i < new_bits.length; i++) {
			queue[index] = new_bits[i];
			this.index++;
		}
	}

	public void writeHeaderToFile(FileOutputStream fos,
			Map<Integer, boolean[]> map2) {
		
	}
	
}
