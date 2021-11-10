//Api
/*
public class ListingFiles
             ListingFiles(String s)      creates a queue with the files listed
             void printFiles()           prints the files listed from the s directory .
 */


import java.io.*;
import java.util.*;

public class ListingFiles {
   String filename ;
   Queue<String> list;
   String indent = "   ";
   int N = 0;

   // isDirectory , checks it the current filename is a directory.
   // getName , returns you the name , without the path file .
   // listFiles , returns you the files inside the directory .

   public static void main(String[] args){
      //var treeDirectory = new ListingFiles("/home/comfypad/Java_Programming/Java-Crap");
      var file = new ListingFiles("/home/comfypad/Java-Crap");
      file.printFiles();
   }

   ListingFiles(String filename){
      list = new Queue<String>();
      this.filename = filename;
      var file = new File(filename);
      list.enqueue(file.getName()+ "/");
      makeTreeListQueue(file);
   }



   public void printFiles(){
      for(String a: list)
         System.out.println(a);
   }

   private void makeTreeListQueue(File filename){
      N++;
      var files = filename.listFiles();
      for(File file: files){
         if(file.isDirectory()){
            list.enqueue(indentedFilename(file.getName()) + "/");
            makeTreeListQueue(file);
         }
         list.enqueue(indentedFilename(file.getName()));
      }
      N--;
   }

   private String indentedFilename(String s){
      var result = new StringBuilder();
      for(int i =0; i < N ; i++)
         result.append(indent);
      result.append(s);
      return result.toString();
   }


   private class Queue<Item> implements Iterable<Item>{
      Node head;
      Node tail;
      int N;

      public void enqueue(Item item){
         if(isEmpty())
         {
            head = new Node(item);
            tail = head;
         }
         else
         {
            Node newTail = new Node(item);
            tail.next = newTail;
            tail = newTail;
         }
         N++;
      }

      public Item dequeue(){
         if(isEmpty())
            throw new NoSuchElementException();

         Item item = head.item;
         head = head.next;
         N--;
         return item;
      }

      public boolean isEmpty(){
         return N == 0;
      }

      public int size(){
         return N;
      }

      public Iterator<Item> iterator(){
         return new ListingFilesIterator();
      }

      private class ListingFilesIterator implements Iterator<Item>{
         Node current = head;

         public boolean hasNext() {
            return  current != null;
         }

      public Item next() {
         Item item = current.item;
         current = current.next;
         return item;
      }

      public void delete(){}
   }

      private class Node{
         Item item;
         Node next;

         Node(){}
         Node(Item item){this.item = item;}
         Node(Item item,Node next){this.item = item ; this.next = next;}
      }
   }

}
