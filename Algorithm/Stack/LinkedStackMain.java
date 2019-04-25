class LinkedStack<T> {
  private int length;
  private ListNode top;

  public LinkedStack(){
    length = 0;
    top = null;
  }

  public void push(int data) {
    ListNode temp = new ListNode(data);
    temp.setNext(top);
    top = temp;
    length++;
  }

  public int pop(){
    if(isEmpty()){
      System.out.println("Stack is empty");
    }
    int result = top.getData();
    top = top.getNext();
    length--;
    return result;
  }

  public int peek(){
    if(isEmpty()){
      System.out.println("Stack is empty");
    }

    return top.getData();
  }

  public boolean isEmpty(){
    return (length == 0);
  }

  public int size(){
    return length;
  }

  public String toString(){
    String result = "";
    ListNode current = top;
    while(current!=null){
      result = result + current.toString() + "\n";
      current = current.getNext();
    }
    return result;
  }
}

class LinkedStackMain{

  public static void main(String [] args){
    LinkedStack st = new LinkedStack();
    st.push(10);
    st.push(20);
    System.out.println("Size of Stack: " + st.size());
    System.out.println("Pop : " + st.pop());
  }

}
