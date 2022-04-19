import java.util.Arrays;
import java.util.Scanner;

public class Craftsman {
   public static void main(String[] args){
      var in = new Scanner(System.in);

      var n = in.nextInt();
      var p = in.nextInt();
      if(n == 0 && p == 0){
         var encryptedMessage = in.next();
         var keys = EncrypterSmith.findKey(encryptedMessage,"bug");
         System.out.println(EncrypterSmith.decrypt(keys[0],keys[1],encryptedMessage));
      }
      else System.out.println(EncrypterSmith.encrypt(n,p,in.next()));
   }
}
