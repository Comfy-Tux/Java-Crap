package aed.collections;

import java.util.Iterator;

public class ULLQueue<Item> implements  IQueue<Item> {
   Node head;
   Node tail;
   int blockSize = 16;
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

      double efficiency = Trial.enqueue(100000,20,1000000,1024);
      System.out.println(" Time elapsed = " + efficiency + " and log is : " + (Math.log(efficiency) / Math.log(2)));
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
         throw new ArrayIndexOutOfBoundsException();

      Item item = head.remove();
      if(head.isEmpty())
         head = head.next;
      N--;
      return item;
   }

   public Item peek() {
      return head.peek();
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
         return (now - start) / 1000.0;
      }
   }

   private static class Trial {
      public static double enqueue(int N, int nTimes,int singleTime ,int blockSize) {
         int i = 0;
         double result = 0;
         var queue = new ULLQueue<Double>(blockSize);
         var array = randomDoubleArray(N);
         for(int j = 0 ; j < N/2; j++)
            queue.enqueue(array[j]);

         while(i < nTimes) {
            int x = (int) (Math.random() * N);
            int y = x / 2;

            var stopwatch = new Stopwatch();
            for(int j = 0; j < singleTime; j++)
               queue.enqueue(0.0);
            double halfTime = stopwatch.elapsedTime() / singleTime;

            for(int j = N/2 + singleTime; j < N ; j++){
               queue.enqueue(array[j]);
            }

            var stopwatch2 = new Stopwatch();
            for(int j = 0; j < singleTime; j++)
               queue.enqueue(0.0);
            double fullTime = stopwatch2.elapsedTime() / singleTime;

            for(int j = 0; j < N/2; j++){
               queue.dequeue();
            }

            result += fullTime / halfTime;
            i++;
         }

         return result/nTimes;
      }
   }

   public static double[] randomDoubleArray(int N){
      var array = new double[N];
      for(int i = 0; i < N ; i++)
         array[i] = Math.random();

      return array;
   }

}
