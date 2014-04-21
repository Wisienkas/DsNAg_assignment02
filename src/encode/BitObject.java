package encode;

public class BitObject {

	private int modifier = 1;
	private int number = 0;
	
	public void add(boolean b) {
		if(b){
			number += modifier;
		}
		modifier *= 2;
	}
	
	public int getNumber(){
		return this.number;
	}
}
