package aed.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ULLQueue<Item> implements  IQueue<Item> {
   Node head;
   Node tail;
   int blockSize = 1024;
   int N;

   public static void main(String[] args) {
      var list = new ULLQueue<Integer>(4);
      for(int i = 0; i < 100; i++)
         list.enqueue(i);
      for(Integer a : list) {
         System.out.print(a + " ");
      }
      System.out.println();


      System.out.println(list.dequeue());
      System.out.println(list.dequeue());
      System.out.println(list.dequeue());
      System.out.println(list.dequeue());
      System.out.println(list.dequeue());

      list.enqueue(555);
      list.enqueue(333);

      for(Integer a : list) {
         System.out.print(a + " ");
      }
      System.out.println();
      System.out.println(list.peek());

      System.out.println(list.dequeue());
      System.out.println(list.dequeue());
      System.out.println(list.dequeue());

      for(Integer a : list) {
         System.out.print(a + " ");
      }
      System.out.println();

      //O(1)
      double efficiency = DoublingRatioTest.enqueue(100);
      System.out.println(" Time elapsed = " + efficiency + " and log is : " + (Math.log(efficiency) / Math.log(2)));

      //O(1)
      double efficiency2 = DoublingRatioTest.dequeue(100);
      System.out.println(" Time elapsed = " + efficiency2 + " and log is : " + (Math.log(efficiency2) / Math.log(2)));

   }

   ULLQueue() {
   }

   ULLQueue(int blockSize) {
      this.blockSize = blockSize;
   }


   public void enqueue(Item item) {
      if(tail == null) {
         tail = new Node();
         tail.add(item);
         head = tail;
      } else {
         if(tail.isFull()) {
            Node newTail = new Node();
            newTail.add(item);
            tail.next = newTail;
            tail = newTail;
         } else
            tail.add(item);
      }
      N++;
   }

   public Item dequeue() {
      if(head == null || N == 0)
         return null;

      Item item = head.remove();
      if(head.isEmpty())
         head = head.next;
      N--;
      return item;
   }

   public Item peek() {return head.peek();
   }

   public boolean isEmpty() {
      return N == 0;
   }

   public int size() {
      return N;
   }

   public IQueue<Item> shallowCopy() {
      var list = new ULLQueue<Item>(blockSize);
      list.head = head;
      list.tail = tail;
      list.N = N;
      list.blockSize = N;

      return list;
   }

   public Iterator<Item> iterator() {
      return new ULLQueueIterator();
   }

   private class ULLQueueIterator implements Iterator<Item> {
      Node current = head;
      int globalIndex = current.removed;

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

      public void delete() {
      }
   }

   private class Node {
      Item[] itemArray;
      int counter;
      int removed; //index for the elements that have been removed from the queue
      Node next;

      @SuppressWarnings("unchecked")
      Node() {
         itemArray = (Item[]) new Object[blockSize];
      }

      @SuppressWarnings("unchecked")
      Node(Node next) {
         itemArray = (Item[]) new Object[blockSize];
         this.next = next;
      }

      public void add(Item item) {
         itemArray[counter] = item;
         counter++;
      }

      public Item remove() {
         Item item = itemArray[removed];
         itemArray[removed] = null;
         removed++;
         return item;
      }

      public Item peek() {
         if(counter == 0)
            return null;
         return itemArray[removed];
      }

      boolean isFull() {
         return counter == itemArray.length;
      }

      boolean isEmpty() {
         return counter == 0 || removed == counter;
      }

   }

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
       for(int i = 0 ; i < 100000 ; i++)
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
            for(int j = 0; j < 100000 ; j++) {
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
}