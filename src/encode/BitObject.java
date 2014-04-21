package encode;

public class BitObject {

	private int modifier = 1;
	private int number = 0;
	
	public BitObject(){
		this.modifier = 1;
		this.number = 0;
	}
	
	public BitObject(int number){
		this.modifier = 128;
		this.number = number;
	}
	
	public void add(boolean b) {
		if(b){
			number += modifier;
		}
		modifier *= 2;
	}
	
	public boolean decrease(){
		if(number / modifier > 0){
			number -= (number / modifier);
			modifier /= 2;
			return true;
		}
		modifier /= 2;
		return false;
	}
	
	public int getNumber(){
		return this.number;
	}
}
