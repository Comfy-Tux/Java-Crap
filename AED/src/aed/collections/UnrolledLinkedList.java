package aed.collections;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.*;

public class UnrolledLinkedList<Item> implements IList<Item>{
   private int blockSize = 16;
   private Node head;
   private Node tail;
   private int N;
   private Node current; // is just a global variable to store side effects on nodes ;
   //Attention "current" is to be used as a variable were you have access to the side effect of the method
   //findIndexNode , the side effect will place the node on the block where the index is present . Due to its high
   //use I thought that the drawback of a global variable is offset by the benefits of the side effect .
   // therefore, if you want to use current it is recommended to start the variable to head .

   public static void main(String[] args){
      var list = new UnrolledLinkedList<Integer>(5);
      for(int i = 0; i < 100 ; i++)
         list.add(i);
      for(Integer a: list){
         System.out.print(a + " ");
      }
      System.out.println();

      list.addAt(35,400);
      list.set(99,55555);

      var list2 = list.shallowCopy();

      for(Integer a: list2){
         System.out.print(a + " ");
      }
      System.out.println();
      Object[][] matrix = list.getArrayOfBlocks();
      Arrays.stream(matrix).map(Arrays::toString).forEach(System.out::println);

      //add is O(N) time elapsed wildly between 0.1 and 0.8 , the reason why this happens because add time is highly
      //dependent on the block size ,so it varies somewhat wildly , even though is slower than get.
      double efficiency = Trial.add(100000,20,10000,1024);
      System.out.println(" Time elapsed = " + efficiency + " and log is : " + (Math.log(efficiency) / Math.log(2)));

      //get is O(N) time elapsed is around 2 and 2.4 , this is more consistent as the time is as dependent on the
      // blockSize as the add function although the higher the blockSize the faster is get most of the time.
      double efficiency2 = Trial.get(100000,20,100000,1024);
      System.out.println(" Time elapsed = " + efficiency2 + " and log is : " + (Math.log(efficiency2) / Math.log(2)));

      //conclusion the best blockSize that I found is 1024
   }

   UnrolledLinkedList(){}
   UnrolledLinkedList(int blockSize){ this.blockSize = blockSize;}

   public void add(Item item) {

      if(head == null)
      {
         head = new Node();
         head.add(item);
         tail = head;
      }
      else
      {

         //last node is full , it splits the node in two nodes is half full , and adds the item to the last
         if(tail.counter == tail.itemArray.length) {
            fullNode(tail);
            tail = tail.next;
         }
         tail.add(item);
      }

      N++;
   }

   public void addAt(int index, Item item) {
      if(index > N)
         throw new ArrayIndexOutOfBoundsException();

      if(head == null)
      {
         head = new Node();
         head.add(item);
         tail = head;
      }
      else
      {
         int  currentIndex = findIndexNode(index,head);

         //node is full it's splits the node in two each with one half , and adds to the one with the appropriate index
         if(current.itemArray.length == current.counter) {
            fullNode(current);
            if(current == tail)
               tail = current.next;

            //if index is in the first node
            if(current.counter < currentIndex) {
               current.addAt(currentIndex, item);

            }
            //if index is in the second node
            else{
               current = current.next;
               current.addAt(currentIndex, item);
            }
         }
         else
            current.addAt(currentIndex,item);
      }
      N++;
   }

   public Item remove() {
      if(N == 0)
         throw new ArrayIndexOutOfBoundsException();

      Item item = tail.remove();
      //if block is empty delete node
      if(head.counter  == 0){
         head = null;
      }
      else {
         if(tail.counter == 0) {
            current = head;
            while(current.next != tail) {
               current = current.next;
            }
            tail = current;
            tail.next = null;
         }
      }
      N--;
      return item;
   }

   public Item remove(int index) {
      if(N == 0 || index >= N)
         throw new ArrayIndexOutOfBoundsException();

      int currentIndex = findIndexNode(index,head);

      Item item = current.removeAt(currentIndex);
      //if block is empty delete node
      if(current.counter == 0){
         //if the block is the head ,
         if(current == head)
            head = head.next;
         else
         {
            Node NextCurrent = current;
            // searches for the node before current , currentIndex is higher than the index or equal , the result
            // will land on the node before the current
            // the result can be discarded , we are only interested on the side effect of placing the node.
            findIndexNode(index -currentIndex -1,head);

            current.next = NextCurrent.next;
            if(NextCurrent.next == null)
               tail = current;
         }
      }

      N--;
      return item;
   }

   public boolean isEmpty() {
      return N == 0;
   }

   public int size() {
      return N;
   }

   public Item get(int index) {
      if(N == 0 || index >= N)
         throw new ArrayIndexOutOfBoundsException();

      int currentIndex = findIndexNode(index,head);

      return current.itemArray[currentIndex];
   }

   public void set(int index, Item element) {
      if(N == 0 || index >= N)
         throw new ArrayIndexOutOfBoundsException();

      int currentIndex = findIndexNode(index,head);

      current.itemArray[currentIndex] = element;
   }

   public IList<Item> shallowCopy() {
      var list = new UnrolledLinkedList<Item>(blockSize);
      list.head = head;

      return list;
   }
/////////////////////////////////////////////////////////////////////////////////
   public Iterator<Item> iterator() {
      return new UnrolledLinkedListIterator();
   }

   private class UnrolledLinkedListIterator implements Iterator<Item>{
      Node current = head;
      int globalIndex = 0;

      public boolean hasNext(){
         return current != null;
      }

      public Item next(){
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

      public void delete(){}
   }

   public Item[][] getArrayOfBlocks(){
      int i = 0;
      current = head;
      while(current != null) {
         current = current.next;
         i++;
      }

      @SuppressWarnings("uncheked")
      Item[][] matrix = (Item[][]) new Object[i][blockSize];

      current = head;
      i = 0;
      while(current != null) {
         matrix[i] = current.itemArray;
         current = current.next;
         i++;
      }

      return matrix;
   }

//////////////////////////////////////////////////////////////////////////////////

   //side effects it alters the current node to the desired position , and returns the current position.
   private int findIndexNode(int index,Node start){
      int currentIndex = index;
      current = start;

      //finds the node were the index is to be removed or inserted;
      while(currentIndex >= current.counter && current.next != null) {
         currentIndex -= current.counter;
         current = current.next;
      }

      return currentIndex ;
   }


   private void fullNode(Node oldTail){
      Node tail = new Node();
      tail.next = oldTail.next;
      oldTail.next = tail;

      int n = oldTail.itemArray.length /2;

      int i = 0;
      while( i < n ){
         tail.add(oldTail.itemArray[n+i]);
         oldTail.itemArray[n+i] = null;
         i++;
      }

      oldTail.counter = n ;
      tail.counter = n;
      if(oldTail.itemArray.length % 2 == 1) {
         tail.itemArray[n] = oldTail.itemArray[oldTail.itemArray.length -1]; // moves the last element to its correspondent position
         oldTail.itemArray[oldTail.itemArray.length -1] = null; // avoid loitering and duplication
         tail.counter++;
      }
   }


   private class Node{
       Item[] itemArray ;
       int counter;
       Node next;

      @SuppressWarnings("uncheked")
      Node(){itemArray = (Item[]) new Object[blockSize]; }
      @SuppressWarnings("unckeked")
      Node(Node next){itemArray = (Item[]) new Object[blockSize]; this.next = next;}

      //adds an item to the array
      void add(Item item){
         itemArray[counter] = item;
         counter++;
      }

      void addAt(int index,Item item){
         itemArray[counter] = item;
         for(int i = counter ; i > index ; i-- ){
            swap(itemArray,i,i-1);
         }
         counter++;
      }

      private void swap(Item[] array,int x,int y){
         Item temp = array[x];
         array[x] = array[y];
         array[y] = temp;
      }

      //removes an item from the array
      Item remove(){
         counter--;
         Item item = itemArray[counter];
         itemArray[counter] = null; //avoid loitering
         return item;
      }

      Item removeAt(int index){
         counter--;
         Item item = itemArray[index];

         for(int i = index +1 ; i <= counter ; i++){
            swap(itemArray,i,i-1);
         }
         itemArray[counter] = null; //avoid loitering
         return item;
      }
   }


   private static class Stopwatch{
      private final long start;

      public Stopwatch(){ start = System.currentTimeMillis();}

      public double elapsedTime(){
         long now = System.currentTimeMillis();
         return (now - start ) / 1000.0;
      }
   }

   private static class Trial{
      public static double add(int N, int nTimes,int singleTime, int blockSize){
         int i = 0;
         double result = 0;
         var list = new UnrolledLinkedList<Double>(blockSize);
         var array = randomDoubleArray(N);
         for(int j = 0 ; j < N; j++)
            list.add(array[j]);

         while(i < nTimes) {
            int x = (int) (Math.random() * N);
            int y = x / 2;

            var stopwatch = new Stopwatch();
               for(int j = 0; j < singleTime; j++)
                  list.addAt(y, 0.0);
            double halfTime = stopwatch.elapsedTime() / singleTime;

            for(int j = 0; j < singleTime; j++) {
                  list.remove();
               }

            var stopwatch2 = new Stopwatch();
            for(int j = 0; j < singleTime; j++)
                  list.addAt(x,0.0);
            double fullTime = stopwatch2.elapsedTime() / singleTime;

            for(int j = 0; j < singleTime; j++){
               list.remove();
            }

            result += fullTime / halfTime;
            i++;
         }

         return result/nTimes;
      }

      public static double get(int N, int nTimes,int singleTime, int blockSize){
         double result = 0;
         var list = new UnrolledLinkedList<Double>(blockSize);
         var array = randomDoubleArray(N);
         for(int j = 0 ; j < N; j++)
            list.add(array[j]);

         int i = 0;
         while(i < nTimes) {

            int x = (int) (Math.random() * N);
            int y = x / 2;

            var stopwatch = new Stopwatch();
            for(int j = 0; j < singleTime; j++)
               list.get(y);
            double halfTime = stopwatch.elapsedTime() / singleTime;

            var stopwatch2 = new Stopwatch();
            for(int j = 0; j < singleTime; j++)
               list.get(x);
            double fullTime = stopwatch2.elapsedTime() / singleTime;

            result += fullTime / halfTime;
            i++;
         }

         return result/nTimes;
      }

      public static double[] randomDoubleArray(int N){
         var array = new double[N];
         for(int i = 0; i < N ; i++)
            array[i] = Math.random();

         return array;
      }

   }
}
