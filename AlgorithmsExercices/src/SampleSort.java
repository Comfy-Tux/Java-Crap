//it's basically a generalization of QuickSort , the size of the sample defines the amount of elements
// to choose the medium from

//BTW AFTER REVISING THE CODE I DON'T THINK THIS SAMPLESORT MEETS THE REQUIREMENTS , THERE'S A PART MISSING ,
//THIS CODE IS FROM GITHUB ALGORITHMS SEDGEWICK SOLUTIONS ,BUT I SUSPECT IT IS WRONG.

import java.util.*;
import java.util.stream.*;

public class SampleSort {

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
      sort(0,a.length-1,a,(int)Math.pow(2.0,2.0) -1);
   }


   private static void sort(int lo,int hi,Comparable[] a,int sampleSize){
      if(lo >= hi)
         return;

      int pivot = partitioning(lo,hi,a,sampleSize);
      sort(lo,pivot,a,sampleSize);
      sort(pivot+1,hi,a,sampleSize);
   }

   //median of 3
   private static int partitioning(int lo,int hi,Comparable[] a,int sampleSize){
      int i = lo;
      int j = hi +1;

      if(sampleSize <= hi - lo +1){
         InsertionSort.sort(a,lo,lo + sampleSize -1);
         int mid = lo + sampleSize/2;
         swap(a,lo,mid);
      }

      Comparable key = a[lo];
      while(true){
         while(less(a[++i],key)) if(i == hi) break;
         while(less(key,a[--j])) if(j == lo) break;
         if(i >= j)
            break;
         swap(a,i,j);
      }
      swap(a,lo,j);
      return j;
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
