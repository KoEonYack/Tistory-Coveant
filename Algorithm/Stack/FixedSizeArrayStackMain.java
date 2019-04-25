class FixedSizeArrayStack{
  protected int capacity;
  public static final int CAPACITY = 10;
  protected int[] stackRep;
  protected int top = -1;

  public FixedSizeArrayStack(){
    this(CAPACITY);
  }

  public FixedSizeArrayStack(int cap){
    capacity = cap;
    stackRep = new int[capacity];
  }
  public int size(){
    return (top + 1);
  }

  public boolean isEmpty(){
    return (top < 0);
  }

  public void push(int data) {
    if (size() == capacity){
      System.out.println("Stack is full");
    }
    stackRep[++top] = data;
  }

  public int top(){
    if(isEmpty()){
      System.out.println("Stack is empty");
    }
    return stackRep[top];
  }

  public int pop() {
    int data;
    if(isEmpty()){
      System.out.println("Stack is empty");
    }
    data = stackRep[top];
    stackRep[top--] = Integer.MIN_VALUE;
    return data;
  }

  public String toString(){
    String s;
    s = "[";
    if(size()> 0){
      s += stackRep[0];
    }

    if(size() > 1){
      for(int i=1; i<=size()-1; i++){
        s+= ", " + stackRep[i];
      }
    }

    return s + "]";
  }

}

class FixedSizeArrayStackMain{

  public static void main(String [] args){
    FixedSizeArrayStack st = new FixedSizeArrayStack(10);
    System.out.println(st.size());
    st.push(10);
    st.push(20);
    System.out.println(st.pop());
  }

}
