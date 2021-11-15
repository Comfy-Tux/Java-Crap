public class MaxPQ<Key extends Comparable<Key>> extends Sort{
   //binary heap implementation
   private Key[] pq;
   private int N = 0;

   @SuppressWarnings("unchecked")
   public MaxPQ(int maxN){
      pq = (Key[]) new Comparable[maxN+1];
   }

   public boolean isEmpty(){
      return N == 0;
   }

   public int size(){
      return N;
   }

   public void insert(Key v){
      pq[++N] = v;
      swim(N);
      if(N == pq.length -1)
         resize(pq.length *2);
   }

   public Key delMax(){
      Key max = pq[1];     //retrieve max key form top.
      exchange(pq,1,N--);  //exchange with last item.
      pq[N+1] = null;      //avoid loitering.
      sink(1);             //restore heap property.
      if(pq.length > 10 && N/4 < pq.length)
         resize(pq.length/2);
      return max;
   }

   private void swim(int k){
      while(k > 1 && less(k/2,k)){
         exchange(pq,k/2,k);
         k = k/2;
      }
   }

   private void sink(int k){
      while(k*2 <= N){
         int j = 2*k;
         if(j < N && less(pq[j],pq[j+1])) j++;
         if(!less(pq[k],pq[j])) break;
         exchange(pq,k,j);
         k = j;
      }
   }

   private void resize(int n){
      `


   }

}
