package encode;

public class Node {
	
	private char character;
	private int occounrence;
	
	public Node(char ch){
		this.character = ch;
		occounrence = 1;
	}
	
	public boolean isChar(char ch){
		if(ch == this.character){
			occounrence++;
			return true;
		}
		return false;
	}
	
	public int getOccounrence(){
		return this.occounrence;
	}
}
