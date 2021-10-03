import java.util.*;

public class romanToInt {
   public static void main(String[] argv){

   }

   public int romanToInt(String s) {
      int result = 0;
      for(int i = 0 ; i < s.length() ; i++){
         switch (s.charAt(i)){
            case 'I' : result += 1; break;
            case 'V' : result += (i != 0 && s.charAt(i-1) == 'I') ? 3 : 5; break;
            case 'X' : result += (i != 0 && s.charAt(i-1) == 'I') ? 8 : 10; break;
            case 'L' : result += (i != 0 && s.charAt(i-1) == 'X') ? 30: 50; break;
            case 'C' : result += (i != 0 && s.charAt(i-1) == 'X') ? 80: 100; break;
            case 'D' : result += (i != 0 && s.charAt(i-1) == 'C') ? 300: 500; break;
            case 'M' : result += (i != 0 && s.charAt(i-1) == 'C') ? 800: 1000; break;
         }
      }
      return result;
         }
   public boolean isValid(String s) {
      if(s.length() % 2 != 0)
         return false;

      var stack = new Stack<Character>();

      for(int i = 0; i < s.length() ; i++) {
         char c = s.charAt(i);
         if (c == '(' || c == '{' || c == '[')
            stack.add(c);
         else {
            if(stack.isEmpty())
               return false;
            switch (c) {
               case '}':
                  if (stack.pop() != '{') return false;
                  break;
               case ')':
                  if (stack.pop() != '(') return false;
                  break;
               case ']':
                  if (stack.pop() != '[') return false;
                  break;
            }
         }
      }
      if(stack.isEmpty())
         return true;
      else
         return false;
   }


}
