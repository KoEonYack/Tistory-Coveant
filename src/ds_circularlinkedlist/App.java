package ds_circularlinkedlist;

import ds_singlylinkedlist.SinglyLinkedList;

public class App {

	public static void main(String[] args) {
		SinglyLinkedList mylist = new SinglyLinkedList();
		mylist.insertFirst(100);
		mylist.insertFirst(50);
		mylist.insertFirst(99);
		mylist.insertFirst(88);
		mylist.insertLast(99999999);
		
		mylist.displayList();
	}

}
