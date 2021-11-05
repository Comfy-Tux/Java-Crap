package aed.collections;

import java.util.*;

public class ULLQueue<Item> implements  IQueue<Item> {
   private Node head;
   private Node tail;
   private int blockSize = 2648;
   private int N;
   private int removed;  //index for the elements that have been removed from the queue

   public static void main(String[] args) {
      var queue = new ULLQueue<Integer>(4);

      for(int i = 0; i < 10 ; i++)
         queue.enqueue(i);


      var queue2 = new ULLQueue<Boolean>(1);

      queue2.enqueue(true);
      System.out.println(queue2.peek());
      queue2.enqueue(false);

      queue2.enqueue(true);

      System.out.println(queue2.peek());
      System.out.println(queue2.peek());
      queue2.dequeue();
      queue2.dequeue();
      queue2.dequeue();
      System.out.println(queue2.peek());
      queue2.enqueue(false);
      queue2.enqueue(true);
      System.out.println( queue2.peek());
      queue2.dequeue();
      System.out.println( queue2.peek());

      System.out.println(queue2.toString());


      //O(1)
      double efficiency = DoublingRatioTest.enqueue(100);
      System.out.println(" Time elapsed = " + efficiency + " and log is : " + (Math.log(efficiency) / Math.log(2)));

      //O(1)
      double efficiency2 = DoublingRatioTest.dequeue(100);
      System.out.println(" Time elapsed = " + efficiency2 + " and log is : " + (Math.log(efficiency2) / Math.log(2)));


   }

   public ULLQueue() {
   }

   public ULLQueue(int blockSize) {
      this.blockSize = blockSize;
   }


   public void enqueue(Item item) {
      if(tail == null) {
         tail = new Node();
         tail.add(item);
         head = tail;
      } else {
         if(tail.isFull()) {
            Node oldTail = tail;
            tail = new Node();
            tail.add(item);
            oldTail.next = tail;
         } else
            tail.add(item);
      }
      N++;
   }

   public Item dequeue() {
      if(head == null)
         return null;

      Item item = head.remove();
      if(head.isEmpty()) {
         if(head == tail)
            tail = null;
         head = head.next;
         removed = 0;
      }
      N--;
      return item;
   }

   public Item peek() {
      if(head == null)
         return null;
      return head.itemArray[removed];
   }

   public boolean isEmpty() {
      return N == 0;
   }

   public int size() {
      return N;
   }

   public IQueue<Item> shallowCopy() {
      var queue = new ULLQueue<Item>(blockSize);
      if(N != 0){
         queue.head = head.shallowCopy(removed);
         queue.N = N;
         Node current = head;
         Node altCurrent = queue.head;
         while(current.next != null) {
            altCurrent.next = current.next.shallowCopy(0);
            altCurrent = altCurrent.next;
            current = current.next;
         }
         queue.tail = altCurrent.next;
      }
      return queue;
   }

   public String toString(){
      if(head == null)
         return "[]";
      Node current = head;
      var result = new StringBuilder("[");
      result.append(head.toString());
      current = current.next;
      while(current != null){
         result.append(",").append(current.toString());
         current = current.next;
      }
      return result.append("]").toString();
   }

   ///////////////////// ITERATOR ///////////////////////////////////////////////////////
   public Iterator<Item> iterator() {
      return new ULLQueueIterator();
   }

   private class ULLQueueIterator implements Iterator<Item> {
      private Node current = head;
      private int globalIndex = 0;

      public ULLQueueIterator(){if(head != null) globalIndex = removed;}

      public boolean hasNext() {
         return current != null;
      }

      public Item next() {
         Item item = current.itemArray[globalIndex];
         globalIndex++;
         //resets the index to 0 , when it arrives at the end of the current array ,
         // so the next node array starts from the beginning , and goes to the next node;
         if(current.counter == globalIndex) {
            globalIndex = 0;
            current = current.next;
         }

         return item;
      }

   }
   //////////////////////// END OF ITERATOR ///////////////////////////////////////////

   //////////////////////// NODE ////////////////////////////////////////////////////////

   private class Node {
      private final Item[] itemArray;
      private int counter;
      private Node next;

      @SuppressWarnings("unchecked")
      public Node() {
         itemArray = (Item[]) new Object[blockSize];
      }

      public void add(Item item) {
         itemArray[counter] = item;
         counter++;
      }

      public Item remove() {
         Item item = itemArray[removed];
         itemArray[removed] = null; // avoid loitering
         removed++;
         return item;
      }

      public Item peek() {
         return itemArray[counter -1 ];
      }

      public boolean isFull() {
         return counter == itemArray.length;
      }

      public boolean isEmpty() {
         return itemArray[counter -1] == null;
      }

      //there is no need to copy next as it will be modified later .
      public Node shallowCopy(int start){
         Node result = new Node();
         result.counter = counter;
         System.arraycopy(itemArray, start, result.itemArray, start, counter - start);
         return result;
      }

      public String toString(){
         StringBuilder result = new StringBuilder("[");

         int start = 0;
         if(this.itemArray == head.itemArray)
            start = removed;

         result.append(itemArray[start].toString());
         for(int i = start +1; i < counter ; i++)
            result.append(",").append(itemArray[i].toString());
         result.append("]");
         return result.toString();
      }
   }
   /////////////////////////// END OF NODE /////////////////////////////////////



   /////////////////////////// DOUBLING TEST //////////////////////////////////
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

   private static class DoublingRatioTest {

      //the queue size is N
      private static double enqueueTimeTrial(ULLQueue<Double> queue) {

         Stopwatch timer = new Stopwatch();
       for(int i = 0 ; i < 200000 ; i++)
         queue.enqueue(0.0);
         return timer.elapsedTime();
      }

      public static double enqueue(long N) {
         int size = 20000000;
         var array = randomDoubleArray(size);
         var halfQueue = new ULLQueue<Double>();
         var fullQueue = new ULLQueue<Double>();

         int halfSize = size / 2;
         for(int i = 0; i < halfSize; i++)
            halfQueue.enqueue(array[i]);

         for(Double a : array)
            fullQueue.enqueue(a);

         double total = 0;
         for(int i = 0; i < N; i++) {
            if(i % 10 == 0) System.gc();
            double prev = enqueueTimeTrial(halfQueue);
            double current = enqueueTimeTrial(fullQueue);
            for(int j = 0; j < 200000 ; j++) {
               halfQueue.dequeue();
               fullQueue.dequeue();
            }
            double ratio = current / prev;
            total += ratio;
   //         System.out.printf("previous = %.10f , next = %.10f , ratio = %.5f , O(N) = %.5f\n",
     //               prev,current,ratio,Math.log(ratio)/Math.log(2));
         }
         return total / ((double) N);
      }

      //the queue size is N
      private static double dequeueTimeTrial(ULLQueue<Double> queue) {

         Stopwatch timer = new Stopwatch();
         for(int i = 0 ; i < 400000 ; i++)
            queue.dequeue();
         return timer.elapsedTime();
      }

      public static double dequeue(long N) {
         int size = 20000000;
         var array = randomDoubleArray(size);
         var halfQueue = new ULLQueue<Double>();
         var fullQueue = new ULLQueue<Double>();

         int halfSize = size / 2;
         for(int i = 0; i < halfSize; i++)
            halfQueue.enqueue(array[i]);

         for(Double a : array)
            fullQueue.enqueue(a);

         double total = 0;
         for(int i = 0; i < N; i++) {
            if(i % 10 == 0) System.gc();
            double prev = dequeueTimeTrial(halfQueue);
            double current = dequeueTimeTrial(fullQueue);
            for(int j = 0; j < 400000 ; j++) {
               halfQueue.enqueue(0.0);
               fullQueue.enqueue(0.0);
            }
            double ratio = current / prev;
            total += ratio;
                     System.out.printf("previous = %.10f , next = %.10f , ratio = %.5f , O(N) = %.5f\n",
                          prev,current,ratio,Math.log(ratio)/Math.log(2));
         }
         return total / ((double) N);
      }
   }

   public static double[] randomDoubleArray(int N) {
      var array = new double[N];
      for(int i = 0; i < N; i++)
         array[i] = Math.random();

      return array;
   }
   //////////////////////////// END OF DOUBLING TEST //////////////////////////////////////////////
}