public class EncrypterSmith {

   //Encrypts string message with key passed in n and p
   //returns the encrypted string
   public static String encrypt(int n, int p, String message){
      if(!isInRange(n,p) || message.length() < 10 || message.length() > 100|| message.substring(0,2).toLowerCase().equals("bug"))
         throw new IllegalArgumentException();

      int N = message.length();
      var encryptedMessage = new StringBuilder();
      int start = (n >= 0) ? N - n : -n;
      for(int i =0 , j = start; i < N ; i++, j = (j+1)%N)
         encryptedMessage.append(Character.toString(message.charAt(j) + p));

      return encryptedMessage.toString();
   }

   //Tries to decrypt encriptedMessage with key passed in n and p
   //returns the decrypted string
   public static String decrypt(int n, int p, String encryptedMessage){
      if(!isInRange(n,p) || encryptedMessage.length() < 10 || encryptedMessage.length() > 100)
         throw new IllegalArgumentException();

      int N = encryptedMessage.length();
      var message = new StringBuilder();
      int start = (n >= 0) ? n : N + n ;
      for(int i =0 , j = start; i < N ; i++, j = (j+1)%N)
         message.append(Character.toString(encryptedMessage.charAt(j) - p));

      return message.toString();
   }


   //Tests all key combinations of n and p until the first letters
   //in the string became "bug".
   //returns found key in an array with 2 ints: n and p
   //word is the word at the beginning of the string: "bug"
   public static int[] findKey(String encryptedMessage, String word){
      if(encryptedMessage.length() < 10 || encryptedMessage.length() > 100)
         throw new IllegalArgumentException();

      for(int n = -9 ; n <= 9 ; n++)
         for(int p = -4; p <= 4 ; p++){
            var message = decrypt(n,p,encryptedMessage);
            if (message.substring(0,3).toLowerCase().equals(word))
               return new int[]{n,p};
         }

      throw new IllegalArgumentException();
   }

   //checks whether n or p are outside permissible range
   private static boolean isInRange(int n,int p){
      return (n < 10 && n > -10 && p > -5 && p < 5);
   }


}
