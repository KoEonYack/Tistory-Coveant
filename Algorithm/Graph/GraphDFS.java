import java.io.*;
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
// ㅁ
class GraphAdjList {
  private final int maxVertices = 20;
  private Vertex vertexList[];
  private int adjMatrix[][];
  private int vertexCount;
  private Stack<Integer> theStack;

  public GraphAdjList(int vertexCount){
    vertexList = new Vertex[maxVertices];
    adjMatrix = new int[maxVertices][maxVertices];
    vertexCount = 0;
    for(int y=0; y<maxVertices; y++){
      for(int x=0; x<maxVertices; x++){
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
    System.out.println(vertexList[v].label);
  }

  public void dfs(){
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

// 441p

public class GraphDFS {
  public static void main(String [] args) {
    // Generate graph
    GraphAdjList g = new GraphAdjList(6);

    // Add Vertex
    g.addVertex('1');
    g.addVertex('2');
    g.addVertex('3');
    g.addVertex('4');
    g.addVertex('5');
    g.addVertex('6');

    // Add Edge
    g.addEdge( 0 , 4 );
    g.addEdge( 0 , 5 );
    g.addEdge( 1 , 2 );
    g.addEdge( 1 , 3 );
    g.addEdge( 1 , 5 );
    g.addEdge( 2 , 3 );
    g.addEdge( 2 , 4 );
    g.addEdge( 4 , 5 );

    System.out.println("[DFS]");
    g.dfs();

  }
}
