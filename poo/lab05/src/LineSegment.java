/**
 * @inv point a must be different from point b.
 */
public class LineSegment {
   final private Point a;
   final private Point b;

   /**
    * @param x1
    * @param y1
    * @param x2
    * @param y2
    *
    * @pre x1 != x2 && y1 != y2
    */
   public LineSegment(float x1,float y1,float x2,float y2){
      if(x1 == x2 && y1 == y2)
         throw new IllegalArgumentException("x1,y1 and x2,y2 must be different points");
      a = new Point(x1,y1);
      b = new Point(x2,y2);
   }


   /**
    *
    * @param other
    * @pre true
    * @return true if lines intersect, false otherwise
    * @post return == other.intersect(this)
    */
   public boolean intersect(LineSegment other){
      //https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/
      //finds the orientations needed to check if the triangles have different orientations.

      int o1 = orientation(a,b,other.a);
      int o2 = orientation(a,b,other.b);
      int o3 = orientation(other.a,other.b,a);
      int o4 = orientation(other.a,other.b,b);

      // General case
      if (o1 != o2 && o3 != o4)
         return true;

      // Special Cases
      // a, b and other.a are collinear and other.a lies on segment (a,b)
      if (o1 == 0 && onSegment(a, other.a, b)) return true;

      // a, b and other.b are collinear and other.b lies on segment (a,b)
      if (o2 == 0 && onSegment(a, other.b, b)) return true;

      // other.a, other.b and a are collinear and a lies on segment (other.a,other.b)
      if (o3 == 0 && onSegment(other.a, a, other.b)) return true;

      // other.a, other.b and b are collinear and b lies on segment (other.a,other.b)
      if (o4 == 0 && onSegment(other.a, b, other.b)) return true;

      return false; // Doesn't fall in any of the above cases
   }

   //given 3 points in a specific order it checks if the lines formed by (p1,p2) (p2,p3) and ((p3,p1) this last line
   //is not necessary), are collinear, counterclockwise or clockwise. 0 collinear; 1 clockwise; 2 counterclockwise;
   private int orientation(Point p1,Point p2,Point p3){
      float o =(p2.y - p1.y)*(p3.x - p2.x) - (p3.y - p2.y)*(p2.x - p1.x);
      return (o == 0) ? 0 : (o > 0) ? 1 : 2;
   }

   // Given three collinear points p, q, r, the function checks if
   // point q lies on the line segment 'pr'
   private boolean onSegment(Point p, Point q, Point r)
   {
      return (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
              q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y));
   }

   //https://www.xarg.org/2016/04/shortest-distance-between-a-point-and-a-line-segment/
   public float distance(Point p){
      float x1 = p.x - a.x;
      float y1 = p.y - a.y;

      float x2 = b.x - a.x;
      float y2 = b.y - a.y;

      float bb = x2*x2 + y2*y2;
      if(bb == 0)
         return (float) Math.hypot(a.x,b.y);

      float ab = x1 * x2 + y1 * y2;
      float t = Math.max(0,Math.min(1, ab/bb));

      float x3 = a.x + t * x2;
      float y3 = a.y + t * y2;

      return (float) Math.hypot(p.x - x3,p.y - y3);
   }
}
