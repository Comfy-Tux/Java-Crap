import java.util.*;
import java.util.stream.*;

public class NonRecursiveQuickSort {


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
      var stack = new Stack<Subarray>();
      stack.push(new Subarray(0,a.length-1));

      while(!stack.isEmpty()){
         var subarray = stack.pop();
         int pivot = partitioning(subarray.lo,subarray.hi,a);
         var firstHalf = new Subarray(subarray.lo,pivot);
         var secondHalf = new Subarray(pivot +1, subarray.hi);

         if(less(firstHalf,secondHalf))
         {
            if(secondHalf.size() > 0)
               stack.push(secondHalf);
            else
               continue;
            if(firstHalf.size() > 0)
               stack.push(firstHalf);
         }
         else
         {
            if(firstHalf.size() > 0)
               stack.push(firstHalf);
            else
               continue;
            if(secondHalf.size() > 0)
               stack.push(secondHalf);
         }
      }
   }

   //median of 3
   private static int partitioning(int lo,int hi,Comparable[] a){
      int i = lo;
      int j = hi +1;
      int mid = (hi -lo)/2 + lo;

      //order lo , mid , hi on the array
      if(less(a[hi],a[lo]))
         swap(a,hi,lo);
      if(less(a[mid],a[lo]))
         swap(a,mid,lo);
      if(less(a[hi],a[mid]))
         swap(a,mid,hi);

      swap(a,mid,lo);
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

   //////////////////////////////// stack //////////////////////////////////////////////////

   private static class Subarray implements Comparable<Subarray>{
      int lo;
      int hi;
      Subarray(int lo,int hi){this.lo = lo; this.hi = hi;}

      public int size(){ return hi-lo;}

      public int compareTo(Subarray x){
         return this.size() - x.size();
      }

   }


   private static class Stack<Item> implements Iterable<Item>{
      Node head;
      int N;

      public void push(Item item){
         if(isEmpty())
            head = new Node(item);
         else
            head = new Node(item,head);
         N++;
      }

      public Item pop(){
         Item item = head.item;
         head = head.next;
         N--;
         return item;
      }

      public boolean isEmpty(){
         return head == null;
      }

      private class Node{
         Item item;
         Node next;

         Node(){}
         Node(Item item){this.item = item;}
         Node(Item item,Node next){this.item = item ; this.next = next;}
      }

      public Iterator<Item> iterator(){
         return new StackIterator();
      }

      private class StackIterator implements Iterator<Item>{
         Node current = head;

         public boolean hasNext(){
            return current != null;
         }

         public Item next(){
            Item item = current.item;
            current = current.next;
            return item;
         }
      }
   }
}
