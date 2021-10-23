/*
API

public class UF
             UF(int N)                 initialize N sites with integer names (0 to N-1)
        void union(int p,int q)        add connection between p and q
         int find(int p)               component identifier for p (0 to N-1)
     boolean connected(int p,int q)    return true if p and q are in the same component
         int count()                   number of components
 */

import java.util.*;

//Union-find implementation with weighted-union and compression
public class UF {
   private int N;
   private final int[] array;
   private final int[] size;
   int height = 0;

   public static void main(String[] args){
      var in = new Scanner(System.in);

      var network = new UF(in.nextInt());

      while(in.hasNextInt())
         network.union(in.nextInt(),in.nextInt());

      System.out.println("Connection : " + network.count());
   }

   UF(int N){
      this.N = N;
      array = new int[N];
      size = new int[N];

      for(int i = 0; i < N; i++) {
         array[i] = i;
         size[i] = 1;
      }
   }

   public int find(int p){
      int root = p;
      while(root != array[root]) {
         root = array[root];
      }

      while(p != root){
         int nextP = array[p];
         array[p] = root;
         p = nextP;
      }

      return root;
   }

   boolean connected(int p,int q){
      return find(p) == find(q);
   }

   int count(){
      return N;
   }

   void union(int p,int q){
      int i = find(p);
      int j = find(q);

      if(i == j)
         return;
      if(size[i] < size[j])
      {
         array[i] = j;
         size[j] += size[i];
      }
      else{
         array[j] = i;
         size[i] += size[j];
      }
      N--;
   }
}
