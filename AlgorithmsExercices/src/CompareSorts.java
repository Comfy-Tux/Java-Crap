import java.util.Scanner;

public class CompareSorts {
   // N - length of the array , M - number of sorts
   private static double testSort(String name,int N,int M){
      double total = 0;
      Double[] array = new Double[N];

      for(int i = 0; i < M ; i++) {
         for(int j = 0; j < N ; j++)
            array[j] = Math.random();
         var timer = new Stopwatch();
         if(name.equals("MergeSort"))
            RecursiveMergeSort.sort(array);
         else if(name.equals("RecursiveMergeSort"))
            RecursiveMergeSort.sort(array);
         else if(name.equals("BottomUpMergeSort"))
            BottomUpMergeSort.sort(array);
         else if(name.equals("ShellSort"))
            ShellSort.sort(array);
         else if(name.equals("InsertionSort"))
            InsertionSort.sort(array);
         else if(name.equals("QuickMergeSort"))
            QuickMergeSort.sort(array);
         else if(name.equals("NaturalMergeSort"))
            NaturalMergeSort.sort(array);
         else if(name.equals("QuickSort"))
            QuickSort.sort(array);
         else if(name.equals("QuickSort3way"))
            QuickSort3way.sort(array);
         else
            return 0.0;
         total += timer.elapsedTime();
      }
      return total/((double)M);
   }


   public static void main(String[] args){
      var in = new Scanner(System.in);
      int precision = 100;
      System.out.println("Insert in format : sort sort N");
      String sort1 = null;
      String sort2 = null;
      int length = 10000;

      while(in.hasNext()){
         if(in.hasNextInt()){
            switch(in.nextInt()) {
               case 1: sort1 = "RecursiveMergeSort"; sort2 = "BottomUpMergeSort"; length = 10000; break;
               case 2: sort1 = "MergeSort";sort2 = "ShellSort"; length = 10000; break;
               case 3: sort1 = "MergeSort";sort2 = "InsertionSort";length = 1000; break;
               case 4: sort1 = "ShellSort";sort2 = "InsertionSort"; length = 1000;break;
               case 5: sort1 = "QuickMergeSort";sort2 = "MergeSort"; length = 10000;break;
               case 6: sort1 = "BottomUpMergeSort";sort2 = "NaturalMergeSort";length = 10000;break;
               case 7: sort1 = "QuickSort";sort2 = "MergeSort";length = 10000;break;
               case 8: sort1 = "QuickSort";sort2 = "QuickSort3way";length = 10000;break;
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
