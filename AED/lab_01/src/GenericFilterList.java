import java.util.*;

public class GenericFilterList {
   public static void main(String[] argv){
      var in = new Scanner(System.in);
      var list = new ArrayList<String>();
      while(in.hasNext()){
         list.add(in.next());
      }

      var filteredList = genericFilterList(list,  (a) ->  a.length() < 7 );

      System.out.println(filteredList.toString());
   }

   public static <T> List<T>  genericFilterList(List<T> list, genericBoolean<T> genericFunction){
      var result = new ArrayList<T>();
      for (T t : list) {
         if (genericFunction.predicate(t))
            result.add(t);
      }

      return result;
   }
}

