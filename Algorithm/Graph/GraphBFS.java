import java.io.*;
import java.util.*;
//import java.util.ArrayList;
//import java.util.Queue;

class Vertex {
  public char label;
  public boolean visited;
  public boolean wasVisited;
  public Vertex(char lab){
    label = lab;
    visited = false;
    wasVisited = false;
  }
}

class GraphAdjList {
  private final int maxVertices = 20;
  private Vertex vertexList[];
  private int adjMatrix[][];
  private int vertexCount;
  private Queue<Integer> theQueue;

  public GraphAdjList(int vertexCount){
    vertexList = new Vertex[maxVertices];
    adjMatrix = new int[maxVertices][maxVertices];
    vertexCount = 0;
    for(int y=0; y<maxVertices; y++){
      for(int x=0; x<maxVertices; x++){
        adjMatrix[x][y] = 0;
      }
    }
    theQueue = new LinkedList<Integer>();
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

  public void bfs(){
    vertexList[0].wasVisited = true;
    displayVertex(0);
    theQueue.add(0);
    int v2;
    while(!theQueue.isEmpty()){
      System.out.println("here");
      int v1 = theQueue.poll(); // remove
      while( (v2=getAdjUnvisitedVertex(v1)) != -1 ){
        vertexList[v2].wasVisited = true;
        displayVertex(v2);
        theQueue.add(v2);
      }
    }
    for(int j=0; j<vertexCount;j++){
      vertexList[j].wasVisited = false;
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

// 448p
public class GraphBFS {
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

    System.out.println("[BFS]");
    g.bfs();

  }
}
