package aed.collections;

import java.util.*;

public class MinPriorityQueue<T extends Comparable<T>> {
   private UnrolledLinkedList<T> list;

   public static void main(String[] args) {
      final int LENGTH = 10000;
      final int nTRIALS = 50;
      int M = 41;

      var array = new Integer[M];

      for(int i = 0; i < M; i++)
         array[i] = i;

      shuffle(array);
      System.out.println("unordered");
      System.out.println(Arrays.toString(array));

      var pq = new MinPriorityQueue<Integer>();

      for(Integer i : array)
         pq.insert(i);

      System.out.println("ordered");
      System.out.println(Arrays.toString(pq.getElements()));
      var pqCopy = pq.shallowCopy();

      for(int i = 0; i < M; i++) {
         pq.removeMin();
         pq.removeMin();
      }
      System.out.println("Is empty " + pq.isEmpty());

      System.out.println("original");
      System.out.println(Arrays.toString(pq.getElements()));
      System.out.println("copy");
      System.out.println(Arrays.toString(pqCopy.getElements()));

      System.out.println(pqCopy.peekMin());
      System.out.println(pqCopy.removeMin());
      System.out.println(pqCopy.isEmpty());
      System.out.println(pqCopy.size());

      for(Integer i : array)
         pq.insert(i);
      System.out.println("original after inserting again");
      System.out.println(Arrays.toString(pq.getElements()));

      ///// difference in time of the insert and removeMin between an increasing array and a decreasing array implementation.

      //0.44
      //0.022

      //0.4
      //0.004

      //amortized O(N)
      var insertTime = DoublingRatio.doublingRatioInsert(nTRIALS, LENGTH, 512);
      System.out.println("An InsertTime is O(" + bigO(insertTime.ratio) + ") worst case , real ratio = " + insertTime.ratio +
              " and it took " + insertTime.time + " seconds");

      //amortized O(1)
      var removeMinTime = DoublingRatio.doublingRatioRemoveMin(nTRIALS, LENGTH, 512);
      System.out.println("An removeMinTime is O(" + bigO(removeMinTime.ratio) + ") worst case , real ratio = " + removeMinTime.ratio +
              " and it took " + removeMinTime.time + " seconds");


        /*
        We need to make a compromise between insert speed and remove , and the best balance was on 512 blocksize.
         */


      for(int i = 64; i < 10000; i += i) {
         System.out.println();
         System.out.println("BLOCKSIZE= " + i);

         //O(N)
         var insertTime2 = DoublingRatio.doublingRatioInsert(nTRIALS, LENGTH,i);
         System.out.println("An InsertTime is O(" + bigO(insertTime2.ratio) + ") worst case , real ratio = " + insertTime2.ratio +
                 " and it took " + insertTime2.time + " seconds");

         //O(1)
         var removeMinTime2 = DoublingRatio.doublingRatioRemoveMin(nTRIALS, LENGTH,i);
         System.out.println("An removeMinTime is O(" + bigO(removeMinTime2.ratio) + ") worst case , real ratio = " + removeMinTime2.ratio +
                 " and it took " + removeMinTime2.time + " seconds");
      }




   }


   public MinPriorityQueue() {
      list = new UnrolledLinkedList<>(512);
   }

   public MinPriorityQueue(int blockSize) {
      list = new UnrolledLinkedList<>(blockSize);
   }


   public void insert(T element) {
      list.addAt( binarySearch(element), element);
   }

   public T peekMin() {
      if(list.isEmpty())
         return null;
      return list.get(0);
   }

   public T removeMin() {
      return list.remove(0);
   }

   public boolean isEmpty() {
      return list.isEmpty();
   }

   public int size() {
      return list.size();
   }

   public MinPriorityQueue<T> shallowCopy() {
      var copy = new MinPriorityQueue<T>();
      for(T t: list)
         copy.list.add(t);
      return copy;
   }

   //needed for testing purposes only
   public Object[] getElements() {
      @SuppressWarnings("unchecked")
      T[] a = (T[]) new Comparable[size()];//
      int i = 0;
      for(T element : list)
         a[i++] = element;
      return a;
   }

   /////////////////////////////////////////////// helper methods ///////////////////////////////////////////

   //this isn't really binarySearch as there can be no elements on the list , and it will return the placement where
   // the key should be , if it finds an elements then returns the index of that element.
   private int binarySearch(T key) {
      int lo = 0;
      int hi = size() - 1;

      while(lo <= hi){
         int mid = lo + (hi - lo) / 2;
         var midKey = list.get(mid);
         if(less(midKey, key))
            lo = mid +1;
         else if(less(key, midKey))
            hi = mid -1;
         else
            return mid;
      }
      return lo ;
   }


   private static <T extends Comparable<T>> boolean less(T v, T w) {
      return v.compareTo(w) < 0;
   }


   private static <T extends Comparable<T>> void shuffle(T[] a) {
      Random r = new Random();

      for(int i = a.length - 1; i > 0; i--) {
         int j = r.nextInt(i + 1);
         exchange(a, i, j);
      }
   }


   protected static <T extends Comparable<T>> void exchange(T[] a, int i, int j) {
      T t = a[i];
      a[i] = a[j];
      a[j] = t;
   }

   //useful method to test if an array is sorted
   public static <T extends Comparable<T>> boolean isSorted(T[] a) {
      for(int i = 1; i < a.length; i++) {
         if(less(a[i], a[i - 1])) return false;
      }
      return true;
   }

   /////////////////////////////////////// DOUBLING RATIO /////////////////////////////////////////////////

   private static class Stopwatch {
      private final long start;

      public Stopwatch() {
         start = System.currentTimeMillis();
      }

      public double elapsedTime() {
         long now = System.currentTimeMillis();
         return (now - start) / 1000.0;
      }
   }

   private static class DoublingRatio {
      public static double timeTrialInsert(MinPriorityQueue<Integer> a, int N, int M) {
         var r = new Random();
         var stopwatch = new Stopwatch();
         for(int i = 0; i < M; i++)
            a.insert(r.nextInt(N));
         return stopwatch.elapsedTime();
      }

      public static double timeTrialRemoveMin(MinPriorityQueue<Integer> a, int N, int M) {
         var r = new Random();
         var stopwatch = new Stopwatch();
         for(int i = 0; i < M; i++)
            a.removeMin();
         return stopwatch.elapsedTime();
      }

      // N is number of times , M is length of the array
      public static TimeAndRatio doublingRatioInsert(int N, int M, int blockSize) {
         int nTrials = 2000;
         double currentTime = 0.0;
         double halfTime = 0.0;

         for(int i = 0; i < N; i++) {
            var queue = new MinPriorityQueue<Integer>(blockSize);
            var halfQueue = new MinPriorityQueue<Integer>(blockSize);
            for(int j = M - 1; j >= 0; j--)
               queue.insert(j);
            for(int j = M / 2 - 1; j >= 0; j--)
               halfQueue.insert(j);

            halfTime += timeTrialInsert(halfQueue, M / 2, nTrials);
            currentTime += timeTrialInsert(queue, M, nTrials);
         }
         return new TimeAndRatio(halfTime + currentTime, currentTime / halfTime);
      }


      // N is number of times , M is length of the array
      public static TimeAndRatio doublingRatioRemoveMin(int N, int M, int blockSize) {
         int nTrials = 2000;
         double currentTime = 0.0;
         double halfTime = 0.0;
         for(int i = 0; i < N; i++) {
            var queue = new MinPriorityQueue<Integer>(blockSize);
            var halfQueue = new MinPriorityQueue<Integer>(blockSize);
            for(int j = 0; j < M; j++)
               queue.insert(j);
            for(int j = 0; j < M / 2; j++)
               halfQueue.insert(j);

            halfTime += timeTrialRemoveMin(halfQueue, M / 2, nTrials);
            currentTime += timeTrialRemoveMin(queue, M, nTrials);
         }
         return new TimeAndRatio(halfTime + currentTime, currentTime / halfTime);
      }
   }

   private static void shuffle(Integer[] a) {
      Random r = new Random();

      for(int i = a.length - 1; i > 0; i--) {
         int j = r.nextInt(i + 1);
         exchange(a, i, j);
      }
   }

   private static String bigO(double ratio){
      int n = (int) Math.round(ratio);
      switch(n) {
         case 0 : return "1";
         case 1 : return "N";
         case 2 : return "N^2";
         case 3 : return "N^3";
         default : return "1";
      }
   }

   private static double bigOExponent(double time) {
      return Math.log(time) / Math.log(2);
   }

   private static class TimeAndRatio {
      double time;
      double ratio;

      TimeAndRatio(double time, double ratio) {
         this.time = time;
         this.ratio = bigOExponent(ratio);
      }
   }


   // old implementation

}