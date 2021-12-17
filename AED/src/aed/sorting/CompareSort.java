package aed.sorting;

import java.util.*;

public class CompareSort {

   private static double testSort(String name,int N,int M){
      double total = 0;
      Double[] array = new Double[N];

      for(int i = 0; i < M ; i++) {
         for(int j = 0; j < N ; j++)
            array[j] = Math.random();
         var timer = new Stopwatch();
         if(name.equals("JumpBubbleSort"))
            JumpBubbleSort.sort(array);
         else if(name.equals("QuickSort3Way"))
            QuickSort3Way.sort(array);
         else if(name.equals("QuickMergeSort"))
            QuickMergeSort.sort(array);
         else
            return 0.0;
         total += timer.elapsedTime();
      }
      return total/((double)M);
   }


   public static void main(String[] args){
      var in = new Scanner(System.in);
      int precision = 50;
      System.out.println("Insert in format : sort sort N");
      String sort1 = null;
      String sort2 = null;
      int length = 100000;

      while(in.hasNext()){
         if(in.hasNextInt()){
            switch(in.nextInt()) {
               case 1: sort1 = "QuickSort3Way";sort2 = "JumpBubbleSort";length = 100000;break;
               case 2: sort1 = "QuickMergeSort";sort2 = "QuickMergeSort"; length = 100000;break;
            }
         }
         else {

            sort1 = in.next();
            sort2 = in.next();
            length = in.nextInt();
         }
         double time1 = testSort(sort1, length, precision);
         double time2 = testSort(sort2, length, precision);
         System.out.println(sort1 + " is " + time2/time1 + " faster than " + sort2);
      }
   }
   //MergeSort InsertionSort 100000

   private static class Stopwatch {
      private final long start;

      public Stopwatch() {
         start = System.currentTimeMillis();
      }

      public double elapsedTime() {
         long now = System.currentTimeMillis();
         return (now - start)/1000.0;
      }
   }

}
