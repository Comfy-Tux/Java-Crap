import java.util.*;

public class LinkedList<Item extends Comparable<Item>> implements Iterable<Item>{
   private Node head;
   private Node tail;
   private int N;

   public static void main(String[] args){
      var list = new LinkedList<Double>();
      int N = 100;
      for(int i = 0;i < N ; i++)
         list.add(Math.random());

      System.out.print("[");
      for(Double a: list)
         System.out.printf(",%.3f",a);
      System.out.println("]");
      list.sort();
      System.out.print("[");
      for(Double a: list)
         System.out.printf(",%.3f",a);
      System.out.println("]");
      System.out.println(list.isSorted());
   }

   public int size() {
      return N;
   }

   public Item get(int index) {
      Node current = head;
      for(int i = 0; i < index;i++)
         current = current.next;
      return current.item;
      }

   public void set(int index, Item element) {
      Node current = head;
      for(int i = 0; i < index;i++)
         current = current.next;
      current.item = element;
      }


   public LinkedList<Item> shallowCopy() {
      var result = new LinkedList<Item>();
         result.head = new Node(head.item,head.next);
      return result;
   }


   public void add(Item item) {
      if(head == null){
            head = new Node(item);
         tail = head;
      }
      else{
         Node oldTail = tail;
         tail = new Node(item);
         oldTail.next = tail;
      }
      N++;
      }

   public void add(Item[] array){
      for(int i =0 ; i < array.length ; i++)
         add(array[i]);
      N += array.length;
   }

   public void addAt(int index, Item item) {
      if(index > N)
         throw new ArrayIndexOutOfBoundsException();
      Node current = head;
         int N = index -1; // before the index
      for(int i = 0; i < N ; i++){
         current = current.next;
      }
      Node newNode = new Node(item,current.next);
      current.next = newNode;
   }

   public Item remove() {
      if(tail == null)
            throw new NoSuchElementException();

      Item item = tail.item;

      Node current = head;
      if(head == tail){
         head = null;
         tail = null;
      }
      else {
         while(current.next != null && current.next != tail)
            current = current.next;
         tail = current;
            current.next = null;
      }
      N--;
      return item;
   }

   public Item remove(int index) {
      if(index > N)
         throw new ArrayIndexOutOfBoundsException();
      Node current = head;
      int N = index -1; // before the index
         for(int i = 0; i < N ; i++){
            current = current.next;
         }
      Item item = current.next.item;
      current.next = current.next.next;
      return item;
   }

   public boolean isEmpty() {
      return N == 0;
   }

   public Iterator<Item> iterator() {
         return new LinkedListIterator() ;
   }


   public void reverse(){
      if(N < 1)
         return;
      Node previous = head;
      Node current = head.next;
      Node next = current.next;

      head.next = null;

         while(current != null){
            next = current.next;
            current.next = previous;
            previous = current;
            current = next;
         }

      tail = head;
      head = previous;
   }

   public String toString(){
      Node current = head.next;
      var result = new StringBuilder("[");
      if(head != null)
         result.append(head.item.toString());
      while(current != null){
         result.append(",");
         result.append(current.item.toString());
         current = current.next;
         }
      result.append("]");
      return result.toString();
   }


   private class Node{
      Item item;
      Node next;

      Node(){};
      Node(Item item){this.item = item;}
      Node(Item item,Node next){this.item = item; this.next = next;}
   }


      private class LinkedListIterator implements Iterator<Item>{
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
      //////////////////////////////////////////////////////////////////////////// sort

   private boolean less(Node a, Node b){
      if(b == null)
         return true;
      if(a == null)
         return false;
      return a.item.compareTo(b.item) < 0;
   }

   private void merge(Node before,Node lo,Node mid,Node hi){
      Node left = lo;
      Node right = mid.next;
      Node end = hi.next;
      mid.next = null;
      hi.next = null;

      //updates the head if mid.next or lo is the head
      if(left == head || right == head)
         if(less(left, right)) {
            head = before = left;
            left = left.next;
         }
         else {
            head = before = right;
            right = right.next;
         }

      //updates the tail if mid or hi is the tail
      if(mid == tail || hi == tail)
         if(less(mid,hi))
            tail = hi;
         else
            tail = mid;

      while(left != null || right != null){
         if(left == mid.next) {
            before.next = right;
            before = right;
            right = right.next;
         }
         else if(right == hi.next){
            before.next = left;
            before = left;
            left = left.next;
         }
         else if(less(left,right)){
            before.next = left;
            before = left;
            left = left.next;
         }
         else{
            before.next = right;
            before = right;
            right = right.next;
         }
      }
      before.next = end;
   }


   public void sort(){
      Node lo = head;
      Node mid = head;
      Node hi = head;
      Node before = head;
      while(true){
         if(lo == null)
            lo = head;
         mid = findNode(lo);
         if(mid == tail)
            if(lo == head)
               break;
            else{
               lo = head;
               before = head;
               continue;
            }

         hi = findNode(mid.next);
         merge(before,lo,mid,hi);
         before = hi;
         lo = hi.next;
      }
   }

   //returns the node before an unordered node , starting from the start node
   private Node findNode(Node start){
      Node current = start;
      while(current.next != null && less(current,current.next))
         current = current.next;
      return current;
   }

   private boolean isSorted(){
      if(N < 2)
         return true;
      Node current = head;
      while(current.next != null) {
         if(less(current.next, current))
            return false;
         current = current.next;
      }
      return true;
   }


}
