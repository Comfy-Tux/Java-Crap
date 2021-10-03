import java.util.*;

public class Grid {
   private final int size = 3;
   public int[] cells = new int[size];


    Grid(int color){
      this.cells[0] = color;
   }

    Grid(int colorOfCell_1, int colorOfCell_2){
      this.cells[0] = colorOfCell_1 ;
      this.cells[1] = colorOfCell_2;
   }

   public void insert(int color , int index){
      this.cells[index] = color;
   }

   public int look(int index){
      return this.cells[index];
   }

   public boolean isCellEqual(Grid x, Grid y,int index){
      return x.look(index) == y.look(index);
   }
   
   public int cellsOccupied(){
       int index = 0;
      while(index < cells.length && cells[index] != 0)
         index++;
      return index;
   }

   public void add(int color){
      assert(cellsOccupied() <= size);
      insert(color, cellsOccupied() );
   }

   public Grid copyAdd(int color){
       Grid result = new Grid(this.cells[0],this.cells[1]);
       result.add(color);
       return result;
   }

   public void println(){
       System.out.println(Arrays.toString(cells));
   }

   public int gridSize(){
      return this.size;
   }

}
