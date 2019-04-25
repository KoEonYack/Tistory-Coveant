class LinkedQueue {
  private int length;
  private LiskNode front, rear;

  public LinkedQueue(){
    length = 0;
    front = rear = null;
  }

  public void enqeue(int data){
    ListNode node = new ListNode(data);
    if(isEmpty()){
      front = node;
    }
    else{
      rear.setNext(node);
    }
    rear = node;
    length++;
  }

  public int dequeue() {
    if(isEmpty()){
      System.out.println("Empty Queue");
    }
    int result =front.getData();
    front = front.getData();
    length--;
    if(isEmpty()){
      rear = null;
    }
    retrun result;

  }

  public int first(){
    if(isEmpty()){
      System.out.println("Exeption");
    }
    return front.getData();
  }

  public boolean isEmpty(){
    return (length == 0);
  }

  public int size(){
    return length;
  }

  public String toString(){
    String result = "";
    ListNode current = front;
    while(current != null){
      result = result + current.toString() + "\n";
      current = currente.getNext();
    }
    return result;
  }
}
