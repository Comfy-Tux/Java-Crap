import java.util.stream.*;

public class NaturalMergeSort {
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

   public static void sort(Comparable[] array) {
      if (array == null || array.length == 1) {
         return;
      }
      aux = new Comparable[array.length];

      int low = 0;
      int middle;
      int high;

      while (true) {
         middle = findSortedSubArray(array, low);
         if (middle == array.length - 1) {
            if (low == 0) // Array is sorted
               break;
            else {
               low = 0;
               continue;
            }
         }
         high = findSortedSubArray(array, middle + 1);
         merge(array,low, middle, high);
         low = (high == array.length - 1) ? 0 : high + 1;
      }
   }

   private static int findSortedSubArray(Comparable[] array, int start) {
      for(int i = start + 1; i < array.length; i++) {
         if (array[i].compareTo(array[i - 1]) < 0) {
            return i - 1;
         }
      }
      return array.length - 1;
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
