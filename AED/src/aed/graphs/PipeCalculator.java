package aed.graphs;

public class PipeCalculator {
   int n;
   float[] well;
   float[][] costs;
   UndirectedWeightedGraph mst;

   public static void main(String[] args){
      int n = 10;
      float[] well = new float[]{0.4f,0.2f,0.3f,0.3f,0.5f,0.7f,0.21f,0.5f,0.1f,0.9f};
      float[][] costs = new float[10][10];

      costs[1][2] = 0.5f; costs[2][1] = 0.5f;
      costs[2][4] = 0.8f; costs[4][2] = 0.8f;
      costs[3][4] = 0.2f; costs[4][3] = 0.2f;
      costs[4][7] = 0.65f; costs[7][4] = 0.65f;
      costs[5][7] = 0.123f; costs[7][5] = 0.123f;
      costs[6][4] = 0.45f; costs[4][6] = 0.45f;
      costs[7][2] = 0.1f; costs[2][7] = 0.1f;
      costs[8][9] = 0.5f; costs[9][8] = 0.5f;
      costs[9][5] = 0.9f; costs[5][9] = 0.9f;
      costs[5][3] = 0.3f; costs[3][5] = 0.3f;
      costs[4][1] = 0.2f; costs[1][4] = 0.2f;
      costs[5][1] = 0.5f; costs[1][5] = 0.5f;
      costs[3][6] = 0.12f; costs[6][3] = 0.12f;
      costs[2][6] = 0.4f; costs[6][2] = 0.4f;

      var pipes = new PipeCalculator(n,well,costs);

      pipes.calculateSolution();
      System.out.println(pipes.getMST().summary());
   }

   public PipeCalculator(int n, float[] well, float[][] costs){ this.n = n; this.well  = well; this.costs = costs; }

   public UndirectedWeightedGraph createGraph(int n, float[] well, float[][] costs){
      var g = new UndirectedWeightedGraph(n +1);

      //adds the all the plumbing between the houses
      for(int i = 0;i < n; i++){
         for(int j = 0;j < i ; j++)
            g.addEdge(new UndirectedEdge(j,i,costs[i][j]));
      }

      //adds a path from the well to the houses
      for(int i = 0;i < n;i++){
         g.addEdge(new UndirectedEdge(i,n,well[i]));
      }

      return g;
   }

   public UndirectedWeightedGraph calculateSolution(UndirectedWeightedGraph g){ return new MaxCycleMST(g).buildMST();}

   public UndirectedWeightedGraph calculateSolution(){
      var g = createGraph(n,well,costs);
      mst =  new MaxCycleMST(g).buildMST();
      return mst;
   }

   public UndirectedWeightedGraph getMST(){ return mst; }
}
