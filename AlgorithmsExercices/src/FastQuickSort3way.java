import java.util.*;
import java.util.stream.*;

public class FastQuickSort3way {
   public static void main(String[] args) {
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
      for(int i = 0; i < N; i++) {
         randomArray[i] = Math.random();
      }

      System.out.print("[");
      Stream.of(randomArray).forEach(x -> System.out.printf("%.5f,", x));
      System.out.println("]");
      sort(randomArray);
      System.out.print("[");
      Stream.of(randomArray).forEach(x -> System.out.printf("%.5f,", x));
      System.out.println("]");
      System.out.println(isSorted(randomArray));
   }

   public static void sort(Comparable[] a) {
      shuffle(a);
      sort(a, 0, a.length - 1);
   }

/*

   private static void sort(Comparable[] a, int lo, int hi) {
      if(hi - lo < 15) {
         InsertionSort.sort(a,lo,hi);
         return;
      }

      int i = lo + 1;
      int j = hi;
      int p = lo + 1;
      int q = hi;

      swap(a,lo,getPivotIndex(a,lo,hi));

      Comparable key = a[lo];
      while(true) {

         while(i <= q) {
            int cmp = a[i].compareTo(key);
            if(cmp > 0)
               break;
            if(cmp == 0)
               swap(a, i, p++);
            i++;
         }

         while(j >= p) {
            int cmp = a[j].compareTo(key);
            if(cmp < 0)
               break;
            if(cmp == 0)
               swap(a, j, q--);
            j--;
         }

         if(i >= j)
            break;
         swap(a, i, j);
      }

      while(p > lo)
         swap(a, --p, --i);
      while(q < hi)
         swap(a, ++q, ++j);

      sort(a, lo, i - 1);
      sort(a, j + 1, hi);
   }
*/
//stolen from github solutions
   @SuppressWarnings("unchecked")
   private static void sort(Comparable[] array, int low, int high) {

      if (low >= high) {
         return;
      }

      if (high - low < 15) {
         InsertionSort.sort(array, low, high);
         return;
      }

      int i = low;
      int j = high + 1;

      int p = low;
      int q = high + 1;

      swap(array,low,getPivotIndex(array, low, high));
      Comparable pivot = array[low];

      while (true) {

         if (i > low && array[i].compareTo(pivot) == 0) {
            swap(array, ++p, i);
         }
         if (j <= high && array[j].compareTo(pivot) == 0) {
            swap(array, --q, j);
         }

         while (less(array[++i], pivot)) {
            if (i == high) {
               break;
            }
         }

         while (less(pivot, array[--j])) {
            if (j == low) {
               break;
            }
         }

         //pointers cross
         if (i == j && array[i].compareTo(pivot) == 0) {
            swap(array, ++p, i);
         }
         if (i >= j) {
            break;
         }

         swap(array, i, j);
      }

      //Currently:
      // array[low..p] == pivot
      // array[p..i] < pivot
      // array[j..q] > pivot
      // array[q..high] == pivot

      i = j + 1;

      for(int k = low; k <= p; k++) {
         swap(array, k, j--);
      }
      for(int k = high; k >= q; k--) {
         swap(array, k, i++);
      }

      //Now:
      // array[low..j] < pivot
      // array[j..i] == pivot
      // array[i..high] > pivot

      sort(array, low, j);
      sort(array, i, high);
   }


   //Tukey's ninther
   private static int getPivotIndex(Comparable[] array, int lo, int hi) {
      int numberOfValues = hi - lo + 1;

      int eps = numberOfValues / 9;
      int mid = lo + (hi - lo) / 2;

      int medianIndex1 = getMedianIndex(array, lo, lo + eps, lo + eps + eps);
      int medianIndex2 = getMedianIndex(array, mid - eps, mid, mid + eps);
      int medianIndex3 = getMedianIndex(array, hi - eps - eps, hi - eps, hi);

      return getMedianIndex(array, medianIndex1, medianIndex2, medianIndex3);
   }

   private static int getMedianIndex(Comparable[] array, int index1, int index2, int index3) {
      return less(array[index1], array[index2]) ?
              (less(array[index2], array[index3]) ? index2 :
                      less(array[index1], array[index3])
                      ? index3 : index1) :
              (less(index1, index3) ? index1 :
                      less(index2, index3) ? index3 : index2);
   }


   private static boolean less(Comparable x, Comparable y) {
      return x.compareTo(y) < 0;
   }

   private static void swap(Comparable[] a, int x, int y) {
      Comparable temp = a[x];
      a[x] = a[y];
      a[y] = temp;
   }

   private static boolean isSorted(Comparable[] a) {
      for(int i = 1; i < a.length; i++)
         if(less(a[i], a[i - 1])) return false;
      return true;
   }

   private static void shuffle(Comparable[] a) {
      Random r = new Random();

      for(int i = a.length - 1; i > 0; i--) {
         int j = r.nextInt(i + 1);
         swap(a, i, j);
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
