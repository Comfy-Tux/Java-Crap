/**
 * @inv N >= 3
 */
public class Polygon extends GeoFig{
   LineSegment[] edges;
   int N; //number of edges



   //the lines must be inserted in {x1,y1,x2,y2,x3,y3} and all the coordinated will be interlinked in
   // the order they were placed to form lines ex: (x1,y1;x2,y2) ; (x2,y2;x3,y3) ; (x3;y3;x1;y1) on a R2 plane.
   /**
    * @param coordinates
    * @pre coordinates.length must be >= 6
    * @pre coordinates.length must be even.
    */
   public Polygon(float[] coordinates){
      int n = coordinates.length;
      if(n < 6)
         throw new IllegalArgumentException("It has less than 3 lines");
      else if(n%2 == 1)
         throw new IllegalArgumentException("It must be a par number of coordinates");

      int N = n/2;
      edges = new LineSegment[N];

      //initializes n-1 edges.
      for(int i = 3, j=0 ;i < n;i+=2)
         edges[j++] = new LineSegment(coordinates[i-3],coordinates[i-2],coordinates[i-1],coordinates[i]);

      edges[N-1] = new LineSegment(coordinates[n-2],coordinates[n-1],coordinates[0],coordinates[1]);
   }

   //sees if any of the edges intersects the line to know if the polynomial intersects the line.

   /**
    *
    * @param line
    * @pre The line must not be contained inside the polygon without touching the edges.
    * @return true if the polygon is intersected by a line
    */
   public boolean intersect(LineSegment line) {
      for(LineSegment edge : edges)
         if(edge.intersect(line))
            return true;
      return false;
   }
}
