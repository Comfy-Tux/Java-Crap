package aed.collections;

import java.util.*;


public class UnrolledLinkedList<Item> implements IList<Item>{
   private int blockSize = 60192;
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
      list.addAt(34,0);



      list.set(99,55555);


      for(Object a: list2){
         System.out.print(a + " ");
      }

      UnrolledLinkedList list3 = (UnrolledLinkedList) list2;

      Object[][] matrix2 = list3.getArrayOfBlocks();
      Arrays.stream(matrix2).map(Arrays::toString).forEach(System.out::println);

      System.out.println();
      Object[][] matrix = list.getArrayOfBlocks();
      Arrays.stream(matrix).map(Arrays::toString).forEach(System.out::println);


      for(Integer a: list){
         System.out.print(a + " ");
      }
      System.out.println();


      //add is O(N) time elapsed is around 2
      double efficiency = DoublingRatioTest.add(200);
      System.out.println(" Time elapsed = " + efficiency + " and log is : " + (Math.log(efficiency) / Math.log(2)));

      //get is O(N)/blocksize time elapsed is around 2 if blocksize is too big , O(N) is approximately O(1);
      double efficiency2 = DoublingRatioTest.get(200);
      System.out.println(" Time elapsed = " + efficiency2 + " and log is : " + (Math.log(efficiency2) / Math.log(2)));


      //conclusion the best blockSize that I found is 12024


   }

   ///////////////////////////////////////////// END OF MAIN //////////////////////////////////////

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

         //last node is full , it splits the node in two nodes with each half full , and adds the item to the last
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
         add(item);
         return;
      }
      else
      {
         int  currentIndex = findIndexNode(index ,head);

         //node is full it's splits the node in two each with one half , and adds to the one with the appropriate index
         if(current.itemArray.length == current.counter) {
            fullNode(current);
            if(current == tail)
               tail = current.next;

            //discovers on which of the nodes the currentIndex is .
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
      //the beforeCurrent node holds the node before current if current is going to become empty otherwise it is useless .
      // The reason why I do it before searching current is two avoid doing two searches on the list .
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

   public IList<Item> shallowCopy() {
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

   public Item[][] getArrayOfBlocks(){
      int i = 0;
      current = head;

      //counts the amount of nodes on the list
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

/////////////////////////////////////////////// NODE //////////////////////////////////////////////////////////////
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
            System.arraycopy(itemArray, index + 1, itemArray, index , counter - (index + 1));
         itemArray[counter -1] = null; //avoid duplication of the last element

         counter--;
         return item;
      }


      //there is no need to copy next as it will be modified later .
      public Node shallowCopy(){
         Node result = new Node();
         System.arraycopy(itemArray, 0, result.itemArray, 0, counter);
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

   //////////////////////////////////// END OF NODE ///////////////////////////////////////////////////////////

   /////////////////////////////////// ITERATOR //////////////////////////////////////////////////////////////

   public Iterator<Item> iterator() {
      return new UnrolledLinkedListIterator();
   }

   private class UnrolledLinkedListIterator implements Iterator<Item> {
      Node current = head;
      int globalIndex = 0;

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

   ///////////////////////////// END OF ITERATOR //////////////////////////////////////////////////////

   ///////////////////////////// DOUBLING RATIO //////////////////////////////////////////////////////
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
         for(int i = 0; i < 2000 ; i++)
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
         for(long i = 0; i < 2000000; i++)
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
//            System.out.printf("previous = %.5f , next = %.5f , ratio = %.5f , O(N) = %.5f\n",
//                    prev,current,ratio,Math.log(ratio)/Math.log(2));
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