import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Stack;

class Vertex {
  public char label;
  public boolean visited;
  public Vertex(char lab){
    label = lab;
    visited = false;
  }
}

class GraphAdjList {
  private final int maxVertices = 20;
  private Vertex vertexList[];
  private int adjMatrix[][];
  private int vertexCount;
  private Stack<Integer> theStack;
  public static int count = -1;

  public GraphAdjList(int vertexCount){
    vertexList = new Vertex[vertexCount];
    adjMatrix = new int[vertexCount][vertexCount];
    for(int y=0; y<vertexCount; y++){
      for(int x=0; x<vertexCount; x++){
        adjMatrix[x][y] = 0;
      }
    }
    theStack = new Stack<Integer>();
  }

  public void addVertex(char lab){
    vertexList[vertexCount++] = new Vertex(lab);
  }
  public void addEdge(int start, int end){
    adjMatrix[start][end] = 1;
    adjMatrix[end][start] = 1;
  }

  public void displayVertex(int v){
    //System.out.println(vertexList[v].label);
    count++;
  }

  public int dfs(){
    vertexList[0].visited = true;

    displayVertex(0);
    theStack.push(0);

    while(!theStack.isEmpty()){
      int v = getAdjUnvisitedVertex((int)theStack.peek());
      if(v==-1){
        theStack.pop();
      }else{
        vertexList[v].visited = true;
        displayVertex(v);
        theStack.push(v);
      }
    }
    for(int j=0; j<vertexCount;j++){
      vertexList[j].visited = false;
    }

    return count;
  }

  public int getAdjUnvisitedVertex(int v){
    for(int j=0; j<vertexCount; j++){
      if(adjMatrix[v][j] ==1 && vertexList[j].visited == false){
        return j;
      }
    }
    return -1;
  }

}

public class Main {
  public static void main(String [] args) {
    Scanner sc = new Scanner(System.in);
    int numberOfNode = sc.nextInt();
    int numberOfEdge = sc.nextInt();

    GraphAdjList g = new GraphAdjList(numberOfNode);

    for(int i=0; i<numberOfNode; i++){
      g.addVertex((char)i);
    }


    for(int i=0; i<numberOfEdge; i++){
      int u = sc.nextInt();
      int v = sc.nextInt();
      g.addEdge(u-1, v-1);
    }

    System.out.print(g.dfs());

  }
}
