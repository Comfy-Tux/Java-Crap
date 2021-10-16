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

public class LinkedListDeque<Item> implements Iterable<Item> {
   int N;
   Node head;
   Node tail;

   public static void main(String[] args) {

      var deque = new LinkedListDeque<Integer>();

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

   boolean isEmpty(){
      return head == null;
   }

   int size(){
      return N;
   }

   void pushLeft(Item item){
      if(head == null){
         head = new Node(item);
         head.previous = null;
         head.next = null;
         tail = head;
      }
      else {
         head = new Node(item, null, head);
         head.next.previous = head;
      }
      N++;
   }

   void pushRight(Item item){
      if(tail == null){
         tail = new Node(item);
         tail.previous = null;
         tail.next = null;
         head = tail;
      }
      else {
         tail = new Node(item, tail, null);
         tail.previous.next = tail;
      }
      N++;
   }

   Item popLeft(){
      if(head == null)
         throw new ArrayIndexOutOfBoundsException();

      Item item = head.item;
      head = head.next;
      if(head != null)
         head.previous = null;
      else
         tail = null;
      N--;
      return item;
   }

   Item popRight(){
      if(head == null)
         throw new ArrayIndexOutOfBoundsException();

      Item item = tail.item;
      tail = tail.previous;
      if(tail != null)
         tail.next = null;
      else
         head = null;
      N--;
      return item;
   }

   private class Node{
      Item item;
      Node next;
      Node previous;
      Node(Item item){this.item = item;}
      Node(Item item,Node previous,Node next){this.item = item;this.previous = previous; this.next = next;}
   }

   public Iterator<Item> iterator(){
      return new dequeIterator();
   }

   private class dequeIterator implements Iterator<Item>{
      Node current;

      dequeIterator(){current = head;}

      public boolean hasNext(){
         return current != null;
      }

      public Item next(){
         Item item = current.item;
         current = current.next;
         return item;
      }

      public void delete(){}
   }
}
