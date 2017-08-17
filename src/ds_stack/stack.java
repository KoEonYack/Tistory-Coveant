package ds_stack;

public class stack {
	private int maxSize;
	private char[] stackArray;
	private int top;
	
	public stack(int size) {
		this.maxSize = size;
		this.stackArray = new char[maxSize];
		this.top = -1;
	}

	public void push(char j) {
		if(isfull()) {
			System.out.println("this stack is already full");
		}
		else {
			top++;
			stackArray[top] = j;
		}
	}
	
	public char pop() {
		
		if(isempty()) {
			System.out.println("the stack is already empty");
			return '0';
		}else {
			int old_top = top;
			top--;
			return stackArray[old_top];
		}
	}
	
	public char peak() {
		return stackArray[top];
	}
	
	public boolean isempty() {
		return (top == -1);
	}
	
	public boolean isfull() {
		return (maxSize-1 == top);
	}

}
