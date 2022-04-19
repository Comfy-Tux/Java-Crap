import java.util.*;

public class EuclideanDistance {
   public static void main(String[] args){
      var in = new Scanner(System.in);
      int N = in.nextInt();
            
      double length = 0;
      var PontoCurrent = new Ponto(in.nextDouble(),in.nextDouble());
      for(int i = 1; i < N; i++){
         var PontoNext = new Ponto(in.nextDouble(),in.nextDouble());
         length += PontoCurrent.dist(PontoNext);
         PontoCurrent = PontoNext;
      }
      System.out.printf("%.2f%n", length);
   }
}