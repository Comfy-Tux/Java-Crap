import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class CvsColumn {
   public static void main(String[] argv) throws FileNotFoundException {
      var file = new File(argv[0]);
      var in = new Scanner(file);
      var list = new ArrayList<String>();

      while(in.hasNextLine())
         list.add(in.nextLine());

      System.out.println(list);

      var columnList = cvsColumnN(list,1);
      System.out.println(columnList);
   }


   public static String getNthWordBetween(String line, String x,int n){
      int i = 0;

      while(n != 0) {
         i = line.indexOf(x, i);
            n--;
      }

      int j = i + 1;
      while( n != -1) {
         j = line.indexOf(x, j);
         n--;
      }

      if(j == -1)
         return "";
      return line.substring(i,j);
   }

   //makes a list with the nth column of the cvs file
   public static ArrayList<String> cvsColumnN(ArrayList<String> list,int n) {
      var result = new ArrayList<String>();

      for (String s : list) result.add(getNthWordBetween(s, ",", n));

      return result;
   }
}
