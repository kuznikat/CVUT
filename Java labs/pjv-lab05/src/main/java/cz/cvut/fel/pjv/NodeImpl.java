package cz.cvut.fel.pjv;

public class NodeImpl implements Node {
    private int value;
    private Node rightChild;
    private Node leftChild;
    public NodeImpl(int value) {
        this.value = value;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public Node getLeft(){
        if (this.leftChild != null){
            return leftChild;
        }else{
            return null;
        }
    }
    public Node getRight(){
        if (this.rightChild != null){
            return rightChild;
        }else{
            return null;
        }
    }
    public int getValue(){
        return value;
    }
}
