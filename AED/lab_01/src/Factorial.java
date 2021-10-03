import java.util.*;

public class Factorial {
   public static void main(String[] argv){
      var in = new Scanner(System.in);

      System.out.print("Insert a number : ");
      while(in.hasNextInt()){
         int x = in.nextInt();
         System.out.println("The Factorial of " + x + " is " + iterativeFactorial(x));
         System.out.print("Insert a number : ");
      }

   }

   public static int  recursiveFactorial(int x){
      return (x == 0 ) ? 1 : recursiveFactorial(x-1)*x;
   }

   public static int iterativeFactorial(int x){
      int result = 1;

      while(x != 0){
         result *= x;
         x--;
      }

      return result;
   }
}
