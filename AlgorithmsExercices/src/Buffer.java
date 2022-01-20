/*
API

public class Buffer
             Buffer()          create an empty buffer
        void insert(char c)    insert c at the cursor position
        char delete()          delete and return the character at the cursor
        void left(int k)       move the cursor k positions to the left
        void right(int k)      move the cursor k positions to the right
         int size()            number of character in the buffer
      String toString()        returns the string stored on the buffer.
 */

import java.util.*;

public class Buffer {
   private final Stack<Character>  write = new Stack<>();
   private final Stack<Character> buffer = new Stack<>();
   private int N;

   public static void main(String[] args){
      var buffer = new Buffer();

      String word = "I'm a buffer , hi , no don't overwrite me ";
      for(int i = 0; i < word.length() ; i++)
         buffer.insert(word.charAt(i));

      System.out.println(buffer.toString());

      buffer.left(25);

      System.out.println(buffer.toString());

      word = ", shut the fuck up you contrarian little bitch";

      for(int i = 0; i < word.length() ; i++)
         buffer.insert(word.charAt(i));

      System.out.println(buffer.toString());

      buffer.right(25);

      word = "Oni-Chan OwO";

      for(int i = 0; i < word.length() ; i++)
         buffer.insert(word.charAt(i));

      System.out.println(buffer.toString());

      buffer.left(83);

      buffer.delete();
      buffer.delete();
      buffer.delete();
      buffer.delete();

      System.out.println(buffer.toString());
   }


   public void insert(char c){
      write.push(c);
      N++;
   }

   public char delete(){
      if(write.isEmpty())
         throw new NoSuchElementException();
      N--;
      return write.pop();
   }

   public void left(int k){
      for(int i = 0 ; i < k ; i++)
         buffer.push(write.pop());
   }

   public void right(int k){
      for(int i =0; i< k; i++)
         write.push(buffer.pop());
   }

   public int size(){
      return N;
   }

   public String toString(){
      var tempStack = new Stack<Character>();
      var temp2Stack = new Stack<Character>();

      for(char c : buffer)
         temp2Stack.push(c);

      for(char c : temp2Stack)
         tempStack.push(c);

      for(char c : write)
         tempStack.push(c);

      var result = new StringBuilder();
      for(char c : tempStack)
         result.append(c);

      return result.toString();
   }


   private class Stack<Item> implements Iterable<Item>{
      Node head;
      int N;

      public void push(Item item){
         if(isEmpty())
            head = new Node(item);
         else
            head = new Node(item,head);
         N++;
      }

      public Item pop(){
         Item item = head.item;
         head = head.next;
         N--;
         return item;
      }

      public Item peek(){
         return head.item;
      }

      public boolean isEmpty(){
         return head == null;
      }

      private class Node{
         Item item;
         Node next;

         Node(){}
         Node(Item item){this.item = item;}
         Node(Item item,Node next){this.item = item ; this.next = next;}
      }

      public Iterator<Item> iterator(){
         return new StackIterator();
      }

      private class StackIterator implements Iterator<Item>{
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
}
