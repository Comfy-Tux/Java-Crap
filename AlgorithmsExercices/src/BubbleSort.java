import java.util.stream.*;
import java.util.*;

public class BubbleSort extends Sort{
   public static void main(String[] args){
      int M = 10;
      var array = new Integer[M];

      for(int i = 0; i < M; i++)
         array[i] = i;

      shuffle(array);
      System.out.println(Arrays.toString(array));

      sort(array);
      System.out.println(Arrays.toString(array));

      int N = 100;
      var randomArray = new Double[N];
      for(int i = 0; i < N; i++){
         randomArray[i] = Math.random();
      }

      System.out.print("[");
      Stream.of(randomArray).forEach(x -> System.out.printf("%.5f,",x));
      System.out.println("]");
      sort(randomArray);
      System.out.print("[");
      Stream.of(randomArray).forEach(x -> System.out.printf("%.5f,",x));
      System.out.println("]");
      System.out.println(isSorted(randomArray));
   }

   public static <T extends Comparable<T>> void sort(T[] a){
       boolean isSorted = false;
      while(!isSorted){
         int N = a.length;
         isSorted = true;
         for(int i = 1; i < N;i++){
            if(less(a[i],a[i-1])){
               exchange(a,i-1,i);
               isSorted = false;
            }
         }
      }
   }
}
