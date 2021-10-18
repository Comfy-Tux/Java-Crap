//API
/*
public class MoveToFront<Item> implements Iterable<Item>
             MoveToFront()      creates an empty linked list
        void insert(Item item)  inserts at the beginning , if the item is duplicate them it's deleted and inserted at the beginning
     boolean isKey(Item key)    searches for the key on the list , return true if found , false otherwise
      String toString()         returns the list on an array;
 */

import java.util.*;

public class MoveToFront<Item> implements Iterable<Item>{
   private Node head;
   private Node tail;

   public static void main(String[] args){
      var in = new Scanner(System.in);

      var list = new MoveToFront<String>();

      while(in.hasNext()){
         String read = in.next();
         System.out.println(list.isKey(read));
         list.insert(read);
         System.out.println(list);
      }
   }

   public void insert(Item key){
      Node current = head;
      if(head == null) {
         head = new Node(key);
         return;
      }

      if(head.item.equals(key))
         return;

      while(current.next != null && !current.next.item.equals(key))
         current = current.next;

      if(current.next != null)
      current.next = current.next.next;

      head = new Node(key,head);
   }

   public boolean isKey(Item key){
      Node current = head;
      while(current != null) {
         if(current.item.equals(key))
            return true;
         current = current.next;
      }
      return false;
   }

   public String toString(){
      Node current = head;
      StringBuilder result = new StringBuilder("[");
      if(head != null) {
         result.append(current.item.toString());
         current = current.next;
      }

      while(current != null){
         result.append(",").append(current.item.toString());
         current = current.next;
      }
      result.append("]");
      return result.toString();
   }


   public Iterator<Item> iterator() {
      return new MoveToFrontIterator();
   }

   private class Node{
      Item item;
      Node next;

      Node(){}
      Node(Item item){this.item = item;}
      Node(Item item,Node next){this.item = item ; this.next = next;}
   }

   private class MoveToFrontIterator implements Iterator<Item>{
      Node current = head;
      public boolean hasNext(){
         return current != null;
      }

      public Item next(){
         Item item = current.item;
         current = current.next;
         return item;
      }
   }
}
