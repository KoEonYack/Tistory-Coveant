class FixedSizeArrayQueue{
  private int [] queueRep;
  private int size, front, rear;
  private static final int CAPACITY = 16;

  public FixedSizeArrayQueue(){
    queueRep = new int [CAPACITY];
    size = 0; front =0; rear = 0;
  }

  public FixedSizeArrayQueue(int cap){
    queueRep = new int[cap];
    size = 0; front =0; rear = 0;
  }

  public void enQueue(int data){
    if(size == CAPACITY){
      System.out.println("Queue is full: Overflow");
    }
    else{
      size++;
      queueRep[rear] = data;
      rear = (rear + 1) % CAPACITY;
    }
  }

  public int deQueue(){
    if(size == 0){
      System.out.println("Queue is empty: Underflow");
    }else{
      size--;
      int data = queueRep[(front%CAPACITY)];
      return data;
    }
    return 0;
  }

  public boolean isEmpty(){
    return (size == 0);
  }

  public boolean isFull(){
    return (size == CAPACITY);
  }

  public int size(){
    return size;
  }

  public String toString(){
    String result = "[";
    for(int i=0; i<size; i++){
      result += Integer.toString(queueRep[(front+i)%CAPACITY]);
      if(i<size-1){
        result +=",";
      }
    }
    result += "]";
    return result;
  }
}


public class FixedSizeArrayQueueMain {

  public static void main(String [] args){
    FixedSizeArrayQueue queue = new FixedSizeArrayQueue(10);
    queue.enQueue(10);
    queue.enQueue(20);
    queue.enQueue(5);
    queue.enQueue(13);
    System.out.println(queue.toString());
  }

}
