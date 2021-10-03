import java.util.*;

/******************************************************
 * Kinda Shit but we are working on the assumption that 0 means no color
 * and 1-3 represents three different colors
 * I would do a enum set for color , but i'm kinda lazy .
 */


public class Solution {
   private final static int colorVariety = 4; // 0 - no color and ( 1,2,3 ) represent different colors , therefore there are 4 colors ;

   public static void main(String[] args) {
      Scanner in = new Scanner(System.in);
      while (in.hasNextInt()) {
         System.out.println(numOfWays(in.nextInt())/2);
      }
   }

   public static int numOfWays(int n) {
      int result = recursiveGridCounter(n,null,null);
      return result;
   }


   // n = the number of grids , 1 <= n <= 5000 , beforeGrid starts as null, and currentGrid as well , to have a predictable(correct) answer;
   private static int recursiveGridCounter(int n, Grid beforeGrid, Grid currentGrid) {
      int result = 0;
      if (n != 0) {
         if( currentGrid != null && currentGrid.gridSize() == currentGrid.cellsOccupied()) {
            result += movingToNextGrid(n,beforeGrid,currentGrid);
         }
         else if(beforeGrid != null) {
            for(int index = 1; index < colorVariety ; index++)
               if(beforeGrid.look(currentGrid.cellsOccupied()) != index && currentGrid.look(currentGrid.cellsOccupied()-1) != index )
                  result += recursiveGridCounter(n,beforeGrid,currentGrid.copyAdd(index));
         }
         else
            result += startRecursiveGridCounter(n, beforeGrid, currentGrid);
      }
      else
         return 1;
      return result;
   }

   private static int movingToNextGrid(int n,Grid beforeGrid,Grid currentGrid){
      int result = 0;
      for (int index = 1; index < colorVariety; index++)
         if (currentGrid.look(0) != index)
            result += recursiveGridCounter(n - 1, currentGrid, new Grid(index));
      return result;
   }

   //starts the first grid on the recursiveGridCounter
   private static int startRecursiveGridCounter(int n, Grid beforeGrid, Grid currentGrid) {
      int result = 0;
      if (currentGrid == null) {
         for (int index = 1; index < colorVariety; index++) {
            result += recursiveGridCounter(n, beforeGrid, new Grid(index));
         }
      }
      else if (currentGrid.cellsOccupied() != currentGrid.gridSize()) {
         for (int index = 1; index < colorVariety; index++) {
            if (currentGrid.look(currentGrid.cellsOccupied() - 1) != index) {
               result += recursiveGridCounter(n, beforeGrid, currentGrid.copyAdd(index));
            }
         }
      }
      return result;
   }
}
