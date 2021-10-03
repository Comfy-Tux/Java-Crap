
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
   public static void main(String[] args){
      String champion = null;

      for(int index = 1; !StdIn.isEmpty() ; index++) {
         String word = StdIn.readString();
         if(StdRandom.bernoulli(1.0/index)){
            champion = word;
         }
      }

      StdOut.println(champion);
   }
}
