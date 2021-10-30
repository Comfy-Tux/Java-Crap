import java.util.stream.*;

public class InsertionSort {

   public static void main(String[] args){
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
   }
   private static boolean less(Comparable x,Comparable y){
      return  x.compareTo(y) < 0;
   }

   private static boolean isSorted(Comparable[] a){
      for(int i = 1; i < a.length ; i++)
         if(less(a[i],a[i-1])) return false;
      return true;
   }

   private static void exch(Comparable[] a,int x,int y){
      Comparable temp = a[x];
      a[x] = a[y];
      a[y] = temp;
   }

   public static void sort(Comparable[] a){
      int N = a.length;
      for(int i = 1; i < N; i++){
         for(int j = i; j > 0 && less(a[j],a[j-1]); j--)
            exch(a,j,j-1);
      }
   }
}
