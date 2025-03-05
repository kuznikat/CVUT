package cz.cvut.fel.pjv;

public class TreeTest {
    public static void main(String [] args){
        TreeImpl tree = new TreeImpl();
        tree.setTree(new int[] {1,2,3,4,5,6,7,8,9,10});
        tree.setTree(new int[] {1,2,3,4,5,6,7});
        System.out.print(tree.toString());
    }
}
