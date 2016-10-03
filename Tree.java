import java.util.*;

/**
 * class will take a String list as input and parse them 
 * into a parse tree. 
 * @author ziyuhuang
 *
 */
public class Tree {
	
	Stack<Tree> stack = new Stack<Tree>(); //use to compute and store the syntax tree
	
	ArrayList<String> opArray = new ArrayList<String>(); //array to store operator symbols
	
	private Node root;
	
	public Tree(){
		root = null;
	}
	
	public Node getRoot(){
		return root;
	}
	
	public void add(String s){
		Node newNode = new Node(s);
		root = newNode;
	}
	
	public void addToLeft(Node leftTree){
		root.left = leftTree;
	}
	
	public void addToRight(Node rightTree){
		root.right = rightTree;
	}
	
	/**
	 * construct a syntax tree using a stack
	 * @param s string array to parse
	 */
	public void constructTree(String[] s){
		opArray.add("+");
		opArray.add("-");
		opArray.add("*");
		opArray.add("/");
		opArray.add("^");
		
		for(String x: s){
			if(!opArray.contains(x)){
				Tree t = new Tree();
				t.add(x);
				stack.push(t);
			}
			else{
				Node rightChild = stack.pop().getRoot();
				Node leftChild = stack.pop().getRoot();
				Tree t = new Tree();
				t.add(x);
				t.addToLeft(leftChild);
				t.addToRight(rightChild);
				stack.push(t);
			}
		}
	}
	
	/**
	 * in order traversal print out the syntax tree
	 */
	public void inorderTraversal(){
		Tree tree = stack.peek();
		Node aroot = tree.getRoot();
		
		inorderTraversal(aroot);
	}
	
	/**
	 * helper method for in order traversal 
	 * @param root
	 */
	public void inorderTraversal(Node root){
		if(root == null) return;
		else{
			inorderTraversal(root.left);
			System.out.print(root.getData() + " ");
			inorderTraversal(root.right);
		}
	}
}
