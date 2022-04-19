import java.util.Scanner;

public class DateClient {
   public static void main(String[] args){
      var in = new Scanner(System.in);

      var date1 = new Date(in.next());
      var str = in.next();

      if(str.equals("ad"))
         System.out.println(date1.ad(in.nextInt()));
      else if(str.equals("sub"))
         System.out.println(date1.sub(in.nextInt()));
      else if(str.equals("dif"))
         System.out.println(date1.diff(new Date(in.next())));
      else if(str.equals("cal"))
         date1.cal();
      else
         System.out.println("Erro");



   }
}
