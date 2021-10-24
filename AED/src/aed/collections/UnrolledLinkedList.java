package aed.collections;

import java.util.*;
import java.util.stream.*;


public class UnrolledLinkedList<Item> implements IList<Item>{
   private int blockSize = 12192;
   private Node head;
   private Node tail;
   private int N;
   private Node current; // is just a global variable to store side effects on nodes ;
   //Attention "current" is to be used as a variable were you have access to the side effect of the method
   //findIndexNode , the side effect will place the node on the block where the index is present . Due to its high
   //use I thought that the drawback of a global variable is offset by the benefits of the side effect .
   // therefore, if you want to use current it is recommended to start the variable to head .

   public static void main(String[] args){
      var list = new UnrolledLinkedList<Integer>(4);
      for(int i = 0; i < 100 ; i++)
         list.add(i);

      var list2 = list.shallowCopy();

      for(Integer a: list){
         System.out.print(a + " ");
      }
      System.out.println();

      list.addAt(34,1);
      list.addAt(34,2);
      list.addAt(36,3);
      list.addAt(36,4);
      System.out.println("list N - "+  list.N);
      list.remove(100);
      list.remove(100);
      //list.remove(37);



      list.set(99,55555);

      list.remove();
      list.remove();
      list.remove();
      list.remove();

      for(Integer a: list2){
         System.out.print(a + " ");
      }

      Object[][] matrix2 = list2.getArrayOfBlocks();
      Arrays.stream(matrix2).map(Arrays::toString).forEach(System.out::println);

      System.out.println();
      Object[][] matrix = list.getArrayOfBlocks();
      Arrays.stream(matrix).map(Arrays::toString).forEach(System.out::println);




      //add is O(N) time elapsed is around 2
      double efficiency = DoublingRatioTest.add(200);
      System.out.println(" Time elapsed = " + efficiency + " and log is : " + (Math.log(efficiency) / Math.log(2)));

      //get is O(N) time elapsed is around 2
      double efficiency2 = DoublingRatioTest.get(200);
      System.out.println(" Time elapsed = " + efficiency2 + " and log is : " + (Math.log(efficiency2) / Math.log(2)));


      //conclusion the best blockSize that I found is 12024

   }

   public UnrolledLinkedList(){}
   public UnrolledLinkedList(int blockSize){ this.blockSize = blockSize;}

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
         return;
      if(index == N){
         add(item);
         return;
      }

      if(head == null)
      {
         head = new Node();
         head.add(item);
         tail = head;
      }
      else
      {
         int  currentIndex = findIndexNode(index ,head);

         //node is full it's splits the node in two each with one half , and adds to the one with the appropriate index
         if(current.itemArray.length == current.counter) {
            fullNode(current);
            if(current == tail)
               tail = current.next;

            //discovers on which nodes the currentIndex is .
            currentIndex = findIndexNode(currentIndex, current);
         }
         current.addAt(currentIndex, item);
      }
      N++;
   }

   public Item remove() {
      if(N == 0)
         return null;

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
         return null;



      Node beforeCurrent = head;
      int currentIndex = index -1;
      //weird code following
      // searches for the node before current , currentIndex is higher than the index or equal , the result
      // will land on the node before the current
      if(index >= head.counter) {
         currentIndex =findIndexNode(currentIndex, head);
         beforeCurrent = current;
      }


       currentIndex = findIndexNode(currentIndex +1,beforeCurrent);

      Item item = current.removeAt(currentIndex);
      //if block is empty delete node
      if(current.counter == 0){
         //if the block is the head ,
         if(current == head)
            head = head.next;
         else
         {
            beforeCurrent.next = current.next;
            if(beforeCurrent.next == null)
               tail = beforeCurrent;
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
         return null;

      int currentIndex = findIndexNode(index,head);

      return current.itemArray[currentIndex];
   }

   public void set(int index, Item element) {
      if(N == 0 || index >= N)
         return;

      int currentIndex = findIndexNode(index,head);

      current.itemArray[currentIndex] = element;
   }

   public UnrolledLinkedList<Item> shallowCopy() {
      var list = new UnrolledLinkedList<Item>(blockSize);
      if(N != 0){
      list.head = head.shallowCopy();
      list.N = N;
         Node current = head;
         Node altCurrent = list.head;
         while(current.next != null) {
            altCurrent.next = current.next.shallowCopy();
            altCurrent = altCurrent.next;
            current = current.next;
         }
         list.tail = altCurrent.next;
      }
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
         return current != null ;
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

   }

   public Item[][] getArrayOfBlocks(){
      int i = 0;
      current = head;
      while(current != null) {
         current = current.next;
         i++;
      }

      @SuppressWarnings("unchecked")
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
      while(currentIndex >= current.counter) {
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

      @SuppressWarnings("unchecked")
      public Node(){itemArray = (Item[]) new Object[blockSize]; }

      //adds an item to the array
      public void add(Item item){
         itemArray[counter] = item;
         counter++;
      }

      public void addAt(int index,Item item){
         itemArray[counter] = item;
         if(counter - index >= 0) System.arraycopy(itemArray, index, itemArray, index + 1, counter - index);
         itemArray[index] = item;

         counter++;
      }

      //removes an item from the array
      public Item remove(){
         counter--;
         Item item = itemArray[counter];
         itemArray[counter] = null; //avoid loitering
         return item;
      }

      public Item removeAt(int index){
         Item item = itemArray[index];

         if(counter - (index + 1) >= 0)
            System.arraycopy(itemArray, index + 1, itemArray, index + 1 - 1, counter - (index + 1));
         itemArray[counter -1] = null; //avoid duplication of the last element

         counter--;
         return item;
      }


      //there is no need to copy next as it will be modified later .
      public Node shallowCopy(){
         Node result = new Node();
         if(counter >= 0) System.arraycopy(itemArray, 0, result.itemArray, 0, counter);
         result.counter = counter;
         return result;
      }

      public String toString(){
         StringBuilder result = new StringBuilder("[");

         if(counter > 0)
            result.append(itemArray[0].toString());
         for(int i = 1; i < counter ; i++)
            result.append(",").append(itemArray[i].toString());
         result.append("]");
         return result.toString();
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

   private static class DoublingRatioTest {
      private static double addTimeTrial(int N , UnrolledLinkedList<Double> list){

         Stopwatch timer = new Stopwatch();
         for(int i = 0; i < 200 ; i++)
         list.addAt(N,0.0);
         return timer.elapsedTime();
      }

      public static double add(long N) {
         int size = 20000000;
         var array = randomDoubleArray(size);
         var list = new UnrolledLinkedList<Double>();

         for(Double a : array)
            list.add(a);

         double analysis = 0;
         double total = 0;
         for(int i = 0; i < N; i++) {
            int random = (int) (10000000.0 + 10000000.0 * Math.random());
            double prev = addTimeTrial(random/2, list);
            double current = addTimeTrial(random, list);
            analysis += prev;
            double ratio = current / prev;
            total += ratio;
            //System.out.printf("previous = %.5f , next = %.5f , ratio = %.5f , O(N) = %.5f\n",
            //        prev,current,ratio,Math.log(ratio)/Math.log(2));
         }
         System.out.println(analysis);
         return total / ((double)N);
      }

      private static double getTimeTrial(int N , UnrolledLinkedList<Double> list){

         Stopwatch timer = new Stopwatch();
         for(long i = 0; i < 20000000; i++)
            list.get(N);
         return timer.elapsedTime();
      }

      public static double get(long N) {
         int size = 20000002;
         var array = randomDoubleArray(size);
         var list = new UnrolledLinkedList<Double>();
         double analysis = 0;

         double total = 0;
         for(int i = 0; i < N; i++) {
            int random = (int) (10000000.0 + 10000000.0 * Math.random());
            double prev = getTimeTrial(random/2, list);
            double current = getTimeTrial(random, list);
            analysis += current;
            double ratio = current / prev;
            total += ratio;
  //          System.out.printf("previous = %.5f , next = %.5f , ratio = %.5f , O(N) = %.5f\n",
  //                  prev,current,ratio,Math.log(ratio)/Math.log(2));
         }
         System.out.println(analysis);
         return total / ((double)N);
      }



      private static double[] randomDoubleArray(int N){
         var array = new double[N];
         for(int i = 0; i < N ; i++)
            array[i] = Math.random();

         return array;
      }

   }
}
