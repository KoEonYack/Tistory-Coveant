public class Start {
    public static void main(String[] args){
        System.out.println("Hello OOP");
    }
}

public class Start3 {
    public static void main(String[] args){
      int i = 10;
      int k = 20;
      
      if (i == 10){
          int m = k + 5;
          k = m;
      }else{
          int p = k+10;
          k = p;
      }

      // k = m + p;
    }
}


public class Start4 {
    public static void main(String[] args){   
      int k = 5;
      int m;
      
      m = square();
    }
  
  	private static int square(int k) {
      int result;
      k = 25;
      result = k;
      return result;
    }
}


public class Start5 {
    static int share;
    public static void main(String[] args){
        share = 55;
    }
}



public class Car {
    int fuel;
    int velocity;

    void doAccel(){
        System.out.println("달려~");
    }
    void doBreak(){
        System.out.println("멈춰~")
    }
}