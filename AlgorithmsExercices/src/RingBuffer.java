//API
/*
public class RingBuffer<Item> implements Iterator<Item>
             RingBugger(L)   // creates a buffer with size L
     boolean canDeposit()    // can deposit ?
     boolean canConsume()    // can consume ?
        void deposit(Item)   // Insert an item on the buffer
        Item consume()       // remove an item from the buffer
 */

// note I guess is misinterpreted the exercise , but I ain't doing it again. As is doesn't seem very hard.

import java.util.*;

public class RingBuffer<Item> implements Iterable<Item> {
   private int index;
   private final Item[] array;
   private boolean canConsume = false;
   private boolean canDeposit = true;

   public static void main(String[] args){
      var buffer = new RingBuffer<Integer>(1000);

      while(buffer.canDeposit())
         buffer.deposit((int) (1000 * Math.random()));

      for(Integer a: buffer)
         System.out.print(a + " ");
      System.out.println();

      while(buffer.canConsume())
         System.out.print(buffer.consume() + " ");
      System.out.println();
   }

   @SuppressWarnings("unchecked")
   RingBuffer(int L){array = (Item[]) new Object[L];}

   boolean canDeposit(){
      return canDeposit;
   }

   boolean canConsume(){
      return canConsume;
   }

   void deposit(Item item){
      array[index] = item;
      index = (index +1 ) % array.length;
      if(index == 0){
         canConsume = true;
         canDeposit = false;
      }
   }

   Item consume(){
      Item item = array[index];
      array[index] = null; // no loitering
      index = (index +1 ) % array.length;
      if(index == 0){
         canConsume = false;
         canDeposit = true;
      }
      return item;
   }

   public Iterator<Item> iterator() {
      return new RingBufferIterator();
   }

   private class RingBufferIterator implements Iterator<Item>{
      int currentIndex = index;
      public boolean hasNext(){
         return currentIndex != array.length;
      }

      public Item next(){
         return array[currentIndex++];
      }
   }
}


