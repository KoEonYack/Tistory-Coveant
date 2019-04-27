public class ListNode  {
  private int data;
  private ListNode next;

  public ListNode(int data){
    this.data = data;
  }

  public void setData(int data){
    this.data = data;
  }

  public int getData(){
    return data;
  }

  public void setNext(ListNode next){
    this.next = next;
  }

  public ListNode getNext(){
    return this.next;
  }

  public int ListLength(ListNode headNode){
    int length = 0;
    ListNode currentNode = headNode;
    while(currentNode != null){
      length++;
      currentNode = currentNode.getNext();
    }
    return lenght;
  }


}
