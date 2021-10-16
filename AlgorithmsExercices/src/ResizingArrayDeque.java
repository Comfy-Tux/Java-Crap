//API
/*

public class Deque<Item> implements Iterable<Item>
             Deque()               create an empty deque
     boolean isEmpty()             is the deque empty?
         int size()                number of items in the deque
        void pushLeft(Item item)   add an item to the left end
        void pushRight(Item item)  add an item to the right end
         Item popLeft()            remove an item from the left end
         Item popRight()           remove an item from the right end
 */

import java.util.*;

public class ResizingArrayDeque<Item> implements Iterable<Item> {
   Item[] array;
   int rightIndex; // it points to the next null element on the right
   int leftIndex; // it points to the next null element on the left
   int N; //size

   public static void main(String[] args){

      var deque = new ResizingArrayDeque<Integer>();

      deque.pushLeft(1);
      deque.pushLeft(2);
      deque.pushRight(3);
      deque.pushLeft(0);
      deque.pushRight(5);

      System.out.print(deque.popLeft() + " ");
      System.out.print(deque.popLeft() + " ");


      while(!deque.isEmpty())
         System.out.print(deque.popRight() + " ");
      System.out.println();


      for(int i = 0; i < 30 ; i++){
         deque.pushLeft(i);
         deque.pushRight(i*2);
      }

      for(Integer a: deque)
         System.out.print(a + " ");
      System.out.println();

      int i =0 ;
      while(i < 25){
         System.out.print(deque.popRight()+ " ");
         i++;
      }
      System.out.println();

      i =0 ;
      while(i < 25){
         System.out.print(deque.popLeft() + " ");
         i++;
      }
      System.out.println();

      for(Integer f : deque)
         System.out.print(f + " ");
      System.out.println();

      while(!deque.isEmpty()) {
         System.out.print(deque.popLeft() + " ");
      }
      System.out.println();

      System.out.println(deque.isEmpty());

      for(int j = 0; j < 30 ; j++){
         deque.pushLeft(j);
         deque.pushRight(j*2);
      }

      for(Integer f: deque)
         System.out.print(f + " ");
      System.out.println();

   }

   @SuppressWarnings("unchecked")
   ResizingArrayDeque(){
      array = (Item[]) new Object[11]; //odd length is more appropriate for this data structure
      rightIndex = array.length/2;
      leftIndex = rightIndex;
   }

   boolean isEmpty(){
      return N == 0;
   }

   int size(){
      return N;
   }

   void pushLeft(Item item){
      if(leftIndex == -1) {
         expand();
      }

      array[leftIndex] = item;
      if(leftIndex == rightIndex)
         rightIndex++;
      leftIndex--;
      N++;
   }

   void pushRight(Item item){
      if(rightIndex == array.length) {
         expand();
      }

      array[rightIndex] = item;
      if(leftIndex == rightIndex)
         leftIndex--;
      rightIndex++;
      N++;
   }

   Item popLeft(){
      if(N == 0)
         throw new ArrayIndexOutOfBoundsException();

      leftIndex++;
      Item item = array[leftIndex];
      array[leftIndex] = null;

      if(array.length/4 > N && N > 11)
         shrink();

      N--;
      return item;
   }

   Item popRight(){
      if(N == 0)
         throw new ArrayIndexOutOfBoundsException();

      rightIndex--;
      Item item = array[rightIndex];
      array[rightIndex] = null;

      if(array.length/4 > N && N > 11)
         shrink();

      N--;
      return item;
   }

   //doubles the size
   private void expand(){
      @SuppressWarnings("unchecked")
      Item[] newArray = (Item[]) new Object[array.length*2];

      int newLeftIndex = newArray.length/2 - N/2;
      int newRightIndex = newArray.length/2 + N/2;

      if(array.length == rightIndex)
         newRightIndex++;

      leftIndex++;

      for(int i = 0; i < N ; i++)
         newArray[newLeftIndex + i] = array[leftIndex + i];

      array = newArray;
      leftIndex = newLeftIndex -1;
      rightIndex = newRightIndex ;
   }

   private void shrink(){
      @SuppressWarnings("unchecked")
      Item[] newArray = (Item[]) new Object[array.length/2];

      int newLeftIndex = newArray.length/2 - N/2;
      int newRightIndex = newArray.length/2 + N/2;

      leftIndex++; //to be able to access the elements

      for(int i = 0; i < N; i++)
         newArray[newLeftIndex + i] = array[leftIndex + i];

      array = newArray;
      leftIndex = newLeftIndex -1;
      rightIndex = newRightIndex ;
   }


   public Iterator<Item> iterator() {
      return new ResizingArrayDequeIterator();
   }

   private class ResizingArrayDequeIterator implements Iterator<Item>{
      int currentIndex = leftIndex +1;

      public boolean hasNext() {
         return currentIndex != rightIndex;
      }

      public Item next() {
         return array[currentIndex++];
      }
   }
}
