import java.util.Scanner;
import java.util.Arrays;

class Main {

    public static void main (String [] args){
        
        Scanner sc = new Scanner(System.in);
        int SIZE = sc.nextInt();

        int[] input = new int[SIZE];
        int max = 0;

        for(int i=0; i<input.length; i++){
            input[i] = sc.nextInt();
        }

        Arrays.sort(input);

        System.out.println(input[input.length-1] + input[input.length-2]);

    } 
}


