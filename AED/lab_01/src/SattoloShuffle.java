public class SattoloShuffle {

   public static void main(String[] args) {
      String[] a =
              {"isto","é","uma","lista","de","palavras","a","baralhar"};
      sattoloShuffle(a);
      for (int i = 0; i < a.length; i++)
         System.out.println(a[i]);
   }

   public static void sattoloShuffle(Object[] a) {
      int n = a.length;
      for (int i = n; i > 1; i--) {
         // choose index uniformly in [0, i-1[
         int r = (int) (Math.random() * (i-1));
         Object swap = a[r];
         a[r] = a[i-1];
         a[i-1] = swap;
      }
   }
}
