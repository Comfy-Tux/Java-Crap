import java.util.stream.*;

public class QuickMergeSort {
   private static Comparable[] aux;

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
      System.out.println(isSorted(randomArray));
   }

   public static void sort(Comparable[] a){
      aux = new Comparable[a.length];
      System.arraycopy(a, 0, aux, 0 , a.length);
      sort(a,0,a.length -1);
      assert(isSorted(a)) ;
   }

   private static void sort(Comparable[] a , int lo, int hi){
      if(hi <= lo) return;
      if(hi - lo <= 20) {
         InsertionSort.sort(a, lo, hi);
         return;
      }
      int mid = lo + (hi - lo)/2;
      sort(a,lo,mid); // a = aux ; aux = a
      Comparable[] temp = aux;
      aux = a;
      a = aux;
      sort(a,mid+1,hi); // a = a ; aux = a;
      if(less(a[mid],a[mid +1]))
         return;
      merge(a,lo,mid,hi);
   }

   @SuppressWarnings("unchecked")
   private static boolean less(Comparable i,Comparable j){
      return i.compareTo(j) < 0;
   }

   private static boolean isSorted(Comparable[] a){
      for(int i = 1; i < a.length ; i++)
         if(less(a[i],a[i-1])) return false;
      return true;
   }

   private static void merge(Comparable[] array,int lo,int mid,int hi){
      //merge array a[lo..mid] with a[mid+1..hi].
      int right = lo;
      int left = mid + 1;

      for(int i = lo; i <= hi ; i++){  //merge back to a[lo..hi].
         if(right > mid)                      array[i] = aux[left++];
         else if(left > hi)                   array[i] = aux[right++];
         else if(less(aux[left],aux[right]))  array[i] = aux[left++];
         else                                 array[i] = aux[right++];
      }
   }
   private static class InsertionSort {
      private static boolean less(Comparable x, Comparable y) {
         return x.compareTo(y) < 0;
      }

      private static void exch(Comparable[] a, int x, int y) {
         Comparable temp = a[x];
         a[x] = a[y];
         a[y] = temp;
      }

      public static void sort(Comparable[] a , int start , int end) {
         int N = end;
         for(int i = start + 1; i <= N; i++) {
            for(int j = i; j > start && less(a[j], a[j - 1]); j--)
               exch(a, j, j - 1);
         }
      }
   }
}
