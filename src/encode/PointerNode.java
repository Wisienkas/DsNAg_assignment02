package encode;

public class PointerNode implements INode{

	public INode leftChild;
	public INode rightChild;
	public int sum;
	public INode parent;
	
	public PointerNode(INode leftChild, INode rightChild){
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		this.sum = leftChild.getSum() + rightChild.getSum();
	}
	
	@Override
	public INode getParent() {
		return this.parent;
	}

	@Override
	public INode getLeftChild() {
		return this.leftChild;
	}

	@Override
	public INode getRightChild() {
		return this.rightChild;
	}

	@Override
	public int getSum() {
		return this.sum;
	}

	@Override
	public Integer getChar() {
		return null;
	}

	@Override
	public void setParent(INode parent) {
		this.parent = parent;
	}

	@Override
	public void setLeftChild(INode leftChild) {
		System.out.println("setting childs not allowed for pointer Node! see Constructor");
	}

	@Override
	public void setRightChild(INode rightChild) {
		System.out.println("setting childs not allowed for pointer Node! see Constructor");
	}
	
}
