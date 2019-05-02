import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Main{

  public static void main(String [] args) throws Exception {
    int i, j, k;
    int index;

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    int inputNum = Integer.parseInt(br.readLine());
    int [] inputArr = new int[10001];

    for(i=0; i<inputNum; i++){
      inputArr[Integer.parseInt(br.readLine())]++;
    }

    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    for(j=0; j<10001; j++){
      if(inputArr[j] > 0){
        for(k=0; k<inputArr[j]; k++){
          bw.write(Integer.toString(j) + "\n");
        }
      }
    }

    br.close();
    bw.close();
  }

}
