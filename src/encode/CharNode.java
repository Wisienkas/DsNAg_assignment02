package encode;

public class CharNode implements INode {

	public INode parent;
	public char c;
	public int sum;

	public CharNode(char ch, int sum){
		this.c = ch;
		this.sum = sum;
	}
	
	@Override
	public INode getParent() {
		return parent;
	}

	@Override
	public INode getLeftChild() {
		return null;
	}

	@Override
	public INode getRightChild() {
		return null;
	}

	@Override
	public int getSum() {
		return sum;
	}

	@Override
	public Character getChar() {
		return c;
	}

	@Override
	public void setParent(INode parent) {
		this.parent = parent;
	}

	@Override
	public void setLeftChild(INode leftChild) {
		System.out.println("Setting childs on a charNode not allowed!");
	}

	@Override
	public void setRightChild(INode rightChild) {
		System.out.println("Setting childs on a charNode not allowed!");
	}
}
