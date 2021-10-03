public class HelloWorld {
   public static void main (String[] argv){
      if(argv.length != 0)
         System.out.println("Hello ,World!\n" + argv[0]);
      else
         System.out.println("No argument , aborting");
   }
}
