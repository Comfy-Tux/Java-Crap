
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.*;
import aed.collections.*;

public class Lab3 {
   public static void main(String[] args){
      var list = new LinkedList<Double>();
      list.add(1.0);
      list.add(2.0);
      list.add(3.0);
      list.add(4.0);
      list.add(5.0);

      list.add(new Double[]{1.0,2.0,3.0,4.0,5.0,6.0,7.0,7.0,8.0,9.0,1321.0,31.0,32311.0,23.0,1321.0,321.0,321.0,312.0});

      //list.reverse();

      System.out.println(list);

      var genericList = ListFilter(list,x -> x > 5);

      System.out.println(genericList);

      System.out.println(map(list,x -> x + 10000));

      System.out.println(any(list,x -> x == 9));

      System.out.println(accumulate(map(list, x -> x/100), (x,y) -> x*y));

   }

   public static <T> IList<T> ListFilter(IList<T> list, Predicate<T> function){
      IList<T> result = new LinkedList<T>();
      for(T t : list)
         if(function.test(t))
            result.add(t);
      return result;
   }

   public static <T> IList<T> map(IList<T> list, Function<T,T> function){
      IList<T> result = new LinkedList<T>();
      for(T t : list)
         result.add(function.apply(t));
      return result;
   }

   public static <T> boolean any(IList<T> list,Predicate<T> function){
      for(T t: list)
         if(function.test(t))
            return true;
      return false;
   }

   public static <T> T accumulate(IList<T> list, BinaryOperator<T> function){
      T result = null;
      if(!list.isEmpty()) {
         Iterator<T> iterator = list.iterator();
         result = iterator.next();;

         while(iterator.hasNext()){
          result = function.apply(result, iterator.next());
         }
      }
      return result;
   }

   public static class LinkedList<Item> implements IList<Item>{
      private Node head;
      private Node tail;
      private int N;

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


      public IList<Item> shallowCopy() {
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
   }
}
