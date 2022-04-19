import java.util.ArrayList;

public class Skin {
   private float width;
   private float length;
   private final ArrayList<Rectangle> rectangles = new ArrayList<>();
   private final ArrayList<Circumference> circumferences = new ArrayList<>();
   private final ArrayList<Triangle> triangles = new ArrayList<>();

   Skin(float width,float length){
      this.width = width;
      this.length = length;
   }

   public void addRectangle(Rectangle rectangle){ rectangles.add(rectangle); }

   public void addCircumferences(Circumference circumference){ circumferences.add(circumference); }

   public void addTriangles(Triangle triangle){ triangles.add(triangle);}

   public int nIntersection(LineSegment line){
      int n = 0;
      for(Rectangle rectangle : rectangles)
         if(rectangle.intersect(line)) n++;

      for(Circumference circumference: circumferences)
         if(circumference.intersect(line)) n++;

      for(Triangle triangle: triangles)
         if(triangle.intersect(line)) n++;

      return n;
   }
}
