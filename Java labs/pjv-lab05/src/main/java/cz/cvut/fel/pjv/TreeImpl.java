package cz.cvut.fel.pjv;

import java.util.Arrays;

public class TreeImpl implements Tree{
    private Node root;
    public TreeImpl() {
        this.root = null;
    }

    public void setTree(int[] values){
        root = buildTree(values);
    }


    private Node buildTree(int[] values){
        if(values.length == 0) return null;

        int middleIndex = values.length / 2;
        NodeImpl node = new NodeImpl(values[middleIndex]);
        node.setLeftChild(buildTree(Arrays.copyOfRange(values,0,middleIndex)));
        node.setRightChild(buildTree(Arrays.copyOfRange(values,middleIndex + 1,values.length)));

        return node;
    }

    public Node getRoot(){
        if(root != null){
            return root;
        }
        return null;
    }

    public String toString() {
        if (root == null) {
            return "";
        }
        StringBuilder strBuilder = new StringBuilder();
        printTree(strBuilder, root, 0);
        return strBuilder.toString();

    }

    private void  printTree(StringBuilder strBuilder,Node node, int depthCount ){
        if(node == null){
            return;
        }
        for (int i = 0; i < depthCount; i++) {
            strBuilder.append(" ");
        }
        strBuilder.append("- ");
        strBuilder.append(node.getValue()).append("\n");
        printTree(strBuilder, node.getLeft(), depthCount + 1);
        printTree(strBuilder, node.getRight(),depthCount + 1);
    }
}
