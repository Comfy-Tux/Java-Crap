package aed.tables;

import java.util.*;

public class Treap<Key extends Comparable<Key>,Value> {
   Node root = null;
   Random r;

   public static void main(String[] args){
      bunchOfTests();

      final int LENGTH = 20000000;
      final int N = 1000000;

      var array1 = orderedArray(LENGTH);

      //////////////////////////////////////// get orderedArray ////////////////////////////////////////


      //log(N)
      var half = DoublingRatio.timeTrialGet(fillTreap(array1,1000),1000,N);

      System.out.println("\n ordered get doubling test \n");
      for(int i = 2000; i < LENGTH ; i+= i) {
         var time = DoublingRatio.timeTrialGet(fillTreap(array1, i),i,N);
         var getOrderedTime = new TimeAndRatio(time,time/half);
         half = time;

         System.out.println("An getOrdered for " + i +  "  is O(" + bigO(getOrderedTime.ratio) + ") worst case , real ratio = " + getOrderedTime.ratio +
                 " and it took " + getOrderedTime.time + " seconds");

      }

      //////////////////////////////////////// get unorderedArray ////////////////////////////////////////

       array1 = unorderedArray(LENGTH);

      //log(N)
       half = DoublingRatio.timeTrialGet(fillTreap(array1,1000),1000,N);

      System.out.println("\n unordered get doubling test \n");
      for(int i = 2000; i < LENGTH ; i+= i) {
         var time = DoublingRatio.timeTrialGet(fillTreap(array1, i),i,N);
         var getUnorderedTime = new TimeAndRatio(time,time/half);
         half = time;

         System.out.println("A getUnordered for " + i +  "  is O(" + bigO(getUnorderedTime.ratio) + ") worst case , real ratio = " + getUnorderedTime.ratio +
                 " and it took " + getUnorderedTime.time + " seconds");
      }

      //////////////////////////////////////// put ////////////////////////////////////////////

      array1 = orderedArray(LENGTH);
      //log(N)
      half = DoublingRatio.timeTrialPut(fillTreap(array1,1000),1000,N);

      System.out.println("\n put doubling test \n");
      for(int i = 2000; i < LENGTH ; i+= i) {
         var time = DoublingRatio.timeTrialDelete(fillTreap(array1, i),i,N);
         var deleteTime = new TimeAndRatio(time,time/half);
         half = time;

         System.out.println("A put for " + i +  "  is O(" + bigO(deleteTime.ratio) + ") worst case , real ratio = " + deleteTime.ratio +
                 " and it took " + deleteTime.time + " seconds");
      }

      //////////////////////////////////////// delete ////////////////////////////////////////////

      //log(N)
      half = DoublingRatio.timeTrialPut(fillTreap(array1,1000),1000,N);

      System.out.println("\n put doubling test \n");
      for(int i = 2000; i < LENGTH ; i+= i) {
         var time = DoublingRatio.timeTrialPut(fillTreap(array1, i),i,N);
         var putTime = new TimeAndRatio(time,time/half);
         half = time;

         System.out.println("A delete for " + i +  "  is O(" + bigO(putTime.ratio) + ") worst case , real ratio = " + putTime.ratio +
                 " and it took " + putTime.time + " seconds");
      }

   }

   private static Treap<Integer,Integer> fillTreap(Integer[] a, int n){
      var treap = new Treap<Integer,Integer>();
      for(int i = 0; i < n; i++)
         treap.put(i,0);
      return treap;
   }

   //if you want to use a different organization than a set of nodes with pointers, you can do it, but you will have to change
   //the implementation of the getHeapArray method, and eventually the printing method (so that is prints a "node" in the same way)
   private class Node {
      private Key key;
      private int priority;
      private Value value;
      private Node left;
      private Node right;
      private int size;

      //you can add additional properties and methods, as long as you maintain this constructor and the toString method with the same semantic
      public Node(Key k, Value v, int size, int priority) {
         this.key = k;
         this.value = v;
         this.size = size;
         this.priority = priority;
      }

      public String toString() {
         return "[k:" + this.key + " v:" + this.value + " p:" + this.priority + " s:" + this.size + "]";
      }

      public Node shallowCopy(){
         return new Node(key,value,size,priority);
      }
   }


   public Treap() {this.r = new Random(100);}

   public Treap(Random r)  { this.r = r; }

   public int size()  {
      if(root == null)
         return 0;
      return root.size;
   }

   public Value get(Key k)  { return get(this.root,k); }

   //get helper method
   private Value get(Node n,Key k){
      if(n == null) return null;
      int cmp = k.compareTo(n.key);
      if(cmp < 0) return get(n.left,k);
      else if(cmp > 0) return get(n.right,k);
      else return n.value;
   }


   private Node getNode(Node n,Key k){
      if(n == null) return null;
      int cmp = k.compareTo(n.key);
      if(cmp < 0) return getNode(n.left,k);
      else if(cmp > 0) return getNode(n.right,k);
      else return n;
   }

  public boolean containsKey(Key k){ return get(k) != null; }

   public void put(Key k, Value v) {
      if(v == null) { delete(k); return; }
      //inserts the node with binary search tree according to its keys and swims
       root = put(root,k,v);
   }

   //insert the key and the value respecting to the binary search tree structure and then on the comeback
   //from the recursion it places the key with respect to the heap structure.
   private Node put(Node n,Key k,Value v){
      if(n == null) return new Node(k,v,1,r.nextInt());

      int cmp = k.compareTo(n.key);
      if(cmp < 0) n.left = put(n.left,k,v);
      else if(cmp > 0) n.right = put(n.right,k,v);
      else n.value = v;

      n.size = size(n);
      return rotate(n);
   }

   private Node splitPut(Node n,Key k){
      if(n == null) return new Node(k,root.value,1,Integer.MAX_VALUE);

      int cmp = k.compareTo(n.key);
      if(cmp < 0) n.left = splitPut(n.left,k);
      else if(cmp > 0) n.right = splitPut(n.right,k);
      else n.priority = Integer.MAX_VALUE;

      n.size = size(n);
      return rotate(n);
   }


   //ok method it could be more efficient for the case were k doesn't exist.
   public void delete(Key k) {
      Node n = getNode(root,k);
      if(n == null) return; //the key doesn't exist

      n.priority = Integer.MIN_VALUE;//sets the node with key k , with the lowest priority possible.

      root = delete(root,k);
   }


   //sinks the node and deletes it when it becomes a leaf .
   private Node delete(Node n,Key k){
      if(n.size ==1) return null;
      int cmp = k.compareTo(n.key);

      if(cmp == 0){
         n = rotate(n);
         cmp = k.compareTo(n.key);
      }

      if(cmp < 0) n.left = delete(n.left,k);
      else if(cmp > 0) n.right = delete(n.right, k);
      n.size = size(n);
      return n;
   }

   @SuppressWarnings("rawtypes")
   public Treap[] split(Key k)
   {


      root = splitPut(root,k); //the value doesn't matter we are just using a not that will achieve root status
      //so that we can split the treap we discard node k to achieve this.

      @SuppressWarnings("unchecked")
      Treap<Key,Value>[] twoTreap = new Treap[2];

      twoTreap[0] = new Treap<>(r);
      twoTreap[0].root = root.left;
      twoTreap[1] = new Treap<>(r);
      twoTreap[1].root = root.right;

      return twoTreap;
   }

   public Key min()
   {
      Node n = root;
      if(n == null)
         return null;
      while(n.left != null)
         n = n.left;
      return n.key;
   }

   public Key max()
   {
      Node n = root;
      if(n == null)
         return null;
      while(n.right != null)
         n = n.right;
      return n.key;
   }

   public void deleteMin() {
      if(root == null) return;
      if(root.left == null) {
         delete(root.key);
         return;
      }

      Node n = root;
      while(n.left.left != null){
         n.size--;
         n = n.left;
      }
      n.left = n.left.right;
      n.size--;
   }

   public void deleteMax(){
      if(root == null) return;
      if(root.right == null) {
         delete(root.key);
         return;
      }

      Node n = root;
      while(n.right.right != null){
         n.size--;
         n = n.right;
      }
      n.right = n.right.left;
      n.size--;
   }

   public int rank(Key k){ return rank(root,k); }

   //helper method
   private int rank(Node n, Key k){
      if(n == null) return 0;
      int cmp = k.compareTo(n.key);
      if(cmp < 0) return rank(n.left,k);
      else if(cmp > 0) return size(n.left) +1 + rank(n.right,k);
      else return size(n.left) ;
   }


   public int size(Key min, Key max)
   {
      if(min.compareTo(max) > 0) return 0;
      return rank(max) - rank(min) + ((get(max) != null) ? 1: 0);
   }
////
   //this is the inverse of the rank function to some extent
   public Key select(int n) { return select(root,n); }

   private Key select(Node n,int k){
      if(n == null) return null;
      int subRank = size(n.left);
      int cmp = k - subRank;
      if(cmp > 0) return select(n.right,k - subRank -1);
      else if(cmp < 0) return select(n.left, k);
      return n.key;
   }

   public Iterable<Key> keys()  { return new KeyIterable(); }

   public Iterable<Value> values(){ return new ValueIterable(); }

   public Iterable<Integer> priorities(){ return new PriorityIterable();}

   public Iterable<Key> keys(Key min, Key max) { return new KeyIterable(min,max); }

   public Treap<Key,Value> shallowCopy()
   {
      var copy = new Treap<Key,Value>();
      copy.root = treapShallowCopy(root);
      return copy;
   }

   //helper method
   private Node treapShallowCopy(Node n){
      if(n == null) return null;
      Node copy = n.shallowCopy();
      copy.left = treapShallowCopy(n.left);
      copy.right = treapShallowCopy(n.right);
      return copy;
   }


   //////////////////// ITERATOR IMPLEMENTATIONS //////////////////////////////////////

   ///////////////////////// KEYS ITERATOR /////////////////////////////////////////////

   private LinkedList<Node> treapToLinkedList(Key min,Key max){
      if(size() == 0)
         return new LinkedList<Node>();
      Node n = root;
      while(n.right != null && min.compareTo(n.key) > 0)
         n = n.right;

      return treapToLinkedList(new LinkedList<Node>(),n,min,max);
   }

   private LinkedList<Node> treapToLinkedList(LinkedList<Node> linkedList,Node n,Key min,Key max){
      if(n == null ) return linkedList;
      if( min.compareTo(n.key) > 0)
         return treapToLinkedList(linkedList,n.right,min,max);
      if(n.left != null) treapToLinkedList(linkedList,n.left,min,max);
      if(max.compareTo(n.key) >= 0){
         linkedList.add(n);
         treapToLinkedList(linkedList,n.right,min,max);
      }
      return linkedList;
   }

   private class KeyIterable implements Iterable<Key>{
      final private Iterator<Key> iteratorObj;

      KeyIterable(){ iteratorObj = new KeyIterator(); }

      KeyIterable(Key min,Key max){ iteratorObj = new KeyIterator(min,max);}

      public Iterator<Key> iterator() {
         return iteratorObj;
      }
   }

   private class KeyIterator implements Iterator<Key>{
      Iterator<Treap<Key,Value>.Node> iterator;

      KeyIterator(){
         iterator = treapToLinkedList(min(),max()).iterator();
      }

      KeyIterator(Key min,Key max){
          iterator = treapToLinkedList(min,max).iterator();
      }

      public boolean hasNext() {return  iterator.hasNext();}

      public Key next() { return iterator.next().key; }
   }

   /////////////////////////// Value Iterator ////////////////////////////////////////////

   private class ValueIterable implements Iterable<Value>{
      public  Iterator<Value> iterator(){ return new ValueIterator(); }
   }

   private class ValueIterator implements Iterator<Value> {
      Iterator<Treap<Key,Value>.Node> iterator;

      ValueIterator(){iterator = treapToLinkedList(min(),max()).iterator();}

      public boolean hasNext() {return  iterator.hasNext();}

      public Value next() { return iterator.next().value; }
   }

   /////////////////////////// Priority Iterator /////////////////////////////////////////

   private class PriorityIterable implements Iterable<Integer>{
      public  Iterator<Integer> iterator(){ return new PriorityIterator(); }
   }

   private class PriorityIterator implements Iterator<Integer> {
      Iterator<Treap<Key,Value>.Node> iterator;

      PriorityIterator(){iterator = treapToLinkedList(min(),max()).iterator();}

      public boolean hasNext() {return  iterator.hasNext();}

      public Integer next() { return iterator.next().priority; }
   }

   /////////////////////////// END OF ITERATORS //////////////////////////////////////////


   ///////////////////////// helperMethods ///////////////////////////////////////////////

   //returns current node n size , its main use it's for updates to size when it's sons exchange.
   //checks for boundaries , if boundary checking is not needed it's inefficient.
   private int size(Node n){
      if(n == null) return 0;
      int rightSize = (n.right != null) ? n.right.size : 0;
      int leftSize = (n.left != null) ? n.left.size : 0;
      return rightSize + leftSize + 1;
   }


   //returns the new father
   private Node rotate(Node n){
      if(n.size == 1) //no child to rotate with
         return n;

      if(n.left == null || n.right == null) {
         if(n.left == null && n.priority < n.right.priority)
            return rotateLeft(n);
         else if(n.right == null && n.priority < n.left.priority)
            return rotateRight(n);
      }
      else
      {
         // if both aren't null
         //rotates with the child that has the highest priority , this comes handy for sink.
         if(n.right.priority > n.left.priority) {
            if(n.priority < n.right.priority)
               return rotateLeft(n);
         } else if(n.priority < n.left.priority)
            return rotateRight(n);
      }
      return n;
   }

   //father can't be null , doesn't make sense to rotate a null
   private Node rotateRight(Node father){
      Node newFather = father.left;
      father.left = newFather.right;
      newFather.right = father;

      //updates size
      father.size = size(father);
      newFather.size = size(newFather);

      return newFather;
   }

   //father can't be null , doesn't make sense to rotate a null
   private Node rotateLeft(Node father){
      Node newFather = father.right;
      father.right = newFather.left;
      newFather.left = father;

      //updates size
      father.size = size(father);
      newFather.size = size(newFather);

      return newFather;
   }


//////////////////////////////////// teacher stuff //////////////////////////////////////

   //helper method that uses the treap to build an array with a heap structure
   private void fillHeapArray(Node n, Object[] a, int position)
   {
      if(n == null) return;

      if(position < a.length)
      {
         a[position] = n;
         fillHeapArray(n.left,a,2*position);
         fillHeapArray(n.right,a,2*position+1);
      }
   }

   //if you want to use a different organization that a set of nodes with pointers, you can do it, but you will have to change
   //this method to be consistent with your implementation
   private Object[] getHeapArray()
   {
      //we only store the first 31 elements (position 0 is not used, so we need a size of 32), to print the first 5 rows of the treap
      Object[] a = new Object[32];
      fillHeapArray(this.root,a,1);
      return a;
   }

   private void printCentered(String line)
   {
      //assuming 120 characters width for a line
      int padding = (120 - line.length())/2;
      if(padding < 0) padding = 0;
      String paddingString = "";
      for(int i = 0; i < padding; i++)
      {
         paddingString +=" ";
      }

      System.out.println(paddingString + line);
   }

   //this is used by some of the automatic tests in Mooshak. It is used to print the first 5 levels of a Treap.
   //feel free to use it for your own tests
   public void printTreapBeginning() {
      Object[] heap = getHeapArray();
      int size = size(this.root);
      int printedNodes = 0;
      String nodeToPrint;
      int i = 1;
      int nodes;
      String line;

      //only prints the first five levels
      for (int depth = 0; depth < 5; depth++) {
         //number of nodes to print at a particular depth
         nodes = (int) Math.pow(2, depth);
         line = "";
         for (int j = 0; j < nodes && i < heap.length; j++) {
            if (heap[i] != null) {
               nodeToPrint = heap[i].toString();
               printedNodes++;
            } else {
               nodeToPrint = "[null]";
            }
            line += " " + nodeToPrint;
            i++;
         }

         printCentered(line);
         if (i >= heap.length || printedNodes >= size) break;
      }
   }

   /////////////////////////////// DOUBLING RATIO ////////////////////////////////////////////

   private static class Stopwatch {
      private final long start;

      public Stopwatch() {
         start = System.currentTimeMillis();
      }

      public double elapsedTime() {
         long now = System.currentTimeMillis();
         return (now - start) / 1000.0;
      }
   }

   private static class DoublingRatio{

      //N is the range that the key can be recovered , and M is the number of times we recover it .
      public static double timeTrialGet(Treap<Integer,Integer> treap,int N, int M){
         var r = new Random();
         var stopwatch = new Stopwatch();
         for(int i = 0; i < M; i++)
            treap.get(r.nextInt(N));
         return stopwatch.elapsedTime();
      }

      //N is range were we know that a key has been put , and M is the number of times we put it .
      public static double timeTrialPut(Treap<Integer,Integer> treap,int N,int M){
         var r = new Random();
         var stopwatch = new Stopwatch();
         for(int i = 0; i < M; i++)
            treap.put(N + r.nextInt(N),i);
         return stopwatch.elapsedTime();
      }

      //N is range in which we delete keys , and M is the number of times we delete it .
      public static double timeTrialDelete(Treap<Integer,Integer> treap,int N,int M){
         var r = new Random();
         var stopwatch = new Stopwatch();
         for(int i = 0; i < M; i++)
            treap.delete(r.nextInt(N));
         return stopwatch.elapsedTime();
      }
   }

   private interface ArrayGenerator{
      public  Integer[] generate(int N);
   }

   private static Integer[] orderedArray(int N){
      var a = new Integer[N];
      for(int i = 0; i < N; i++)
         a[i] = i;
      return a;
   }

   private static Integer[] unorderedArray(int N){
      var a = orderedArray(N);
      shuffle(a);
      return a;
   }

   private static<T extends Comparable<T>> void shuffle(T[] a){
      Random r = new Random();

      for(int i = a.length -1 ; i > 0; i--) {
         int j = r.nextInt(i + 1);
         exchange(a, i, j);
      }
   }

   protected static <T extends Comparable<T>> void exchange(T[] a, int i, int j)
   {
      T t = a[i];
      a[i] = a[j];
      a[j] = t;
   }


   private static String bigO(double ratio){
      int n = (int) Math.round(ratio);
      switch(n) {
         case 0 : return "1";
         case 1 : return "N";
         case 2 : return "N^2";
         case 3 : return "N^3";
         default : return "1";
      }
   }

   private static double bigOExponent(double time) {
      return Math.log(time) / Math.log(2);
   }

   private static class TimeAndRatio {
      double time;
      double ratio;

      TimeAndRatio(double time, double ratio) {
         this.time = time;
         this.ratio = bigOExponent(ratio);
      }
   }



   //////////////////////////////////////// MISCELLANEOUS STUFF ///////////////////////////////////

   private static void bunchOfTests(){
      var treap = new Treap<Integer,Integer>();

      treap.delete(1);
      treap.delete(100);
      treap.delete(-100);
      treap.delete(0);

      treap.put(1,4);
      treap.put(4,12);
      treap.put(5,15);
      treap.put(7,93);
      treap.put(3,44);
      treap.put(7,34);
      treap.put(2,123);

      var copy = treap.shallowCopy();

      System.out.println(" \n\t\t\t\t\t\t\t\t\t\t\t\t\t\t normal  \n");

      treap.printTreapBeginning();


      System.out.println("select = " + treap.select(0) );
      System.out.println("select = " + treap.select(1) );
      System.out.println("select = " + treap.select(2) );
      System.out.println("select = " + treap.select(3) );
      System.out.println("select = " + treap.select(4) );
      System.out.println("select = " + treap.select(5) );
      System.out.println("rank of 3 = " + treap.rank(3));
      System.out.println("min() = " + treap.min());
      System.out.println("max() = " + treap.max());
      System.out.println("size() = " + treap.size());
      System.out.println("size() = " + treap.size());
      System.out.println("size(2,4) = " + treap.size(2,4));

      System.out.print("Iterator treap.keys() = ");
      for(Integer p : treap.keys()){
         System.out.print(p + " ");
      }
      System.out.println();

      System.out.print("Iterator treap.keys(2,4) = ");
      for(Integer p : treap.keys(4,7)){
         System.out.print(p + " ");
      }
      System.out.println();

      System.out.print("Iterator treap.values = ");
      for(Integer p : treap.values()){
         System.out.print(p + " ");
      }
      System.out.println();

      System.out.print("Iterator treap.priorities = ");
      for(Integer p : treap.priorities()){
         System.out.print(p + " ");
      }


      System.out.println();

      //tests deleteMax and deleteMin and these delete functions are just a special case of delete it is also
      // a delete test
      treap.deleteMax();
 /*     treap.deleteMax();
      treap.deleteMin();
      treap.deleteMin();*/

      System.out.println(" \n\t\t\t\t\t\t\t\t\t\t\t\t after deleted elements  \n");


      treap.printTreapBeginning();

      System.out.println(" \n\t\t\t\t\t\t\t\t\t\t\t\t\t\t copy  \n");


      copy.printTreapBeginning();

      System.out.println();



      treap.delete(4);
      treap.delete(3);
      treap.delete(7);

      //refills again
      treap.put(1,4);
      treap.put(4,12);
      treap.put(5,15);
      treap.put(7,93);
      treap.put(3,44);
      treap.put(7,34);
      treap.put(2,123);



      System.out.println(" \n\t\t\t\t\t\t\t\t\t\t\t\t\t new normal  \n");

      treap.printTreapBeginning();

      var twoTreap = treap.split(10);

      var treapA = twoTreap[0];
      var treapB = twoTreap[1];

      System.out.println(" \n\t\t\t\t\t\t\t\t\t\t\t\t\t split part 1  \n");

      treapA.printTreapBeginning();

      System.out.println(" \n\t\t\t\t\t\t\t\t\t\t\t\t\t split part 2  \n");

      treapB.printTreapBeginning();

   }
}
