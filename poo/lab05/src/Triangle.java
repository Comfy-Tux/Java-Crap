/**
 * @inv super.edges must not have congruent edges.
 */
public class Triangle extends Polygon{
   /**
    *
    * @param x1
    * @param y1
    * @param x2
    * @param y2
    * @param x3
    * @param y3
    *
    * @pre x1,y1 and x2,y2 and x3,y3 cannot form a 180 angle between them.
    */
   public Triangle(float x1,float y1,float x2,float y2,float x3,float y3){
      super(new float[]{x1,y1,x2,y2,x3,y3});
      if((y2-y1)*(x3-x2) - (y3-y2)*(x2-x1) == 0)
         throw new IllegalArgumentException("congruent edges : not a triangle");
   }
}
