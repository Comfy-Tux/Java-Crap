import java.util.Arrays;

public class MaxPQ<Key extends Comparable<Key>> extends Sort {
   //binary heap implementation
   private Key[] pq;
   private int N = 0;
   private int maxN = 11;

   public static void main(String[] args) {
      int N = 21;
      var a = new Integer[N];
      for(int i = 0; i < N ;i++)
         a[i] = i;
      shuffle(a);
      System.out.println("unordered array");
      System.out.println(Arrays.toString(a));

      var pq = new MaxPQ<Integer>(10);
      for(Integer t: a)
         pq.insert(t);

      System.out.println("pq");
      System.out.println(Arrays.toString(pq.getArray()));
      System.out.println(pq.delMax());
      System.out.println(Arrays.toString(pq.getArray()));
      System.out.println(pq.delMax());
      System.out.println(Arrays.toString(pq.getArray()));

      for(int i = 0; i < 15; i++)
         pq.delMax();
      System.out.println(Arrays.toString(pq.getArray()));
   }

   @SuppressWarnings("unchecked")
   public MaxPQ(int maxN) {
      pq = (Key[]) new Comparable[maxN + 1];
      this.maxN = maxN +1;
   }

   @SuppressWarnings("unchecked")
   public MaxPQ(){
      pq = (Key[]) new Comparable[maxN + 1];
   }

   public boolean isEmpty() {
      return N == 0;
   }

   public int size() {
      return N;
   }

   public void insert(Key v) {
      if(N == pq.length - 1)
         resize(pq.length * 2);
      pq[++N] = v;
      swim(N);
   }

   public Key delMax() {
      Key max = pq[1];     //retrieve max key form top.
      exchange(pq, 1, N--);  //exchange with last item.
      pq[N + 1] = null;      //avoid loitering.
      sink(1);             //restore heap property.
      if(pq.length > maxN && N < pq.length/4)
         resize(pq.length / 2);
      return max;
   }

   private void swim(int k) {
      while(k > 1 && less(pq[k / 2], pq[k])) {
         exchange(pq, k / 2, k);
         k = k / 2;
      }
   }

   private void sink(int k) {
      while(k * 2 <= N) {
         int j = 2 * k;
         if(j < N && less(pq[j], pq[j + 1])) j++;
         if(!less(pq[k], pq[j])) break;
         exchange(pq, k, j);
         k = j;
      }
   }


   private void resize(int n) {
      @SuppressWarnings("unchecked")
      Key[] resizedPQ = (Key[]) new Comparable[n];
      int length = N +1;
      for(int i =1; i < length; i++)
         resizedPQ[i] = pq[i];
      pq = resizedPQ;
   }

   /////////////////////////////////////////////////// debugging methods ////////////////////////////////////////////
   public Key[] getArray() {
      return pq;
   }
}