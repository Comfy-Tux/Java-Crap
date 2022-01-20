package aed.graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class MaxCycleMST {
   private final UndirectedWeightedGraph G;
   private UndirectedWeightedGraph mst;
   private final int V;

   public static void  main(String[] args) throws FileNotFoundException {
      UndirectedWeightedGraph graph;
      Double time = 0.0;

/*
      graph = UndirectedWeightedGraph.parse(new Scanner(new File(args[1])));

     System.out.println(graph + "\n\n");


*///
/*
      var stopwatch = new Stopwatch();
      for(int i = 0; i < 200; i++)
         new MaxCycleMST(graph).buildMST();
      time = stopwatch.elapsedTime();

*//*


      var stopwatch = new Stopwatch();
      new MaxCycleMST(graph).buildMST();
      time = stopwatch.elapsedTime();

      System.out.println(new MaxCycleMST(graph).buildMST().summary());

      System.out.println("Time = " + time);
*/

      //from my analysis I deducted that the order of growth is E*V because we insert E edges , and after insertion
      // we see if there are any cycles if there are we remove the max Edge , because of this behavior we can assure that
      // the number of edges on mst never surpasses V i.e.(number of vertices) because that is how big the graph is for
      // dfs to find a cycle which leaves us with V + V = 2V = V , therefore it's order of growth will be
      // E*V as we had stated at the beginning , so if that is the case the doubling test
      //should increase by double if I multiply E or V by 2 , and it should increase by four if
      //multiply the graph E*V by 2 .

      int n = 50;
      int V = 254;
      int E = 4096;


      //half times
      double half1 = timeTrialMST(new MaxCycleMST(randomGraph(64,128)),n);
      System.out.println(half1);
      double half2 = timeTrialMST(new MaxCycleMST(randomGraph(65,E)),n);
      System.out.println(half2);
      double half3 = timeTrialMST(new MaxCycleMST(randomGraph(V,64)),n);
      System.out.println(half3);

      for(int i = 128; i < 5000; i += i) {
         System.out.println();
         System.out.println("V = " + i + ", E = " + i*2);
         var g1 = new MaxCycleMST(randomGraph(i,i*2)); // (V*E)*2 , should increase by 4
         var g2 = new MaxCycleMST(randomGraph(i,E));// (V*2)*E should increase by 2
         var g3 = new MaxCycleMST(randomGraph(V,i)); // V*(E*2) should increase by 2

         //O(V*E*4)
         double time1 = timeTrialMST(g1,n);
         var g1Growth = new TimeAndRatio(time1 ,time1/half1);
         half1 = time1;
         System.out.println("An 2*(V*E) is O(" + bigO(g1Growth.ratio) + ") worst case , real ratio = " + g1Growth.ratio +
                 " and it took " + g1Growth.time + " seconds");

         //O(V*E*2)
         double time2 = timeTrialMST(g2,n);
         var g2Growth = new TimeAndRatio(time2 ,time2/half2);
         half2 = time2;
         System.out.println("An (V*2)*E is O(" + bigO(g2Growth.ratio) + ") worst case , real ratio = " + g2Growth.ratio +
                 " and it took " + g2Growth.time + " seconds");

         //O(V*E*2)
         double time3 = timeTrialMST(g3,n);
         var g3Growth = new TimeAndRatio(time3 ,time3/half3);
         half3 = time3;
         System.out.println("An V*(E*2)  is O(" + bigO(g3Growth.ratio) + ") worst case , real ratio = " + g3Growth.ratio +
                 " and it took " + g3Growth.time + " seconds");
      }

   }

   public MaxCycleMST(UndirectedWeightedGraph G) {
      this.G = G;
      this.V = G.vCount();
   }

   public UndirectedEdge determineMaxInCycle(UndirectedWeightedGraph g) {
      return new MaxDelCycleDFS(g).maxEdge;
   }


   //the efficiency should be around E*V
   public UndirectedWeightedGraph buildMST() {
      mst = new UndirectedWeightedGraph(V);

      for(var e : G.allEdges())
         if(!hasEdge(e)) {
            int v1 = e.v1();
            int v2 = e.v2();
            mst.addEdge(new UndirectedEdge(v1, v2, e.weight()));
            if(mst.degree(v1) > 1 && mst.degree(v2) > 1) {
               var maxEdge = determineMaxInCycle(mst);
               if(maxEdge != null)
                  mst.removeEdge(maxEdge);
            }
         }
      return mst;
   }

   //helper method
   public boolean hasEdge(UndirectedEdge e){
      for(var otherE : mst.adj(e.v1()))
         if(otherE.other(e.v1()) == e.v2())
            return true;
      return false;
   }

   public UndirectedWeightedGraph getMST() { return mst; }

   //helper class
   private class MaxDelCycleDFS{
      boolean[] marked ;
      UndirectedEdge[] edgeTo ;
      UndirectedEdge maxEdge = null;
      UndirectedWeightedGraph g ;

      public MaxDelCycleDFS(UndirectedWeightedGraph g){
         this.g = g;
         marked = new boolean[g.vCount()];
         edgeTo = new UndirectedEdge[g.vCount()];

         for(var e : g.allEdges()) {
            if(maxEdge != null) return;
            if(!marked[e.v2()]) {
               int v = e.v1();
               marked[v] = true;
               edgeTo[v] = e;
               for(var e2 : g.adj(v))
                  dfs(e2, e2.other(v), v);
            }
         }
      }


      //w is the older vertex and e is new vertex , e cannot go back to w
      public void dfs(UndirectedEdge e,int v,int w){
         if(maxEdge != null) return;
         marked[v] = true;
         edgeTo[v] = e;

         for(var otherE : g.adj(v)) {
            if(maxEdge != null) return;
            int nextV = otherE.other(v);
            if(!marked[nextV]) {
               dfs(otherE, nextV, v);
            } else if(nextV != w) {
               maxEdge = otherE;

               for(var i = v; i != nextV; i = edgeTo[i].other(i))
                  if(maxEdge.compareTo(edgeTo[i]) < 0)
                     maxEdge = edgeTo[i];
            }
         }
      }

      public UndirectedEdge getMaxEdge(){return maxEdge;}
   }

   private static double timeTrialMST(MaxCycleMST mst,int n){
      var stopwatch = new Stopwatch();
      for(int i = 0; i < n; i++)
         mst.buildMST();
      return stopwatch.elapsedTime();
   }

   private static UndirectedWeightedGraph randomGraph(int V,int E){
      var g = new UndirectedWeightedGraph(V);
      SparseVector[] edges = new SparseVector[V];
      for(int i = 0;i < V;i++) edges[i] = new SparseVector();

      var r = new Random();
      for(int i = 0; i < E ; i++){
         int m = r.nextInt(V);
         int n = r.nextInt(V);
         if(m != n && !(edges[m].get(n) || edges[n].get(m))) {
            g.addEdge(new UndirectedEdge(m, n, r.nextFloat()));
            edges[m].put(n,true);
         }
         else
            i--;
      }

      return g;
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

   public static class  SparseVector {
      private final Hashtable<Integer,Boolean> st;

      public SparseVector(){ st = new Hashtable<>();}

      public int size(){ return st.size();}

      public void put(int i,boolean x){ st.put(i,x);}

      public boolean get(int i){
         return st.contains(i);
      }
   }

}