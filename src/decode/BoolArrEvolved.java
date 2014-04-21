package decode;

public class BoolArrEvolved {
	
	private int size;
	private int egde;
	private boolean[] array;
	
	public BoolArrEvolved(){
		this.size = 0;
		this.egde = 2;
		this.array = new boolean[this.egde];
	}
	
	public void add(boolean b){
		if(this.size == this.egde){
			evolveArray();
		}
	}

	private void evolveArray() {
		this.egde *= 2;
		boolean[] new_arr = new boolean[this.egde];
		for (int i = 0; i < this.array.length; i++) {
			new_arr[i] = this.array[i];
		}
		this.array = new_arr;
	}
	
	public boolean getBool(int i){
		if(i < size && i >= 0){
			return this.array[i];
		}
		System.out.println("called out of array");
		return false;
	}
	
	public boolean[] getArray(){
		boolean[] result = new boolean[this.size];
		for (int i = 0; i < result.length; i++) {
			result[i] = this.array[i];
		}
		return result;
	}
	
}
