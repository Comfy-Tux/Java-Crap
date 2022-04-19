/**
 * @inv radius > 0
 */
public class Circumference extends GeoFig{
   final private float radius;
   final private Point center;

   /**
    *
    * @param x
    * @param y
    * @param radius
    *
    * @pre radius > 0
    */
   public Circumference(float x,float y,float radius){
      if(radius <= 0)
         throw new IllegalArgumentException("Illegal radius must be higher than 0");
      this.radius = radius;
      this.center = new Point(x,y);
   }

   /**
    *
    * @param line
    * @pre true
    * @return true if the line intersect the circle.
    */
   public boolean intersect(LineSegment line) { return (line.distance(center) <= radius); }
}
