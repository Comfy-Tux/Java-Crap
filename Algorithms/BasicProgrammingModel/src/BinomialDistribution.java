
public class BinomialDistribution {
   public static void main(String[] argv){
      System.out.println(binomial(100,50,0.25));
   }


   public static double binomial(int N,int k,double p){
      var array = new double[N+1][k+1];

      for(int i = 0; i < array.length ; i++)
         for(int j = 0; j < array[i].length ; j++)
            array[i][j] = -1;
      return binomial(N,k,p,array);
   }

   public static double binomial(int N, int k, double p, double[][] array) {


      if ((N == 0) && (k == 0)) return 1.0;
      if ((N < 0) || (k < 0)) return 0.0;

         if(array[N][k] != -1)
            return array[N][k];

         array[N][k] = (1 - p) * binomial(N - 1, k, p, array) + p * binomial(N - 1, k - 1, p, array);
         return array[N][k];

   }
}
