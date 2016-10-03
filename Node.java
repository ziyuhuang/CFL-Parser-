/**
 * node class to form the node of a tree
 * @author ziyuhuang
 *
 */
public class Node {
	private String data;
	public Node left;
	public Node right;
	
	public Node(String data){
		this.data = data;
		left = null;
		right = null;
	}
	
	public String getData(){
		return data;
	}
}
