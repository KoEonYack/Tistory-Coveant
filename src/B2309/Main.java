import java.util.Scanner;
import java.util.Arrays;

class Main{
    public static void main(String[] args){

        int MAX = 987654321;

        int sum_height = 0;

        Scanner sc = new Scanner(System.in);
        int [] input = new int[9];
        for (int i=0; i<9; i++){
            input[i] = sc.nextInt();
            sum_height = sum_height + input[i];
        }
        
        for(int j=0; j <= 8; j++){
            for(int k=j+1; k<=8; k++){
                if( sum_height - input[j] - input[k] == 100){
                    input[j] = MAX;
                    input[k] = MAX;
                }
            }
        }

        Arrays.sort(input);
        for(int i=0; i<7 ;i++){
            System.out.println(input[i]);
        }

    }
}
