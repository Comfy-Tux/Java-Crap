//Implements the queue API
/*
FIFO Queue

public class QueueCircularLinkedList<Item> implements Iterable<Item>
             QueueCircularLinkedList()        creates a empty queue
        void enqueue(Item item)               add an Item
        Item dequeue()                        remove the least recently added Item
     boolean isEmpty()                        is the queue empty
         int size()                           number of items in the queue
*/

import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.*;


public class QueueCircularLinkedList<Item> implements Iterable<Item>{
   private Node last;
   private int N;


   public static void main(String[] args){
      var list = new QueueCircularLinkedList<Integer>();

      int i = 0;
      while(i < 10)
      list.enqueue(i++);

      for(int a : list)
         System.out.println(a);
   }

   QueueCircularLinkedList(){}

   void enqueue(Item item){
      if(last == null){
         last = new Node(item);
         last.next = last;
      }
      else {
         last.next = new Node(item, last.next);
         last = last.next;
      }
      N++;
   }

   Item dequeue(){
      N--;
      if(last == null)
         throw new ArrayIndexOutOfBoundsException();

      if(last.next == last){
         Item item = last.item;
         last = null;
         return item;
      }

      Item item = last.next.item;
      last.next = last.next.next;

      return item;
   }

   boolean isEmpty(){
      return last == null;
   }

   int size(){
      return N;
   }

   public Iterator<Item> iterator(){
      return new CircularListIterator();
   }

   private class Node{
      Item item;
      Node next;

      Node(Item item){this.item = item;}
      Node(Item item,Node next){this.item = item ; this.next = next;}
   }

   private class CircularListIterator implements Iterator<Item>{
      Node current = last.next;
      Node head = last.next;

      CircularListIterator(){last.next = null;}

      public boolean hasNext() {
         if(current != null)
            return true;
         else {
            last.next = head;
            return false;
         }
      }

      public Item next() {
         Item item = current.item;
         current = current.next;
         return item;
      }

      public void delete(){}
   }
}
