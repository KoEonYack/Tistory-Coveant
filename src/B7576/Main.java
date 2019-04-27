import java.util.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Queue;

class POINT{
  int x;
  int y;
  POINT(int x, int y){
    this.x = x;
    this.y = y;
  }
}

public class Main{

  public static Queue<POINT> q = new LinkedList<POINT>();
  static int[] dx = {-1, 0, 1, 0};
  static int[] dy = {0, 1, 0, -1};

  public static void print(int[][] arr, int N, int M) {
      for (int i = 0; i < N; i++) {
          for (int j = 0; j < M; j++) {
              System.out.print(arr[i][j] + " ");
          }
          System.out.println();
      }
  }

  public static void BFS(int [][] arr, int M, int N){
    int ans= 0;
    while(!q.isEmpty()){
      POINT p = q.poll(); // return and remove

      for(int i=0; i<4; i++){
        int nx = p.x + dx[i];
        int ny = p.y + dy[i];
        System.out.println("x:" + p.x + " y:"+p.y + " nx:"+nx + " ny:" + ny);
        if(nx < 0 || nx >= M || ny < 0 || ny >= N) continue;
        if( arr[nx][ny] > 0 ) continue;
        arr[nx][ny] = arr[nx][ny] + 1;
        q.add(new POINT(nx, ny));
      }
    }

    print(arr, M, N);

    for(int i=0; i<M; i++){
      for(int j=0; j<N; j++){
        if(arr[i][j] == 0){
          System.out.println("-1");
          System.exit(0);
        }
        if( ans < arr[i][j]) ans = arr[i][j];
      }
    }
    System.out.println(ans-1);


  }

  public static void main(String [] args){
    Scanner sc = new Scanner(System.in);

    int M = sc.nextInt();
    int N = sc.nextInt();
    int[][] inputArr = new int[M][N];
    int inputNum;
    int ans = 0;

    for(int i=0; i<M; i++){
      for(int j=0; j<N; j++){
        inputArr[i][j] = sc.nextInt();
        if(inputArr[i][j] == 1){
          q.add(new POINT(i, j));
        }
      }
    }

    BFS(inputArr, M, N);

  }
}


// Ref
// https://zoonvivor.tistory.com/131
// https://rebas.kr/650
