import java.util.*;
import java.util.stream.*;

public class QuickSort3way {

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

   public static void sort(Comparable[] a){
      shuffle(a);
      sort(0,a.length-1,a);
   }


   private static void sort(int lo,int hi,Comparable[] a){
      if(hi <= lo) return;
      int lt = lo , i = lo+1 , gt = hi;
      Comparable key = a[lo];
      while(i <= gt) {
         int cmp = a[i].compareTo(key);
         if(cmp < 0) swap(a, lt++, i++);
         else if(cmp > 0) swap(a, i, gt--);
         else i++;
      }
      sort(lo,lt-1,a);
      sort(gt+1,hi,a);
   }


   private static boolean less(Comparable x,Comparable y){
      return x.compareTo(y) < 0;
   }

   private static void swap(Comparable[] a,int x,int y){
      Comparable temp = a[x];
      a[x] = a[y];
      a[y] = temp;
   }

   private static boolean isSorted(Comparable[] a){
      for(int i = 1; i < a.length ; i++)
         if(less(a[i],a[i-1])) return false;
      return true;
   }

   private static void shuffle(Comparable[] a){
      Random r = new Random();

      for(int i = a.length -1 ; i > 0; i--){
         int j = r.nextInt(i+1);
         swap(a,i,j);
      }
   }

}
