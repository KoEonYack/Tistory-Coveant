package ds_linkedlist;

public class App {

	public static void main(String[] args) {
		Node nodeA = new Node();
		nodeA.data = 4;
		
		Node nodeB = new Node();
		nodeB.data = 3;

		Node nodeC = new Node();
		nodeB.data = 7;

		Node nodeD = new Node();
		nodeB.data = 8;
		
		nodeA.next = nodeB;
		nodeB.next = nodeC;
		nodeC.next = nodeD;

		System.out.println(listlength(nodeA));
		System.out.println(listlength(nodeB));
	}
	public static int listlength(Node aNode) {
		
		int length = 0;
		
		Node currentNode = aNode;
		
		while(currentNode != null) {
			length++;
			currentNode = currentNode.next;
		}
		
		return length;
	}
	
}
