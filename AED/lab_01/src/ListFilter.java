import java.util.*;

public class ListFilter {
   public static void main(String[] argv){
      var in = new Scanner(System.in);
      var list = new ArrayList<Integer>();

      //reads Integers into an ArrayList
      while(in.hasNextInt()){
         list.add(in.nextInt());
      }

      var listBelow10 = listFilter(list);

      System.out.println(listBelow10);
   }

   public static List<Integer> listFilter(List<Integer> a){
      var result = new ArrayList<Integer>();
      for (Integer integer : a) {
         if (integer <= 10)
            result.add(integer);
      }

      return result;
   }
}
