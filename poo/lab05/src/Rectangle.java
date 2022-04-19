/**
 * @inv super.edges must have edges with 90 degrees from each other in order to form a rectangle.
 */
public class Rectangle extends Polygon{
   /**
    *
    * @param x1
    * @param y1
    * @param x2
    * @param y2
    *
    * @pre x1 != x2 && y1 != y2
    * @pre x1 < x2 && y1 < y2
    */
   public Rectangle(float x1,float y1,float x2,float y2){
      super(new float[]{x1,y1,x2,y1,x2,y2,x1,y2});
      if(x1 > x2 || y1 > y2)
         throw new IllegalArgumentException(
                 "x2,y2 must be the top right corner and x1,y1 on the bottom left corner");
      else if(x1 == x2 || y1 == y2)
         throw new IllegalArgumentException(
                 "xMin , xMax or yMin , yMax must be different otherwise there are congruent edges");
   }
}
