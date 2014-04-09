package encode;

public interface INode {
	
	INode getParent();
	INode getLeftChild();
	INode getRightChild();
	void setParent(INode parent);
	void setLeftChild(INode leftChild);
	void setRightChild(INode rightChild);
	int getSum();
	Character getChar();
	
}
