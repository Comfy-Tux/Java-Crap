import java.util.stream.*;

public class ShellSort {

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
      return x.compareTo(y) < 0;
   }

   private static void exch(Comparable[] a,int x,int y){
      Comparable temp = a[x];
      a[x] = a[y];
      a[y] = temp;
   }

   private static boolean isSorted(Comparable[] a){
      for(int i = 1; i < a.length ; i++)
         if(less(a[i],a[i-1])) return false;
      return true;
   }

   public static void sort(Comparable[] a){
      int N = a.length;
      int h = 1;
      while(h < N/3)
         h = 3*h +1;

      while(h > 0){
         //h sort the array.
         for(int i = h; i < N ; i++){
            //Insert a[i] among a[i-h], a[i-2*h] , a[i-3*h] , ... .
            for(int j = i; j >= h && less(a[j],a[j-h]) ;j -= h)
               exch(a,j,j-h);
         }
         h /= 3;
      }
      assert(isSorted(a));
   }
}
