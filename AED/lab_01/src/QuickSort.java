import java.util.*;

public class QuickSort {
   public static void main(String[] argv){
      quicksortTest();
   }

   //quicksort  Hores partition scheme

   public static void quicksortTest(){
      int[] a = {5,3,2,5,6,7,8,1,2,3,1,4,0,8,6};
      System.out.println("Unsorted " + Arrays.toString(a));
      quicksort(a);
      System.out.println("Sorted   " + Arrays.toString(a));
   }

   public static void quicksort(int[] a){
      quicksort(a, 0, a.length -1 );
   }

   public static void quicksort(int[] a,int lo ,int hi){
      if(lo >= 0 && hi >= 0 && lo < hi){
         int p = partition(a,lo,hi);

         quicksort(a,lo,p); //left side of pivot
         quicksort(a,p+1 , hi); //right side of pivot
      }
   }

   public static int partition(int[] a,int lo ,int hi){
      int pivot = a[(hi+lo)/2]; //pivot has the value of the middle of the array

      //left index
      int i = lo -1;

      //right index
      int j = hi + 1;

      for(;;){

         do
            i++;
         while(a[i]< pivot);

         do
            j--;
         while(a[j] > pivot);

         if(i >= j)
            return j;

         swap(a,i,j);
      }

   }





   public static void swap(int[] a, int x ,int y){
      int temp = a[x];
      a[x] = a[y];
      a[y] = temp;
   }
}
