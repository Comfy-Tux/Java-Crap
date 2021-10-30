import java.util.stream.*;


public class BottomUpMergeSort {
   private static Comparable[] aux;

   public static void main(String[] args){
      int N = 3;
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

   public static void sort(Comparable[] a){
      int N = a.length;
      aux = new Comparable[N];

      for(int i = 1; i < a.length ; i += i){
         int M = N - i;
         for(int j = 0; j < M ; j += i + i){
            merge(a,j,j + i -1,Math.min(j + i + i -1, N-1));
         }
      }

      assert(isSorted(a));
   }

   @SuppressWarnings("unchecked")
   private static boolean less(Comparable i,Comparable j){
      return i.compareTo(j) < 0;
   }

   private static void merge(Comparable[] array,int lo,int mid,int hi){
      //merge array a[lo..mid] with a[mid+1..hi].
      int right = lo;
      int left = mid + 1;

      System.arraycopy(array, lo, aux, lo, hi + 1 - lo);

      for(int i = lo; i <= hi ; i++){  //merge back to a[lo..hi].
         if(right > mid)                      array[i] = aux[left++];
         else if(left > hi)                   array[i] = aux[right++];
         else if(less(aux[left],aux[right]))  array[i] = aux[left++];
         else                                 array[i] = aux[right++];
      }
   }

   private static boolean isSorted(Comparable[] a){
      for(int i = 1; i < a.length ; i++)
         if(less(a[i],a[i-1])) return false;
      return true;
   }
}
